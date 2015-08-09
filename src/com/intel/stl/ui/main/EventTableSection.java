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
 *  File Name: ${file_name}
 *
 *  Archive Source: $$Source$$
 *
 *  Archive Log:    $$Log: $$
 *
 *  Overview: Table section of Rick's Test page
 *
 *  @author: ${user}
 *
 ******************************************************************************/
package com.intel.stl.ui.main;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.EventDescription;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.EventTableController;
import com.intel.stl.ui.common.EventTableModel;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.view.EventTableView;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.framework.IAppEvent;

public class EventTableSection extends
        BaseSectionController<ISectionListener, JSectionView<ISectionListener>> {
    private final EventTableController tableController;

    public EventTableSection(EventTableModel tableModel,
            JSectionView<ISectionListener> view, EventTableView tableView,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        tableController = new EventTableController(tableModel, tableView);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.ISection#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return null;
    }

    /**
     * 
     * Description: updates the table on the card
     * 
     * @param event
     *            - event message
     */
    public void updateTable(EventDescription event) {
        tableController.addEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISection#clear()
     */
    @Override
    public void clear() {
        tableController.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseSectionController#getSectionListener()
     */
    @Override
    protected ISectionListener getSectionListener() {
        return this;
    }

}
