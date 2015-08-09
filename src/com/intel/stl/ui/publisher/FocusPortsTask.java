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
 *  File Name: FocusPortsTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/01/21 21:19:12  rjtierne
 *  Archive Log:    Removed individual refresh rates for task registration. Now using
 *  Archive Log:    refresh rate supplied by user input in preferences wizard.
 *  Archive Log:    Reinitialization of scheduler service not yet implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/22 15:50:32  jijunwan
 *  Archive Log:    fixed a typo
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/12 21:05:06  jijunwan
 *  Archive Log:    1) added description to task
 *  Archive Log:    2) applied failure management to TaskScheduler
 *  Archive Log:    3) some code auto-reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/05 17:17:19  jijunwan
 *  Archive Log:    added VF related tasks to TaskScheduler
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:52:29  jijunwan
 *  Archive Log:    reference to new accessible Selection and PAConstant
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 15:16:13  jijunwan
 *  Archive Log:    added ApiBroker to schedule a task to repeatedly get data from a FEC driver
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.subnet.Selection;
import com.intel.stl.ui.common.UILabels;

public class FocusPortsTask<E> extends Task<E> {
    final private Selection selection;

    final private int range;

    final private boolean isVF;

    public FocusPortsTask(String name, Selection selection, int range) {
        this(name, selection, range, false);
    }

    public FocusPortsTask(String name, Selection selection, int range,
            boolean isVF) {
        super(isVF ? PAConstants.STL_PA_ATTRID_GET_VF_FOCUS_PORTS
                : PAConstants.STL_PA_ATTRID_GET_FOCUS_PORTS, name,
                UILabels.STL40006_FOCUSPORTS_TASK.getDescription(name,
                        selection));
        this.selection = selection;
        this.range = range;
        this.isVF = isVF;
    }

    /**
     * @return the selection
     */
    public Selection getSelection() {
        return selection;
    }

    /**
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * @return the isVF
     */
    public boolean isVF() {
        return isVF;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (isVF ? 1231 : 1237);
        result = prime * result + range;
        result =
                prime * result
                        + ((selection == null) ? 0 : selection.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FocusPortsTask other = (FocusPortsTask) obj;
        if (isVF != other.isVF) {
            return false;
        }
        if (range != other.range) {
            return false;
        }
        if (selection != other.selection) {
            return false;
        }
        return true;
    }

}
