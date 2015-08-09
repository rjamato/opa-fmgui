/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
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
 *  File Name: PortProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17  2015/02/05 21:21:47  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/04 21:44:16  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/01/23 20:15:18  jijunwan
 *  Archive Log:    PR 126673 - "Unsupported VL Cap(0X08)" for all Switch ports other than Switch port 0
 *  Archive Log:    STL is using VL Cap as a number rather than an enum. Removed VL Cap related IB enum, and represent Cap as as a byte number
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/01/13 20:18:47  jijunwan
 *  Archive Log:    method name change
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/01/13 18:22:36  jijunwan
 *  Archive Log:    support UniversalDiagCode and VendorDiagCode
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/22 16:51:26  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/08/12 20:58:01  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/05 18:39:04  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/29 13:48:52  fernande
 *  Archive Log:    Removed repetitive conversion from FE values to API enums
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/22 21:54:21  fernande
 *  Archive Log:    Adding models to support device properties
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/01 19:10:14  jijunwan
 *  Archive Log:    used the new method on LinkSpeedMask
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/16 21:02:41  jijunwan
 *  Archive Log:    code clean up
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/11 22:15:37  jijunwan
 *  Archive Log:    added more info about Link To in property subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/05 18:32:51  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 14:29:25  jijunwan
 *  Archive Log:    handling unsigned short attributes that we will be treated as numbers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 18:32:10  jincoope
 *  Archive Log:    Moved from configuration package to this package
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 16:20:50  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/16 15:15:49  jijunwan
 *  Archive Log:    use HexUtils for hex string display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 15:06:06  fernande
 *  Archive Log:    Fixed wrong import
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

import static com.intel.stl.ui.common.STLConstants.K0388_OR;

import java.util.List;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.CapabilityMask;
import com.intel.stl.api.configuration.LinkSpeedMask;
import com.intel.stl.api.configuration.LinkWidthMask;
import com.intel.stl.api.configuration.MTUSize;
import com.intel.stl.api.configuration.PhysicalState;
import com.intel.stl.api.configuration.PortState;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.PortStatesBean;
import com.intel.stl.ui.common.STLConstants;

/**
 * @deprecated use {@link com.intel.stl.ui.model.DevicePropertyCategory}
 * @author jincoope
 * 
 */
@Deprecated
public class PortProperties extends NodeProperties {

    private PortRecordBean portRec = null;

    private PortInfoBean portInfo = null;

    private NodeRecordBean nodeRec = null;

    private LinkRecordBean linkRec = null;

    private NodeType nodeType = null;

    private String neighborNodeDesc = null;

    boolean hasData = false;

    boolean isEndPort = false;

    public PortProperties() {

    }

    /**
     * constructor
     * 
     * @param ptRec
     * @param nodeRec
     */
    public PortProperties(PortRecordBean ptRec, NodeRecordBean ndRec,
            LinkRecordBean lnkRec) {
        super(ndRec, null);

        if ((ptRec != null) && (ndRec != null)) {
            portRec = ptRec;
            nodeRec = ndRec;
            linkRec = lnkRec;

            if (portRec.getPortInfo() != null) {
                portInfo = portRec.getPortInfo();

                nodeType =
                        NodeType.getNodeType(nodeRec.getNodeInfo()
                                .getNodeType());
                int portIdx = nodeRec.getNodeInfo().getLocalPortNum();

                if ((nodeType != NodeType.SWITCH) || (portIdx == 0)) {
                    this.isEndPort = true;
                }
            }
        }
        hasData = portInfo != null;
    }

