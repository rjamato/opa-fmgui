/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.8  2015/08/17 18:54:02  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
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
