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
 *  File Name: STLConnection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.40.2.2  2015/05/06 19:29:25  jijunwan
 *  Archive Log:    Fix for wait outside a loop, which would not handle "spurious wakeups"
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/05/01 21:44:57  jijunwan
 *  Archive Log:    fixed a minor issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/04/29 17:27:54  robertja
 *  Archive Log:    Add MAD status check for SM or PM unavailable.
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/04/22 16:57:48  fernande
 *  Archive Log:    Fix for Klocwork issues
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/04/16 21:12:30  fernande
 *  Archive Log:    Fixes for Klocwork: moved SocketChannel creation to STLConnection, where the logic to close it is.
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/04/16 17:37:43  fernande
 *  Archive Log:    Setting the InetAddress in HostInfo, both the local copy and in the global SubnetDescription (in SubnetContext)
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/04/08 20:31:24  fernande
 *  Archive Log:    Adding support for Timeout exception going to failover processing.
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/04/08 15:17:33  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/04/06 21:16:51  fernande
 *  Archive Log:    Improving the handling of connection errors.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/06 11:04:55  jypak
 *  Archive Log:    Klockwork: Back End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.impl;

import static com.intel.stl.common.STLMessages.STL2001_CONNECTION_ERROR;
import static com.intel.stl.common.STLMessages.STL2002_CONNECTION_CLOSED;
import static com.intel.stl.common.STLMessages.STL2003_CONNECTION_TIMEOUT;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.ISecurityHandler;
import com.intel.stl.api.MadException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.NoticeType;
import com.intel.stl.api.notice.TrapType;
import com.intel.stl.api.subnet.GIDGlobal;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.api.subnet.SubnetConfigurationException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.Constants;
import com.intel.stl.common.STLMessages;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.ConnectionEvent;
import com.intel.stl.fecdriver.FEResourceAdapter;
import com.intel.stl.fecdriver.IApplicationEventListener;
import com.intel.stl.fecdriver.IChannel;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.IConnectionEventListener;
import com.intel.stl.fecdriver.IFailoverEventListener;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;
import com.intel.stl.fecdriver.messages.command.FVCommand;
import com.intel.stl.fecdriver.messages.response.FVResponse;
import com.intel.stl.fecdriver.messages.response.sa.FVRspNotice;

public class STLConnection implements IConnection {
    private static Logger log = LoggerFactory.getLogger(STLConnection.class);

    private static final long CONNECTION_TIMEOUT = 10000L;

    private static boolean DEBUG = false;

    protected final SubnetDescription description;

    private final ISecurityHandler securityHandler;

    private final ICertsAssistant certsAssistant;

    private final AtomicInteger numHolders;

    private IChannel feChannel;

    private Properties clientInfo = new Properties();

    protected AtomicBoolean closed = new AtomicBoolean(false);

    private HostInfo connectionHost;

    private boolean connected = false;

    protected SocketChannel socketChannel = null;

    private Exception connectError;

    private final List<WeakReference<STLStatement>> statements =
            new ArrayList<WeakReference<STLStatement>>();

    private final List<IConnectionEventListener> connEventListeners =
            new CopyOnWriteArrayList<IConnectionEventListener>();

    private final List<IApplicationEventListener> appEventListeners =
            new CopyOnWriteArrayList<IApplicationEventListener>();

    private final List<IFailoverEventListener> foEventListeners =
            new CopyOnWriteArrayList<IFailoverEventListener>();

    private final List<IEventListener<NoticeBean>> noticeListeners =
            new CopyOnWriteArrayList<IEventListener<NoticeBean>>();

    private final ConcurrentLinkedQueue<OobPacket> outPackets =
            new ConcurrentLinkedQueue<OobPacket>();

    private final ConcurrentHashMap<Long, FVCommand<?, ?>> resHandlers =
            new ConcurrentHashMap<Long, FVCommand<?, ?>>();

    private InetAddress inetAddress;

    private boolean reconnect;

    private final boolean handShakeInProgress = false;

    protected STLConnection(SubnetDescription description,
            ISecurityHandler securityHandler, ICertsAssistant assistant) {
        this.description = description;
        this.securityHandler = securityHandler;
        this.certsAssistant = assistant;
        HostInfo hostInfo = description.getCurrentFE();
        String host = hostInfo.getHost();
        int port = hostInfo.getPort();

        setClientInfo(FEResourceAdapter.HOST, host);
        setClientInfo(FEResourceAdapter.PORT, Integer.toString(port));

        numHolders = new AtomicInteger(1);
    }

    @Override
    public void setClientInfo(Properties clientInfo) {
        if (clientInfo != null) {
            this.clientInfo = clientInfo;
        }
    }

    @Override
    public void setClientInfo(String name, String value) {
        clientInfo.put(name, value);
    }

