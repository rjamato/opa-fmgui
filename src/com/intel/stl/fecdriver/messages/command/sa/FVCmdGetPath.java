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

import com.intel.stl.api.subnet.PathRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.Constants;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.sa.PathRecord;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.command.InputArgument;
import com.intel.stl.fecdriver.messages.response.sa.FVRspGetPath;

/**
 * @author jijunwan
 * 
 */
public class FVCmdGetPath extends
        SACommand<PathRecordBean, PathRecord, PathRecordBean> {

    /**
     * @param command
     */
    public FVCmdGetPath() {
        setResponse(new FVRspGetPath());
        setInput(new InputArgument());
    }

    public FVCmdGetPath(InputArgument input) {
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
        comm.setBaseVersion(Constants.IB_BASE_VERSION);
        comm.setClassVersion(SAConstants.IB_SUBN_ADM_CLASS_VERSION);
        comm.setAttributeID(SAConstants.STL_SA_ATTR_PATH_RECORD);
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
        // case InputTypeNoInput:
        // mask = SAConstants.IB_PATH_RECORD_COMP_SGID |
        // SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE |
        // SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
        // header.setComponentMask(mask);
        // record.setReversible(true);
        // record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
        // break;
            case InputTypePKey:
                mask =
                        SAConstants.IB_PATH_RECORD_COMP_SGID
                                | SAConstants.IB_PATH_RECORD_COMP_PKEY
                                | SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE
                                | SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
                header.setComponentMask(mask);
                record.setReversible(true);
                record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
                record.setSGID(input.getSourceGid());
                record.setPKey(input.getPKey());
                break;
            case InputTypeSL:
                mask =
                        SAConstants.IB_PATH_RECORD_COMP_SGID
                                | SAConstants.IB_PATH_RECORD_COMP_SL
                                | SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE
                                | SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
                header.setComponentMask(mask);
                record.setReversible(true);
                record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
                record.setSGID(input.getSourceGid());
                record.setSL(input.getSL());
                break;
            case InputTypeServiceId:
                mask =
                        SAConstants.IB_PATH_RECORD_COMP_SGID
                                | SAConstants.IB_PATH_RECORD_COMP_SERVICEID
                                | SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE
                                | SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
                header.setComponentMask(mask);
                record.setReversible(true);
                record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
                record.setSGID(input.getSourceGid());
                record.setServiceId(input.getServiceId());
                break;
            // case InputTypePortGuidPair:
            // mask = SAConstants.IB_PATH_RECORD_COMP_SGID |
            // SAConstants.IB_PATH_RECORD_COMP_DGID |
            // SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE |
            // SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
            // header.setComponentMask(mask);
            // record.setReversible(true);
            // record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
            // GID.Global sgid = new GID.Global(input.getSourcePortGuid());
            // record.setSGID(sgid);
            // GID.Global dgid = new GID.Global(input.getDestPortGuid());
            // record.setDGID(dgid);
            // break;
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
            // case InputTypePortGuid:
            // mask = SAConstants.IB_PATH_RECORD_COMP_SGID |
            // SAConstants.IB_PATH_RECORD_COMP_DGID |
            // SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE |
            // SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
            // header.setComponentMask(mask);
            // record.setReversible(true);
            // record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
            // dgid = new GID.Global(input.getPortGuid());
            // record.setDGID(dgid);
            // break;
            case InputTypePortGid:
                mask =
                        SAConstants.IB_PATH_RECORD_COMP_SGID
                                | SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE
                                | SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
                header.setComponentMask(mask);
                record.setReversible(true);
                record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
                record.setSGID(input.getPortGid());
                break;
            case InputTypeLid:
                mask =
                        SAConstants.IB_PATH_RECORD_COMP_SGID
                                | SAConstants.IB_PATH_RECORD_COMP_DLID
                                | SAConstants.IB_PATH_RECORD_COMP_REVERSIBLE
                                | SAConstants.IB_PATH_RECORD_COMP_NUMBPATH;
                header.setComponentMask(mask);
                record.setReversible(true);
                record.setNumPath(SAConstants.PATHRECORD_NUMBPATH);
                record.setSGID(input.getSourceGid());
                record.setDLidLow((short) (input.getLid() & 0xffff));
                break;
            default:
                throw new IllegalArgumentException("Unsupported input type "
                        + input.getType());
        }
    }
}
