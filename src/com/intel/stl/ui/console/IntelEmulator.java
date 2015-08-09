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
 *  File Name: IntelEmulator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/10/13 14:55:01  rjtierne
 *  Archive Log:    Remove command filtering to allow all commands pass to remote system
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/07 19:55:13  rjtierne
 *  Archive Log:    Added filter to ensure only fast fabric commands are sent to remote host
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/01 20:24:55  rjtierne
 *  Archive Log:    Off-by-one error in sendBytes()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/01 19:48:03  rjtierne
 *  Archive Log:    Overriding Emulator sendBytes() to provide filtering for security
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/23 19:46:16  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: The IntelEmulator extends the Gritty Emulator class to provide 
 *  filtering of commands entered at a console  
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console;

import java.awt.Dimension;
import java.io.IOException;

import com.wittams.gritty.Emulator;
import com.wittams.gritty.RequestOrigin;
import com.wittams.gritty.TerminalWriter;
import com.wittams.gritty.TtyChannel;

public class IntelEmulator extends Emulator {

    private final TerminalWriter tw;

    /**
     * Description:
     * 
     * @param tw
     * @param channel
     */
    public IntelEmulator(TerminalWriter tw, TtyChannel channel) {
        super(tw, channel);
        this.tw = tw;
    }

    @Override
    public void postResize(Dimension dimension, final RequestOrigin origin) {
        Dimension pixelSize;
        synchronized (tw) {

            dimension =
                    new Dimension(Math.max(1, dimension.width), Math.max(1,
                            dimension.height));
            pixelSize = tw.resize(dimension, origin);
        }
        channel.postResize(dimension, pixelSize);
    }

    @Override
    public void sendBytes(final byte[] bytes) throws IOException {

        String commandToSend = new String(bytes, "UTF-8");

        // Send the command
        if (commandToSend != null) {
            channel.sendBytes(commandToSend.getBytes());
        }
    }
}
