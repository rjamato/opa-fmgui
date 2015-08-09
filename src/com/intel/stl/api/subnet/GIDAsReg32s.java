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

/**
 * The following static classes refer to the different usages of the GID, as
 * stated in the IB specification (section 4.1.1) and the corresponding header
 * file in the FM implementation. They are not used in the GUI application; they
 * are included here for completeness
 * 
 * /ALL_EMB/IbAcess/Common/Inc/ib_type.h
 * 
 * @author jijunwan
 * 
 */

public class GIDAsReg32s extends GIDBean {
    private static final long serialVersionUID = 1891408975809955819L;

    private int hh, hl, lh, ll;

    public GIDAsReg32s() {
        super();
    }

    public GIDAsReg32s(int hh, int hl, int lh, int ll) {
        super();
        this.hh = hh;
        this.hl = hl;
        this.lh = lh;
        this.ll = ll;
    }

    /**
     * @return the hh
     */
    public int getHh() {
        return hh;
    }

    /**
     * @param hh
     *            the hh to set
     */
    public void setHh(int hh) {
        this.hh = hh;
    }

    /**
     * @return the hl
     */
    public int getHl() {
        return hl;
    }

    /**
     * @param hl
     *            the hl to set
     */
    public void setHl(int hl) {
        this.hl = hl;
    }

    /**
     * @return the lh
     */
    public int getLh() {
        return lh;
    }

    /**
     * @param lh
     *            the lh to set
     */
    public void setLh(int lh) {
        this.lh = lh;
    }

    /**
     * @return the ll
     */
    public int getLl() {
        return ll;
    }

    /**
     * @param ll
     *            the ll to set
     */
    public void setLl(int ll) {
        this.ll = ll;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AsReg32s [hh=" + hh + ", hl=" + hl + ", lh=" + lh + ", ll="
                + ll + "]";
    }
}
