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
 *  File Name: PreemptRankRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:54:01  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/07/16 21:26:32  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - restrict numbers to be positive integer, i.e. dot and minus are invalid chars
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/07/13 19:16:57  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - In VF config, PreemptRank must be in the format of "###", the value shall in range [0, 127]. If a user types in a decimal number it will be cast to an integer. Default value is 0.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/30 14:25:37  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.virtualfabrics;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.intel.stl.api.management.virtualfabrics.PreemptRank;
import com.intel.stl.ui.admin.view.FieldRenderer;
import com.intel.stl.ui.common.view.ExFormattedTextField;
import com.intel.stl.ui.common.view.SafeNumberField;

public class PreemptRankRenderer extends FieldRenderer<Byte, PreemptRank> {
    /**
     * Description:
     * 
     * @param format
     * @param min
     * @param max
     * @param defaultValue
     */
    public PreemptRankRenderer() {
        super(new DecimalFormat("###"), (byte) 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.FieldRenderer#createFiled(java.text.NumberFormat
     * )
     */
    @Override
    protected ExFormattedTextField createFiled(NumberFormat format) {
        SafeNumberField<Byte> res =
                new SafeNumberField<Byte>(format, (byte) 0, true, (byte) 127,
                        true);
        // only positive byte
        res.setValidChars("0123456789");
        return res;
    }

    @Override
    protected PreemptRank createAttr(Byte value) {
        return new PreemptRank(value);
    }

}
