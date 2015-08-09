/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *  
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: FailoverManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19  2015/04/29 17:30:57  robertja
 *  Archive Log:    Add debug code for "SM unavailable" testing.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/23 14:49:20  robertja
 *  Archive Log:    Add PAQuery to fail-over criteria to insure we have a working PM.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/04/21 15:30:54  robertja
 *  Archive Log:    Update fail-over progress bar.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/17 12:48:55  robertja
 *  Archive Log:    Fixed critical Klocwork issue where NIO SocketChannel was not closed on exit.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/10 13:16:51  robertja
 *  Archive Log:    Added wait after connection refused.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/04/09 18:28:26  robertja
 *  Archive Log:    Handle case where single available FE/SM connectivity is lost briefly.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/04/07 17:16:12  robertja
 *  Archive Log:    Change to STL Connection tryConnect to avoid polluting the cache with temporary connections.  Also, don't register SocketChannel for connection events if the host address cannot be resolved.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/04/06 11:04:55  jypak
 *  Archive Log:    Klockwork: Back End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/03 18:42:17  robertja
 *  Archive Log:    Restore methods accidently deleted.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/03 18:37:34  robertja
 *  Archive Log:    Get list of SM's from initial connection to verify fail-over to the correct subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/31 16:16:24  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/26 15:03:18  robertja
 *  Archive Log:    Check for connection null before clean-up.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/26 14:59:42  robertja
 *  Archive Log:    Move clean-up of STLConnection to finally block.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/25 21:47:24  jijunwan
 *  Archive Log:    changed fail-over message id to another number
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/25 17:40:38  robertja
 *  Archive Log:    Add constructor to support JUnit tests.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/24 18:43:11  robertja
 *  Archive Log:    Add logging and SM fail-over exception.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/23 15:19:23  robertja
 *  Archive Log:    Correct file header for CVS.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: robertja
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.performance.impl.PAHelper;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.common.STLMessages;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.IFailoverEventListener;
import com.intel.stl.fecdriver.IFailoverManager;

/**
 * @author robertja
 * 
 */
public class FailoverManager implements IFailoverManager {

    private final static Logger log = LoggerFactory
            .getLogger(FailoverManager.class);

    private final int SM_FAILOVER_WAIT = 10000; // 10 sec * 1000 ms/sec

    private final int SM_SELECTOR_EVENT_TIMEOUT = 2000;

    private final int SM_FAILOVER_RETRIES = 12;

    private List<SMRecordBean> subnetManagerRecords = null;

    private final STLAdapter adapter;

    private ArrayList<HostChannel> channels = null;

    private SocketChannel channel = null;

    private Double progressIncrement = null;

    /**
	 * 
	 */
    public FailoverManager(STLAdapter adapter) {
        this.adapter = adapter;
        subnetManagerRecords = new ArrayList<SMRecordBean>();
        channels = new ArrayList<HostChannel>();
    }

    // Class to conveniently associate the HostInfo object with the
    // SocketChannel
    // created based on the host/port stored there.
    private class HostChannel {
        private final HostInfo hostInfo;

        private final SocketChannel sockChannel;

        protected HostChannel(HostInfo hostInfo, SocketChannel sockChannel) {
            this.hostInfo = hostInfo;
            this.sockChannel = sockChannel;
        }

        protected HostInfo getHostInfo() {
            return hostInfo;
        }

        protected SocketChannel getSockChannel() {
            return sockChannel;
        }

    }

    public void setSubnetManagerRecords(List<SMRecordBean> subnetManagerRecords) {
        this.subnetManagerRecords = subnetManagerRecords;
    }

    public List<SMRecordBean> getSubnetManagerRecords() {
        return subnetManagerRecords;
    }

    public void clearSubnetManagerRecords() {
        subnetManagerRecords.clear();
    }

    @Override
    public long getFailoverTimeout() {
        return ((SM_FAILOVER_WAIT + SM_SELECTOR_EVENT_TIMEOUT) * SM_FAILOVER_RETRIES);
    }

