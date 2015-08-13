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
 *  File Name: BaseApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:21:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 17:33:34  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/21 13:36:02  fernande
 *  Archive Log:    Fixing spelling error in method names
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/14 20:44:52  jijunwan
 *  Archive Log:    1) improved to set SubnetContext invalid when we have network connection issues
 *  Archive Log:    2) improved to recreate SubnetContext when we query for it and the current one is invalid. We also clean up (include shutdown) the old context before we replace it with a new one
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.intel.stl.fecdriver.ConnectionEvent;
import com.intel.stl.fecdriver.IConnectionEventListener;

public class BaseApi implements IErrorSupport, IConnectionEventListener {
    private final List<IErrorHandler> handlers =
            new CopyOnWriteArrayList<IErrorHandler>();

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.IErrorSupport#addErrroHandler(com.intel.stl.api.
     * IErrorHandler)
     */
    @Override
    public void addErrorHandler(IErrorHandler handler) {
        handlers.add(handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.IErrorSupport#removeErrroHandler(com.intel.stl.api.
     * IErrorHandler)
     */
    @Override
    public void removeErrorHandler(IErrorHandler handler) {
        handlers.remove(handler);
    }

    protected void fireError(Throwable error) {
        for (IErrorHandler handler : handlers) {
            handler.onError(error);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.fecdriver.IConnectionEventListener#connectionOnClose(com
     * .intel.stl.fecdriver.ConnectionEvent)
     */
    @Override
    public void connectionClose(ConnectionEvent event) {
        if (event.getReason() != null) {
            fireError(event.getReason());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.fecdriver.IConnectionEventListener#connectionErrorOccurred
     * (com.intel.stl.fecdriver.ConnectionEvent)
     */
    @Override
    public void connectionError(ConnectionEvent event) {
        if (event.getReason() != null) {
            fireError(event.getReason());
        }
    }
}
