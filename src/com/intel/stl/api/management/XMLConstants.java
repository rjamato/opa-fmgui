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
 *  File Name: XMLConstants.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/25 19:10:09  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:33:18  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/13 20:57:01  jijunwan
 *  Archive Log:    minor  improvement on FM Application
 *  Archive Log:    Added support on FM DeviceGroup
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

public class XMLConstants {
    public final static String NAME = "Name";

    public final static String APPLICATIONS = "Applications";

    public final static String APPLICATION = "Application";

    public final static String SERVICEID = "ServiceID";

    public final static String SERVICEID_RANGE = "ServiceIDRange";

    public final static String SERVICEID_MASKED = "ServiceIDMasked";

    public final static String MGID = "MGID";

    public final static String MGID_RANGE = "MGIDRange";

    public final static String MGID_MASKED = "MGIDMasked";

    public final static String SELECT = "Select";

    public final static String INCLUDE_APPLICATION = "IncludeApplication";

    public final static String DEVICE_GROUPS = "DeviceGroups";

    public final static String DEVICE_GROUP = "DeviceGroup";

    public final static String SI_GUID = "SystemImageGUID";

    public final static String NODE_GUID = "NodeGUID";

    public final static String PORT_GUID = "PortGUID";

    public final static String NODE_DESC = "NodeDesc";

    public final static String NODE_TYPE = "NodeType";

    public final static String INCLUDE_GROUP = "IncludeGroup";

    public final static String VIRTUAL_FABRICS = "VirtualFabrics";

    public final static String VIRTUAL_FABRIC = "VirtualFabric";

    public final static String MEMBER = "Member";

    public final static String LIMITED_MEMBER = "LimitedMember";

    public final static String ENABLE = "Enable";

    public final static String SECURITY = "Security";

    public final static String PKEY = "PKey";

    public final static String MAX_MTU = "MaxMTU";

    public final static String MAX_RATE = "MaxRate";

    public final static String STANDBY = "Standby";

    public final static String QOS = "QOS";

    public final static String HIGH_PRIORITY = "HighPriority";

    public final static String BANDWIDTH = "Bandwidth";

    public final static String PKT_LT_MULT = "PktLifeTimeMult";

    public final static String BASE_SL = "BaseSL";

    public final static String FLOW_CONTR_DISABLE = "FlowControlDisable";

    public final static String PREEMPT_RANK = "PreemptRank";

    public final static String HOQ_LIFE = "HoqLife";

    public final static String UNLIMITED = "Unlimited";
}
