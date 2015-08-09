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
package com.intel.stl.fecdriver.messages.adapter;

import com.intel.stl.api.IdGenerator;
import com.intel.stl.common.Constants;

/**
 * ref: /ALL_EMB/IbAccess/Common/Inc/ib_mad.h
 * 
 * <pre>
 * typedef struct _MAD_COMMON {
 * 	uint8	BaseVersion;			// Version of management datagram base  
 * 									// format.Value is set to 1 
 * 	uint8	MgmtClass;				// Class of operation.  
 * 	uint8	ClassVersion;			// Version of MAD class-specific format. 
 * 									// Value is set to 1 except  
 * 									// for the Vendor class 
 * 	union {
 * 		uint8	AsReg8;
 * 		struct { IB_BITFIELD2(uint8,
 * 			R		:1,		// Request/response field,  
 * 							// conformant to base class definition 
 * 			Method	:7)		// Method to perform based on  
 * 							// the management class 
 * 		} PACK_SUFFIX s;
 * 	} mr;
 * 
 * 	union {
 * 		// All MADs use this structure 
 * 		struct {
 * 			MAD_STATUS	Status;		// Code indicating status of method 
 * 			uint16		Reserved1;	// Reserved. Shall be set to 0 
 * 		} NS;						// Normal MAD 
 * 
 * 		// This structure is used only for Directed Route SMP's 
 * 		struct {
 * 			struct { IB_BITFIELD2(uint16,
 * 				D		:1,	// Direction bit to determine  
 * 							// direction of packet 
 * 				Status	:15)/* Code indicating status of method 
 * 			} PACK_SUFFIX s;						
 * 			uint8	HopPointer;		// used to indicate the current byte  
 * 									// of the Initial/Return Path field. 
 * 			uint8	HopCount;		// used to contain the number of valid  
 * 									// bytes in the Initial/Return Path 
 * 		} DR;				// Directed Route only 
 * 	} u;
 * 	uint64	TransactionID;			// Transaction specific identifier 
 * 	uint16	AttributeID;			// Defines objects being operated  
 * 									// on by a management class 
 * 	uint16	Reserved2;				// Reserved. Shall be set to 0 
 * 	uint32	AttributeModifier;		// Provides further scope to  
 * 									// the Attributes, usage  
 * 									// determined by the management class 
 * } PACK_SUFFIX MAD_COMMON;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class CommonMad extends SimpleDatagram<Void> {
    public CommonMad() {
        super(24);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.resource.stl.data.SimpleDatagram#initData()
     */
    @Override
    protected void initData() {
        super.initData();
        setTransactionID(IdGenerator.id());
        setNSStatus(Constants.MAD_STATUS_SUCCESS);
    }

    public void setBaseVersion(byte version) {
        buffer.put(0, version);
    }

    public void setMgmtClass(byte mgmtClass) {
        buffer.put(1, mgmtClass);
    }

    public void setClassVersion(byte version) {
        buffer.put(2, version);
    }

    public void setMethod(byte mr) {
        buffer.put(3, mr);
    }

    // for normal mad
    public void setNSStatus(short status) {
        buffer.putShort(4, status);
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    public short getNSStatus() {
        return buffer.getShort(4);
    }

    // for directed route
    public void setDRStatus(short status, byte hopPointer, byte hopCount) {
        buffer.putShort(4, status);
        buffer.put(6, hopPointer);
        buffer.put(7, hopCount);
    }

    public void setTransactionID(long id) {
        buffer.putLong(8, id);
    }

    public long getTransactionId() {
        return buffer.getLong(8);
    }

    public void setAttributeID(short id) {
        buffer.putShort(16, id);
    }

    public short getAttributeID() {
        return buffer.getShort(16);
    }

    public void setAttributeModifer(int modifer) {
        buffer.putInt(20, modifer);
    }

}
