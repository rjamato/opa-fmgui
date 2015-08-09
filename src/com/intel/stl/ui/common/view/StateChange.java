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
 *  Archive Log:    Revision 1.1  2014/04/18 13:22:56  jypak
 *  Archive Log:    String constants and UI messages updates. Look and Feel updates. Additional updates regarding MVC.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import javax.swing.JButton;

import com.intel.stl.ui.common.UIConstants;

/**
 * JComponent state change implementation.
 * 
 * @author jypak
 */
public class StateChange {

    public static void enableActionButton(JButton button){
        button.setEnabled(true);
        button.setBackground(UIConstants.INTEL_BLUE);
        button.setForeground(UIConstants.INTEL_WHITE);
    }
    
    public static void enableDeleteButton(JButton button){
        button.setEnabled(true);
        button.setBackground(UIConstants.INTEL_RED);
        button.setForeground(UIConstants.INTEL_WHITE);
    }
    
    public static void enableCancelButton(JButton button){
        button.setEnabled(true);
        button.setBackground(UIConstants.INTEL_TABLE_BORDER_GRAY);
        button.setForeground(UIConstants.INTEL_DARK_GRAY);
    }
    
    public static void disableButton(JButton button){
        button.setEnabled(false);
        button.setBackground(UIConstants.INTEL_BACKGROUND_GRAY);
        button.setForeground(UIConstants.INTEL_LIGHT_GRAY);
    }
}