    @Override
    public Properties getClientInfo() {
        return clientInfo;
    }

    @Override
    public String getClientInfo(String name) {
        return clientInfo.getProperty(name);
    }

    @Override
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    @Override
    public synchronized STLStatement createStatement() {
        STLStatement res = new STLStatement(this);
        statements.add(new WeakReference<STLStatement>(res));
        return res;
    }

    @Override
    public boolean isClosed() {
        boolean res = closed.get();
        // This is done for testing; get() is final
        closed.weakCompareAndSet(res, res);
        return res;
    }

    @Override
    public SubnetDescription getConnectionDescription() {
        return description;
    }

    /**
     * Sets the socketChannel
     * 
     * @throws IOException
     */
    public synchronized void initSocketChannel() throws IOException {
        this.socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        HostInfo hostInfo = description.getCurrentFE();
        if (hostInfo.isSecureConnect()) {
            this.feChannel = new FESecureChannel(this, certsAssistant);
        } else {
            this.feChannel = new FEChannel(this, securityHandler);
        }

    }

    /**
     * @return the socketChannel
     */
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    protected void connect() throws Exception {
        if (socketChannel != null
                && !(socketChannel.isConnected() || socketChannel
                        .isConnectionPending())) {
            HostInfo hostInfo = description.getCurrentFE();
            String host = hostInfo.getHost();
            int port = hostInfo.getPort();
            InetSocketAddress address = new InetSocketAddress(host, port);
            this.inetAddress = address.getAddress();
            hostInfo.setInetAddress(this.inetAddress);
            this.connectionHost = hostInfo.copy();
            this.connectionHost.setInetAddress(this.inetAddress);
            socketChannel.connect(address);
        }
    }

    protected HostInfo getConnectionHost() {
        return connectionHost;
    }