    /********** Port Device ***********/
    /**
     * 
     * @return Local ID of the port in String
     * 
     */
    public String getLID() {
        String retVal = "";

        if (hasData) {
            if (isEndPort) {
                retVal = StringUtils.intHexString(portInfo.getLid());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;

    }

    /**
     * 
     * @return State of the port in String
     */
    public String getState() {
        String retVal = "";

        if (hasData) {
            PortStatesBean portState = portInfo.getPortStates();

            if (portState != null) {
                PortState ptState = portState.getPortState();
                if (ptState != null) {
                    retVal = PortStateViz.getPortStateStr(ptState);
                } else {
                    retVal = STLConstants.K0387_UNKNOWN.getValue();
                }
            }
        }

        return retVal;
    }

    /**
     * 
     * @return Physical state of the port in String
     */
    public String getPhysicalState() {
        String retVal = "";

        if (hasData) {
            PortStatesBean PortState = portInfo.getPortStates();

            if (PortState != null) {
                PhysicalState phyState = PortState.getPortPhysicalState();
                if (phyState != null) {
                    retVal = PhysicalStateViz.getPhysicalStateStr(phyState);
                } else {
                    retVal = STLConstants.K0387_UNKNOWN.getValue();
                }

            }
        }

        return retVal;
    }

    // /**
    // *
    // * @return
    // */
    // public String getSuperNodeGUID() {
    // String retVal = "";
    //
    // if (HasData) {
    // retVal = this.getNodeGUID();
    // }
    //
    // return retVal;
    // }

    public String getSubnetPrefix() {
        String retVal = "";

        if (hasData) {
            if (isEndPort) {
                retVal = StringUtils.longHexString(portInfo.getSubnetPrefix());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getUniversalDiagCode() {

        String retVal = "";

        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getUniversalDiagCode();
                // TODO: STL hasn't define it yet, so just display its numerical
                // value for now
                retVal = Integer.toString(val);
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getVendorDiagCode() {

        String retVal = "";

        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getVendorDiagCode();
                // TODO: STL hasn't define it yet, so just display its numerical
                // value for now
                retVal = Integer.toString(val);
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getNeighborMTUSize() {
        String retVal = "";

        if (hasData) {
            byte val = portInfo.getNeighborVL0MTU()[0]; // this is how it is
                                                        // done
                                                        // in fillPortInfo()

            MTUSize mtusize = MTUSize.getMTUSize(val);
            if (mtusize != null) {
                retVal = MTUSizeViz.getMTUSizeStr(mtusize);
            } else {
                retVal = STLConstants.K0387_UNKNOWN.getValue();
            }
        }

        return retVal;
    }

    public String getMTUCapability() {
        String retVal = "";

        if (hasData) {
            MTUSize mtusize = portInfo.getMtuCap();

            if (mtusize != null) {
                retVal = MTUSizeViz.getMTUSizeStr(mtusize);
            } else {
                retVal = STLConstants.K0387_UNKNOWN.getValue();
            }
        }

        return retVal;
    }

    // public String getGUIDCap() {
    // if (HasData) {
    // if (this.IsEndPort) {
    // return Byte.toString(PortInfo.getGuidCap());
    // }
    // else {
    // return STLConstants.K0383_NA.getValue();
    // }
    // }
    // else
    // return "";
    //
    // }

    /*** Port Link ***/
    public String getLinkWidthEnabled() {
        String retVal = "";

        if (hasData) {
            short val = portInfo.getLinkWidthEnabled();

            retVal = getLinkWidthString(val);
        }

        return retVal;
    }

    public String getLinkWidthSupported() {
        String retVal = "";

        if (hasData) {
            short val = portInfo.getLinkWidthSupported();

            retVal = getLinkWidthString(val);
        }

        return retVal;
    }

    public String getLinkWidthActive() {
        String retVal = "";

        if (hasData) {
            short val = portInfo.getLinkWidthActive();

            retVal = getLinkWidthString(val);
        }

        return retVal;
    }

    private String getLinkWidthString(short val) {
        StringBuilder lwStr = new StringBuilder();
        String join = "";
        String or = " " + K0388_OR.getValue() + " ";
        List<LinkWidthMask> masks = LinkWidthMask.getWidthMasks(val);
        for (LinkWidthMask mask : masks) {
            lwStr.append(join);
            lwStr.append(LinkWidthMaskViz.getLinkWidthMaskStr(mask));
            join = or;
        }
        return lwStr.toString();
    }

    public String getLinkSpeedEnabled() {

        String retVal = "";

        if (hasData) {
            short val = portInfo.getLinkSpeedEnabled();

            retVal = getLinkSpeedString(val);
        }

        return retVal;
    }

    public String getLinkSpeedSupported() {

        String retVal = "";

        if (hasData) {
            short val = portInfo.getLinkSpeedSupported();

            retVal = getLinkSpeedString(val);
        }

        return retVal;

    }

    public String getLinkSpeedActive() {

        String retVal = "";

        if (hasData) {
            short val = portInfo.getLinkSpeedActive();
            retVal = getLinkSpeedString(val);
        }

        return retVal;
    }

    private String getLinkSpeedString(short val) {
        StringBuffer ret = new StringBuffer();
        String or = " " + K0388_OR.getValue() + " ";
        String orConn = "";
        List<LinkSpeedMask> masks = LinkSpeedMask.getSpeedMasks(val);
        for (LinkSpeedMask mask : masks) {
            ret.append(orConn);
            ret.append(LinkSpeedMaskViz.getLinkSpeedMaskStr(mask));
            orConn = or;
        }

        return ret.toString();
    }

    /**
     * @param neighborNodeDesc
     *            the neighborNodeDesc to set
     */
    public void setNeighborNodeDesc(String neighborNodeDesc) {
        this.neighborNodeDesc = neighborNodeDesc;
    }

    /**
     * Description:
     * 
     * @return
     */
    public String getLinkToNodeDesc() {
        String retVal = "";

        if (hasData) {
            retVal =
                    linkRec == null ? STLConstants.K0383_NA.getValue()
                            : neighborNodeDesc;
        }

        return retVal;
    }

    /**
     * Description:
     * 
     * @return
     */
    public String getLinkToGUID() {
        String retVal = "";

        if (hasData) {
            retVal =
                    linkRec == null ? STLConstants.K0383_NA.getValue()
                            : StringUtils.longHexString(portInfo
                                    .getNeighborNodeGUID());
        }

        return retVal;
    }

    public String getLinkToPortIndex() {
        String retVal = "";

        if (hasData) {
            retVal =
                    linkRec == null ? STLConstants.K0383_NA.getValue()
                            : Integer.toString(linkRec.getToPortIndex());
        }

        return retVal;
    }

    /***** Port Capability *****/
    public String getSMCapability() {
        String retVal = "";
        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getCapabilityMask();
                if (CapabilityMask.HAS_SM.hasThisMask(val)) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getNoticeCapability() {
        String retVal = "";
        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getCapabilityMask();
                if (CapabilityMask.HAS_NOTICE.hasThisMask(val)) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getVendorCapability() {
        String retVal = "";
        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getCapabilityMask();
                if (CapabilityMask.HAS_VENDORCLASS.hasThisMask(val)) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;

    }

    public String getDeviceManCapability() {
        String retVal = "";
        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getCapabilityMask();
                if (CapabilityMask.HAS_DEVICEMANAGEMENT.hasThisMask(val)) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getConnManCapability() {
        String retVal = "";
        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getCapabilityMask();
                if (CapabilityMask.HAS_CONNECTIONMANAGEMENT.hasThisMask(val)) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getAutoMigCapability() {
        String retVal = "";
        if (hasData) {
            if (isEndPort) {
                int val = portInfo.getCapabilityMask();
                if (CapabilityMask.HAS_AUTOMIGRATION.hasThisMask(val)) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    /***** Port Virtual Lane *****/
    public String getVLCap() {

        String retVal = "";

        if (hasData) {
            byte cap = portInfo.getVl().getCap();
            retVal = Byte.toString(cap);
        }

        return retVal;

    }

    public String getHighLimit() {
        if (hasData) {
            return Integer.toString(portInfo.getVl().getHighLimit());
        } else {
            return "";
        }
    }

    public String getHiArbitrationCap() {
        if (hasData) {
            return Short.toString(portInfo.getVl().getArbitrationHighCap());
        } else {
            return "";
        }
    }

    public String getLowArbitrationCap() {
        if (hasData) {
            return Short.toString(portInfo.getVl().getArbitrationLowCap());
        } else {
            return "";
        }
    }

    public String getVLStallCount() {
        String retVal = "";
        if (hasData) {
            if (nodeType == NodeType.SWITCH) {
                retVal = Byte.toString(portInfo.getVlStallCount()[0]);
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getHOQlifeLabel() {
        String retVal = "";

        if (hasData) {
            if (nodeType != NodeType.HFI) {
                retVal = Byte.toString(portInfo.getHoqLife()[0]);
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getOperationalVLs() {
        String retVal = "";

        if (hasData) {
            byte cap = portInfo.getOperationalVL();
            retVal = Byte.toString(cap);
        }

        return retVal;

    }

    /***** Port Diagnostic *****/
    public String getMastersSMSL() {
        String retVal = "";
        if (hasData) {
            if (this.isEndPort) {
                retVal = Byte.toString(portInfo.getMasterSMSL());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getMKeyViolations() {
        String retVal = "";
        if (hasData) {
            if (this.isEndPort) {
                retVal = Short.toString(portInfo.getMKeyViolation());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }
        return retVal;
    }

    public String getPKeyViolations() {
        String retVal = "";
        if (hasData) {
            if (this.isEndPort) {
                retVal = Short.toString(portInfo.getPKeyViolation());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }
        return retVal;
    }

    public String getQKeyViolations() {
        String retVal = "";
        if (hasData) {
            if (this.isEndPort) {
                retVal = Short.toString(portInfo.getQKeyViolation());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }
        return retVal;
    }

    public String getSubnetTimeout() {
        String retVal = "";

        if (hasData) {
            if (this.isEndPort) {
                double exp = Math.pow(2.0, portInfo.getSubnetTimeout());
                double val = 4.096 * exp;
                retVal = Double.toString(val);
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
            ;
        }
        return retVal;
    }

    public String getRespTime() {
        String retVal = "";

        if (hasData) {
            if (this.isEndPort) {
                double exp = Math.pow(2.0, portInfo.getRespTimeValue());
                double val = 4.096 * exp;
                retVal = Double.toString(val);
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }
        return retVal;
    }

    // public String getPhysicalError() {
    // String retVal = "";
    // if (HasData) {
    // retVal = Byte.toString(PortInfo.getLocalPhysErrors());
    // }
    //
    // return retVal;
    // }

    // public String getOverrunError() {
    //
    // String retVal = "";
    // if (HasData) {
    // retVal = Byte.toString(PortInfo.getOverrunErrors());
    // }
    //
    // return retVal;
    // }

    /***** partition enforcement *****/
    public String getPartEnforceIn() {

        String retVal = "";

        if (hasData) {
            if (nodeType == NodeType.SWITCH) {
                if (portInfo.isPartitionEnforcementInbound()) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }
        return retVal;
    }

    public String getPartEnforceOut() {

        String retVal = "";

        if (hasData) {
            if (nodeType == NodeType.SWITCH) {
                if (portInfo.isPartitionEnforcementOutbound()) {
                    retVal = STLConstants.K0385_TRUE.getValue();
                } else {
                    retVal = STLConstants.K0386_FALSE.getValue();
                }
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }
        return retVal;
    }

    // public String getFilterRawPktIn() {
    //
    // String retVal = "";
    //
    // if (HasData) {
    // if (nodeType == Constants.NodeType.SWITCH) {
    // if (PortInfo.isFilterRawInbound())
    // retVal = STLConstants.K0385_TRUE.getValue();
    // else
    // retVal = STLConstants.K0386_FALSE.getValue();
    // }
    // else
    // retVal = STLConstants.K0383_NA.getValue();
    // }
    // return retVal;
    //
    // }

    // public String getFilterRawPktOut() {
    //
    // String retVal = "";
    //
    // if (HasData) {
    // if (nodeType == Constants.NodeType.SWITCH) {
    // if (PortInfo.isFilterRawOutbound())
    // retVal = STLConstants.K0385_TRUE.getValue();
    // else
    // retVal = STLConstants.K0386_FALSE.getValue();
    // }
    // else
    // retVal = STLConstants.K0383_NA.getValue();
    // }
    // return retVal;
    //
    // }

    /*** management information ***/
    public String getMKey() {
        String retVal = "";

        if (hasData) {
            if (this.isEndPort) {
                retVal = Long.toString(portInfo.getMKey());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getMasterLID() {
        String retVal = "";

        if (hasData) {
            if (this.isEndPort) {
                retVal = StringUtils.intHexString(portInfo.getMasterSMLID());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

    public String getMKeyLeasePeriod() {
        String retVal = "";

        if (hasData) {
            if (this.isEndPort) {
                retVal = Integer.toString(portInfo.getMKeyLeasePeriod());
            } else {
                retVal = STLConstants.K0383_NA.getValue();
            }
        }

        return retVal;
    }

}
