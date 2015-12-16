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
 *  File Name: IntelSplitPaneDivider.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.2  2015/08/17 18:53:36  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.1  2015/06/22 13:11:51  jypak
 *  Archive Log: PR 128980 - Be able to search devices by name or lid.
 *  Archive Log: New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jypak
 * 
 ******************************************************************************/
package com.intel.stl.ui.common.view;
import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.intel.stl.ui.common.UIImages;
;

public class IntelSplitPaneDivider extends BasicSplitPaneDivider {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final Cursor defaultCursor = Cursor
            .getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    public IntelSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }

    @Override
    protected JButton createLeftOneTouchButton() {
        JButton b =
                ComponentFactory.getImageButton(UIImages.EXPAND_UP
                        .getImageIcon());
        b.setFocusPainted(false);
        b.setCursor(defaultCursor);
        return b;
    }

    @Override
    protected JButton createRightOneTouchButton() {
        JButton b =
                ComponentFactory.getImageButton(UIImages.EXPAND_DOWN
                        .getImageIcon());
        b.setFocusPainted(false);
        b.setCursor(defaultCursor);
        return b;
    }

}
