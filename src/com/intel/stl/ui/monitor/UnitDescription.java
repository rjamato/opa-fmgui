/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: UnitDescription.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/05/21 21:09:47  jijunwan
 *  Archive Log:    PR 128855 - Incorrect value conversion on flits
 *  Archive Log:    - added 1 fit = 8 bytes to value conversion
 *  Archive Log:    - changed to use MB rather than MiB
 *  Archive Log:    - some code lean up
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/10 11:46:46  jypak
 *  Archive Log:    Updates to make delta data and cumulative data in same unit.
 *  Archive Log:    For Port performance, the DataChartScaleGroupManager is already updating the unit based on upper value among received/transmitted delta data. Introduced UnitDescription as a wrapper class to grab the unit information to be passed to counter (error) section from chart section.
 *  Archive Log:    For Node performance, since we need to convert data for all the ports, the conversion is done in PerformanceTableSection. the units will be decided by the delta data, not the cumulative data because it's smaller. With this update, received delta/cumulative data will be in a same unit and transmitted delta/cumulative data will be in same unit. However, it is possible that received data and transmitted data can be in different units. The charts are in same unit because it goes down to a double precision but table section is all in integer, so, we don't necessarily want to make them always in a same unit for now.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

public class UnitDescription {

    private String unitStr;

    private double unitSize;

    public UnitDescription(String unitStr, double unitSize) {
        this.unitStr = unitStr;
        this.unitSize = unitSize;
    }

    /**
     * @return the unitStr
     */
    public String getUnitStr() {
        return unitStr;
    }

    /**
     * @return the unitSize
     */
    public double getUnitSize() {
        return unitSize;
    }

    /**
     * @param unitStr
     *            the unitStr to set
     */
    public void setUnitStr(String unitStr) {
        this.unitStr = unitStr;
    }

    /**
     * @param unitSize
     *            the unitSize to set
     */
    public void setUnitSize(long unitSize) {
        this.unitSize = unitSize;
    }

}
