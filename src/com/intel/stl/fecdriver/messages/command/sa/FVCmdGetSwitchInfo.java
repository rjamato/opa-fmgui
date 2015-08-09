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
package com.intel.stl.fecdriver.messages.command.sa;

import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.adapter.sa.SwitchInfoRecord;
import com.intel.stl.fecdriver.messages.command.InputArgument;
import com.intel.stl.fecdriver.messages.response.sa.FVRspGetSwitchInfo;

/**
 *
 */

public class FVCmdGetSwitchInfo extends
        SACommand<SwitchRecordBean, SwitchInfoRecord, SwitchRecordBean> {
    /**
     * FVCmdGetSwitchInfo Constructor. Creates and instance of the Command
     * GetSwitchInfo and set's its Fabric Response.
     */
    public FVCmdGetSwitchInfo() {
        setResponse(new FVRspGetSwitchInfo());
        setInput(new InputArgument());
    }

    public FVCmdGetSwitchInfo(InputArgument input) {
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
        comm.setAttributeID(SAConstants.STL_SA_ATTR_SWITCHINFO_RECORD);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.message.command.sa.SACommand#buildRecord()
     */
    @Override
    protected SwitchInfoRecord buildRecord() {
        SwitchInfoRecord record = new SwitchInfoRecord();
        record.build(true);
        return record;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vieo.fv.message.command.sa.SACommand#fillInput(com.vieo.fv.resource
     * .stl.data.sa.SAHeader, com.vieo.fv.resource.stl.data.IDatagram)
     */
    @Override
    protected void fillInput(SAHeader header, SwitchInfoRecord record) {
        InputArgument input = getInput();

        switch (input.getType()) {
            case InputTypeLid:
                header.setComponentMask(SAConstants.IB_LINK_RECORD_COMP_FROMLID);
                record.setLid(input.getLid());
                break;
            case InputTypeNoInput:
                header.setComponentMask(0);
                break;
            default:
                throw new IllegalArgumentException("Unsupported input type "
                        + input.getType());
        }
    }

}
