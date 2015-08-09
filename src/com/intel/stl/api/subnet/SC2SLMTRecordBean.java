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
package com.intel.stl.api.subnet;

/* SC2SLMappingTableRecord
 * 
 * STL Differences:
 *      New for STL.
 * 
 * typedef struct {
 *     struct {
 *         uint32  LID;    
 *         uint16  Reserved;               
 *     } PACK_SUFFIX RID;
 *    
 *     uint16      Reserved2;
 *    
 *     STL_SL      SCSLMap[STL_MAX_SLS];   
 *    
 * } PACK_SUFFIX STL_SC2SL_MAPPING_TABLE_RECORD;
 * 
 */

/**
 * Title:        SC2SLMTRecordBean
 * Description:  SC2SL Mapping Record from SA populated by the connect manager.
 * 
 * @author jypak
 * @version 0.0
 */
import java.io.Serializable;
import java.util.Arrays;

public class SC2SLMTRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lid;

    private byte[] data;

    // How about number of blocks? List<LFTRecordBean> size will be it.

    public SC2SLMTRecordBean() {
        super();
    }

    public SC2SLMTRecordBean(int lid, byte[] data) {
        super();
        this.lid = lid;
        this.data = data;
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
        return "SC2SLMTRecordBean [lid=" + lid + ", data="
                + Arrays.toString(data) + "]";
    }

}