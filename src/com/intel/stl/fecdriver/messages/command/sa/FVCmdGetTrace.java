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
import com.intel.stl.api.subnet.TraceRecordBean;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.sa.PathRecord;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.command.InputArgument;
import com.intel.stl.fecdriver.messages.response.sa.FVRspGetTrace;

/**
 * @author jijunwan
 * 
 */
public class FVCmdGetTrace extends
        SACommand<TraceRecordBean, PathRecord, TraceRecordBean> {

    /**
     * @param command
     */
    public FVCmdGetTrace() {
        setResponse(new FVRspGetTrace());
        setInput(new InputArgument());
    }

    public FVCmdGetTrace(InputArgument input) {
        this();
        setInput(input);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.message.command.sa.SACommand#fillCommonMad(com.intel
     * .hpc.stl.resourceadapter.data.CommonMad)
     */
    @Override
    protected void fillCommonMad(CommonMad comm) {
        super.fillCommonMad(comm);
        comm.setAttributeID(SAConstants.STL_SA_ATTR_TRACE_RECORD);
        comm.setMethod(SAConstants.SUBN_ADM_GETTRACETABLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.message.command.sa.SACommand#buildRecord()
     */
    @Override
    protected PathRecord buildRecord() {
        PathRecord rec = new PathRecord();
        rec.build(true);
        return rec;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.message.command.sa.SACommand#fillInput(com.intel.hpc
     * .stl.resourceadapter.data.sa.SAHeader,
     * com.intel.hpc.stl.resourceadapter.data.IDatagram)
     */
    @Override
    protected void fillInput(SAHeader header, PathRecord record) {
        InputArgument input = getInput();

        long mask = 0;
        switch (input.getType()) {
            case InputTypeGidPair:
                mask =
                        SAConstants.IB_PATH_RECORD_COMP_SGID
                                | SAConstants.IB_PATH_RECORD_COMP_DGID
                                | SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE
                                | SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
                header.setComponentMask(mask);
                record.setReversible(true);
                record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
                record.setSGID(input.getSourceGid());
                record.setDGID(input.getDestGid());
                break;
            default:
                throw new IllegalArgumentException("Unsupported input type "
                        + input.getType());
        }
    }

}
