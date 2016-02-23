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
 *  File Name: TempRequestDispatcher.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/28 14:56:47  fernande
 *  Archive Log:    PR 128703 - Fail over doesn't work on A0 Fabric. Fix for another issue where requests that get a SA or PM unavailable status are not resent after failover
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:49:10  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/02 20:32:52  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Fix for temporary sessions; when the remove host closes the connection, the TempRequestDispatcher goes into a loop.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/06/08 16:04:22  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code, especially for SSL connections
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/29 20:37:04  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/05/26 15:39:14  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.dispatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.adapter.IAdapter;
import com.intel.stl.fecdriver.session.ISession;
import com.intel.stl.fecdriver.session.TemporarySession;

public class TempRequestDispatcher extends SubnetRequestDispatcher implements
        ITempRequestDispatcher {
    private static Logger log = LoggerFactory
            .getLogger(TempRequestDispatcher.class);

    private static final String TEMP_SUBNET_NAME = "~tempconnections";

    public TempRequestDispatcher(IAdapter adapter) throws IOException {
        super(new SubnetDescription(TEMP_SUBNET_NAME), adapter, null,
                new NoPoolingPolicy());
    }

    @Override
    public ISession createTemporarySession(HostInfo host,
            IConnectionEventListener listener) {
        log.debug("Creating temporary session to host {}.", host);
        Connection conn = createConnection(host, listener, true);
        addPendingConnection(conn);
        ISession tempSession = new TemporarySession(this, conn);
        sessions.add(tempSession);
        wakeupDispatcher();
        return tempSession;
    }

    @Override
    public void removeSession(ISession session, Connection conn) {
        closeConnection(conn);
        synchronized (connPool) {
            Iterator<Connection> it = connPool.iterator();
            while (it.hasNext()) {
                if (it.next().equals(conn)) {
                    it.remove();
                }
            }
        }
        super.removeSession(session);
    }

    @Override
    public <E extends IResponse<F>, F> void queueCmd(ICommand<E, F> cmd,
            Connection conn) {
        addPendingCommand(cmd, conn);
        wakeupDispatcher();
    }

    @Override
    protected void processConnectionError(Exception ce, Connection conn) {
        // Don't do fail over
        List<ICommand<?, ?>> cmds = new ArrayList<ICommand<?, ?>>();
        cmds.addAll(conn.getPendingCommands());
        closeConnection(conn);
        cancelPendingCmds(cmds, ce);
    }

    @Override
    protected void processRequestError(final Exception re, final Connection conn) {
        // Don't do failure handling
    }
}
