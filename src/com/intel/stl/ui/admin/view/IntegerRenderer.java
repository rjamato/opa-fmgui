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
 *  File Name: NumberRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/30 14:25:39  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 15:47:49  jijunwan
 *  Archive Log:    first version of VirtualFabric UI
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view;

import java.text.NumberFormat;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.ui.common.UIConstants;

public abstract class IntegerRenderer<T extends Number, E extends WrapperNode<T>>
        extends FieldRenderer<T, E> {
    protected int min, max;

    public IntegerRenderer(int min, int max, T defaultValue) {
        this(NumberFormat.getInstance(), min, max, defaultValue);
    }

    /**
     * Description:
     * 
     * @param name
     * @param format
     */
    public IntegerRenderer(NumberFormat format, int min, int max, T defaultValue) {
        super(format, defaultValue);
        this.min = min;
        this.max = max;
        field.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                String txt = field.getText();
                boolean isValid = isValid(txt);
                inidcateValidationState(isValid);
                return isValid;
            }

        });
    }

    protected boolean isValid(String str) {
        if (str.isEmpty()) {
            return true;
        }
        try {
            Integer intVal = Integer.decode(str);
            if (intVal >= min && intVal <= max) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    protected void inidcateValidationState(boolean isValid) {
        if (isValid) {
            field.setBackground(UIConstants.INTEL_WHITE);
        } else {
            field.setBackground(UIConstants.INTEL_LIGHT_RED);
        }
    }

}
