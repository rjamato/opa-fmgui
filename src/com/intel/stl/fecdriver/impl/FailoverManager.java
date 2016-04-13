/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.intel.stl.fecdriver.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.intel.stl.fecdriver.IFailoverHelper;
import com.intel.stl.fecdriver.IFailoverManager;
import com.intel.stl.fecdriver.IFailoverProgressListener;
import com.intel.stl.fecdriver.dispatcher.SMFailoverException;

/**
 */
public class FailoverManager implements IFailoverManager {

    private final static Logger log = LoggerFactory
            .getLogger(FailoverManager.class);

    private final int SM_FAILOVER_WAIT = 10000; // 10 sec * 1000 ms/sec

    private final int SM_SELECTOR_EVENT_TIMEOUT = 2000;

    private final int SM_FAILOVER_RETRIES = 12;

    private STLConnection originalConnection = null;

    private List<HostInfo> backupSMs = null;

    private Double progressIncrement = null;

    List<SMRecordBean> subnetManagerRecords = null;

    private boolean stopFailover;

    private boolean failoverSuccessful;

    private final boolean FO_DEBUG = false;

    private final IFailoverHelper helper;

    public FailoverManager(IFailoverHelper adapter) {
        backupSMs = new ArrayList<HostInfo>();
        subnetManagerRecords = new ArrayList<SMRecordBean>();
        failoverSuccessful = false;
        this.helper = adapter;
    }

    @Override
    public void stopFailover() {
        this.stopFailover = true;
    }

    @Override
    public long getFailoverTimeout() {
        return (SM_FAILOVER_WAIT * SM_FAILOVER_RETRIES);
    }

    public SubnetDescription getSubnetDescription() {
        return originalConnection.getConnectionDescription();
    }

