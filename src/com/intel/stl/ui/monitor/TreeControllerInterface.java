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
 *  File Name: TreeControllerInterface.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:53:41  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/15 19:05:04  fernande
 *  Archive Log:    PR 129199 - Checking copyright test as part of the build step. Fixed year appearing in copyright notice
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/14 11:32:09  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class serves as the public interface for the tree controller 
 *  
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.main.Context;

/**
 * @author rjtierne
 * 
 */
public interface TreeControllerInterface {

    public void setContext(Context pContext, IProgressObserver observer);

    public void onRefresh(IProgressObserver observer);

    public void onNodeUpdate(NodeUpdateEvent evt);
}
