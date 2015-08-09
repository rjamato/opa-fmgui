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
package com.intel.stl.fecdriver.messages.command.pa;

import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.pa.PortCounters;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.command.InputArgument;
import com.intel.stl.fecdriver.messages.response.pa.FVRspGetPortCounters;

/**
 * @author jijunwan
 * 
 */
public class FVCmdGetPortCounters extends
        PACommand<PortCountersBean, PortCounters, PortCountersBean> {
    public FVCmdGetPortCounters() {
        setResponse(new FVRspGetPortCounters());
    }

    public FVCmdGetPortCounters(InputArgument input) {
        this();
        setInput(input);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vieo.fv.message.command.sa.SACommand#fillCommonMad(com.vieo.fv.resource
     * .stl.data.CommonMad)
     */
    @Override
    protected void fillCommonMad(CommonMad comm) {
        super.fillCommonMad(comm);
        comm.setMethod(PAConstants.STL_PA_CMD_GET);
        comm.setAttributeID(PAConstants.STL_PA_ATTRID_GET_PORT_CTRS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.message.command.sa.SACommand#buildRecord()
     */
    @Override
    protected PortCounters buildRecord() {
        PortCounters res = new PortCounters();
        res.build(true);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vieo.fv.message.command.sa.SACommand#fillInput(com.vieo.fv.resource
     * .stl.data.sa.SAHeader, com.vieo.fv.resource.stl.data.IDatagram)
     */
    @Override
    protected void fillInput(SAHeader header, PortCounters record) {
        InputArgument input = getInput();
        if (input == null) {
            throw new IllegalArgumentException("No input argument");
        }

        switch (input.getType()) {
            case InputTypeLidPortNumber:
                record.setNodeLid(input.getLid());
                record.setPortNumber(input.getPortNumber());
                record.setImageNumber(input.getImageId().getImageNumber());
                record.setImageOffset(input.getImageId().getImageOffset());
                record.setFlags(input.isDelta() ? 1 : 0);
                break;
            default:
                throw new IllegalArgumentException("Unsupported input type "
                        + input.getType());
        }
    }

}
