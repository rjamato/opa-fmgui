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
 *  File Name: BaseCardController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/10/02 21:26:14  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
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

import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.framework.IAppEvent;

public abstract class BaseCardController<E extends ICardListener, V extends JCardView<E>>
        implements ICardController<V>, ICardListener {
    protected E listener;

    protected V view;

    protected final MBassador<IAppEvent> eventBus;

    /**
     * Description:
     * 
     * @param name
     * @param view
     */
    public BaseCardController(V view, MBassador<IAppEvent> eventBus) {
        super();
        if (view == null) {
            throw new IllegalArgumentException("View can not be null!");
        }
        this.view = view;
        this.eventBus = eventBus;
        installListener();
    }

    protected void installListener() {
        listener = getCardListener();
        view.setCardListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ICardController#claer()
     */
    @Override
    public void clear() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ICardController#getView()
     */
    @Override
    public V getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.ICardListener#onPin()
     */
    @Override
    public void onPin() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.ICardListener#onHelp()
     */
    @Override
    public void onHelp() {
        // TODO Auto-generated method stub

    }

    public abstract E getCardListener();
}
