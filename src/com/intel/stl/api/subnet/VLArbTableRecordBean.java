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
 *  File Name: VLArbTableRecordBean.java
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
 *  Overview: VLArb Table Record from SA populated by the connect manager.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

/*
 * VLArbitrationRecord
 * 
 * STL Differences:
 *		Switch LID extended.
 *		Blocknum now defined as 0 - 3 as per the VL Arbitration Table MAD.
 *		Length of Low, High tables extended to 128 bytes.
 *		Preempt table added.
 */
//typedef struct {
//	struct {
//		uint32	LID;				
//		uint8	OutputPortNum;	
//		uint8	BlockNum;
//	} PACK_SUFFIX RID;
//
//	uint16		Reserved;
//	
//	STL_VLARB_TABLE VLArbTable;
//	
//} PACK_SUFFIX STL_VLARBTABLE_RECORD;
//
//
//#define VLARB_TABLE_LENGTH 128
//typedef union {
//	STL_VLARB_TABLE_ELEMENT  Elements[VLARB_TABLE_LENGTH]; /* RW */
//	uint32                   Matrix[STL_MAX_VLS];	/* RW */
//													/* POD: 0 */
//
//} PACK_SUFFIX STL_VLARB_TABLE;

//typedef struct {
//struct { IB_BITFIELD2( uint8,
//	Reserved:		3,
//	VL:				5 )		/* RW */
//} s;
//
//uint8   Weight;				/* RW */
//
//} PACK_SUFFIX STL_VLARB_TABLE_ELEMENT;

import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.Utils;

public class VLArbTableRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lid;

    private short outputPortNum; // promote to handle unsigned byte

    private short blockNum; // promote to handle unsigned byte

    private VLArbTableBean[] vlArbTableElement;

    private int[] matrix;

    public VLArbTableRecordBean() {
        super();
    }

    public VLArbTableRecordBean(int lid, byte outputPortNum, byte blockNum) {
        super();
        this.lid = lid;
        this.outputPortNum = Utils.unsignedByte(outputPortNum);
        this.blockNum = Utils.unsignedByte(blockNum);
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
     * @return the outputPortNum
     */
    public short getOutputPortNum() {
        return outputPortNum;
    }

    /**
     * @param outputPortNum
     *            the outputPortNum to set
     */
    public void setOutputPortNum(short outputPortNum) {
        this.outputPortNum = outputPortNum;
    }

    /**
     * @param outputPortNum
     *            the outputPortNum to set
     */
    public void setOutputPortNum(byte outputPortNum) {
        this.outputPortNum = Utils.unsignedByte(outputPortNum);
    }

    /**
     * @return the blockNum
     */
    public short getBlockNum() {
        return blockNum;
    }

    /**
     * @param blockNum
     *            the blockNum to set
     */
    public void setBlockNum(short blockNum) {
        this.blockNum = blockNum;
    }

    /**
     * @param blockNum
     *            the blockNum to set
     */
    public void setBlockNum(byte blockNum) {
        this.blockNum = Utils.unsignedByte(blockNum);
    }

    /**
     * @return the vlArbTableElement
     */
    public VLArbTableBean[] getVlArbTableElement() {
        return vlArbTableElement;
    }

    /**
     * @param vlArbTableElement
     *            the vlArbTableElement to set
     */
    public void setVlArbTableElement(VLArbTableBean[] vlArbTableElement) {
        this.vlArbTableElement = vlArbTableElement;
    }

    /**
     * @return the matrix
     */
    public int[] getMatrix() {
        return matrix;
    }

    /**
     * @param matrix
     *            the matrix to set
     */
    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VLArbTableRecordBean [lid=" + lid + ", outputPortNum="
                + outputPortNum + ", blockNum=" + blockNum
                + ", vlArbTableElement=" + Arrays.toString(vlArbTableElement)
                + ", matrix=" + Arrays.toString(matrix) + "]";
    }

}