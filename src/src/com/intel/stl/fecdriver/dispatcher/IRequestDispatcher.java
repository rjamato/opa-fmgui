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
 *  File Name: IRequestDispatcher.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:49:10  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/29 20:37:05  fernande
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

import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLEngine;

import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.adapter.ISMEventListener;
import com.intel.stl.fecdriver.session.ISession;

public interface IRequestDispatcher {

    /**
     * 
     * <i>Description:</i> creates a session
     * 
     * @return a session
     */
    ISession createSession();

    /**
     * 
     * <i>Description:</i> creates a session, specifying a Subnet Manager event
     * listener
     * 
     * @param listener
     *            the Subnet Manager event listener
     * @return a session
     */
    ISession createSession(ISMEventListener listener);

    /**
     * 
     * <i>Description:</i> removes a session
     * 
     * @param session
     *            the session to be removed
     */
    void removeSession(ISession session);

    /**
     * 
     * <i>Description:</i> queues a command to be dispatched
     * 
     * @param cmd
     *            the command to be dispatched
     */
    <E extends IResponse<F>, F> void queueCmd(ICommand<E, F> cmd);

    /**
     * 
     * <i>Description:</i> notifies this dispatcher of a timeout occurring in a
     * command
     * 
     * @param toe
     *            the TimeoutException associated with the request
     */
    void onRequestTimeout(TimeoutException toe);

    /**
     * 
     * <i>Description:</i> starts this dispatcher
     * 
     */
    void start();

    /**
     * 
     * <i>Description:</i> shuts down this dispatcher
     * 
     */
    void shutdown();

    /**
     * 
     * <i>Description:</i> refreshes the subnet description for this dispatcher
     * 
     * @param subnet
     *            the new subnet description
     */
    void refreshSubnetDescription(SubnetDescription subnet);

    /**
     * 
     * <i>Description:</i> returns a SSLEngine for the specified host info
     * 
     * @param hostInfo
     * @return
     */
    SSLEngine getSSLEngine(HostInfo hostInfo) throws Exception;

    /**
     * 
     * <i>Description:</i> returns the subnet description associated with this
     * dispatcher
     * 
     * @return subnet description
     */
    SubnetDescription getSubnetDescription();
}
