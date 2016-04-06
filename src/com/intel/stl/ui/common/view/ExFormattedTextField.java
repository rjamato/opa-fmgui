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
 *  Archive Log:    Revision 1.7  2016/03/02 18:27:31  jijunwan
 *  Archive Log:    PR 133067 - Add a popup window that e-mail was sent successfully when "test" button is click
 *  Archive Log:
 *  Archive Log:    - changed to disable button after we click test button
 *  Archive Log:    - changed to show "sending email..." message when we are sending out a test email
 *  Archive Log:    - changed to show "Test message sent out, please check your email account." after email sent out
 *  Archive Log:    - change to recover message to normal text when there is a user action
 *  Archive Log:    - added undo/redo capability to email address text area
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/11/18 23:56:43  rjtierne
 *  Archive Log:    PR 130965 - ESM support on Log Viewer
 *  Archive Log:    - Override setEnabled() so extended formatted text fields could be disabled in a manner similar to the standard JTextField
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/10/21 16:04:38  jijunwan
 *  Archive Log:    PR 131108 - Overwrite enabled on all FM GUI text fields
 *  Archive Log:    - changed to insert mode
 *  Archive Log:    - improved to support undo/redo with  Ctrl+Z/Y
 *  Archive Log:
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
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;

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
        AbstractFormatter formatter = getFormatter();
        if (formatter != null && formatter instanceof DefaultFormatter) {
            ((DefaultFormatter) formatter).setOverwriteMode(false);
        }

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
                    setBorder(BorderFactory
                            .createLineBorder(UIConstants.INTEL_RED, 2));

                    if (validationTooltip != null) {
                        if (orgTooltip == null) {
                            orgTooltip = getToolTipText();
                        }
                        setToolTipText(validationTooltip);
                        // show tooltip immediately
                        ToolTipManager.sharedInstance()
                                .mouseMoved(new MouseEvent(
                                        ExFormattedTextField.this, 0, 0, 0, 0,
                                        0, 0, false));
                    }
                }
            }
        });

        Util.makeUndoable(this);
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

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (!b) {
            setBackground(UIConstants.INTEL_BACKGROUND_GRAY);
        } else {
            setBackground(UIConstants.INTEL_WHITE);
        }
    }

}
