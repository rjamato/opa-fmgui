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
 *  File Name: IResourceSubpageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/22 20:08:35  rjtierne
 *  Archive Log:    Changed prototype of showPath(). Added a second showPath() method
 *  Archive Log:    to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/18 13:39:02  rjtierne
 *  Archive Log:    Changed prototype for showPath() to accept a list of nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/10 21:20:52  rjtierne
 *  Archive Log:    Added showPath() method to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/10 15:41:26  rjtierne
 *  Archive Log:    Added setDescription() method to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/10 14:31:01  rjtierne
 *  Archive Log:    Added new prototype showLink()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/08 20:20:57  rjtierne
 *  Archive Log:    Removed IProgressObserver from interface prototypes
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/03 14:09:02  rjtierne
 *  Archive Log:    Added observer to showNode() prototype
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/24 20:30:31  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public interface IResourceNodeSubpageController extends IPageController {

    public void showNode(FVResourceNode source, GraphNode node);
}