    protected synchronized void resetConnection(long waitExtension) {
        this.connected = false;
        this.reconnect = true;
        if (socketChannel != null) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socketChannel = null;
                for (FVCommand<?, ?> cmd : resHandlers.values()) {
                    cmd.getResponse().extendWaitTime(waitExtension);
                }
            }
        }
    }

    protected void setReconnecting(boolean reconnect) {
        this.reconnect = reconnect;
    }

    protected boolean isReconnecting() {
        return reconnect;
    }

    protected synchronized void increaseNumHolders() {
        numHolders.incrementAndGet();
    }

    protected int getNumHolders() {
        return numHolders.get();
    }

    protected boolean isHandshakeInProgress() {
        return handShakeInProgress;
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * 
     * <i>Description:</i> prepares for handshake processing
     * 
     * @throws Exception
     */
    protected synchronized void prepareForHandshake() throws Exception {
        feChannel.initialize(description);
    }

    /**
     * 
     * <i>Description:</i> begins the handshake process; typically it starts the
     * protocol sequence.
     * 
     * @throws Exception
     */
    protected synchronized void beginHandshake() throws Exception {
        feChannel.beginHandshake();
    }

    protected synchronized void setConnected(boolean connected) {
        this.connected = connected;
        if (connected) {
            this.notify();
            NoticeBean notice = createConnEstablishNotice();
            fireNotice(new NoticeBean[] { notice });
            STLStatement stmt = new STLStatement(this);
            // This resubmits commands if this connection is being reconnected,
            // otherwise no pending response handlers should be present
            for (FVCommand<?, ?> cmd : resHandlers.values()) {
                long msgId = cmd.getMessageID();
                OobPacket packet = stmt.createSendPacket(cmd);
                packet.getRmppMad().getCommonMad().setTransactionID(msgId);
                packet.getRmppMad().getCommonMad().getByteBuffer().flip();
                outPackets.add(packet);
            }
        } else {
            NoticeBean notice = createConnLostNotice();
            fireNotice(new NoticeBean[] { notice });
            for (FVCommand<?, ?> cmd : resHandlers.values()) {
                ClosedChannelException cce = new ClosedChannelException();
                cmd.getResponse().setError(cce);
            }
        }
    }

    @Override
    public synchronized boolean waitForConnect() throws IOException {
        while (connectError == null && !connected) {
            try {
                this.wait(CONNECTION_TIMEOUT);
            } catch (InterruptedException e) {
            }
        }

        if (connectError != null) {
            if (connectError instanceof IOException) {
                throw (IOException) connectError;
            } else {
                throw new RuntimeException(
                        STL2001_CONNECTION_ERROR.getDescription(description
                                .getName(), (connectError == null ? "N/A"
                                : StringUtils.getErrorMessage(connectError))),
                        connectError);
            }
        }
        return socketChannel.isConnected();
    }

    public synchronized void setConnectError(Exception e) {
        connectError = e;
        closed.set(true);
        setError(e);
    }

    /**
     * @return the connectError
     */
    public synchronized Exception getConnectError() {
        return connectError;
    }

    private void setError(Exception e) {
        for (FVCommand<?, ?> cmd : resHandlers.values()) {
            FVResponse<?> resp = cmd.getResponse();
            resp.setError(e);
        }
        fireConnectionError(e);
        notify();
    }

    /**
     * Logically closes the connection for the application
     */
    @Override
    public void close() {
        int currVal = numHolders.decrementAndGet();
        if (currVal > 0) {
            log.info("Still has " + currVal + " holders");
        } else if (currVal < 0) {
            log.debug("Something wrong numHolders=" + currVal
                    + ". This shouldn't happen!");
            numHolders.set(0);
        }
        fireOnClose();
    }

    protected void cleanUp() {
        statements.clear();
        connEventListeners.clear();
        outPackets.clear();
        resHandlers.clear();
    }

    @Override
    public void addConnectionEventListener(IConnectionEventListener listener) {
        connEventListeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(IConnectionEventListener listener) {
        connEventListeners.remove(listener);
    }

    @Override
    public void addApplicationEventListener(IApplicationEventListener listener) {
        appEventListeners.add(listener);
    }

    @Override
    public void removeApplicationEventListener(
            IApplicationEventListener listener) {
        appEventListeners.remove(listener);
    }

    @Override
    public void addFailoverEventListener(IFailoverEventListener listener) {
        foEventListeners.add(listener);
    }

    @Override
    public void removeFailoverEventListener(IFailoverEventListener listener) {
        foEventListeners.remove(listener);
    }

    @Override
    public void addNoticeListener(IEventListener<NoticeBean> listener) {
        noticeListeners.add(listener);
    }

    @Override
    public void removeNoticeListener(IEventListener<NoticeBean> listener) {
        noticeListeners.add(listener);
    }

    protected synchronized void write() throws Exception {
        feChannel.write();
    }

    protected boolean arePacketsPending() {
        return !outPackets.isEmpty();
    }

    protected OobPacket getNextPacket() {
        OobPacket packet = null;
        while (!outPackets.isEmpty()) {
            packet = outPackets.poll();
            long msgId = packet.getRmppMad().getCommonMad().getTransactionId();
            if (packet.getExpireTime() > System.currentTimeMillis()
                    && resHandlers.containsKey(msgId)) {
                break;
            }
            log.info("Ignore packet id=" + msgId + " expire="
                    + packet.getExpireTime() + " "
                    + (resHandlers.containsKey(msgId)));
        }
        return packet;
    }

    protected synchronized void read() throws Exception {
        feChannel.read();
    }

    protected void check() {
        if (resHandlers.isEmpty()) {
            return;
        }

        List<Long> cancelled = new ArrayList<Long>();
        for (Long id : resHandlers.keySet()) {
            if (resHandlers.get(id).getResponse().isCancelled()) {
                cancelled.add(id);
            }
        }
        for (Long id : cancelled) {
            resHandlers.remove(id);
            log.info("Remove cancelled tasks " + id);
        }
    }

    protected void processResponse(ByteBuffer buffer) throws Exception {
        byte[] bytes = buffer.array();
        OobPacket packet = new OobPacket();
        int pos = packet.wrap(bytes, buffer.arrayOffset());

        RmppMad mad = new RmppMad();
        pos = mad.wrap(bytes, pos);
        SimpleDatagram<Void> data =
                new SimpleDatagram<Void>(bytes.length - pos);
        pos = data.wrap(bytes, pos);
        mad.setData(data);
        if (DEBUG) {
            mad.dump("", System.out);
        }
        CommonMad comMad = mad.getCommonMad();
        long transId = comMad.getTransactionId();
        short attrId = comMad.getAttributeID();
        if (attrId == SAConstants.STL_SA_ATTR_NOTICE) {
            System.out.println(this);
            FVRspNotice response = new FVRspNotice();
            response.processMad(mad);
            // mad.dump("", System.out);
            try {
                fireNotice(response.get().toArray(new NoticeBean[0]));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            FVCommand<?, ?> cmd = resHandlers.remove(transId);
            if (cmd != null && cmd.getResponse() != null) {
                FVResponse<?> response = cmd.getResponse();
                short status = comMad.getNSStatus();
                if (status != Constants.MAD_STATUS_SUCCESS) {
                    if (status == Constants.MAD_STATUS_SM_UNAVAILABLE) {
                        throw new SubnetConfigurationException(
                                STLMessages.STL2004_SM_UNAVAILABLE,
                                response.getClass(), attrId, status,
                                response.getDescription());
                    } else if (status == Constants.MAD_STATUS_PM_UNAVAILABLE) {
                        throw new SubnetConfigurationException(
                                STLMessages.STL2005_PM_UNAVAILABLE,
                                response.getClass(), attrId, status,
                                response.getDescription());
                    }

                    response.setError(new MadException(response.getClass(),
                            attrId, status, response.getDescription()));
                } else {
                    response.processMad(mad);
                    if (response.getError() != null) {
                        // mad.dump("", System.out);
                        log.error("Failed on processing "
                                + response.getClass().getSimpleName()
                                + " data: TransactionID="
                                + StringUtils.longHexString(transId));
                    }
                }
            } else {
                // Ignore response already cancelled
                // mad.dump("", System.out);
                log.info("No respond found for mad: AttributeID="
                        + StringUtils.shortHexString(attrId)
                        + " TransactionID="
                        + StringUtils.longHexString(transId));
            }
        }
    }

    protected synchronized void submitCmd(long id, OobPacket sendPacket,
            FVCommand<?, ?> cmd) {
        if (!connected && connectError == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            if (!connected) {
                if (connectError != null) {
                    throw new RuntimeException(
                            STL2001_CONNECTION_ERROR.getDescription(
                                    description.getName(),
                                    (connectError == null ? "N/A" : StringUtils
                                            .getErrorMessage(connectError))),
                            connectError);
                } else {
                    throw new RuntimeException(
                            STL2003_CONNECTION_TIMEOUT
                                    .getDescription(description.getName()));
                }
            }
        }

        if (isClosed()) {
            throw new RuntimeException(
                    STL2002_CONNECTION_CLOSED.getDescription(description
                            .getName(), (connectError == null ? "N/A"
                            : StringUtils.getErrorMessage(connectError))));
        }

        outPackets.add(sendPacket);
        resHandlers.putIfAbsent(id, cmd);
    }

    public synchronized void cancelCommandsFor(STLStatement statement) {
        for (long id : resHandlers.keySet()) {
            FVCommand<?, ?> cmd = resHandlers.get(id);
            if (cmd.getSubmittingStatement().equals(statement)) {
                cmd.getResponse().cancel(true);
                resHandlers.remove(id);
            }
        }
    }

    protected void fireOnTimeout() {
        ApplicationEvent event = new ApplicationEvent(this);
        for (IApplicationEventListener listener : appEventListeners) {
            try {
                listener.connectionTimeout(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireOnClose() {
        ApplicationEvent event = new ApplicationEvent(this);
        for (IApplicationEventListener listener : appEventListeners) {
            try {
                listener.connectionClose(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireConnectionClose() {
        ConnectionEvent event = new ConnectionEvent(this);
        for (IConnectionEventListener listener : connEventListeners) {
            try {
                listener.connectionClose(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireConnectionError(Throwable error) {
        ConnectionEvent event = new ConnectionEvent(this, error);
        for (IConnectionEventListener listener : connEventListeners) {
            try {
                listener.connectionError(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireFailoverStart() {
        ApplicationEvent event = new ApplicationEvent(description);
        for (IFailoverEventListener listener : foEventListeners) {
            try {
                listener.onFailoverStart(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireFailoverEnd(Throwable result) {
        ApplicationEvent event = new ApplicationEvent(description, result);
        for (IFailoverEventListener listener : foEventListeners) {
            try {
                listener.onFailoverEnd(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireFailoverProgress(ApplicationEvent event) {
        for (IFailoverEventListener listener : foEventListeners) {
            try {
                listener.onFailoverProgress(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void fireNotice(NoticeBean[] notices) {
        log.info("Fire " + notices.length + " notices "
                + Arrays.toString(notices));
        for (IEventListener<NoticeBean> listener : noticeListeners) {
            try {
                listener.onNewEvent(notices);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected NoticeBean createConnEstablishNotice() {
        NoticeBean bean = new NoticeBean(true);
        GenericNoticeAttrBean attr = new GenericNoticeAttrBean();
        attr.setGeneric(true);
        attr.setType(NoticeType.INFO.getId());
        attr.setTrapNumber(TrapType.FE_CONNECTION_ESTABLISH.getId());
        bean.setAttributes(attr);
        bean.setData(new byte[0]);
        bean.setIssuerGID(new GIDGlobal());
        bean.setClassData(new byte[0]);
        return bean;
    }

    protected NoticeBean createConnLostNotice() {
        NoticeBean bean = new NoticeBean(true);
        GenericNoticeAttrBean attr = new GenericNoticeAttrBean();
        attr.setGeneric(true);
        attr.setType(NoticeType.FATAL.getId());
        attr.setTrapNumber(TrapType.FE_CONNECTION_LOST.getId());
        bean.setAttributes(attr);
        bean.setData(new byte[0]);
        bean.setIssuerGID(new GIDGlobal());
        bean.setClassData(new byte[0]);
        return bean;
    }

    // For testing
    protected Map<Long, FVCommand<?, ?>> getResHandlers() {
        return resHandlers;
    }

}
