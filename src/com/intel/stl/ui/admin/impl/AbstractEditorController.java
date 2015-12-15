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
 *  File Name: AbstractEditorController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:28  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/14 17:06:01  jijunwan
 *  Archive Log:    PR 129541 - Should forbid save or deploy when there is invalid edit on management panel
 *  Archive Log:    - throw InvalidEditException when there is invalid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/05/14 17:19:44  jijunwan
 *  Archive Log:    PR 128697 - Handle empty list of items
 *  Archive Log:    - Added code to handle null item
 *  Archive Log:    - Added code to clean panel when it gets a null item
 *  Archive Log:    - Enable/disable buttons properly when we get an empty item list or null item
 *  Archive Log:    - Improved to handle item selection when the index is invalid, such as -1
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/27 15:47:50  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:20  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl;

import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.ui.admin.InvalidEditException;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.view.AbstractEditorPanel;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.publisher.TaskScheduler;

public class AbstractEditorController<T, E extends AbstractEditorPanel<T>> {
    protected final E view;

    protected IManagementApi mgtApi;

    protected TaskScheduler taskScheduler;

    /**
     * Description:
     * 
     * @param view
     */
    public AbstractEditorController(E view) {
        super();
        this.view = view;
    }

    public void setContext(Context context) {
        mgtApi = context.getManagementApi();
        taskScheduler = context.getTaskScheduler();
    }

    public void initData() throws Exception {
    }

    public void setItem(Item<T> item, Item<T>[] items) {
        if (item == null) {
            view.clear();
            return;
        } else if (items.length < 1) {
            // this shouldn't happen
            throw new IllegalArgumentException("Invalid item array");
        }

        view.setItem(item, items);
    }

    public void updateItem(Item<T> item) throws InvalidEditException {
        view.updateItem(item);
    }
}