    @Override
    public SubnetDescription connectionLost(final STLConnection conn) {
        System.out.println("FailoverManager - connectionLost called: "
                + System.currentTimeMillis() / 1000);

        SubnetDescription snDescription = conn.getConnectionDescription();
        List<HostInfo> backupSMs = snDescription.getFEList();
        subnetManagerRecords = snDescription.getSMList();
        progressIncrement = getProgressIncrement(backupSMs.size());

        IFailoverEventListener listener = new IFailoverEventListener() {
            @Override
            public void onFailoverStart(ApplicationEvent event) {
                conn.fireFailoverStart();
            }

            @Override
            public void onFailoverProgress(ApplicationEvent event) {
                conn.fireFailoverProgress(event);
            }

            @Override
            public void onFailoverEnd(ApplicationEvent event) {
                conn.fireFailoverEnd(event.getReason());
            }
        };

        Selector selector;
        SelectionKey key;
        boolean failoverSuccessful = false;
        int j = 0;

        while ((j++ < SM_FAILOVER_RETRIES) && (failoverSuccessful == false)) {
            try {
                listener.onFailoverProgress(new ApplicationEvent(
                        "FAILOVER CONNECTION ATTEMPT " + (j + 1)));
                listener.onFailoverProgress(new ApplicationEvent(
                        progressIncrement));
                channels.clear();
                // Create the selector to monitor connection events on all
                // SocketChannels.
                System.out.println("FailoverManager - Create the selector.");
                selector = Selector.open();

                // Create one SocketChannel per SM back-up.
                Iterator<HostInfo> backupSMIterator = backupSMs.iterator();
                while (backupSMIterator.hasNext()) {
                    HostInfo host = backupSMIterator.next();
                    // Create regular socketConnection.
                    System.out
                            .println("FailoverManager - Create the regular SockectChannel.");
                    channel = SocketChannel.open();

                    try {
                        // Configure socket and connect.
                        channel.configureBlocking(false);
                        channel.connect(new InetSocketAddress(host.getHost(),
                                host.getPort()));
                    } catch (Exception e) {
                        channel.close();
                        System.out
                                .println("FailoverManager - Unresolved address in list of back-ups.");
                        log.error(StringUtils.getErrorMessage(e), e);
                    }
                    if (channel.isOpen() == true) {
                        // We care about connection event only.
                        channel.register(selector, SelectionKey.OP_CONNECT);
                        channels.add(new HostChannel(host, channel));
                    } else {
                        channels.add(new HostChannel(host, channel));
                    }
                }

                if (channels.isEmpty() == false) {
                    // We have at least one back-up SM listed.
                    // Block waiting for a timeout or a connection event.
                    if (selector.select(SM_SELECTOR_EVENT_TIMEOUT) > 0) {
                        System.out
                                .println("FailoverManager - Selector.select returned.");
                        Set<SelectionKey> selectedKeys =
                                selector.selectedKeys();
                        Iterator<SelectionKey> keyIterator =
                                selectedKeys.iterator();

                        while (keyIterator.hasNext()
                                && (failoverSuccessful == false)) {
                            key = keyIterator.next();
                            if (key.isConnectable()) {
                                // A connection was established with a remote
                                // server.
                                System.out
                                        .println("FailoverManager - Connection isConnectable event.");
                                SocketChannel socketChannel =
                                        (SocketChannel) key.channel();
                                STLConnection connection = null;

                                try {
                                    if (socketChannel.finishConnect() == true) {
                                        System.out
                                                .println("FailoverManager - SocketChannel connection was established with a remote server.");
                                        // The SocketChannel connection has
                                        // found an active node. Now
                                        // we set up an STLConnection to query
                                        // for SMs.

                                        SubnetDescription testSubnetDescription =
                                                snDescription;
                                        // Change current FE in
                                        // SubnetDescription to get connection
                                        // to active FE.
                                        testSubnetDescription
                                                .setCurrentFEIndex(getHostIndex(socketChannel));
                                        connection =
                                                adapter.tryConnect(testSubnetDescription);

                                        System.out
                                                .println("FailoverManager - tryConnect.");

                                        try {
                                            if (connection != null
                                                    && connection
                                                            .waitForConnect() == true) {
                                                SAHelper helper =
                                                        new SAHelper(connection);
                                                // Send the query for the list
                                                // of SMs.
                                                System.out
                                                        .println("FailoverManager - verify correct subnet connected.");
                                                List<SMRecordBean> smRecords =
                                                        helper.getSMs();
                                                if (foundCorrectSubnet(smRecords) == true) {
                                                    // Set new current master in
                                                    // SubnetDescription.
                                                    System.out
                                                            .println("FailoverManager - foundCorrectSubnet = true - check for PM.");
                                                    PAHelper paHelper =
                                                            new PAHelper(
                                                                    connection);
                                                    PMConfigBean configBean =
                                                            null;
                                                    configBean =
                                                            paHelper.getPMConfig();
                                                    if (configBean != null) {
                                                        System.out
                                                                .println("FailoverManager - Updating SubnetDescription.");
                                                        snDescription
                                                                .setCurrentFEIndex(getHostIndex(socketChannel));
                                                        failoverSuccessful =
                                                                true;
                                                    }
                                                } else {
                                                    System.out
                                                            .println("FailoverManager - foundCorrectSubnet = false.");
                                                }

                                            }

                                        } catch (IOException e) {
                                            System.out
                                                    .println("FailoverManager - STLConnection waitForConnect Exception.");
                                            log.error(StringUtils
                                                    .getErrorMessage(e), e);
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            System.out
                                                    .println("FailoverManager - STLConnection getSMs timeout exception.");
                                            log.error(StringUtils
                                                    .getErrorMessage(e), e);
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (IOException e) {
                                    System.out
                                            .println("FailoverManager - IOException on SockectChannel.finishConnect().");
                                    log.error(StringUtils.getErrorMessage(e), e);
                                    socketChannel.close();
                                } finally {
                                    // Clean up STLConnection.
                                    System.out
                                            .println("FailoverManager - Clean uo STLConnection.");
                                    if (connection != null) {
                                        connection.close();
                                    }

                                }

                            }
                            System.out
                                    .println("FailoverManager - End of while (keyIterator.hasNext().");
                            keyIterator.remove();
                        }
                    } else {
                        // Timeout has occurred.
                        System.out
                                .println("FailoverManager - Selector timeout has occurred.");
                    }

                } else {
                    // No Back-up SMs were listed.
                    System.out
                            .println("FailoverManager - No Back-up SMs were listed.");
                }
            } catch (IOException e) {
                log.error(StringUtils.getErrorMessage(e), e);
                System.out
                        .println("FailoverManager - connectionLost IOException..");
                e.printStackTrace();
            } finally {
                // Clean up SocketChannels.
                System.out
                        .println("FailoverManager - Clean up SocketChannels.");
                Iterator<HostChannel> channelIterator = channels.iterator();
                while (channelIterator.hasNext()) {
                    SocketChannel channel =
                            channelIterator.next().getSockChannel();
                    // Close SocketChannel.
                    System.out
                            .println("FailoverManager - Close SockectChannel.");
                    try {
                        channel.close();
                    } catch (IOException e) {
                        System.out
                                .println("FailoverManager - Exception during Close SockectChannel.");
                    }
                }
            }

            try {
                System.out.println("FailoverManager - connectionLost sleep: "
                        + System.currentTimeMillis() / 1000);
                Thread.sleep(SM_FAILOVER_WAIT);
            } catch (InterruptedException e) {
                log.error(StringUtils.getErrorMessage(e), e);
                System.out
                        .println("FailoverManager - connectionLost InterruptedException.");
            }
        } // End of while ((j < RETRIES) && (failoverSuccessful ==
          // false...

        if (channel.isOpen() == true) {
            try {
                channel.close();
            } catch (IOException e) {
                System.out
                        .println("FailoverManager - channel close IOException.");
            }
        }

        // Take action based on fail-over success or failure. If successful,
        // return SubnetDescriprtion with new current master. Otherwise, throw
        // an exception.
        if (failoverSuccessful == true) {
            System.out
                    .println("FailoverManager - Failover successful. Return new SM master.");
            return snDescription;
        } else {
            System.out
                    .println("FailoverManager - Failover unsuccessful. Throw exception.");
            SMFailoverException fe =
                    new SMFailoverException(
                            STLMessages.STL64000_SM_FAILOVER_UNSUCCESSFUL);
            log.error(StringUtils.getErrorMessage(fe), fe);
            throw fe;
        }
    }

    // Find the channels ArrayList HostInfo object associated with the
    // SocketChannel argument. A return value of null indicates that the
    // object was not found.
    protected HostInfo getHostInfo(SocketChannel sock) {
        HostInfo hostInfo = null;
        if (channels.isEmpty() == false) {
            Iterator<HostChannel> iterator = channels.iterator();
            while (iterator.hasNext()) {
                HostChannel hostChannel = iterator.next();
                if (hostChannel.getSockChannel().equals(sock)) {
                    hostInfo = hostChannel.getHostInfo();
                }
            }
        }
        return hostInfo;
    }

    // Find the channels ArrayList index of the HostInfo object associated
    // with the SocketChannel argument. A return of -1 indicates that the
    // object was not found.
    protected int getHostIndex(SocketChannel sock) {
        int index = -1;
        if (channels.isEmpty() == false) {
            Iterator<HostChannel> iterator = channels.iterator();
            while (iterator.hasNext()) {
                HostChannel hostChannel = iterator.next();
                if (hostChannel.getSockChannel().equals(sock)) {
                    index = channels.lastIndexOf(hostChannel);
                }
            }
        }
        return index;
    }

    // Determine if the information from the connnected subnet matches the
    // originally connected subnet.
    protected boolean foundCorrectSubnet(List<SMRecordBean> smRecords) {
        // Return true if:
        // - any GUID in smRecords matches a GUID in subnetRecords
        // - subnetRecords is null
        // - subnetRecords is empty (we are connecting for the first time.
        //
        // Return false otherwise.
        boolean isFound = false;
        System.out.println("FailoverManager - foundCorrectSubnet called.");
        if ((subnetManagerRecords != null)
                && (subnetManagerRecords.isEmpty() == false)) {
            if ((smRecords != null) && (smRecords.isEmpty() == false)) {
                ArrayList<SMRecordBean> smRecordsAL =
                        (ArrayList<SMRecordBean>) smRecords;
                Iterator<SMRecordBean> smRecordsALIterator =
                        smRecordsAL.iterator();
                // Compare GUIDS in both lists.
                while (smRecordsALIterator.hasNext() && (isFound != true)) {
                    SMRecordBean smRecordsALBean = smRecordsALIterator.next();
                    Iterator<SMRecordBean> subnetManagerRecordsIterator =
                            subnetManagerRecords.iterator();
                    while (subnetManagerRecordsIterator.hasNext()) {
                        SMRecordBean subnetManagerRecordsBean =
                                subnetManagerRecordsIterator.next();
                        if (smRecordsALBean.getSmInfo().getPortGuid() == subnetManagerRecordsBean
                                .getSmInfo().getPortGuid()) {
                            // We found a match.
                            isFound = true;
                            break;

                        }
                    }
                }
            }
        } else {
            // First time connecting, we have nothing to compare.
            System.out
                    .println("FailoverManager - foundCorrectSubnet isFound = true.");
            isFound = true;
        }

        return isFound;
    }

    private Double getProgressIncrement(int feListSize) {
        Double increment;
        if (feListSize > 0) {
            increment =
                    (new Double(feListSize) / new Double(SM_FAILOVER_RETRIES));
        } else {
            increment = new Double(0);
        }
        return increment;
    }
}
