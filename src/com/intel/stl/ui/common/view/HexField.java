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
 *  File Name: LongField.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/09 16:39:12  jijunwan
 *  Archive Log:    improved to support decimal for long value
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 15:49:24  jijunwan
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
import java.math.BigInteger;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.UIConstants;

/**
 * Field for a user to input hex value
 */
public class HexField<E extends Number> extends JPanel {
    private static final long serialVersionUID = 7780981211073008225L;

    private final Class<?> type;

    private final String name;

    protected JTextField field;

    private E lastValue;

    public HexField(String name, E defaultValue) {
        super();
        this.name = name;
        this.type = defaultValue.getClass();
        setName(name);
        initComponent();
        lastValue = defaultValue;
        setValue(defaultValue);
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

        if (name != null) {
            gc.gridwidth = 1;
            gc.weightx = 0;
            JLabel label = new JLabel(name);
            add(label, gc);
        }

        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;
        field = createTextField();
        add(field, gc);
    }

    protected JTextField createTextField() {
        JTextField res = new JTextField();
        res.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                String txt = field.getText();
                boolean isValid = isValid(txt);
                if (isValid) {
                    input.setBackground(UIConstants.INTEL_WHITE);
                } else {
                    input.setBackground(UIConstants.INTEL_LIGHT_RED);
                }
                return isValid;
            }

        });
        return res;
    }

    @SuppressWarnings("unchecked")
    protected boolean isValid(String txt) {
        if (txt.isEmpty()) {
            return true;
        }
        try {
            Object val = parseValue(txt);
            setValue((E) val);
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    protected Object parseValue(String txt) throws Exception {
        if (type == Long.class) {
            int radix = 10;
            if (txt.startsWith("0x") || txt.startsWith("0X")) {
                txt = txt.substring(2, txt.length());
                radix = 16;
            }
            BigInteger bi = new BigInteger(txt, radix);
            if (bi.bitLength() > 64) {
                throw new IllegalArgumentException("Invalid range");
            }
            return bi.longValue();
        } else if (type == Integer.class) {
            long val = Long.decode(txt);
            if (val > Integer.MAX_VALUE * 2 + 1 || val < Integer.MIN_VALUE) {
                throw new IllegalArgumentException("Invalid range");
            }
            return (int) val;
        } else if (type == Short.class) {
            int val = Integer.decode(txt);
            if (val > Short.MAX_VALUE * 2 + 1 || val < Short.MIN_VALUE) {
                throw new IllegalArgumentException("Invalid range");
            }
            return (short) val;
        } else if (type == Byte.class) {
            short val = Short.decode(txt);
            if (val > Byte.MAX_VALUE * 2 + 1 || val < Byte.MIN_VALUE) {
                throw new IllegalArgumentException("Invalid range");
            }
            return (byte) val;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setValue(E val) {
        String txt = getValueString(val);
        field.setText(txt);
    }

    protected String getValueString(E value) {
        if (value instanceof Long) {
            return StringUtils.longHexString((Long) value);
        } else if (value instanceof Integer) {
            return StringUtils.intHexString((Integer) value);
        } else if (value instanceof Short) {
            return StringUtils.shortHexString((Short) value);
        } else if (value instanceof Byte) {
            return StringUtils.byteHexString((Byte) value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("unchecked")
    public E getValue() {
        String text = field.getText();
        Object val;
        try {
            val = parseValue(text);
            lastValue = (E) val;
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return lastValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        field.setEditable(enabled);
    }

    public void setEditable(boolean b) {
        field.setEditable(b);
    }
}
