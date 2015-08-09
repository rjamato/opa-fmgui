/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
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
 *  File Name: BaseApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
