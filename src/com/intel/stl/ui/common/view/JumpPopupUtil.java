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
 *  File Name: JumpPopopUtil.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/02 21:26:19  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/18 21:03:29  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.event.JumpDestination;

public class JumpPopupUtil {
    public static JMenu appendPopupMenu(JPopupMenu popup, boolean withSeprator,
            IActionCreator actionCreator) {
        if (withSeprator) {
            popup.addSeparator();
        }

        JMenu jumpToMenu = new JMenu(STLConstants.K1055_INSPECT.getValue());
        jumpToMenu.setIcon(UIImages.LINK.getImageIcon());
        popup.add(jumpToMenu);

        JMenuItem[] jumpToDestinations =
                new JMenuItem[JumpDestination.values().length];
        for (int i = 0; i < jumpToDestinations.length; i++) {
            JumpDestination destination = JumpDestination.values()[i];
            jumpToDestinations[i] =
                    new JMenuItem(actionCreator.createAction(destination));
            jumpToMenu.add(jumpToDestinations[i]);
        }
        return jumpToMenu;
    }

    public interface IActionCreator {
        Action createAction(JumpDestination destination);
    }
}
