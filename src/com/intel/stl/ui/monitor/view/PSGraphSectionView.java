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
 *  File Name: PSGraphSectionView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:54:25  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/22 18:48:38  jijunwan
 *  Archive Log:    moved ChartsSectionView/ChartsSectionController to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/11 19:28:16  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:25:18  rjtierne
 *  Archive Log:    Renamed from PerfSummaryGraphSectionView and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *
 *  Overview: View for the graphs section of the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import static com.intel.stl.ui.common.STLConstants.K0204_GENERAL_SUMMARY;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.intel.stl.ui.common.view.ChartsSectionView;

/**
 * @author jijunwan
 * 
 */
public class PSGraphSectionView extends ChartsSectionView {
    private static final long serialVersionUID = -2957418754040069251L;

    public PSGraphSectionView() {
        super(K0204_GENERAL_SUMMARY.getValue());
    }

    @Override
    protected void setLayout(JPanel mainPanel) {
        mainPanel.setLayout(new GridLayout(2, 3, 5, 5));
    }

}
