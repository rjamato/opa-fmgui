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
 *  File Name: SwitchCategory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/12/31 17:41:15  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:    Some of the SubnetApi, CachedSubnetApi updates should be undone when the FE supports cable info, SC2SL, SC2VLt, SC2VLnt.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/29 18:51:55  fernande
 *  Archive Log:    Adding UserOptions XML and  saving it to the database. Includes XML schema validation.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SwitchCategory.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="SwitchCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NODE_INFO"/>
 *     &lt;enumeration value="DEVICE_GROUPS"/>
 *     &lt;enumeration value="NODE_PORT_INFO"/>
 *     &lt;enumeration value="SWITCH_INFO"/>
 *     &lt;enumeration value="SWITCH_FORWARDING"/>
 *     &lt;enumeration value="SWITCH_ROUTING"/>
 *     &lt;enumeration value="SWITCH_IPADDR"/>
 *     &lt;enumeration value="SWITCH_PARTITION_ENFORCEMENT"/>
 *     &lt;enumeration value="SWITCH_ADAPTIVE_ROUTING"/>
 *     &lt;enumeration value="MFT_TABLE"/>
 *     &lt;enumeration value="LFT_HISTOGRAM"/>
 *     &lt;enumeration value="LFT_TABLE"/>
 *     &lt;enumeration value="SC2SLMT_CHART"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SwitchCategory")
@XmlEnum
public enum SwitchCategory {

    NODE_INFO,
    DEVICE_GROUPS,
    NODE_PORT_INFO,
    SWITCH_INFO,
    SWITCH_FORWARDING,
    SWITCH_ROUTING,
    SWITCH_IPADDR,
    SWITCH_PARTITION_ENFORCEMENT,
    SWITCH_ADAPTIVE_ROUTING,
    MFT_TABLE,
    LFT_HISTOGRAM,
    LFT_TABLE,
    SC2SLMT_CHART;

    public String value() {
        return name();
    }

    public static SwitchCategory fromValue(String v) {
        return valueOf(v);
    }

}
