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
 *  File Name: SubnetRefreshTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/12/11 18:52:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/22 01:16:19  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/16 13:18:05  fernande
 *  Archive Log:    Changes to AbstractTask to support an onFinally method that is guaranteed to be called no matter what happens in the onTaskSuccess and onTaskFailure implementations for a task.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/13 21:07:16  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/09 12:59:15  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import static com.intel.stl.ui.main.FabricController.PROGRESS_NOTE_PROPERTY;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.ProgressObserver;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.framework.AbstractTask;
import com.intel.stl.ui.monitor.tree.FVTreeManager;

public class SubnetRefreshTask extends AbstractTask<FabricModel, Void, String> {

    private static Logger log = LoggerFactory.getLogger(SubnetRefreshTask.class);

    private final List<IPageController> pages;

    private final FVTreeManager builder;

    public SubnetRefreshTask(FabricModel model, FVTreeManager builder,
            List<IPageController> pages) {
        super(model);
        this.pages = pages;
        this.builder = builder;
    }

    @Override
    public Void processInBackground(Context context) throws Exception {
        log.info("Refresh subnet '" + model.getCurrentSubnet() + "'");

        builder.setDirty();
        for (int i = 0; i < pages.size(); i++) {
            IPageController page = pages.get(i);
            setProgressNote(UILabels.STL10111_REFRESHING_PAGE
                    .getDescription(page.getName()));
            int estimatedWork = page.getRefreshWeight().getWeight();
            ProgressObserver observer =
                    new ProgressObserver(this, estimatedWork);
            page.onRefresh(observer);
            observer.onFinish();
        }

        return null;
    }

    @Override
    public void onTaskSuccess(Void result) {
        getController().notifyModelChanged();
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        model.setErrorMessage(StringUtils.getErrorMessage(caught));
        getController().notifyModelUpdateFailed(caught);
    }

    @Override
    public void onFinally() {
    }

    @Override
    public void processIntermediateResults(List<String> intermediateResults) {
    }

    protected void setProgressNote(String note) {
        firePropertyChange(PROGRESS_NOTE_PROPERTY, null, note);
    }
}
