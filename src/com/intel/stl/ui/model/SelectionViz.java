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
 *  File Name: PortLinkModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/05 21:48:55  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 21:36:23  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.EnumMap;

import com.intel.stl.api.subnet.Selection;
import com.intel.stl.ui.common.STLConstants;

public enum SelectionViz {
    UTILIZATION_HIGH(Selection.UTILIZATION_HIGH,
            STLConstants.K1630_SEL_UTILIZATION_HIGH.getValue()),
    PACKET_RATE_HIGH(Selection.PACKET_RATE_HIGH,
            STLConstants.K1631_SEL_PACKET_RATE_HIGH.getValue()),
    UTILIZATION_LOW(Selection.UTILIZATION_LOW,
            STLConstants.K1632_SEL_UTILIZATION_LOW.getValue()),
    INTEGRITY_ERRORS_HIGH(Selection.INTEGRITY_ERRORS_HIGH,
            STLConstants.K1633_SEL_INTEGRITY_ERRORS_HIGH.getValue()),
    CONGESTION_ERRORS_HIGH(Selection.CONGESTION_ERRORS_HIGH,
            STLConstants.K1634_SEL_CONGESTION_ERRORS_HIGH.getValue()),
    SMA_CONGESTION_ERRORS_HIGH(Selection.SMA_CONGESTION_ERRORS_HIGH,
            STLConstants.K1635_SEL_SMA_CONGESTION_ERRORS_HIGH.getValue()),
    BUBBLE_ERRORS_HIGH(Selection.BUBBLE_ERRORS_HIGH,
            STLConstants.K1636_SEL_BUBBLE_ERRORS_HIGH.getValue()),
    SECURITY_ERRORS_HIGH(Selection.SECURITY_ERRORS_HIGH,
            STLConstants.K1637_SEL_SECURITY_ERRORS_HIGH.getValue()),
    ROUTING_ERRORS_HIGH(Selection.ROUTING_ERRORS_HIGH,
            STLConstants.K1638_SEL_ROUTING_ERRORS_HIGH.getValue());

    private final static EnumMap<Selection, SelectionViz> SelectionMap =
            new EnumMap<Selection, SelectionViz>(Selection.class);
    static {
        for (SelectionViz lqz : SelectionViz.values()) {
            SelectionMap.put(lqz.selection, lqz);
        }
    };

    private final Selection selection;

    private final String description;

    private SelectionViz(Selection selection, String description) {
        this.selection = selection;
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the selection
     */
    public Selection getSelection() {
        return selection;
    }

    public static String getSelectionStr(Selection selection) {
        SelectionViz viz = SelectionMap.get(selection);
        if (viz != null) {
            return viz.getDescription();
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find SelectionViz for " + selection);
        }
    }

    public static String getSelectionStr(int value) {
        for (SelectionViz lqz : SelectionViz.values()) {
            if (lqz.getSelection().getSelect() == value) {
                return lqz.getDescription();
            }
        }
        return null;
    }

}
