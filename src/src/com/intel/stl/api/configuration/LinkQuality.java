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
 *  Archive Log:    Revision 1.6  2015/08/17 18:48:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/14 14:47:08  rjtierne
 *  Archive Log:    PR 128680 - Add reserved link quality and redefine unknown
 *  Archive Log:    Changed value of UNKNOWN to -1 to indicate a problem, distinguishable
 *  Archive Log:    from the new value RESERVED link quality (7) to indicated value received
 *  Archive Log:    from the FM; i.e. UNKNOWN should not be EXCELLENT link quality.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/05/14 13:21:04  rjtierne
 *  Archive Log:    Changed value of UNKNOWN to -1 to indicate a problem, distinguishable
 *  Archive Log:    from the new value RESERVED link quality (7) to indicated value received
 *  Archive Log:    from the FM; i.e. UNKNOWN should not be EXCELLENT link quality.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/16 19:39:59  jijunwan
 *  Archive Log:    updated to handle a bug on DC firmware that provides value 7 not specified in spec
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 18:27:31  jijunwan
 *  Archive Log:    PR 126371 - STL1 Spec inconsistencies - LinkQuality indicator
 *  Archive Log:    added LinkQuality, updated PortCounter data structure
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

import com.intel.stl.api.StringUtils;

/***
 * <pre>
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pm.h v1.34
 * LinkQualityIndicator values
 * #define STL_LINKQUALITY_EXCELLENT   5   // working as intended
 * #define STL_LINKQUALITY_VERY_GOOD   4   // slightly below preferred,
 *                                         // performance impact < 1%,
 *                                         // no action needed
 * #define STL_LINKQUALITY_GOOD        3   // low end of acceptable,
 *                                         // performance impact ~1-5%,
 *                                         // recommend corrective action on
 *                                         // next maintenance window
 * #define STL_LINKQUALITY_POOR        2   // below acceptable,
 *                                         // performance impact,
 *                                         // recommend timely corrective action
 * #define STL_LINKQUALITY_BAD         1   // far below acceptable,
 *                                         // reduced stability,
 *                                         // immediate corrective action
 * #define STL_LINKQUALITY_NONE        0   // link down
 * 
 * </pre>
 */
public enum LinkQuality {
    UNKNOWN((byte) -1),
    RESERVED((byte) 7),
    EXCELLENT((byte) 5),
    VERY_GOOD((byte) 4),
    GOOD((byte) 3),
    POOR((byte) 2),
    BAD((byte) 1),
    NONE((byte) 0);

    private final byte value;

    private LinkQuality(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static LinkQuality getLinkQuality(byte value) {
        for (LinkQuality linkQuality : LinkQuality.values()) {
            if (linkQuality.getValue() == value) {
                return linkQuality;
            }
        }
        throw new IllegalArgumentException("Unsupported LinkQuality "
                + StringUtils.byteHexString(value));
    }

}
