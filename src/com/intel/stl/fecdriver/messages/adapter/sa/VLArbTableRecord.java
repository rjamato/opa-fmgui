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

import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.api.subnet.VLArbTableBean;
import com.intel.stl.api.subnet.VLArbTableRecordBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92<br>
 * /ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * <pre>
 * VLArbitrationRecord
 * 
 * STL Differences:
 * 	Switch LID extended.
 * 	Blocknum now defined as 0 - 3 as per the VL Arbitration Table MAD.
 * 	Length of Low, High tables extended to 128 bytes.
 * 	Preempt table added.
 * 
 * typedef struct {
 * 	struct {
 * 		uint32	LID;				
 * 		uint8	OutputPortNum;	
 * 		uint8	BlockNum;
 * 	} PACK_SUFFIX RID;
 * 
 * 	uint16		Reserved;
 * 	
 * 	STL_VLARB_TABLE VLArbTable;
 * 	
 * } PACK_SUFFIX STL_VLARBTABLE_RECORD;
 * 
 * typedef struct {
 * 	struct { IB_BITFIELD2( uint8,
 * 		Reserved:		3,
 * 		VL:				5 )		// RW 
 * 	} s;
 * 
 * 	uint8   Weight;				// RW 
 * 
 * } PACK_SUFFIX STL_VLARB_TABLE_ELEMENT;
 * 
 * #define VLARB_TABLE_LENGTH 128
 * typedef union {
 * 	STL_VLARB_TABLE_ELEMENT  Elements[VLARB_TABLE_LENGTH]; // RW 
 * 	uint32                   Matrix[STL_MAX_VLS];	// RW 
 * 													// POD: 0 
 * 
 * } PACK_SUFFIX STL_VLARB_TABLE;
 * 
 * #define STL_MAX_VLS			32
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class VLArbTableRecord extends SimpleDatagram<VLArbTableRecordBean> {

    public VLArbTableRecord() {
        super(264);
    }

    public void setLID(int lid) {
        buffer.putInt(0, lid);
    }

    public void setOutputPortNum(byte num) {
        buffer.put(4, num);
    }

    public void setBlockNum(byte num) {
        buffer.put(5, num);
    }

    public void setElements(byte[] vls, byte[] weights) {
        if (vls.length != SAConstants.VLARB_TABLE_LENGTH) {
            throw new IllegalArgumentException("Invalid VLs length. Expected "
                    + SAConstants.VLARB_TABLE_LENGTH + ", got " + vls.length);
        }
        if (weights.length != SAConstants.VLARB_TABLE_LENGTH) {
            throw new IllegalArgumentException(
                    "Invalid Weights length. Expected "
                            + SAConstants.VLARB_TABLE_LENGTH + ", got "
                            + weights.length);
        }

        buffer.position(8);
        for (int i = 0; i < SAConstants.VLARB_TABLE_LENGTH; i++) {
            buffer.put((byte) (vls[i] & 0x1f));
            buffer.put(weights[i]);
        }
    }

    public void setMatrix(int[] matrix) {
        if (matrix.length != SAConstants.STL_MAX_VLS) {
            throw new IllegalArgumentException(
                    "Invalid matrix length. Expected "
                            + SAConstants.STL_MAX_VLS + ", got "
                            + matrix.length);
        }

        buffer.position(8);
        for (int i = 0; i < SAConstants.STL_MAX_VLS; i++) {
            buffer.putInt(matrix[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public VLArbTableRecordBean toObject() {
        buffer.clear();
        VLArbTableRecordBean bean =
                new VLArbTableRecordBean(buffer.getInt(), buffer.get(),
                        buffer.get());

        // TODO: when to interpret it as Matrix?
        buffer.position(8);
        VLArbTableBean[] data =
                new VLArbTableBean[SAConstants.VLARB_TABLE_LENGTH];
        for (int i = 0; i < SAConstants.VLARB_TABLE_LENGTH; i++) {
            data[i] =
                    new VLArbTableBean((byte) (buffer.get() & 0x1f),
                            buffer.get());
        }
        bean.setVlArbTableElement(data);

        buffer.position(8);
        int[] matrix = new int[SAConstants.STL_MAX_VLS];
        for (int i = 0; i < SAConstants.STL_MAX_VLS; i++) {
            matrix[i] = buffer.getInt();
        }
        bean.setMatrix(matrix);

        return bean;
    }

}
