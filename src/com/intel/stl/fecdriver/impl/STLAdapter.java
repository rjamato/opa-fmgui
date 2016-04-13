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
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLHandshakeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.ISecurityHandler;
import com.intel.stl.api.failure.BaseFailureEvaluator;
import com.intel.stl.api.failure.BaseTaskFailure;
import com.intel.stl.api.failure.FailureManager;
import com.intel.stl.api.failure.IFailureEvaluator;
import com.intel.stl.api.failure.IFailureManagement;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetConfigurationException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.Constants;
import com.intel.stl.common.STLMessages;
import com.intel.stl.configuration.AsyncTask;
import com.intel.stl.configuration.CachedProcessingService;
import com.intel.stl.configuration.FecServiceThreadFactory;
import com.intel.stl.configuration.ProcessingService;
import com.intel.stl.configuration.ResultHandler;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.FEResourceAdapter;
import com.intel.stl.fecdriver.IApplicationEventListener;
import com.intel.stl.fecdriver.IFailoverHelper;
import com.intel.stl.fecdriver.IFailoverManager;
import com.intel.stl.fecdriver.IFailoverProgressListener;
import com.intel.stl.fecdriver.adapter.IAdapter;
import com.intel.stl.fecdriver.adapter.ISMEventListener;
import com.intel.stl.fecdriver.dispatcher.IConnectionEventListener;
import com.intel.stl.fecdriver.session.ISession;

/**
 * A simple pool strategy here. Should improve it later.
 */
