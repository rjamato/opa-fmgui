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
 *  File Name: PinBoardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:02  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/06/25 15:34:20  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - designed and implemented pin framework
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import java.awt.Rectangle;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.VerticalLayout;

import com.intel.stl.ui.common.view.DecoratedPinCardView;
import com.intel.stl.ui.common.view.JScrollablePanel;

public class PinBoardView extends JScrollablePanel {
    private static final long serialVersionUID = 1545198498434020983L;

    public PinBoardView() {
        super(new VerticalLayout());
    }

    public void appendPinCardView(final DecoratedPinCardView cardView) {
        add(cardView);
        revalidate();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollRectToVisible(cardView.getBounds());
            }
        });
    }

    public void removePinCardView(DecoratedPinCardView cardView) {
        remove(cardView);
        revalidate();
    }

    public void setPinCardViews(DecoratedPinCardView[] cardViews) {
        removeAll();
        for (DecoratedPinCardView cardView : cardViews) {
            add(cardView);
        }
        revalidate();
    }

    public void clear() {
        removeAll();
        revalidate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.JScrollablePanel#getScrollableBlockIncrement
     * (java.awt.Rectangle, int, int)
     */
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 50;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.JScrollablePanel#getScrollableUnitIncrement
     * (java.awt.Rectangle, int, int)
     */
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 50;
    }

}
