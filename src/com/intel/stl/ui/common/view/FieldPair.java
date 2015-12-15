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
 *  File Name: FieldPair.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.6  2015/08/17 18:53:36  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.5  2015/08/17 17:44:44  jijunwan
 *  Archive Log: PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log: - improvement on FieldPair to support flexible label width
 *  Archive Log:
 *  Archive Log: Revision 1.4  2015/07/16 21:22:53  jijunwan
 *  Archive Log: PR 129528 - input validation improvement
 *  Archive Log: - extended SafeTextField to apply rules in name check
 *  Archive Log: - moved valid chars to UIConstants
 *  Archive Log: - made FieldPair more generic and flexible
 *  Archive Log:
 *  Archive Log: Revision 1.3  2015/07/13 18:37:24  jijunwan
 *  Archive Log: PR 129528 - input validation improvement
 *  Archive Log: - updated generic classes to use the new text field
 *  Archive Log:
 *  Archive Log: Revision 1.2  2015/06/10 19:58:51  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: 
 * 
 ******************************************************************************/
package com.intel.stl.ui.common.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.UIConstants;

public class FieldPair<C extends Component> extends JPanel {
    private static final long serialVersionUID = 2171493826241135628L;

    protected int maxValue;

    protected String validChars;

    protected int maxLength;

    protected JLabel label;

    protected C field;

    public FieldPair(String name, C component) {
        this(name, -1, component);
    }

    public FieldPair(String name, int labelWidth, C component) {
        initComponents(name, labelWidth, component);
    }

    protected void initComponents(String name, int labelWidth, C component) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setOpaque(true);
        setBackground(UIConstants.INTEL_WHITE);

        // Create a label
        label = createLabel(name, labelWidth);
        add(label);
        add(Box.createHorizontalStrut(5));

        this.field = component;
        add(component);
    }

    protected JLabel createLabel(String name, int labelWidth) {
        JLabel lblName = ComponentFactory.getH5Label(name + ": ", Font.BOLD);
        lblName.setHorizontalAlignment(JLabel.RIGHT);
        if (labelWidth > 0) {
            lblName.setPreferredSize(new Dimension(labelWidth, lblName
                    .getPreferredSize().height));
        }
        return lblName;
    }

    public JLabel getLabel() {
        return label;
    }

    /**
     * @return the field
     */
    public C getField() {
        return field;
    }

} // class FieldPair