public class STLAdapter implements FEResourceAdapter,
        IApplicationEventListener, IFailoverHelper, IAdapter {
    public final static String WORKER_THREAD_NAME = "fec-thread";

    public final static String VERSION = "1.0";

    private static Logger log = LoggerFactory.getLogger(STLAdapter.class);

    private int loginTimeout = 0;

    private int poolSize = 10;

    private Selector selector;

    private final ConcurrentLinkedQueue<STLConnection> newConnections =
            new ConcurrentLinkedQueue<STLConnection>();

    private final ConcurrentHashMap<SubnetDescription, List<STLConnection>> cachedConnections =
            new ConcurrentHashMap<SubnetDescription, List<STLConnection>>();

    private Thread worker;

    private boolean toStop, stopped;

    private IFailureManagement failureMgr;

    private final BaseFailureEvaluator failureEvaluator;

    private IFailoverManager simFailoverMgr;

    private static volatile STLAdapter instance = null;

    private ICertsAssistant certsAssistant;

    private ISecurityHandler securityHandler;

    private ProcessingService processor;

    private List<IFailoverManager> failoverManagers = null;

    private STLAdapter() {
        failureEvaluator = new BaseFailureEvaluator();
        failureEvaluator.setRecoverableErrors(IOException.class,
                RuntimeException.class);
        failureEvaluator.setUnrecoverableErrors(ClosedChannelException.class,
                SSLHandshakeException.class);
        failoverManagers = new CopyOnWriteArrayList<IFailoverManager>();

        init();

    }

    public static STLAdapter instance() {
        if (instance == null) {
            synchronized (STLAdapter.class) {
                if (instance == null) {
                    instance = new STLAdapter();
                }
            }
        }
        return instance;
    }

    public synchronized void open() {
        log.info("Opening adapter " + this);
        if (worker != null) {
            log.info("Adapter already opened.");
            return;
        }
        toStop = stopped = false;

        init();
    }

    protected void init() {
        try {
            selector = Selector.open();
            worker = new Thread(new Worker());
            worker.setName(WORKER_THREAD_NAME);
            worker.setDaemon(true);
            worker.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        failureMgr = FailureManager.getManager();
        ThreadFactory threadFactory = new FecServiceThreadFactory();
        this.processor = new CachedProcessingService(threadFactory);
    }

    @Override
    public int getProtocolVersion() {
        return Constants.PROTOCAL_VERSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.driver.IDriver#getVersion()
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * @return the poolSize
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * @param poolSize
     *            the poolSize to set
     */
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * <i>Description:</i>
     * 
     * @param assistant
     */
    public void registerCertsAssistant(ICertsAssistant assistant) {
        certsAssistant = assistant;
    }

    public void clearCertsInfoFor(SubnetDescription subnet) {
        if (certsAssistant != null) {
            certsAssistant.clearSubnetFactories(subnet);
        }
    }

    public void registerSecurityHandler(ISecurityHandler securityHandler) {
        this.securityHandler = securityHandler;
    }

    @Override
    public ISession createSession(SubnetDescription subnet) throws IOException {
        return createSession(subnet, null);
    }

    @Override
    public ISession createSession(SubnetDescription subnet,
            ISMEventListener listener) throws IOException {
        STLConnection subnetApiConn = connect(subnet);
        STLConnection perfApiConn = connect(subnet);
        STLConnection noticeApiConn = connect(subnet);
        if (listener != null) {
            noticeApiConn.addNoticeListener(listener);
        }
        subnetApiConn.waitForConnect();
        perfApiConn.waitForConnect();
        noticeApiConn.waitForConnect();
        return new ConnectionSession(subnetApiConn, perfApiConn, noticeApiConn);
    }

    @Override
    public STLConnection connect(SubnetDescription subnet) throws IOException {
        return connect(subnet, null);
    }

    @Override
    public STLConnection connect(SubnetDescription subnet, Properties info)
            throws IOException {
        List<STLConnection> conns = cachedConnections.get(subnet);
        STLConnection conn = null;
        if (conns == null) {
            final List<STLConnection> tmpConns =
                    Collections
                            .synchronizedList(new ArrayList<STLConnection>());
            conns = cachedConnections.putIfAbsent(subnet, tmpConns);
            if (conns == null) {
                conns = tmpConns;
            }
        }

        // Check first if there is a cached connection with no usage
        for (STLConnection cachedConn : conns) {
            if (cachedConn.getNumHolders() == 0) {
                cachedConn.increaseNumHolders();
                conn = cachedConn;
                return conn;
            }
        }
        if (conns.size() < poolSize) {
            // All connections a subnet share the same instance
            SubnetDescription connSubnet = getConnectionDescriptionFor(subnet);
            if (connSubnet != null) {
                conn = createConnection(connSubnet);
                conn.initSocketChannel();
                synchronized (this) {
                    newConnections.add(conn);
                    selector.wakeup();
                    conns.add(conn);
                }
                log.info("Created connection " + conn);
            } else {
                // This should never happen
                throw new IllegalArgumentException(
                        "No connections found for subnet " + subnet.getName());
            }
        } else {
            int minHolders = Integer.MAX_VALUE;
            for (STLConnection cachedConn : conns) {
                if (cachedConn.getNumHolders() < minHolders) {
                    minHolders = cachedConn.getNumHolders();
                    conn = cachedConn;
                }
            }
            if (conn != null) {
                conn.increaseNumHolders();
            } else {
                // this shouldn't happen
                throw new RuntimeException(
                        "No existing connection! Size of condistate connections is "
                                + conns.size() + ", poolSize is " + poolSize);
            }
            log.info("Reuse connection " + conn);
        }
        return conn;
    }

    @Override
    public void setLoginTimeout(int seconds) {
        loginTimeout = seconds;
    }

    @Override
    public int getLoginTimeout() {
        return loginTimeout;
    }

    public IFailoverManager getFailoverManager() {
        if (simFailoverMgr != null) {
            return simFailoverMgr;
        } else {
            return new FailoverManager(this);
        }
    }

    @Override
    public STLConnection tryConnect(SubnetDescription subnet)
            throws IOException {
        if (subnet == null) {
            throw new IllegalArgumentException("Subnet cannot be null");
        }

        SubnetDescription tempSubnet = subnet.copy();
        tempSubnet.setSubnetId(0);
        STLConnection conn = createConnection(tempSubnet);
        conn.initSocketChannel();
        synchronized (this) {
            newConnections.add(conn);
            selector.wakeup();
        }
        log.info("Created temporary connection " + conn);
        return conn;
    }

    public SubnetDescription checkConnectivityFor(SubnetDescription subnet) {
        STLConnection conn =
                new STLConnection(subnet, securityHandler, certsAssistant);
        return getFailoverManager().connectionLost(conn);

    }

    public void refreshSubnetDescription(SubnetDescription subnet) {
        SubnetDescription connSubnet = getConnectionDescriptionFor(subnet);
        if (connSubnet != null) {
            connSubnet.setName(subnet.getName());
            connSubnet.setPrimaryFEIndex(subnet.getPrimaryFEIndex());
            HostInfo currFE = connSubnet.getCurrentFE();
            List<HostInfo> feList = subnet.getFEList();
            int currFEIdx = -1;
            for (int i = 0; i < feList.size(); i++) {
                if (currFE.equals(feList.get(i))) {
                    currFEIdx = i;
                    break;
                }
            }
            connSubnet.setFEList(feList);
            if (currFEIdx >= 0) {
                connSubnet.setCurrentFEIndex(currFEIdx);
            } else {
                connSubnet.setCurrentFEIndex(subnet.getPrimaryFEIndex());
            }
        }
    }

    private STLConnection createConnection(SubnetDescription subnet) {
        STLConnection conn =
                new STLConnection(subnet, securityHandler, certsAssistant);
        conn.addApplicationEventListener(this);
        return conn;
    }

    private SubnetDescription getConnectionDescriptionFor(
            SubnetDescription subnet) {
        for (SubnetDescription thisSubnet : cachedConnections.keySet()) {
            if (thisSubnet.equals(subnet)) {
                return thisSubnet;
            }
        }
        return null;
    }

    @Override
    public void connectionClose(ApplicationEvent event) {
        STLConnection conn = (STLConnection) event.getSource();
        List<STLConnection> conns =
                cachedConnections.get(conn.getConnectionDescription());
        if (conns == null) {
            // This is a trial connection, close it
            closeConnection(conn);
        } else {
            if (!conns.contains(conn)) {
                // This is a trial connection, close it
                closeConnection(conn);
            }
        }
    }

    @Override
    public void connectionTimeout(ApplicationEvent event) {
        STLConnection conn = (STLConnection) event.getSource();
        List<STLConnection> conns =
                cachedConnections.get(conn.getConnectionDescription());
        if (conns == null) {
            // This is a trial connection, do nothing

        } else {
            if (!conns.contains(conn)) {
                // This is a trial connection, do nothing
            } else {
                handleConnectionError(conn, conn.getSocketChannel(),
                        new TimeoutException());
            }
        }
    }

    @Override
    public void connectionDescriptionChanged(ApplicationEvent event) {
        STLConnection conn = (STLConnection) event.getSource();
        SubnetDescription subnet = conn.getConnectionDescription();
        HostInfo newHost = subnet.getCurrentFE();
        HostInfo currHost = conn.getConnectionHost();
        if (currHost != null && !currHost.equals(newHost)) {
            // There has been a change in the host definition. Reset all
            // connections first
            resetConnections(subnet);
            // And now reset socketChannels
            resetChannels(subnet);
        }
    }

    protected synchronized void resetConnections(SubnetDescription subnet) {
        long waitExtension = getFailoverManager().getFailoverTimeout();

        List<STLConnection> conns = cachedConnections.get(subnet);
        if (conns != null) {
            // There are connections to reset
            synchronized (conns) {
                for (STLConnection conn : conns) {
                    resetConnection(conn, waitExtension);
                }
            }
        }
    }

    private void resetConnection(STLConnection conn, long waitExtension) {
        SocketChannel socketChannel = conn.getSocketChannel();
        if (socketChannel != null) {
            SelectionKey key = socketChannel.keyFor(selector);
            if (key != null) {
                key.cancel();
            }
        }
        conn.resetConnection(waitExtension);
    }

    protected synchronized void resetChannels(SubnetDescription subnet) {
        List<STLConnection> conns = cachedConnections.get(subnet);
        if (conns != null) {
            synchronized (conns) {
                for (STLConnection conn : conns) {
                    try {
                        conn.setReconnecting(false);
                        conn.initSocketChannel();
                        newConnections.add(conn);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            selector.wakeup();
        }
    }

    private void closeConnection(STLConnection conn) {
        try {
            cancelKey(conn);
        } finally {
            conn.resetConnection(0);
            synchronized (this) {
                selector.wakeup();
            }
            log.info("Closed connection " + conn);
        }
    }

    private synchronized void cancelKey(STLConnection conn) {
        SocketChannel socketChannel = conn.getSocketChannel();
        if (socketChannel != null) {
            SelectionKey key = socketChannel.keyFor(selector);
            if (key != null) {
                key.cancel();
            }
        }
    }

    protected void processSelectionKey(SelectionKey selKey) {
        // Since the ready operations are cumulative,
        // need to check readiness for each operation
        if (selKey.isValid() && selKey.isConnectable()) {
            handleConnectionFinish(selKey);
        }
        if (selKey.isValid() && selKey.isReadable()) {
            handleConnectionRead(selKey);
        }
        if (selKey.isValid() && selKey.isWritable()) {
            handleConnectionWrite(selKey);
        }
    }

    /*-
     * Handles processing after a connection is established with the FE. The
     * following flow is followed:
     * 
     * 1. A separate thread is started to get user and/or passwords for signon/SSL
     *    handshake. This is done so that a UI request for password from the
     *    ICertsAssistant doesn't hold up the STLAdapter worker thread.
     * 2. prepareHandshake() is invoked for the connection. This might require a
     *    UI request.
     * 3. beginHandshake() is invoked from the same thread. This puts the connection
     *    in handshake-in-progress state and invokes processHandshake to begin the
     *    handshake protocol.
     * 4. The STLAdapter worker thread may invoke processHandshake() several times 
     *    to complete the handshake protocol. Method processHandshake() is responsible
     *    to exchange the necessary messages with the server to complete the handshake
     *    process. When the handshake is complete, it must call handshakeComplete().
     * 5. handshakeComplete() takes the connection out of handshake-in-progress state
     *    and notifies any waiting threads that the connection is complete. 
     * 
     */
    private void handleConnectionFinish(SelectionKey selKey) {
        SocketChannel sChannel = (SocketChannel) selKey.channel();
        STLConnection conn = (STLConnection) selKey.attachment();
        try {
            if (sChannel.isConnectionPending()) {
                boolean success = sChannel.finishConnect();
                if (success) {
                    submitPrepareTask(selKey);
                } else {
                    log.debug("SocketChannel.finishConnect() returned false: "
                            + sChannel);
                }
            }
        } catch (Exception e) {
            log.debug(
                    "Connection setup has thrown an exception: "
                            + e.getMessage(), e);
            handleConnectionError(conn, sChannel, e);
        }
    }

    private void submitPrepareTask(final SelectionKey selKey) {
        PrepareHandshakeTask completeConnTask =
                new PrepareHandshakeTask(selKey);
        processor.submit(completeConnTask, new ResultHandler<STLConnection>() {

            @Override
            public void onTaskCompleted(Future<STLConnection> result) {
                try {
                    STLConnection conn = result.get();
                    conn.beginHandshake();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (ExecutionException e) {
                    Exception cause = (Exception) e.getCause();
                    SocketChannel sChannel = (SocketChannel) selKey.channel();
                    STLConnection conn = (STLConnection) selKey.attachment();
                    log.debug(
                            "Handshake setup for connection has thrown an exception: "
                                    + e.getMessage(), cause);
                    handleConnectionError(conn, sChannel, cause);
                } catch (Exception e) {
                    SocketChannel sChannel = (SocketChannel) selKey.channel();
                    STLConnection conn = (STLConnection) selKey.attachment();
                    log.debug(
                            "Handshake setup for connection has thrown an exception: "
                                    + e.getMessage(), e);
                    handleConnectionError(conn, sChannel, e);
                }
            }

        });
    }

    private void handleConnectionRead(SelectionKey selKey) {
        // Get channel with bytes to read
        STLConnection conn = (STLConnection) selKey.attachment();
        try {
            conn.read();
        } catch (ClosedChannelException cce) {
            SocketChannel sChannel = (SocketChannel) selKey.channel();
            selKey.cancel();
            log.error("FE server has closed connection " + conn);
            handleConnectionError(conn, sChannel, cce);
        } catch (SubnetConfigurationException sce) {
            SocketChannel sChannel = (SocketChannel) selKey.channel();

            if (sce.getErrorCode() == STLMessages.STL20004_SM_UNAVAILABLE
                    .getErrorCode()) {
                log.error("SM is unavailable through connection " + conn);
            } else if (sce.getErrorCode() == STLMessages.STL20005_PM_UNAVAILABLE
                    .getErrorCode()) {
                log.error("PM is unavailable through connection " + conn);
            }
            selKey.cancel();
            handleConnectionError(conn, sChannel, sce);
        } catch (Exception e) {
            handleRequestError(selKey, e);
        }
    }

    private void handleConnectionWrite(SelectionKey selKey) {
        // Get channel that's ready for more bytes
        STLConnection conn = (STLConnection) selKey.attachment();
        try {
            conn.write();
        } catch (ClosedChannelException cce) {
            SocketChannel sChannel = (SocketChannel) selKey.channel();
            selKey.cancel();
            handleConnectionError(conn, sChannel, cce);
        } catch (Exception e) {
            handleRequestError(selKey, e);
        }
    }

    private void handleConnectionError(STLConnection conn,
            SocketChannel socketChannel, Exception e) {
        SubnetDescription subnet = conn.getConnectionDescription();
        boolean isFailoverInProgress = subnet.isFailoverInProgress();
        log.info("STLAdapter - handleConnectionError");
        if (!conn.isReconnecting()) {
            List<STLConnection> conns = cachedConnections.get(subnet);
            if (conns != null) {
                if (!isFailoverInProgress) {
                    if (subnet
                            .setFailoverInProgress(isFailoverInProgress, true)) {
                        log.info("Starting failover processing for subnet "
                                + subnet.getName());
                        conn.fireFailoverStart();
                        resetConnections(subnet);
                        submitFailoverTask(conn, e);
                    } else {
                        log.info("Another thread has just started failover processing for subnet "
                                + subnet.getName());

                    }
                } else {
                    log.info("Failover is already processing for subnet "
                            + subnet.getName());
                    // This a new connection created after failover was started.
                    long waitExtension =
                            getFailoverManager().getFailoverTimeout();
                    resetConnection(conn, waitExtension);
                }
            } else {
                // This is for temporary connections
                conn.setConnectError(e);
                conn.setConnected(false);
            }
        } else {
            conn.setConnectError(e);
            conn.setConnected(false);
        }
    }

    private void submitFailoverTask(final STLConnection conn,
            final Exception originalException) {
        final SubnetDescription subnet = conn.getConnectionDescription();
        final List<STLConnection> conns = cachedConnections.get(subnet);
        if (conns != null) {
            final IFailoverManager failoverManager = getFailoverManager();
            failoverManagers.add(failoverManager);
            FailoverTask failoverTask = new FailoverTask(failoverManager, conn);
            processor.submit(failoverTask,
                    new ResultHandler<SubnetDescription>() {

                        @Override
                        public void onTaskCompleted(
                                Future<SubnetDescription> result) {
                            Throwable cause = null;
                            try {
                                SubnetDescription newsubnet = result.get();
                                resetChannels(newsubnet);
                                log.info("Failover completed for  subnet "
                                        + subnet.getName());
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            } catch (ExecutionException e) {
                                cause = e.getCause();
                                if (failoverManager != null) {
                                    failoverManagers.remove(failoverManager);
                                }
                                log.error(
                                        "Failover for subnet "
                                                + subnet.getName()
                                                + " has thrown an exception: "
                                                + cause.getMessage(), cause);
                                synchronized (conns) {
                                    Iterator<STLConnection> it =
                                            conns.iterator();
                                    while (it.hasNext()) {
                                        STLConnection conn = it.next();
                                        it.remove();
                                        conn.setConnectError(originalException);
                                        conn.setConnected(false);
                                    }
                                }
                            } finally {
                                boolean isFailoverInProgress =
                                        subnet.isFailoverInProgress();
                                subnet.setFailoverInProgress(
                                        isFailoverInProgress, false);
                                conn.fireFailoverEnd(cause);
                            }
                        }

                    });
        }
    }

    private void handleRequestError(SelectionKey selKey, Exception e) {
        SocketChannel sChannel = (SocketChannel) selKey.channel();
        STLConnection conn = (STLConnection) selKey.attachment();
        if (e instanceof IOException) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("connection was forcibly closed")) {
                // We need to send a ping request to the FE to see if connection
                // is still available; in the meantime, we do this ugly check
                selKey.cancel();
                log.error("Connection was forcibly closed", e);
                handleConnectionError(conn, sChannel, e);
                return;
            }
        }
        log.error("Error on connection " + conn + " connected to subnet "
                + conn.getConnectionDescription().getName(), e);
        conn.setConnectError(e);
        handleFailure(selKey, e);
    }

    public synchronized void close() {
        log.info("Closing adapter " + this);
        try {
            processor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            failureMgr.cleanup();
        } catch (InterruptedException e1) {
        }

        for (IFailoverManager failoverManager : failoverManagers) {
            failoverManager.stopFailover();
        }
        failoverManagers.clear();

        newConnections.clear();
        for (List<STLConnection> conns : cachedConnections.values()) {
            synchronized (conns) {
                Iterator<STLConnection> it = conns.iterator();
                while (it.hasNext()) {
                    STLConnection conn = it.next();
                    it.remove();
                    conn.close();
                    closeConnection(conn);
                }
            }
        }
        cachedConnections.clear();

        toStop = true;
        selector.wakeup();
        while (!stopped) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        worker = null;
        log.info("Closed adapter " + this);
    }

    public boolean isClosed() {
        return stopped;
    }

    class Worker implements Runnable {

        @Override
        public void run() {
            try {
                while (!toStop) {
                    try {
                        // Wait for an event
                        selector.select();
                    } catch (IOException e) {
                        // Handle error with selector
                        log.error(
                                "selector.selection exception: "
                                        + e.getMessage(), e);
                        break;
                    }

                    while (!newConnections.isEmpty()) {
                        STLConnection conn = newConnections.poll();
                        processNewConnection(conn);
                    }

                    Set<SelectionKey> keys;
                    synchronized (STLAdapter.this) {
                        keys = selector.selectedKeys();
                    }

                    // Process each key at a time
                    for (SelectionKey selKey : keys) {
                        processSelectionKey(selKey);
                    }
                    keys.clear();
                }
            } finally {
                synchronized (STLAdapter.this) {
                    try {
                        selector.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopped = true;
                    STLAdapter.this.notify();
                }
            }
        }

        private void processNewConnection(STLConnection conn) {
            SocketChannel channel = conn.getSocketChannel();
            SelectionKey selKey = null;
            try {
                if (channel != null) {
                    selKey =
                            channel.register(selector, channel.validOps(), conn);
                }
            } catch (ClosedChannelException cce) {
                log.error("SocketChannel has been closed: " + cce.getMessage(),
                        cce);
                handleConnectionError(conn, channel, cce);
                return;
            } catch (Exception e) {
                log.error(
                        "Exception occurred while registering selector to channel: "
                                + channel, e);
                handleConnectionError(conn, channel, e);
                return;
            }
            try {
                conn.connect();
                log.debug("Register " + conn);
            } catch (Exception e) {
                log.error("Exception occurred while connecting channel to "
                        + conn.getConnectionHost(), e);
                if (selKey != null) {
                    selKey.cancel();
                }
                handleConnectionError(conn, channel, e);
            }
        }
    }

    protected void handleFailure(final SelectionKey selKey, final Exception e) {
        BaseTaskFailure<Void> taskFailure =
                new BaseTaskFailure<Void>(selKey, failureEvaluator) {

                    @Override
                    public Callable<Void> getTask() {
                        // we do NOT retry!
                        return null;
                    }

                    @Override
                    public void onFatal() {
                        log.info("Fatal Failure - Close connection!");
                        STLConnection conn =
                                (STLConnection) selKey.attachment();
                        conn.close();
                        selKey.cancel();
                    }

                };
        failureMgr.submit(taskFailure, e);
    }

    public void stopFailoverInstance(SubnetDescription desc) {
        if (desc != null) {
            for (IFailoverManager failoverManager : failoverManagers) {
                SubnetDescription description =
                        ((FailoverManager) failoverManager)
                                .getSubnetDescription();
                if (description.equals(desc)) {
                    failoverManager.stopFailover();
                }
            }
        }
    }

    private class PrepareHandshakeTask extends AsyncTask<STLConnection> {

        private final SelectionKey selKey;

        public PrepareHandshakeTask(SelectionKey selKey) {
            this.selKey = selKey;
        }

        @Override
        public STLConnection process() throws Exception {
            // Get channel with connection request
            STLConnection conn = (STLConnection) selKey.attachment();
            conn.prepareForHandshake();
            return conn;
        }

    }

    private class FailoverTask extends AsyncTask<SubnetDescription> {

        private final STLConnection conn;

        private final IFailoverManager failoverMgr;

        public FailoverTask(IFailoverManager failoverMgr, STLConnection conn) {
            this.failoverMgr = failoverMgr;
            this.conn = conn;
        }

        @Override
        public SubnetDescription process() throws Exception {
            SubnetDescription connSubnet = conn.getConnectionDescription();
            SubnetDescription modSubnet = failoverMgr.connectionLost(conn);
            connSubnet.setCurrentFEIndex(modSubnet.getCurrentFEIndex());
            failoverManagers.remove(failoverMgr);
            return modSubnet;
        }

    }

    // The methods below are for testing
    public void startSimulatedFailover(String subnetName) {
        FailOverSimulator simulator = new FailOverSimulator(subnetName);
        simulator.start();
        // Remove this line if you want to use the real failover manager
        simFailoverMgr = new SimulatedFailoverManager();
    }

    protected Map<SubnetDescription, List<STLConnection>> getCachedConnections() {
        return cachedConnections;
    }

    public void setSimFailoverManager(IFailoverManager failoverMgr) {
        this.simFailoverMgr = failoverMgr;
    }

    private class FailOverSimulator extends Thread {

        private static final String NAME = "fosimthread";

        private static final long DEFAULT_WAITTIME = 10000; // 30 sec

        private final long waitTime;

        private final String subnetName;

        public FailOverSimulator(String subnetName) {
            this.subnetName = subnetName;
            this.setName(NAME);
            this.waitTime = DEFAULT_WAITTIME;
        }

        @Override
        public void run() {
            System.out.println("Starting Failover simulator in 10 seconds...");
            try {
                Thread.sleep(waitTime);
                SubnetDescription subnet = null;
                Iterator<SubnetDescription> it =
                        cachedConnections.keySet().iterator();
                if (subnetName == null) {
                    if (cachedConnections.size() > 0) {
                        subnet = it.next();
                    }
                } else {
                    while (it.hasNext()) {
                        SubnetDescription tmpSubnet = it.next();
                        if (tmpSubnet.getName().equals(subnetName)) {
                            subnet = tmpSubnet;
                            break;
                        }
                    }
                }
                if (subnet != null) {
                    System.out.println("Simulating failover for subnet "
                            + subnet.getName());
                    List<STLConnection> conns = cachedConnections.get(subnet);
                    if (conns != null && conns.size() > 0) {
                        STLConnection conn = conns.get(0);
                        RuntimeException rte =
                                new RuntimeException(
                                        "Simulated exception on connection");
                        handleConnectionError(conn, conn.getSocketChannel(),
                                rte);
                    } else {
                        System.out.println("No connections found for subnet.");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /*-
     * TODO: Remove this SimulatedFailoverManager class
     * This code is for demo purposes; it does what the failover manager is
     * supposed to do, including notifying listeners of its progress. The 
     * contents of ApplicationEvent is implementation dependent (see
     * SubnetContext) and you can extend it to enable more information for
     * the UI
     * 
     */
    private class SimulatedFailoverManager implements IFailoverManager {

        @Override
        public long getFailoverTimeout() {
            return 5000;
        }

        @Override
        public void stopFailover() {

        }

        @Override
        public SubnetDescription connectionLost(STLConnection connection) {
            SubnetDescription subnet = connection.getConnectionDescription();
            List<HostInfo> feList = subnet.getFEList();
            for (HostInfo hostInfo : feList) {
                String note =
                        "Attempting to connect to " + hostInfo.getHost() + ":"
                                + hostInfo.getPort();
                ApplicationEvent event = new ApplicationEvent(note);
                connection.fireFailoverProgress(event);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Double progress = new Double(1.0);
                event = new ApplicationEvent(progress);
                connection.fireFailoverProgress(event);
            }
            return subnet;
        }

        @Override
        public SubnetDescription connectionLost(SubnetDescription subnet,
                IFailoverProgressListener listener) {
            return null;
        }
    }

    @Override
    public ISession createTemporarySession(HostInfo host,
            IConnectionEventListener listener) throws IOException {
        SubnetDescription tempSubnet = new SubnetDescription("tempsubnet");
        tempSubnet.getFEList().add(host);
        STLConnection conn = tryConnect(tempSubnet);
        return new ConnectionSession(conn, conn, conn);
    }

    @Override
    public SSLEngine getSSLEngine(HostInfo hostInfo) throws Exception {
        if (certsAssistant != null) {
            return certsAssistant.getSSLEngine(hostInfo);
        }
        return null;
    }

    @Override
    public SSLEngine getSSLEngine(HostInfo hostInfo, CertsDescription certs)
            throws Exception {
        if (certsAssistant != null) {
            return certsAssistant.getSSLEngine(hostInfo);
        }
        return null;
    }

    @Override
    public SocketChannel createChannel() {
        try {
            return SocketChannel.open();
        } catch (IOException e) {
            log.error("Error creating channel", e);
            return null;
        }
    }

    @Override
    public IFailureManagement getFailureManager() {
        return failureMgr;
    }

    @Override
    public IFailureEvaluator getFailureEvaluator() {
        return failureEvaluator;
    }

    @Override
    public <R> void submitTask(AsyncTask<R> asyncTask,
            ResultHandler<R> resultHandler) {
        // This is not implemented here
    }

    @Override
    public void shutdownSubnet(SubnetDescription subnet) {
        // This is not implemented here
    }

    @Override
    public int getNumInitialConnections() {
        return 3;
    }

    @Override
    public long getFailoverTimeout() {
        return 120000;
    }

}
