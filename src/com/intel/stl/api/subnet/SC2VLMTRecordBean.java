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
 *  File Name: SC2VLMTRecordBean.java
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
 *  Overview: SC2VL Mapping Record from SA populated by the connect manager.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

/*
 * SC2VL Mapping Table Records
 *
 * There are three possible SC to VL mapping tables: NT, T and R. SC2VL_R 
 * will not be implemented in STL Gen 1. While they are all three separate 
 * SA MAD attributes, they all have identical structure.
 *
 * typedef struct {
 *     struct {
 *         uint32  LID;
 *         uint8   Port;
 *     } PACK_SUFFIX RID;
 *    
 *     uint8       Reserved[3];
 *    
 *     STL_VL      SCVLMap[STL_MAX_VLS];   
 *    
 * } PACK_SUFFIX STL_SC2VL_R_MAPPING_TABLE_RECORD;
 */

import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.Utils;

public class SC2VLMTRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lid;

    private short port; // promote to handle unsigned byte

    private byte[] data;

    // How about number of blocks? List<LFTRecordBean> size will be it.

    public SC2VLMTRecordBean() {
        super();
    }

    public SC2VLMTRecordBean(int lid, byte port, byte[] data) {
        super();
        setLid(lid);
        setPort(port);
        setData(data);
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
     * @return the port
     */
    public short getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(short port) {
        this.port = port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(byte port) {
        this.port = Utils.unsignedByte(port);
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SC2VLMTRecordBean [lid=" + lid + ", port=" + port + ", data="
                + Arrays.toString(data) + "]";
    }

}