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
 *  File Name: ChartsSectionController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/08/17 18:54:12  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/06 13:18:09  jypak
 *  Archive Log:    PR 129707 - Device Types or Device Groups and All/Internal/External labels.
 *  Archive Log:    When disable irrelevant data types for different device types (All, HFI, SW etc.), set a default data type for the device type.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/05 03:00:43  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on charts to track chart  change, jump event
 *  Archive Log:    - applied undo mechanism on chart section to track group change
 *  Archive Log:    - improved OptionChartsView to support undoable data type and history selection
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/06/25 20:50:04  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
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
import com.intel.stl.ui.event.JumpToEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.main.UndoHandler;
import com.intel.stl.ui.model.ChartGroup;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.performance.GroupSource;
import com.intel.stl.ui.performance.IGroupController;
import com.intel.stl.ui.performance.provider.DataProviderName;

public abstract class ChartsSectionController extends
        BaseSectionController<ISectionListener, ChartsSectionView> implements
        ChangeListener {
    public static final String UTIL = "Util";

    public static final String ERR = "Error";

    protected int topN = 10;

    private final List<IGroupController<GroupSource>> groups =
            new ArrayList<IGroupController<GroupSource>>();

    private UndoHandler undoHandler;

    public ChartsSectionController(ChartsSectionView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        ChartGroup utilGroup = new ChartGroup(UTIL, null);
        IGroupController<GroupSource>[] tmp = getUtilGroups();
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

    protected abstract IGroupController<GroupSource>[] getUtilGroups();

    protected abstract IGroupController<GroupSource>[] getErrorGroups();

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
        if (context != null && context.getController() != null) {
            undoHandler = context.getController().getUndoHandler();
        }

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

    public void setSource(GroupSource name) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setDataSources(new GroupSource[] { name });
        }
    }

    /**
     * Description:
     * 
     * @param name
     */
    public void setDataProvider(DataProviderName name) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setDataProvider(name);
        }
    }

    public void setDisabledDataTypes(DataType defaultType, DataType... types) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setDisabledDataTypes(defaultType, types);
        }
    }

    public void setOrigin(JumpToEvent origin) {
        for (IGroupController<GroupSource> group : groups) {
            group.setOrigin(origin);
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

        if (undoHandler != null && !undoHandler.isInProgress()) {
            UndoableChartGroupSelection undoSel =
                    new UndoableChartGroupSelection(panel,
                            panel.getPreviousSelection(), selection);
            undoHandler.addUndoAction(undoSel);
        }
    }

    protected void initGroup(String name, IGroupController<GroupSource>[] groups) {
        for (IGroupController<GroupSource> group : groups) {
            if (group.getGroup().getName().equals(name)) {
                group.setSleepMode(false);
            } else {
                group.setSleepMode(true);
            }
        }
    }
}
