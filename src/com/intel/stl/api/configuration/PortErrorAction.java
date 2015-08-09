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
 *  File Name: PortErrorActionMask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/21 22:51:00  jijunwan
 *  Archive Log:    improved to throw exception when we encounter unsupported value. This will help us identify problems when it happens.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 16:39:46  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.StringUtils;

public enum PortErrorAction {

    EXCESSIVE_BUFFER_OVERRUN(0x80000000),
    FM_CONFIG_ERROR_EXCEEDMULTICASTLIMIT(0x00800000),
    FM_CONFIG_ERROR_BADCONTROLFLIT(0x00400000),
    FM_CONFIG_ERROR_BADPREEMPT(0x00200000),
    FM_CONFIG_ERROR_UNSUPPORTEDVLMARKER(0x00100000),
    FM_CONFIG_ERROR_BADCRDTACK(0x00080000),
    FM_CONFIG_ERROR_BADCTRLDIST(0x00040000),
    FM_CONFIG_ERROR_BADTAILDIST(0x00020000),
    FM_CONFIG_ERROR_BADHEADDIST(0x00010000),
    PORT_RCV_ERROR_BADVLMARKER(0x00002000),
    PORT_RCV_ERROR_PREEMPTVL15(0x00001000),
    PORT_RCV_ERROR_PREEMPTERROR(0x00000800),
    PORT_RCV_ERROR_BADMIDTAIL(0x00000200),
    PORT_RCV_ERROR_RESERVED(0x00000100),
    PORT_RCV_ERROR_BADSC(0x00000080),
    PORT_RCV_ERROR_BADL2(0x00000040),
    PORT_RCV_ERROR_BADDLID(0x00000020),
    PORT_RCV_ERROR_BADSLID(0x00000010),
    PORT_RCV_ERROR_PKTLENTOOSHORT(0x00000008),
    PORT_RCV_ERROR_PKTLENTOOLONG(0x00000004),
    PORT_RCV_ERROR_BADPKTLEN(0x00000002);

    private final int mask;

    private PortErrorAction(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    public static PortErrorAction getPortErrorAction(int value) {
        for (PortErrorAction pea : PortErrorAction.values()) {
            if (pea.mask == value) {
                return pea;
            }
        }
        throw new IllegalArgumentException("Unsupported PortErrorAction "
                + StringUtils.intHexString(value));
    }

    public static List<PortErrorAction> getPortErroActions(int value) {
        List<PortErrorAction> errorActions = new ArrayList<PortErrorAction>();
        for (PortErrorAction pea : PortErrorAction.values()) {
            if ((value & pea.mask) == pea.mask) {
                errorActions.add(pea);
            }
        }
        return errorActions;
    }
}
