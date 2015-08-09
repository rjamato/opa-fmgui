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
 *  File Name: PerfErrorsItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/04/14 21:12:10  rjtierne
 *  Archive Log:    PR 128036 - SendFECN is tabulated as a neighbor error, refine recvFECN tabulation.
 *  Archive Log:    Added new class attribute fromNeighbor to indicate whether the associated performance
 *  Archive Log:    error item originates at a neighboring port
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/10 18:20:53  jypak
 *  Archive Log:    Fall back to previous way of displaying received/transmitted data in performance page(chart section, table section, counter (error) section).
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/07 14:38:28  jypak
 *  Archive Log:    PR 126998 - Received/Transmitted data counters for Device Node and Device ports should show in MB rather than Flits. Fixed by converting units to Byte/KB/MB/GB. Also, tool tips were added to show the units for each value.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/25 13:57:42  jypak
 *  Archive Log:    Correct comment header
 *  Archive Log:
 *  
 *  Overview: Performance Errors Item
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

public class PerfErrorsItem {

    private String keyStr;

    private String valStr;

    private boolean isTtl = false;

    private boolean fromNeighbor = false;

    public PerfErrorsItem(String inK, String inV, boolean fromNeighbor) {
        keyStr = inK;
        valStr = inV;
        this.fromNeighbor = fromNeighbor;
    }

    public PerfErrorsItem(String inK, long inV, boolean flag) {
        keyStr = inK;
        valStr = String.valueOf(inV);
        isTtl = flag;
    }

    /**
     * @return the keyStr
     */
    public String getKeyStr() {
        return keyStr;
    }

    /**
     * @return the valStr
     */
    public String getValStr() {
        return valStr;
    }

    /**
     * @return the isTtl
     */
    public boolean isTtl() {
        return isTtl;
    }

    /**
     * @param keyStr
     *            the keyStr to set
     */
    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    /**
     * @param valStr
     *            the valStr to set
     */
    public void setValStr(String valStr) {
        this.valStr = valStr;
    }

    /**
     * @param isTtl
     *            the isTtl to set
     */
    public void setTtl(boolean isTtl) {
        this.isTtl = isTtl;
    }

    /**
     * @return fromNeighbor
     */
    public boolean isFromNeighbor() {
        return fromNeighbor;
    }

    /**
     * @param fromNeighbor
     *            the fromNeighbor to set
     */
    public void setFromNeighbor(boolean fromNeighbor) {
        this.fromNeighbor = fromNeighbor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ViewItem [keyStr=" + keyStr + ", valStr=" + valStr + ", isTtl="
                + isTtl + ", fromNeighbor=" + fromNeighbor + "]";
    }
}
