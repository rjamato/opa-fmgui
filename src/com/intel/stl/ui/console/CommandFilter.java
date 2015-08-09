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
 *  File Name: CommandFilter.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/07 19:53:18  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.console;

import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.wittams.gritty.CharacterUtils;

public class CommandFilter {

    private final List<String> topicIdList;

    public CommandFilter(List<String> topicIdList) {

        this.topicIdList = topicIdList;
    }

    public String validate(byte[] bytes) {

        String command = parseCommand(bytes);

        command = (topicIdList.contains(command)) ? command : null;

        return command;

    }

    protected String parseCommand(byte[] bytes) {

        // Assume that the submitted command contains a carriage return
        // and may or may not contain arguments
        String command = new String();
        String tempCommand = "";
        try {
            command = new String(bytes, "UTF-8");
            tempCommand = command;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // If there is a space in the command, then there are parameters
        String baseCommand = new String();
        int spaceIndex = tempCommand.indexOf(" ");
        if (spaceIndex >= 0) {
            baseCommand = tempCommand.substring(0, spaceIndex);
        } else {
            // If the command contains no space, then there are no parameters
            // and the delimiter must be a carriage return
            int crIndex =
                    tempCommand.indexOf(CharacterUtils
                            .getCode(KeyEvent.VK_ENTER)[0]);
            if (crIndex >= 0) {
                baseCommand = tempCommand.substring(0, crIndex);
            }
        }

        return baseCommand;
    }
}
