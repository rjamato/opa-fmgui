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
 *  File Name: FVCmdGetPMConfig.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/09/12 20:02:36  fernande
 *  Archive Log:    New command to get PM Configuration
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.command.pa;

import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.pa.PMConfig;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.response.pa.FVRspGetPMConfig;

public class FVCmdGetPMConfig extends
        PACommand<PMConfigBean, PMConfig, PMConfigBean> {

    public FVCmdGetPMConfig() {
        setResponse(new FVRspGetPMConfig());
    }

    @Override
    protected void fillCommonMad(CommonMad comm) {
        super.fillCommonMad(comm);
        comm.setMethod(PAConstants.STL_PA_CMD_GET);
        comm.setAttributeID(PAConstants.STL_PA_ATTRID_GET_PM_CONFIG);
    }

    @Override
    protected PMConfig buildRecord() {
        PMConfig pmConfig = new PMConfig();
        pmConfig.build(true);
        return pmConfig;
    }

    @Override
    protected void fillInput(SAHeader header, PMConfig record) {
    }

}
