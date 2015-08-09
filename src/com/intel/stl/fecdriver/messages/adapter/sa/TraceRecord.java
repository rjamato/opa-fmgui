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
package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.TraceRecordBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 *  TraceRecord
 *  
 *  STL Differences
 *  
 *  	GIDPrefix deleted.
 *  	EntryPort, ExitPort moved for alignment.
 *  		Reserved2 added to word/qword-align NodeID.
 *  
 *  typedef struct {
 * [2] 	uint16		IDGeneration;
 * [3] 	uint8		Reserved;
 * [4] 	uint8		NodeType;
 * [5] 	uint8		EntryPort;
 * [6] 	uint8		ExitPort;
 * [8] 	uint16		Reserved2;
 *  	
 * [16] 	uint64		NodeID;
 * [24] 	uint64		ChassisID;
 * [32] 	uint64		EntryPortID;
 * [40] 	uint64		ExitPortID;
 *  	
 *  } PACK_SUFFIX STL_TRACE_RECORD;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class TraceRecord extends SimpleDatagram<TraceRecordBean> {
    private static final long STL_TRACE_RECORD_COMP_ENCRYPT_MASK = 0x55555555;

    public TraceRecord() {
        super(40);
    }

    public void setIDGeneration(short iDGeneration) {
        buffer.putShort(0, iDGeneration);
    }

    public void setNodeType(NodeType type) {
        buffer.put(3, (byte) type.ordinal());
    }

    public void setEntryPort(byte port) {
        buffer.put(4, port);
    }

    public void setExitPort(byte port) {
        buffer.put(5, port);
    }

    public void setNodeId(long id) {
        buffer.putLong(8, id);
    }

    public void setChassisId(long id) {
        buffer.putLong(16, id);
    }

    public void setEntryPortId(long id) {
        buffer.putLong(24, id);
    }

    public void setExitPortId(long id) {
        buffer.putLong(32, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public TraceRecordBean toObject() {
        buffer.clear();
        short idGeneration = buffer.getShort();
        buffer.position(3);
        byte nodeType = buffer.get();
        byte entryPort = buffer.get();
        byte exitPort = buffer.get();
        buffer.position(8);
        long nodeId =
                Long.reverseBytes(buffer.getLong())
                        ^ STL_TRACE_RECORD_COMP_ENCRYPT_MASK;
        long chassisId = buffer.getLong() ^ STL_TRACE_RECORD_COMP_ENCRYPT_MASK;
        long entryPortId =
                buffer.getLong() ^ STL_TRACE_RECORD_COMP_ENCRYPT_MASK;
        long exitPortId = buffer.getLong() ^ STL_TRACE_RECORD_COMP_ENCRYPT_MASK;

        TraceRecordBean bean =
                new TraceRecordBean(idGeneration, nodeType, entryPort,
                        exitPort, nodeId, chassisId, entryPortId, exitPortId);
        return bean;
    }

}
