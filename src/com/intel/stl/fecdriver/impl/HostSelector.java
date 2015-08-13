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

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: HostSelector.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:22:10  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/16 20:12:09  jijunwan
 *  Archive Log:    fixed a RLK.NIO issue reported by Klockwork
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/09 22:49:21  jijunwan
 *  Archive Log:    a new FailoverManager based on the old one
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
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

import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.IFailoverEventListener;

public class HostSelector {
    private final STLAdapter adapter;

    private final long timeout;

    /**
     * Description:
     * 
     * @param hosts
     * @param subnet
     */
    public HostSelector(STLAdapter adapter, long timeout) {
        super();
        this.adapter = adapter;
        this.timeout = timeout;
    }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    public HostInfo getHost(List<HostInfo> hosts, SubnetDescription subnet,
            IFailoverEventListener listener) {
        Selector selector = null;
        try {
            selector = Selector.open();
            List<SocketChannel> channels = registerChannels(selector, hosts);
            try {
                if (!channels.isEmpty()) {
                    HostInfo res =
                            getFirstValidHost(selector, hosts, subnet, listener);
                    return res;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                for (SocketChannel channel : channels) {
                    try {
                        channel.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    protected List<SocketChannel> registerChannels(Selector selector,
            List<HostInfo> hosts) throws IOException {
        List<SocketChannel> res = new ArrayList<SocketChannel>(hosts.size());
        for (HostInfo host : hosts) {
            // Create regular socketConnection.
            System.out
                    .println("FailoverManager - Create the regular SockectChannel.");
            SocketChannel channel = SocketChannel.open();
            try {
                // Configure socket and connect.
                channel.configureBlocking(false);
                try {
                    channel.connect(new InetSocketAddress(host.getHost(), host
                            .getPort()));
                } catch (Exception e) {
                    channel.close();
                    System.out
                            .println("FailoverManager - Unresolved address in list of back-ups.");
                    e.printStackTrace();
                }
                if (channel.isOpen() == true) {
                    // We care about connection event only.
                    channel.register(selector, SelectionKey.OP_CONNECT, host);
                }
            } finally {
                res.add(channel);
            }
        }
        return res;
    }

    protected HostInfo getFirstValidHost(Selector selector,
            List<HostInfo> hosts, SubnetDescription subnet,
            IFailoverEventListener listener) throws IOException {
        HostInfo res = null;
        while (true) {
            // Block waiting for a timeout or a connection event.
            if (selector.select(timeout) > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey key : selectedKeys) {
                    HostInfo host = (HostInfo) key.attachment();
                    reportProgress("Connect host " + host.getHost() + ":"
                            + host.getPort(), listener);
                    if (key.isConnectable()) {
                        SocketChannel socketChannel =
                                (SocketChannel) key.channel();
                        // A connection was established with a remote
                        // server.
                        System.out
                                .println("FailoverManager - Connection isConnectable event.");
                        try {
                            if (socketChannel.finishConnect() == true) {
                                System.out
                                        .println("FailoverManager - Connection was established with a remote server.");
                                key.cancel();
                                subnet.setCurrentFEIndex(hosts.indexOf(host));
                                if (isValidConnection(socketChannel, subnet)) {
                                    res = host;
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            System.out
                                    .println("FailoverManager - Exception on SockectChannel.finishConnect().");
                            e.printStackTrace();
                        }
                    } else {
                        reportProgress("Unconnectabel host " + host.getHost()
                                + ":" + host.getPort(), listener);
                        key.cancel();
                        System.out
                                .println("FailoverManager - Unconnectabel host "
                                        + key.attachment());
                    }
                }
            } else {
                // Timeout has occurred.
                System.out
                        .println("FailoverManager - Connection timeout has occurred.");
                break;
            }
        }
        return res;
    }

    protected boolean isValidConnection(SocketChannel channel,
            SubnetDescription subnet) {
        // Change current FE in
        // SubnetDescription to get connection
        // to active FE.
        STLConnection connection = null;
        try {
            connection = adapter.tryConnect(subnet);
            if (connection != null && connection.waitForConnect() == true) {
                SAHelper helper = new SAHelper(connection);
                List<SMRecordBean> subnetManagerRecords = subnet.getSMList();
                return hasCommandSM(subnetManagerRecords, helper.getSMs());
            }

            System.out
                    .println("FailoverManager - STLConnection waitForConnection timeout.");
        } catch (IOException e) {
            System.out
                    .println("FailoverManager - STLConnection waitForConnection Exception.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out
                    .println("FailoverManager - STLConnection getSMs timeout exception.");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    // Determine if the information from the connnected subnet matches the
    // originally connected subnet.
    protected boolean hasCommandSM(List<SMRecordBean> lst1,
            List<SMRecordBean> lst2) {
        // Return true if:
        // - any GUID in smRecords matches a GUID in subnetRecords
        // - subnetRecords is null
        // - subnetRecords is empty (we are connecting for the first time.
        //
        // Return false otherwise.
        boolean isFound = false;
        if ((lst1 != null) && (lst1.isEmpty() == false)) {
            if ((lst2 != null) && (lst2.isEmpty() == false)) {
                ArrayList<SMRecordBean> smRecordsAL =
                        (ArrayList<SMRecordBean>) lst2;
                Iterator<SMRecordBean> smRecordsALIterator =
                        smRecordsAL.iterator();
                // Compare GUIDS in both lists.
                while (smRecordsALIterator.hasNext() && (isFound != true)) {
                    SMRecordBean smRecordsALBean = smRecordsALIterator.next();
                    Iterator<SMRecordBean> subnetManagerRecordsIterator =
                            lst1.iterator();
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
            isFound = true;
        }

        return isFound;
    }

    protected void reportProgress(String msg, IFailoverEventListener listener) {
        ApplicationEvent event = new ApplicationEvent(msg);
        listener.onFailoverProgress(event);
    }
}
