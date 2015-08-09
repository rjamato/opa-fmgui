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
 *  File Name: IGroupController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/13 23:05:35  jijunwan
 *  Archive Log:    PR 126911 - Even though HFI does not represent "Internal" data under opatop, FV still provides drop down for "Internal"
 *  Archive Log:     -- added a feature to be able to disable unsupported types
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/12 19:40:10  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:14:34  jijunwan
 *  Archive Log:    added refresh function to performance charts
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/17 16:25:39  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:09:00  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.ChartGroup;
import com.intel.stl.ui.model.DataType;

public interface IGroupController {
    /**
     * 
     * Description: set Context
     * 
     * @param context
     * @param observer
     */
    void setContext(Context context, IProgressObserver observer);

    /**
     * 
     * <i>Description:</i>
     * 
     * @param observer
     */
    void onRefresh(IProgressObserver observer);

    /**
     * 
     * Description: select one of the registered data providers as the current
     * one
     * 
     * @param name
     */
    void setDataProvider(String name);

    /**
     * 
     * Description: Set Device Group or VFabric names this will controller will
     * collect data from.
     * 
     * @param names
     */
    void setDataSources(String... names);

    void setDisabledDataTypes(DataType... types);

    /**
     * 
     * Description: return a ChartGroup that organizes its charts in
     * hierarchical structure
     * 
     * @return the ChartGroup
     */
    ChartGroup getGroup();

    /**
     * 
     * Description: When we are under sleep mode, we deregister all tasks except
     * the one that will be used for sparkline displaying from
     * {@link com.intel.stl.ui.publisher.TaskScheduler}. When we leave sleep
     * mode, all tasks should re-register to
     * {@link com.intel.stl.ui.publisher.TaskScheduler}. This is useful when we
     * want to reduce FE traffic
     * 
     * @param names
     */
    void setSleepMode(boolean b);

    boolean isSleepMode();
}
