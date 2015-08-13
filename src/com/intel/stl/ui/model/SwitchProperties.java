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
 *  File Name: SwitchProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/08/22 16:51:26  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/12 20:58:01  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/05 17:25:54  jijunwan
 *  Archive Log:    fixed the wrong data processing on switch LFT and MFT data
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/02 21:48:51  jijunwan
 *  Archive Log:    Fixed the wrong LFT and MFT data processing. Added column headers to paged items. Adjust layout for switch property subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 14:29:25  jijunwan
 *  Archive Log:    handling unsigned short attributes that we will be treated as numbers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 18:32:10  jincoope
 *  Archive Log:    Moved from configuration package to this package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:48:03  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/10 12:52:24  jincoope
 *  Archive Log:    Attribute names start with lower case
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/09 19:51:30  jincoope
 *  Archive Log:    changed the name of this pack to all lower case
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/09 17:59:44  jincoope
 *  Archive Log:    Added for displaying properties
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jincoope
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.LFTRecordBean;
import com.intel.stl.api.subnet.MFTRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.ui.common.STLConstants;

/**
 * @deprecated use {@link com.intel.stl.ui.model.DevicePropertyCategory}
 */
@Deprecated
public class SwitchProperties extends NodeProperties {

    private SwitchRecordBean swRec = null;

    private NodeRecordBean nodeRec = null;

    private List<LFTRecordBean> lftRecs = null;

    private List<MFTRecordBean> mftRecs = null;

    private List<String> deviceGrp = null;

    boolean hasData = false;

    public SwitchProperties(SwitchRecordBean swR, NodeRecordBean nodeR,
            List<LFTRecordBean> lftR, List<MFTRecordBean> mftR, List<String> grp) {
        super(nodeR, grp);
        swRec = swR;
        lftRecs = lftR;
        mftRecs = mftR;
        nodeRec = nodeR;
        deviceGrp = grp;
        // NodeRec = nodeRec;

        if ((swRec != null) && (nodeRec != null) && (lftRecs != null)
                && (mftRecs != null) && (swRec.getSwitchInfo() != null)
                && (deviceGrp != null)) {
            hasData = true;
        }
    }

    public String getIPChassisName() {
        String retVal = STLConstants.K0383_NA.getValue();

        return retVal;
    }

    public String getNumEntries() {
        if (hasData) {
            return Integer.toString(swRec.getSwitchInfo()
                    .getPartitionEnforcementCap());
        } else {
            return "";
        }
    }

    public String getEnhancedPort0Support() {
        String retVal = "";
        if (hasData) {
            if (swRec.getSwitchInfo().isEnhancedPort0()) {
                retVal = STLConstants.K0385_TRUE.getValue();
            } else {
                retVal = STLConstants.K0386_FALSE.getValue();
            }
        }

        return retVal;
    }

    public List<String[]> getLIDsAndForwardedPorts() {
        List<String[]> retVal = new ArrayList<String[]>();

        if (hasData) {
            for (LFTRecordBean lft : lftRecs) {
                int lid = lft.getBlockNum() * SAConstants.FDB_DATA_LENGTH;
                byte[] retPts = lft.getLinearFdbData();
                for (int i = 0; i < retPts.length; i++, lid++) {
                    if (retPts[i] != -1) {
                        retVal.add(new String[] {
                                StringUtils.intHexString(lid),
                                Short.toString((short) (retPts[i] & 0xff)) });
                    }
                }
            }
        }

        return retVal;

    }

    public List<String[]> getLIDsAndMultiForwardedPorts() {
        List<String[]> retVal = new ArrayList<String[]>();

        if (hasData) {
            for (MFTRecordBean mft : mftRecs) {
                int lid =
                        mft.getBlockNum()
                                * SAConstants.STL_NUM_MFT_ELEMENTS_BLOCK
                                + SAConstants.LID_MCAST_START;
                long[] retMasks = mft.getMftTable();
                for (int i = 0; i < retMasks.length; i++, lid++) {
                    if (retMasks[i] > 0) {
                        retVal.add(new String[] {
                                StringUtils.intHexString(lid),
                                StringUtils.longHexString(retMasks[i]) });
                    }
                }
            }
        }
        return retVal;
    }

}
