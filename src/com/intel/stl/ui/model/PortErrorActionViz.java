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
 *  File Name: PortErrorActionViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/08/22 16:51:26  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import static com.intel.stl.ui.common.STLConstants.K0776_EXCESSIVE_BUFF_OVERRUN;
import static com.intel.stl.ui.common.STLConstants.K0777_FM_CONFIG_ERROR;
import static com.intel.stl.ui.common.STLConstants.K0778_PORT_RCV_ERROR;
import static com.intel.stl.ui.common.STLConstants.K0779_EXCEEDMULTICASTLIMIT;
import static com.intel.stl.ui.common.STLConstants.K0780_BADCONTROLFLIT;
import static com.intel.stl.ui.common.STLConstants.K0781_BADPREEMPT;
import static com.intel.stl.ui.common.STLConstants.K0782_UNSUPPORTEDVLMARKER;
import static com.intel.stl.ui.common.STLConstants.K0784_BADCTRLDIST;
import static com.intel.stl.ui.common.STLConstants.K0785_BADTAILDIST;
import static com.intel.stl.ui.common.STLConstants.K0786_BADHEADDIST;
import static com.intel.stl.ui.common.STLConstants.K0787_BADVLMARKER;
import static com.intel.stl.ui.common.STLConstants.K0788_PREEMPTVL15;
import static com.intel.stl.ui.common.STLConstants.K0789_PREEMPTERROR;
import static com.intel.stl.ui.common.STLConstants.K0790_BADMIDTAIL;
import static com.intel.stl.ui.common.STLConstants.K0791_RESERVED;
import static com.intel.stl.ui.common.STLConstants.K0792_BADSC;
import static com.intel.stl.ui.common.STLConstants.K0793_BADL2;
import static com.intel.stl.ui.common.STLConstants.K0794_BADDLID;
import static com.intel.stl.ui.common.STLConstants.K0795_BADSLID;
import static com.intel.stl.ui.common.STLConstants.K0796_PKTLENTOOSHORT;
import static com.intel.stl.ui.common.STLConstants.K0797_PKTLENTOOLONG;
import static com.intel.stl.ui.common.STLConstants.K0798_BADPKTLEN;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.configuration.PortErrorAction;

public enum PortErrorActionViz {

