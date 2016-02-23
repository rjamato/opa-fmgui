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
 *  File Name: OutputModuleTypeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/19 21:22:36  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - minor adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/19 21:06:36  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - adapt to latest FM code
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:53:46  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/07 14:57:58  jypak
 *  Archive Log:    PR 129397 -gaps in cableinfo output and handling.
 *  Archive Log:    Updates on the formats of the cableinfo output and also new enums were defined for different output values.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import com.intel.stl.api.subnet.OutputModuleType;
import com.intel.stl.ui.common.STLConstants;

public enum OutputModuleTypeViz {

    SDR(OutputModuleType.SDR, STLConstants.K1139_CABLE_SDR.getValue()),
    DDR(OutputModuleType.DDR, STLConstants.K1140_CABLE_DDR.getValue()),
    QDR(OutputModuleType.QDR, STLConstants.K1141_CABLE_QDR.getValue()),
    FDR(OutputModuleType.FDR, STLConstants.K1142_CABLE_FDR.getValue()),
    EDR(OutputModuleType.EDR, STLConstants.K1143_CABLE_EDR.getValue()),
    UNKNOWN(OutputModuleType.UNKNOWN, STLConstants.K0016_UNKNOWN.getValue());

    private final OutputModuleType type;

    private final String name;

    private OutputModuleTypeViz(OutputModuleType type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * @return the type
     */
    public OutputModuleType getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public static String getOutputModuleTypeVizFor(OutputModuleType[] types)
            throws Exception {
        OutputModuleTypeViz[] values = OutputModuleTypeViz.values();
        StringBuffer sb = new StringBuffer();
        for (int i = values.length - 1; i >= 0; i--) {
            for (OutputModuleType type : types) {
                if (type == values[i].getType()) {
                    sb.append(values[i].getName() + " ");
                }
            }
        }
        return sb.toString();
    }
}
