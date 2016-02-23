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
 *  File Name: LongField.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/10/06 20:19:41  fernande
 *  Archive Log:    PR130749 - FM GUI virtual fabric information doesn't match opafm.xml file. A previous fix disabled the update of most fields used in the Admin configuration editor. Restored the updating capability in those fields
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/17 18:53:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/07/16 21:22:53  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - extended SafeTextField to apply rules in name check
 *  Archive Log:    - moved valid chars to UIConstants
 *  Archive Log:    - made FieldPair more generic and flexible
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/14 17:02:38  jijunwan
 *  Archive Log:    PR 129541 - Should forbid save or deploy when there is invalid edit on management panel
 *  Archive Log:    - Introduce isEditValid for attribute renders
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/07/13 18:37:24  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - updated generic classes to use the new text field
 *  Archive Log:
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
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.SafeTextField.SafeStringFormatter;

/**
 * Field for a user to input hex value
 */
public class HexField<E extends Number> extends JPanel {
    private static final long serialVersionUID = 7780981211073008225L;

    // private final Class<?> type;

    private final String name;

    protected JFormattedTextField field;

    private E lastValue;

    public HexField(String name, E defaultValue) {
        super();
        this.name = name;
        lastValue = defaultValue;
        // this.type = defaultValue.getClass();
        setName(name);
        initComponent();
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

    protected JFormattedTextField createTextField() {
        HexFormatter<E> formatter = new HexFormatter<E>(lastValue);
        ExFormattedTextField res = new ExFormattedTextField(formatter);
        formatter.setParent(res);
        return res;
    }

    public void setValue(E val) {
        field.setValue(val);
    }

    @SuppressWarnings("unchecked")
    public E getValue() {
        lastValue = (E) field.getValue();
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

    public boolean isEditValid() {
        return field.isEditValid();
    }

    public class HexFormatter<N extends Number> extends SafeStringFormatter {
        private static final long serialVersionUID = -233085733398543484L;

        /**
         * Description:
         * 
         */
        public HexFormatter(N defaultValue) {
            super(true);
            if (defaultValue != null) {
                setValueClass(defaultValue.getClass());
            }
            setValidCharacters(UIConstants.HEX_CHARS);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.intel.stl.ui.common.view.SafeTextField.SafeStringFormatter#
         * stringToValue(java.lang.String)
         */
        @Override
        public Object stringToValue(String txt) throws ParseException {
            checkEmpty(txt);
            checkLength(txt);
            checkValidChar(txt);

            try {
                Class<?> type = getValueClass();
                if (type == Long.class) {
                    setValidationTooltip(UILabels.STL81021_RANGE1_VALIDATION
                            .getDescription("0x0", "0xFFFFFFFFFFFFFFFF"));
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
                    setValidationTooltip(UILabels.STL81021_RANGE1_VALIDATION
                            .getDescription("0x0", "0xFFFFFFFF"));
                    long val = Long.decode(txt);
                    if (val > Integer.MAX_VALUE * 2 + 1
                            || val < Integer.MIN_VALUE) {
                        throw new IllegalArgumentException("Invalid range");
                    }
                    return (int) val;
                } else if (type == Short.class) {
                    setValidationTooltip(UILabels.STL81021_RANGE1_VALIDATION
                            .getDescription("0x0", "0xFFFF"));
                    int val = Integer.decode(txt);
                    if (val > Short.MAX_VALUE * 2 + 1 || val < Short.MIN_VALUE) {
                        throw new IllegalArgumentException("Invalid range");
                    }
                    return (short) val;
                } else if (type == Byte.class) {
                    setValidationTooltip(UILabels.STL81021_RANGE1_VALIDATION
                            .getDescription("0x0", "0xFF"));
                    short val = Short.decode(txt);
                    if (val > Byte.MAX_VALUE * 2 + 1 || val < Byte.MIN_VALUE) {
                        throw new IllegalArgumentException("Invalid range");
                    }
                    return (byte) val;
                } else {
                    throw new UnsupportedOperationException();
                }
            } catch (Exception e) {
                throw new ParseException(e.getMessage(), 0);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.text.DefaultFormatter#valueToString(java.lang.Object)
         */
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value == null) {
                return null;
            }
            if (value instanceof Long) {
                return StringUtils.longHexString((Long) value);
            } else if (value instanceof Integer) {
                return StringUtils.intHexString((Integer) value);
            } else if (value instanceof Short) {
                return StringUtils.shortHexString((Short) value);
            } else if (value instanceof Byte) {
                return StringUtils.byteHexString((Byte) value);
            }
            return null;
        }
    }
}