    EXCESSIVE_BUFFER_OVERRUN(
            PortErrorAction.EXCESSIVE_BUFFER_OVERRUN,
            K0776_EXCESSIVE_BUFF_OVERRUN.getValue(),
            ""),
    FM_CONFIG_ERROR_EXCEEDMULTICASTLIMIT(
            PortErrorAction.FM_CONFIG_ERROR_EXCEEDMULTICASTLIMIT,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0779_EXCEEDMULTICASTLIMIT.getValue()),
    FM_CONFIG_ERROR_BADCONTROLFLIT(
            PortErrorAction.FM_CONFIG_ERROR_BADCONTROLFLIT,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0780_BADCONTROLFLIT.getValue()),
    FM_CONFIG_ERROR_BADPREEMPT(
            PortErrorAction.FM_CONFIG_ERROR_BADPREEMPT,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0781_BADPREEMPT.getValue()),
    FM_CONFIG_ERROR_UNSUPPORTEDVLMARKER(
            PortErrorAction.FM_CONFIG_ERROR_UNSUPPORTEDVLMARKER,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0782_UNSUPPORTEDVLMARKER.getValue()),
    FM_CONFIG_ERROR_BADCRDTACK(
            PortErrorAction.FM_CONFIG_ERROR_BADCRDTACK,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0782_UNSUPPORTEDVLMARKER.getValue()),
    FM_CONFIG_ERROR_BADCTRLDIST(
            PortErrorAction.FM_CONFIG_ERROR_BADCTRLDIST,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0784_BADCTRLDIST.getValue()),
    FM_CONFIG_ERROR_BADTAILDIST(
            PortErrorAction.FM_CONFIG_ERROR_BADTAILDIST,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0785_BADTAILDIST.getValue()),
    FM_CONFIG_ERROR_BADHEADDIST(
            PortErrorAction.FM_CONFIG_ERROR_BADHEADDIST,
            K0777_FM_CONFIG_ERROR.getValue(),
            K0786_BADHEADDIST.getValue()),
    PORT_RCV_ERROR_BADVLMARKER(
            PortErrorAction.PORT_RCV_ERROR_BADVLMARKER,
            K0778_PORT_RCV_ERROR.getValue(),
            K0787_BADVLMARKER.getValue()),
    PORT_RCV_ERROR_PREEMPTVL15(
            PortErrorAction.PORT_RCV_ERROR_PREEMPTVL15,
            K0778_PORT_RCV_ERROR.getValue(),
            K0788_PREEMPTVL15.getValue()),
    PORT_RCV_ERROR_PREEMPTERROR(
            PortErrorAction.PORT_RCV_ERROR_PREEMPTERROR,
            K0778_PORT_RCV_ERROR.getValue(),
            K0789_PREEMPTERROR.getValue()),
    PORT_RCV_ERROR_BADMIDTAIL(
            PortErrorAction.PORT_RCV_ERROR_BADMIDTAIL,
            K0778_PORT_RCV_ERROR.getValue(),
            K0790_BADMIDTAIL.getValue()),
    PORT_RCV_ERROR_RESERVED(
            PortErrorAction.PORT_RCV_ERROR_RESERVED,
            K0778_PORT_RCV_ERROR.getValue(),
            K0791_RESERVED.getValue()),
    PORT_RCV_ERROR_BADSC(
            PortErrorAction.PORT_RCV_ERROR_BADSC,
            K0778_PORT_RCV_ERROR.getValue(),
            K0792_BADSC.getValue()),
    PORT_RCV_ERROR_BADL2(
            PortErrorAction.PORT_RCV_ERROR_BADL2,
            K0778_PORT_RCV_ERROR.getValue(),
            K0793_BADL2.getValue()),
    PORT_RCV_ERROR_BADDLID(
            PortErrorAction.PORT_RCV_ERROR_BADDLID,
            K0778_PORT_RCV_ERROR.getValue(),
            K0794_BADDLID.getValue()),
    PORT_RCV_ERROR_BADSLID(
            PortErrorAction.PORT_RCV_ERROR_BADSLID,
            K0778_PORT_RCV_ERROR.getValue(),
            K0795_BADSLID.getValue()),
    PORT_RCV_ERROR_PKTLENTOOSHORT(
            PortErrorAction.PORT_RCV_ERROR_PKTLENTOOSHORT,
            K0778_PORT_RCV_ERROR.getValue(),
            K0796_PKTLENTOOSHORT.getValue()),
    PORT_RCV_ERROR_PKTLENTOOLONG(
            PortErrorAction.PORT_RCV_ERROR_PKTLENTOOLONG,
            K0778_PORT_RCV_ERROR.getValue(),
            K0797_PKTLENTOOLONG.getValue()),
    PORT_RCV_ERROR_BADPKTLEN(
            PortErrorAction.PORT_RCV_ERROR_BADPKTLEN,
            K0778_PORT_RCV_ERROR.getValue(),
            K0798_BADPKTLEN.getValue());

    private final PortErrorAction errorAction;

    private final String generalReason;

    private final String specificReason;

    private PortErrorActionViz(PortErrorAction errorAction,
            String generalReason, String specificReason) {
        this.errorAction = errorAction;
        this.generalReason = generalReason;
        this.specificReason = specificReason;
    }

    public PortErrorAction getPortErrorAction() {
        return errorAction;
    }

    public String getGeneralReason() {
        return generalReason;
    }

    public String getSpecificReason() {
        return specificReason;
    }

    public String getReasonDescription() {
        if (specificReason == null || specificReason.length() == 0) {
            return generalReason;
        }
        return generalReason + ": " + specificReason;
    }

    public static PortErrorActionViz getPortErrorActionViz(int value) {
        for (PortErrorActionViz peav : PortErrorActionViz.values()) {
            if (peav.errorAction.getMask() == value) {
                return peav;
            }
        }
        return null;
    }

    public static List<PortErrorActionViz> getPortErroActions(int value) {
        List<PortErrorActionViz> errorActions = new ArrayList<PortErrorActionViz>();
        for (PortErrorActionViz peav : PortErrorActionViz.values()) {
            int mask = peav.errorAction.getMask();
            if ((value & mask) == mask) {
                errorActions.add(peav);
            }
        }
        return errorActions;
    }

}
