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
 *  File Name: HelpMainWindow.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2016/03/17 19:45:52  jijunwan
 *  Archive Log:    PR 133329 - Icon update
 *  Archive Log:
 *  Archive Log:    - updated to the new icon
 *  Archive Log:    - fixed one issue on help window icon
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2016/01/06 21:35:23  jijunwan
 *  Archive Log:    PR 132307 - Multiple Help Windows and exception on closing them
 *  Archive Log:    - changed to create JDialog as frame case
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/10/20 16:28:39  jypak
 *  Archive Log:    PR 130913 - Java Help Window missing icon.
 *  Archive Log:    Null check first on window parameter.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/10/14 23:26:32  jypak
 *  Archive Log:    PR 130913 - Java Help Window missing icon.
 *  Archive Log:    Use a correct JDialog constructor in HelpMainWindow.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:53:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/07/22 18:13:40  jypak
 *  Archive Log:    PR 129129 - Multiple Help Windows.
 *  Archive Log:    HelpMainWindow is created to override setActivationWindow method so that only a dialog with modality is created for whole application.
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.awt.Frame;
import java.awt.Window;

import javax.help.HelpSet;
import javax.help.Presentation;
import javax.help.WindowPresentation;
import javax.swing.JDialog;

public class HelpMainWindow extends WindowPresentation {

    /**
     * Description:
     *
     * @param hs
     */
    private HelpMainWindow(HelpSet hs) {
        super(hs);
    }

    static public Presentation getPresentation(HelpSet hs, String name) {
        return new HelpMainWindow(hs);
    }

    @Override
    public void setActivationWindow(Window window) {
        if (window == null) {
            JDialog dialog = null;
            Frame[] frames = Frame.getFrames();
            if (frames != null && frames.length > 0) {
                dialog = new JDialog(Frame.getFrames()[frames.length - 1],
                        true);
            }
            super.setActivationWindow(dialog);
        } else if (window instanceof Frame) {
            JDialog dialog = new JDialog((Frame) window, true);
            super.setActivationWindow(dialog);
        } else {
            super.setActivationWindow(window);
        }
    }
}
