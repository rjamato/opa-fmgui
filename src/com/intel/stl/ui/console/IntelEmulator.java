/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.6  2015/08/17 18:54:27  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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
