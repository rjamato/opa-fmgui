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
 *  File Name: ChartsSectionController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/13 23:05:37  jijunwan
 *  Archive Log:    PR 126911 - Even though HFI does not represent "Internal" data under opatop, FV still provides drop down for "Internal"
 *  Archive Log:     -- added a feature to be able to disable unsupported types
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/12 19:40:08  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/09 12:32:57  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext).
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/15 15:24:31  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/26 15:15:34  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 18:48:41  jijunwan
 *  Archive Log:    moved ChartsSectionView/ChartsSectionController to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/17 16:25:38  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/16 15:19:29  jijunwan
 *  Archive Log:    applied new performance framework and performance group viz to support bandwidth, packet rate, congestion and integrity data
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/11 19:23:23  fernande
 *  Archive Log:    Adding event bus and linking from UI elements to the Performance tab
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.view.ChartsSectionView;
import com.intel.stl.ui.common.view.ChartsSectionView.TabbedPanel;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.ChartGroup;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.performance.IGroupController;

public abstract class ChartsSectionController extends
        BaseSectionController<ISectionListener, ChartsSectionView> implements
        ChangeListener {
    public static final String UTIL = "Util";

    public static final String ERR = "Error";

    protected int topN = 10;

    private final List<IGroupController> groups =
            new ArrayList<IGroupController>();

    public ChartsSectionController(ChartsSectionView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        ChartGroup utilGroup = new ChartGroup(UTIL, null);
        IGroupController[] tmp = getUtilGroups();
        for (int i = 0; i < tmp.length; i++) {
            utilGroup.addMember(tmp[i].getGroup());
            groups.add(tmp[i]);
        }

        ChartGroup errGroup = new ChartGroup(ERR, null);
        tmp = getErrorGroups();
        for (int i = 0; i < tmp.length; i++) {
            errGroup.addMember(tmp[i].getGroup());
            groups.add(tmp[i]);
        }

        view.setListener(this);
        view.setChartGroups(new ChartGroup[] { utilGroup, errGroup });
    }

    protected abstract IGroupController[] getUtilGroups();

    protected abstract IGroupController[] getErrorGroups();

    /**
     * @return the topN
     */
    public int getTopN() {
        return topN;
    }

    /**
     * @param topN
     *            the topN to set
     */
    public void setTopN(int topN) {
        this.topN = topN;
    }

    @Override
    public ICardController<?>[] getCards() {
        return null;
    }

    public void setContext(Context context, IProgressObserver observer) {
        if (observer == null) {
            observer = new ObserverAdapter();
        }
        IProgressObserver[] subObservers =
                observer.createSubObservers(groups.size());
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setContext(context, subObservers[i]);
            subObservers[i].onFinish();
        }

        observer.onFinish();
    }

    public void onRefresh(IProgressObserver observer) {
        if (observer == null) {
            observer = new ObserverAdapter();
        }
        IProgressObserver[] subObservers =
                observer.createSubObservers(groups.size());
        for (int i = 0; i < groups.size() && !observer.isCancelled(); i++) {
            groups.get(i).onRefresh(subObservers[i]);
            subObservers[i].onFinish();
        }

        observer.onFinish();
    }

    public void setSource(String name) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setDataSources(name);
        }
    }

    /**
     * Description:
     * 
     * @param name
     */
    public void setDataProvider(String name) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setDataProvider(name);
        }
    }

    public void setDisabledDataTypes(DataType... types) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setDisabledDataTypes(types);
        }
    }

    @Override
    protected ISectionListener getSectionListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
     * )
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        TabbedPanel panel = (TabbedPanel) e.getSource();
        String category = panel.getName();
        String selection = panel.getSelection();
        if (category == UTIL) {
            initGroup(selection, getUtilGroups());
        } else if (category == ERR) {
            initGroup(selection, getErrorGroups());
        }
    }

    protected void initGroup(String name, IGroupController[] groups) {
        for (IGroupController group : groups) {
            if (group.getGroup().getName().equals(name)) {
                group.setSleepMode(false);
            } else {
                group.setSleepMode(true);
            }
        }
    }
}
