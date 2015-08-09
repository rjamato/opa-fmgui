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
 *  File Name: ErrorChartTickUnit.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/04/12 19:52:05  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/03 20:52:25  jijunwan
 *  Archive Log:    on going work on "Home" page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;

public class ErrorChartTickUnit extends NumberTickUnit {
    private static final long serialVersionUID = 2654598599166333554L;
    
    // Buckets:
    public static final int LOWER_BOUND = 0;
    public static final int B0_25       = LOWER_BOUND;
    public static final int B26_50      = 1;
    public static final int B51_75      = 2;
    public static final int B76_100     = 3;
    public static final int B100Plus    = 4;
    public static final int UPPER_BOUND = 5;

    public ErrorChartTickUnit()
    {
        super((double)0);
    }

    public ErrorChartTickUnit(double d)
    {
        super((double)d);
    }

    public TickUnits genTickUnits()
    {
        TickUnits tickUnits = new TickUnits();

        tickUnits.add(new ErrorChartTickUnit(B0_25));
        tickUnits.add(new ErrorChartTickUnit(B26_50));
        tickUnits.add(new ErrorChartTickUnit(B51_75));
        tickUnits.add(new ErrorChartTickUnit(B76_100));
        tickUnits.add(new ErrorChartTickUnit(B100Plus));
        tickUnits.add(new ErrorChartTickUnit(UPPER_BOUND));

        return tickUnits;
    }

    public String valueToString(double value)
    {
        if     (value == (double)B0_25)    return "0+%";
        else if(value == (double)B26_50)   return "25+%";
        else if(value == (double)B51_75)   return "50+%";
        else if(value == (double)B76_100)  return "75+%";
        else if(value == (double)B100Plus) return "100+%";
        else                               return "";
    }

}
