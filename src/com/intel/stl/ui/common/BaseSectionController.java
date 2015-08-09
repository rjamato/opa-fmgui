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
 *  File Name: BaseSectionController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/09/15 15:24:31  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/11 19:20:46  fernande
 *  Archive Log:    Adding event bus and linking from UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 19:25:40  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.framework.IAppEvent;

public abstract class BaseSectionController<E extends ISectionListener, V extends JSectionView<E>>
        implements ISectionController<V>, ISectionListener {
    protected E listener;

    protected V view;

    protected MBassador<IAppEvent> eventBus;

    public BaseSectionController(V view, MBassador<IAppEvent> eventBus) {
        this.view = view;
        this.eventBus = eventBus;
        installListener();
    }

    protected void installListener() {
        listener = getSectionListener();
        view.setSectionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISectionController#getView()
     */
    @Override
    public V getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.ISectionListener#onHelp()
     */
    @Override
    public void onHelp() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISectionController#clear()
     */
    @Override
    public void clear() {
    }

    protected abstract E getSectionListener();
}
