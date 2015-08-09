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
 *  File Name: IStateMonitor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/05/19 22:08:53  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/16 15:17:14  jijunwan
 *  Archive Log:    Added filter capability to EventCalculator
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/08 19:03:24  jijunwan
 *  Archive Log:    backend support for states based on notices
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:20:24  jijunwan
 *  Archive Log:    added state and heal score calculation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.util.EnumMap;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.model.TimedScore;

public interface IStateMonitor {
    /**
     * 
     *  Description: score in [0, 100]
     *  
     *  @return
     */
    TimedScore getHealthScore();
    EnumMap<NoticeSeverity, Integer> getSwitchStates();
    EnumMap<NoticeSeverity, Integer> getHFIStates();
    StateSummary getSummary();
}
