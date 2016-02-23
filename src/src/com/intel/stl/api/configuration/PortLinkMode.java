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
 *  File Name: PortLinkMode.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:48:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/07/16 16:33:33  jijunwan
 *  Archive Log:    PR 129228 - remove PortLTPCRCMode of Al
 *  Archive Log:    -  removed STL_PORT_LTP_CRC_MODE_ALL and STL_PORT_LINK_MODE_ALL_SUPPORTED
 *  Archive Log:    - changed to use STLConstants for string print out
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 18:40:03  jijunwan
 *  Archive Log:    PR 126387 - SCMappingTableRecord PortLinkMode ambiguous
 *  Archive Log:    updated to the latest version stl_sa.h v1.92
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:28:32  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.Arrays;

import com.intel.stl.api.StringUtils;

/**
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_sm.h v1.148
 * STL Port link mode, indicated as follows:
 * values are additive for Supported and Enabled fields
 * 
 * #define STL_PORT_LINK_MODE_NOP  0       // No change
 * // reserved 1
 * #define STL_PORT_LINK_MODE_ETH  2       // Port mode is ETH (Gateway)
 * #define STL_PORT_LINK_MODE_STL  4       // Port mode is STL
 * 
 * </pre>
 */
public enum PortLinkMode {
    NOP((byte) 0x00),
    ETH((byte) 0x02),
    STL((byte) 0x04);

    private final byte mode;

    private PortLinkMode(byte mode) {
        this.mode = mode;
    }

    public byte getMode() {
        return mode;
    }

    public static PortLinkMode getPortLinkMode(byte mode) {
        for (PortLinkMode plm : PortLinkMode.values()) {
            if (plm.getMode() == mode) {
                return plm;
            }
        }
        throw new IllegalArgumentException("Unsupported PortLinkMode "
                + StringUtils.byteHexString(mode));
    }

    public static PortLinkMode[] getPortLinkModes(byte mode) {
        if (isNoneSupported(mode)) {
            return new PortLinkMode[] { NOP };
        }
        PortLinkMode[] portLinkModes = PortLinkMode.values();
        PortLinkMode[] modes = new PortLinkMode[portLinkModes.length];
        int numModes = 0;
        for (PortLinkMode plm : portLinkModes) {
            if (plm != NOP && ((plm.mode & mode) == plm.mode)) {
                modes[numModes] = plm;
                numModes++;
            }
        }
        return Arrays.copyOf(modes, numModes);
    }

    public static boolean isNoneSupported(byte mode) {
        return mode == NOP.mode;
    }

}
