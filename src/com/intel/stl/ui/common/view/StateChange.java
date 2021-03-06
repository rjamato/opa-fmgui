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
 *  File Name: 
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/08/17 18:53:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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
