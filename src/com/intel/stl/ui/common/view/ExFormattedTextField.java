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
 *  File Name: ExFormattedTextFiled.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/10/06 20:19:22  fernande
 *  Archive Log:    PR130749 - FM GUI virtual fabric information doesn't match opafm.xml file. Removed overriding of method setFormatter
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/27 11:51:43  rjtierne
 *  Archive Log:    PR 130139 - Wizard window go blank after abandoning duplicate subnet
 *  Archive Log:    Checked in Jijun's code to fix 2 bugs in the Wizard:
 *  Archive Log:    1. Wizard tabs go blank when clicking on an existing subnet button after abandoning a duplicate subnet
 *  Archive Log:    2. Dialog with message "Do you want to abandon changes?" is displayed even if nothing was changed
 *  Archive Log:    Needed ExFormattedTextField to override setFormatter() to prevent the "abandon changes" from being displayed
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:53:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/07/13 16:02:34  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - added extended FormattedTextField that will
 *  Archive Log:    1) use AbstractFormatter to verify input
 *  Archive Log:    2) when we have invalid value
 *  Archive Log:    2.1) keep focus
 *  Archive Log:    2.2) change textfield to light red background color with red border
 *  Archive Log:    2.3) automatically show tooptip for invalid value
 *  Archive Log:    - added basic SafeTextField that will check text whether it's empty or not, whether it contain chars not supported
 *  Archive Log:    - added SafeNumberField that will check value range and valid chars
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatterFactory;

import com.intel.stl.ui.common.UIConstants;

/**
 * Extended JFormattedTextFiled that will show error hint on the TextField
 */
public class ExFormattedTextField extends JFormattedTextField {
    private static final long serialVersionUID = 6265122315036017331L;

    private Border orgBorder;

    private String orgTooltip;

    private String validationTooltip;

    /**
     * Description:
     * 
     * @param formatter
     */
    public ExFormattedTextField(AbstractFormatter formatter) {
        super(formatter);
        init();
    }

    /**
     * Description:
     * 
     * @param format
     */
    public ExFormattedTextField(Format format) {
        super(format);
        init();
    }

    protected void init() {
        // keep focus when we have invalid value
        setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JFormattedTextField ftf = (JFormattedTextField) input;
                return ftf.isEditValid();
            }
        });

        addPropertyChangeListener("editValid", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() == Boolean.TRUE) {
                    setBackground(UIConstants.INTEL_WHITE);
                    setBorder(orgBorder);
                    setToolTipText(orgTooltip);
                } else {
                    setBackground(UIConstants.INTEL_LIGHT_RED);

                    if (orgBorder == null) {
                        orgBorder = getBorder();
                    }
                    setBorder(BorderFactory.createLineBorder(
                            UIConstants.INTEL_RED, 2));

                    if (validationTooltip != null) {
                        if (orgTooltip == null) {
                            orgTooltip = getToolTipText();
                        }
                        setToolTipText(validationTooltip);
                        // show tooltip immediately
                        ToolTipManager.sharedInstance().mouseMoved(
                                new MouseEvent(ExFormattedTextField.this, 0, 0,
                                        0, 0, 0, 0, false));
                    }
                }
            }
        });
    }

    /**
     * @param validationTooltip
     *            the validationTooltip to set
     */
    public void setValidationTooltip(String validationTooltip) {
        this.validationTooltip = validationTooltip;
    }

    public void setFixedFormatter(AbstractFormatter formatter) {
        setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

}
