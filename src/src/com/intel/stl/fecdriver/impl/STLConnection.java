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
 *  File Name: STLConnection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.49  2015/08/17 18:49:07  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/07/06 21:07:10  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Fix for issue where connection reads and writes are executed before the connection is completed, resulting in a NPE.
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/06/16 15:55:04  fernande
 *  Archive Log:    PR 129034 Support secure FE. Changes to the IResponse interface to throw only checked IOExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/05/29 20:37:27  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/05/29 10:15:15  robertja
 *  Archive Log:    PR128703 - Fix check for fail-over in progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/05/26 15:40:06  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/05/26 14:48:33  robertja
 *  Archive Log:    PR128703 - Fix check for fail-over in progress.  Also, suppress command response errors during fail-over.
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/05/12 17:37:50  rjtierne
 *  Archive Log:    PR 128623 - Klocwork and FindBugs fixes for backend
 *  Archive Log:    In method submitCmd(), it is necessary to put the call to wait() in a loop and check
 *  Archive Log:    the condition in case a notify() (or notifyAll()) is called that does not meet the intended condition.
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

import static com.intel.stl.common.STLMessages.STL20001_CONNECTION_ERROR;
import static com.intel.stl.common.STLMessages.STL20002_CONNECTION_CLOSED;
import static com.intel.stl.common.STLMessages.STL20003_CONNECTION_TIMEOUT;

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
import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.IConnectionEventListener;
import com.intel.stl.fecdriver.IFailoverEventListener;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;
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

    private final ConcurrentHashMap<Long, ICommand<?, ?>> resHandlers =
            new ConcurrentHashMap<Long, ICommand<?, ?>>();

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
                for (ICommand<?, ?> cmd : resHandlers.values()) {
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
            for (ICommand<?, ?> cmd : resHandlers.values()) {
                long msgId = cmd.getMessageID();
                OobPacket packet = stmt.createSendPacket(cmd);
                packet.getRmppMad().getCommonMad().setTransactionID(msgId);
                packet.getRmppMad().getCommonMad().getByteBuffer().flip();
                outPackets.add(packet);
            }
        } else {
            NoticeBean notice = createConnLostNotice();
            fireNotice(new NoticeBean[] { notice });
            for (ICommand<?, ?> cmd : resHandlers.values()) {
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
                        STL20001_CONNECTION_ERROR.getDescription(description
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
        for (ICommand<?, ?> cmd : resHandlers.values()) {
            IResponse<?> resp = cmd.getResponse();
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
        if (!connected) {
            return;
        }
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
        if (!connected) {
            return;
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ICommand<?, ?> cmd = resHandlers.remove(transId);
            if (cmd != null && cmd.getResponse() != null) {
                IResponse<?> response = cmd.getResponse();
                short status = comMad.getNSStatus();
                if (status == Constants.MAD_STATUS_SUCCESS) {
                    response.processMad(mad);
                    if (response.getError() != null) {
                        // mad.dump("", System.out);
                        log.error("Failed on processing "
                                + response.getClass().getSimpleName()
                                + " data: TransactionID="
                                + StringUtils.longHexString(transId));
                    }
                } else {
                    if (status == Constants.MAD_STATUS_SM_UNAVAILABLE) {
                        throw new SubnetConfigurationException(
                                STLMessages.STL20004_SM_UNAVAILABLE,
                                response.getClass(), attrId, status,
                                response.getDescription());
                    } else if (status == Constants.MAD_STATUS_PM_UNAVAILABLE) {
                        throw new SubnetConfigurationException(
                                STLMessages.STL20005_PM_UNAVAILABLE,
                                response.getClass(), attrId, status,
                                response.getDescription());
                    } else {
                        response.setError(new MadException(response.getClass(),
                                attrId, status, response.getDescription()));
                    }
                }
            } else {
                // Ignore response already cancelled
                // mad.dump("", System.out);
                log.info("No response found for mad: AttributeID="
                        + StringUtils.shortHexString(attrId)
                        + " TransactionID="
                        + StringUtils.longHexString(transId));
            }
        }
    }

    protected synchronized <E extends IResponse<F>, F> void submitCmd(long id,
            OobPacket sendPacket, ICommand<E, F> cmd) {

        // FindBugs: wait() is put in a loop to prevent a spurious notify
        // from causing it to wake up under the wrong conditions
        while (!connected && connectError == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            if (!connected) {
                if (connectError != null) {
                    throw new RuntimeException(
                            STL20001_CONNECTION_ERROR.getDescription(
                                    description.getName(),
                                    (connectError == null ? "N/A" : StringUtils
                                            .getErrorMessage(connectError))),
                            connectError);
                } else {
                    throw new RuntimeException(
                            STL20003_CONNECTION_TIMEOUT
                                    .getDescription(description.getName()));
                }
            }
        }

        if (isClosed()) {
            throw new RuntimeException(
                    STL20002_CONNECTION_CLOSED.getDescription(description
                            .getName(), (connectError == null ? "N/A"
                            : StringUtils.getErrorMessage(connectError))));
        }

        outPackets.add(sendPacket);
        resHandlers.putIfAbsent(id, cmd);
    }

    public synchronized void cancelCommandsFor(STLStatement statement) {
        for (long id : resHandlers.keySet()) {
            ICommand<?, ?> cmd = resHandlers.get(id);
            if (cmd.getStatement().equals(statement)) {
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

    private void fireConnectionClose() {
        ConnectionEvent event = new ConnectionEvent(this);
        for (IConnectionEventListener listener : connEventListeners) {
            try {
                listener.connectionClose(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fireConnectionError(Throwable error) {
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
    protected Map<Long, ICommand<?, ?>> getResHandlers() {
        return resHandlers;
    }

}
