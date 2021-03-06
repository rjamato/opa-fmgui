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
 *  File Name: GIDField.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:53:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/14 17:02:38  jijunwan
 *  Archive Log:    PR 129541 - Should forbid save or deploy when there is invalid edit on management panel
 *  Archive Log:    - Introduce isEditValid for attribute renders
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/27 15:49:24  jijunwan
 *  Archive Log:    changed LongField to more general HexField
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:47:08  jijunwan
 *  Archive Log:    init value for Long and GID fields
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:37:28  jijunwan
 *  Archive Log:    new common widgets
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Field for a user to input gid value that is 128 bit long and is represented
 * as two 64 bit values separated by a colon (:)
 */
public class GIDField extends JPanel {
    private static final long serialVersionUID = 7780981211073008225L;

    private final String name;

    private HexField<Long> lowerField;

    private HexField<Long> upperField;

    public GIDField(String name) {
        super();
        this.name = name;
        setName(name);
        initComponent();
        setValue(0, 0);
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    protected void initComponent() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridwidth = 1;
        gc.weightx = 0;
        JLabel label = new JLabel(name);
        add(label, gc);

        gc.weightx = 1;
        lowerField = new HexField<Long>(null, 0L);
        add(lowerField, gc);

        gc.weightx = 0;
        label = new JLabel(":");
        add(label, gc);

        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;
        upperField = new HexField<Long>(null, 0L);
        add(upperField, gc);
    }

    public void setValue(long lower, long upper) {
        lowerField.setValue(lower);
        upperField.setValue(upper);
    }

    public long[] getValue() {
        long[] res = new long[2];
        res[0] = lowerField.getValue();
        res[1] = upperField.getValue();
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        lowerField.setEnabled(enabled);
        upperField.setEnabled(enabled);
    }

    public void setEditable(boolean b) {
        lowerField.setEditable(b);
        upperField.setEditable(b);
    }

    public boolean isEditValid() {
        return lowerField.isEditValid() && upperField.isEditValid();
    }
}
