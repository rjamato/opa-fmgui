/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
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
}
