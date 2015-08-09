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
 *  File Name: P_KeyTableRecordBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/04 21:37:55  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: P_Key Table Record from SA populated by the connect manager.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

//typedef struct {
//	struct {
//		uint32	LID;
//		uint16	Blocknum;
//		uint8	PortNum;
//	} PACK_SUFFIX RID;
//	
//	uint8		Reserved;	 
//	
//	STL_PARTITION_TABLE	PKeyTblData;
//	
//} PACK_SUFFIX STL_P_KEY_TABLE_RECORD;
//
//
//typedef struct {
//
//	STL_PKEY_ELEMENT PartitionTableBlock[NUM_PKEY_ELEMENTS_BLOCK];	/* RW List of P_Key Block elements */
//
//} PACK_SUFFIX STL_PARTITION_TABLE;

//typedef union {
//uint16  AsReg16;
//struct { IB_BITFIELD2( uint16,
//	MembershipType:		1,				/* 0=Limited, 1=Full */
//	P_KeyBase:			15 )			/* Base value of the P_Key that */
//										/*  the endnode will use to check */
//										/*  against incoming packets */
//} s;
//
//} PACK_SUFFIX STL_PKEY_ELEMENT;
//

import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.Utils;

public class P_KeyTableRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lid;

    private int blockNum; // promote to handle unsigned short

    private short portNum; // promote to handle unsigned byte

    private P_KeyTableBean[] pKeyTableData;

    public P_KeyTableRecordBean() {
        super();
    }

    public P_KeyTableRecordBean(int lid, short blockNum, byte portNum,
            P_KeyTableBean[] pKeyTableData) {
        super();
        this.lid = lid;
        this.blockNum = Utils.unsignedShort(blockNum);
        this.portNum = Utils.unsignedByte(portNum);
        this.pKeyTableData = pKeyTableData;
    }

    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /**
     * @param lid
     *            the lid to set
     */
    public void setLid(int lid) {
        this.lid = lid;
    }

    /**
     * @return the blockNum
     */
    public int getBlockNum() {
        return blockNum;
    }

    /**
     * @param blockNum
     *            the blockNum to set
     */
    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    /**
     * @param blockNum
     *            the blockNum to set
     */
    public void setBlockNum(short blockNum) {
        this.blockNum = Utils.unsignedShort(blockNum);
    }

    /**
     * @return the portNum
     */
    public short getPortNum() {
        return portNum;
    }

    /**
     * @param portNum
     *            the portNum to set
     */
    public void setPortNum(short portNum) {
        this.portNum = portNum;
    }

    /**
     * @param portNum
     *            the portNum to set
     */
    public void setPortNum(byte portNum) {
        this.portNum = Utils.unsignedByte(portNum);
    }

    /**
     * @return the pKeyTableData
     */
    public P_KeyTableBean[] getpKeyTableData() {
        return pKeyTableData;
    }

    /**
     * @param pKeyTableData
     *            the pKeyTableData to set
     */
    public void setpKeyTableData(P_KeyTableBean[] pKeyTableData) {
        this.pKeyTableData = pKeyTableData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "P_KeyTableRecordBean [lid=" + lid + ", blockNum=" + blockNum
                + ", portNum=" + portNum + ", pKeyTableData="
                + Arrays.toString(pKeyTableData) + "]";
    }

}