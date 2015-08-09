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
 *  File Name: 
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/05/21 21:09:45  jijunwan
 *  Archive Log:    PR 128855 - Incorrect value conversion on flits
 *  Archive Log:    - added 1 fit = 8 bytes to value conversion
 *  Archive Log:    - changed to use MB rather than MiB
 *  Archive Log:    - some code lean up
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/06 13:31:05  jypak
 *  Archive Log:    Performance-Performance subpage updates.
 *  Archive Log:    1. Synchronize y-axis(range axis) bound for a group of charts (packet, data).
 *  Archive Log:    2. Auto conversion of y-axis label title and tick label based on the max value of data in the series.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import org.jfree.chart.axis.NumberTickUnit;


public class ShiftedNumberTickUnit extends NumberTickUnit {
    
    /**
     * 
     */
    private static final long serialVersionUID = -811003783349565742L;
    private double tenMultiplier;
    
    /**
     * 
     * Description: 
     *
     * @param size
     * @param tickUnitSize - Long integer to hold upto Giga numbers.
     */
    public ShiftedNumberTickUnit(double size, double tickUnitSize) {
        super(size);
        this.tenMultiplier = tickUnitSize;
    }
    
    @Override
    public String valueToString(double value) {
        return Long.toString((long) (value / tenMultiplier));
    }

}
