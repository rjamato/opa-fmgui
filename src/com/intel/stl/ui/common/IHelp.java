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
 *  File Name: ConsoleHelpView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/17 19:34:17  rjtierne
 *  Archive Log:    Added parser to navigate the help system based on user input
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/01 19:39:36  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Interface for the console help utility
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.common;

import java.util.List;

import javax.swing.JPanel;

/**
 * @author rjtierne
 * 
 */
public interface IHelp {

    public void showTopic(String topicId);

    public JPanel getView();

    public void updateSelection(String value);

    public List<String> getTopicIdList();

    public void parseCommand(String command);
}