    @Override
    public SubnetDescription connectionLost(final STLConnection conn) {
        if (FO_DEBUG) {
            System.out.println("FailoverManager - connectionLost called: "
                    + System.currentTimeMillis() / 1000);
        }

        SubnetDescription snDescription = null;

        originalConnection = conn;
        stopFailover = false;
        SubnetDescription subnetDescn = conn.getConnectionDescription();
        long subnetID = subnetDescn.getSubnetId();
        backupSMs = subnetDescn.getFEList();
        subnetManagerRecords = subnetDescn.getSMList();
        progressIncrement = getProgressIncrement(backupSMs.size());
        List<STLConnection> connections = null;

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

        // This is the main fail-over loop. It creates a list of
        // STLConnections to hosts that are potential new connections to
        // the subnet. It then walks down that list looking for a working
        // connection to the correct subnet (with a working PM). It will
        // continue walking the list until a new subnet connection is found
        // or re-tries are exhausted.
        int j = 0;
        while ((j++ < SM_FAILOVER_RETRIES) && (stopFailover == false)) {
            try {

                // Create list of STLConnections to back-up FE hosts.
                connections = fillSTLConnectionList(conn);

                // Process the list of STLConnections.
                if ((connections != null) && (connections.isEmpty() == false)) {
                    // Walk through each STLConnection looking for a new
                    // connection to the SM/PM.
                    Iterator<STLConnection> connIterator =
                            connections.iterator();
                    while (connIterator.hasNext() && (stopFailover == false)) {
                        STLConnection connection = connIterator.next();

                        // Check for presence of correct SM and presence of PM.
                        failoverSuccessful = processSTLConnection(connection);

                        if (failoverSuccessful == true) {
                            // Update index in SubnetDescription for return to
                            // caller.
                            if (FO_DEBUG) {
                                System.out
                                        .println("FailoverManager - Updating SubnetDescription.");
                            }
                            snDescription =
                                    connection.getConnectionDescription();
                            snDescription.setSubnetId(subnetID);
                            stopFailover = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                if (FO_DEBUG) {
                    System.out
                            .println("FailoverManager - Exception in retry loop.");
                }
                e.printStackTrace();
            } finally {
                // Close STLConnections.
                if (FO_DEBUG) {
                    System.out
                            .println("FailoverManager - Clean up STLConnections.");
                }
                cleanSTLConnections(connections);

                try {
                    // Update progress and wait for period before starting the
                    // loop again.
                    if (failoverSuccessful == false) {

                        listener.onFailoverProgress(new ApplicationEvent(
                                STLMessages.STL64002_SM_FAILOVER_CONNECTION_ATTEMPT
                                        .getDescription(Integer.toString(j))));
                        listener.onFailoverProgress(new ApplicationEvent(
                                progressIncrement));

                        if (FO_DEBUG) {
                            System.out
                                    .println("FailoverManager - connectionLost sleep: "
                                            + System.currentTimeMillis() / 1000);
                        }
                        Thread.sleep(SM_FAILOVER_WAIT);
                    }
                } catch (Exception e) {
                    log.error(StringUtils.getErrorMessage(e), e);
                    if (FO_DEBUG) {
                        System.out
                                .println("FailoverManager - connectionLost InterruptedException.");
                    }
                    e.printStackTrace();
                }
            }

        } // End of while ((j < RETRIES) && (failoverSuccessful ==
          // false...

        // Take action based on fail-over success or failure. If successful,
        // return SubnetDescriprtion with new current master. Otherwise, throw
        // an exception.
        if (failoverSuccessful == true) {
            if (FO_DEBUG) {
                System.out
                        .println("FailoverManager - Failover successful. Return new SM master.");
            }
            log.info("FailoverManager - Failover successful. Return new SM master.");
            return snDescription;
        } else {
            if (FO_DEBUG) {
                System.out
                        .println("FailoverManager - Failover unsuccessful. Throw exception.");
            }
            SMFailoverException fe =
                    new SMFailoverException(
                            STLMessages.STL64000_SM_FAILOVER_UNSUCCESSFUL);
            log.error(StringUtils.getErrorMessage(fe), fe);
            throw fe;
        }
    } // End of connectionLost.

    // Clean up connection-related infrastructure.
    protected void cleanSTLConnections(List<STLConnection> conns) {
        // Close connections.
        if ((conns != null) && (conns.isEmpty() == false)) {
            for (STLConnection connection : conns) {
                if (FO_DEBUG) {
                    System.out
                            .println("FailoverManager - Close STLConnection; "
                                    + connection);
                }
                try {
                    connection.resetConnection(0);
                } catch (Exception e) {
                    if (FO_DEBUG) {
                        System.out
                                .println("FailoverManager - Exception while closing; "
                                        + connection);
                    }
                    e.printStackTrace();
                }
            }
            conns.clear();
        }
    }

    // Determine if the information from the connected subnet matches the
    // originally connected subnet.
    protected boolean isCorrectSubnet(List<SMRecordBean> origSmRecords,
            List<SMRecordBean> smRecords) {
        // Return true if:
        // - any GUID in smRecords matches a GUID in origSmRecords
        // - origSmRecords is null
        // - origSmRecords is empty (we are connecting for the first time).
        //
        // Return false otherwise.
        boolean isFound = false;
        if (FO_DEBUG) {
            System.out.println("FailoverManager - foundCorrectSubnet called.");
        }
        if ((origSmRecords != null) && (origSmRecords.isEmpty() == false)) {
            if ((smRecords != null) && (smRecords.isEmpty() == false)) {
                ArrayList<SMRecordBean> smRecordsAL =
                        (ArrayList<SMRecordBean>) smRecords;
                Iterator<SMRecordBean> smRecordsALIterator =
                        smRecordsAL.iterator();
                // Compare GUIDS in both lists.
                while (smRecordsALIterator.hasNext() && (isFound != true)) {
                    SMRecordBean smRecordsALBean = smRecordsALIterator.next();
                    Iterator<SMRecordBean> subnetManagerRecordsIterator =
                            origSmRecords.iterator();
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
            if (FO_DEBUG) {
                System.out
                        .println("FailoverManager - foundCorrectSubnet isFound = true.");
            }
            isFound = true;
        }

        return isFound;
    }

    // Calculate increment for progress bar for each pass through re-try loop.
    protected double getProgressIncrement(int feListSize) {
        // Note that the total is set in
        // FabricController->onSubnetManagerConnectionLost.
        double increment;
        if (feListSize > 0) {
            increment = (double) feListSize / (double) SM_FAILOVER_RETRIES;
        } else {
            increment = 0.0;
        }
        return increment;
    }

    // Tests the STLConnection to see if an FM exists on the other end.
    // Returns true if:
    // A host with an FM on the correct subnet has has been found.
    // The PM is available and responding to queries.
    private boolean processSTLConnection(STLConnection connection) {
        boolean isHostFound = false;
        try {
            if (connection != null && connection.isConnected() == true) {
                SAHelper helper = new SAHelper(connection.createStatement());

                // Once connected, send the query for the list of SMs.
                if (FO_DEBUG) {
                    System.out
                            .println("FailoverManager - verify correct subnet connected.");
                }
                List<SMRecordBean> smRecords = helper.getSMs();
                if (FO_DEBUG) {
                    System.out
                            .println("FailoverManager - received list of SMs.");
                }
                if (isCorrectSubnet(subnetManagerRecords, smRecords) == true) {
                    // We are connected to the correct subnet. Now check for
                    // the availability of the PM.
                    if (FO_DEBUG) {
                        System.out
                                .println("FailoverManager - foundCorrectSubnet = true - check for PM.");
                    }
                    PAHelper paHelper =
                            new PAHelper(connection.createStatement());
                    PMConfigBean configBean = paHelper.getPMConfig();
                    if (configBean != null) {
                        if (FO_DEBUG) {
                            System.out
                                    .println("FailoverManager - PM available.");
                        }
                        isHostFound = true;
                    } else {
                        if (FO_DEBUG) {
                            System.out
                                    .println("FailoverManager - PM unavailable.");
                        }
                    }
                } else {
                    if (FO_DEBUG) {
                        System.out
                                .println("FailoverManager - foundCorrectSubnet = false.");
                    }
                }
            } else {
                if (FO_DEBUG) {
                    System.out
                            .println("FailoverManager - processSTLConnection - connection is not connected.");
                }
            }
        } catch (IOException e) {
            if (FO_DEBUG) {
                System.out
                        .println("FailoverManager - processSTLConnection - IOException.");
            }
            log.error(StringUtils.getErrorMessage(e), e);
        } catch (Exception e) {
            if (FO_DEBUG) {
                System.out
                        .println("FailoverManager - processSTLConnection - Exception.");
            }
            log.error(StringUtils.getErrorMessage(e), e);
        }
        return isHostFound;
    }

    // From the list of back-up SM's, create a list of STLConnections
    // to those hosts.
    protected List<STLConnection> fillSTLConnectionList(
            STLConnection startingConnection) {

        List<STLConnection> connections = new ArrayList<STLConnection>();
        int size =
                startingConnection.getConnectionDescription().getFEList()
                        .size();
        STLConnection connection = null;

        for (int i = 0; i < size; i++) {
            SubnetDescription subnet =
                    startingConnection.getConnectionDescription().copy();
            subnet.setCurrentFEIndex(i);
            if (isConnectableHost(subnet) == true) {
                if (FO_DEBUG) {
                    System.out.println("FailoverManager - host is reachable.");
                }
                try {
                    connection = helper.tryConnect(subnet);
                    if ((connection != null)
                            && (connection.waitForConnect() == true)) {

                        if (FO_DEBUG) {
                            System.out
                                    .println("FailoverManager - add connection to list: "
                                            + connection);
                        }
                        connections.add(connection);
                    } else {
                        // If waitForConnect fails, we skip this connection on
                        // this re-try. With the current implementation of
                        // tryConnect, we should never get here but we'll do the
                        // close for completeness.
                        if (connection != null) {
                            connection.close();
                        }

                    }
                } catch (IOException e) {
                    if (FO_DEBUG) {
                        System.out
                                .println("FailoverManager - fillSTLConnectionList - IOException.");
                    }
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
        }
        return connections;
    }

    @Override
    public SubnetDescription connectionLost(SubnetDescription subnet,
            IFailoverProgressListener listener) {
        return null;
    }

    protected boolean isConnectableHost(SubnetDescription subnet) {

        boolean connectable = false;
        Selector selector;
        SocketChannel socketChannel = null;
        String host = subnet.getCurrentFE().getHost();
        int port = subnet.getCurrentFE().getPort();

        try {
            // Open the selector
            selector = Selector.open();

            // Create a non-blocking socket channel
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            // Register the connect key
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            // Attempt to connect
            socketChannel.connect(new InetSocketAddress(host, port));

            // Wait for the channel to be selected
            if (selector.select(SM_SELECTOR_EVENT_TIMEOUT) > 0) {

                // Finish the connect operation
                connectable = socketChannel.finishConnect();
            }

        } catch (IOException e) {
        } catch (ClosedSelectorException e) {
        } catch (UnresolvedAddressException e) {
        } finally {
            try {
                // Close the socket
                if (socketChannel != null) {
                    socketChannel.close();
                }
            } catch (IOException e) {
            }
        }
        return connectable;
    }
} // End of FailoverManager.
