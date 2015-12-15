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
 *  File Name: PortChartArgument.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:53:49  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/06/30 22:31:41  jijunwan
 *  Archive Log:    PR 129215 - Need short chart name to support pin capability
 *  Archive Log:    - use short name as pin card title
 *  Archive Log:    - improved pin argument to include full name and provide data source description
 *  Archive Log:    - fixed improper full name issues on trend charts
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/06/25 16:23:32  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - designed and implemented pin framework
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.intel.stl.ui.common.STLConstants;

/**
 * This class takes SINGLE source. It doesn't support multiple ports as sources.
 * May support it in the future if we have requirement on it.
 */
public class PortChartArgument extends ChartArgument<PortSourceName> {
    private static final long serialVersionUID = -6818285518643166649L;

    protected final static String DELIMITER = ":";

    public static final String VF_NAME = "VF Name";

    public static final String NODE_DESC = "Node Desc.";

    public static final String NODE_LID = "Node LID";

    public static final String PORT_NUM = "Port Num.";

    public String getVfName() {
        return getProperty(VF_NAME);
    }

    public String getNodeDesc() {
        return getProperty(NODE_DESC);
    }

    public int getLid() {
        String str = getProperty(NODE_LID);
        if (str != null) {
            return Integer.parseInt(str);
        } else {
            throw new RuntimeException("No LID found!");
        }
    }

    public short getPortNum() {
        String str = getProperty(PORT_NUM);
        if (str != null) {
            return Short.parseShort(str);
        } else {
            throw new RuntimeException("No PortNum found!");
        }
    }

    public void setSource(String nodeDesc, int lid, short portNum) {
        if (nodeDesc != null) {
            if (nodeDesc.contains(DELIMITER)) {
                throw new IllegalArgumentException(
                        "Node desc cannot contains the DELIMITER '" + DELIMITER
                                + "'");
            }
            put(NODE_DESC, nodeDesc);
        }
        put(NODE_LID, Integer.toString(lid));
        put(PORT_NUM, Short.toString(portNum));
    }

    public void setSource(String vfName, String nodeDesc, int lid, short portNum) {
        if (vfName != null) {
            if (vfName.contains(DELIMITER)) {
                throw new IllegalArgumentException(
                        "VF name cannot contains the DELIMITER '" + DELIMITER
                                + "'");
            }
            put(VF_NAME, vfName);
        }
        setSource(nodeDesc, lid, portNum);
    }

    public void setSource(String sourceName) {
        String[] segs = sourceName.split(DELIMITER);
        if (segs.length == 3) {
            put(NODE_DESC, segs[0]);
            put(NODE_LID, segs[1]);
            put(PORT_NUM, segs[2]);
        } else if (segs.length == 4) {
            put(VF_NAME, segs[0]);
            put(NODE_DESC, segs[1]);
            put(NODE_LID, segs[2]);
            put(PORT_NUM, segs[3]);
        } else {
            throw new IllegalArgumentException("Invalid source name '"
                    + sourceName + "'");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.performance.ChartArgument#getSources()
     */
    @Override
    public PortSourceName[] getSources() {
        PortSourceName source =
                new PortSourceName(getVfName(), getNodeDesc(), getLid(),
                        getPortNum());
        return new PortSourceName[] { source };
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinArgument#getSourceDescription()
     */
    @Override
    public Map<String, String> getSourceDescription() {
        Map<String, String> res = new LinkedHashMap<String, String>();
        String vfName = getProperty(VF_NAME);
        if (vfName != null) {
            res.put(STLConstants.K0116_VIRTUAL_FABRIC.getValue(), vfName);
        }
        res.put(STLConstants.K1035_CONFIGURATION_PORT.getValue(), getNodeDesc()
                + DELIMITER + getPortNum());
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.performance.ChartArgument#setSources(com.intel.stl.ui
     * .performance.ISource[])
     */
    @Override
    public void setSources(PortSourceName[] sources) {
        setSource(sources[0].getVfName(), sources[0].getNodeDesc(),
                sources[0].getLid(), sources[0].getPortNum());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(getSources());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PortChartArgument other = (PortChartArgument) obj;
        if (!Arrays.equals(getSources(), other.getSources())) {
            return false;
        }
        return true;
    }

}
