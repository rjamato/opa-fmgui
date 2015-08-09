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
package com.intel.stl.api.performance;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author jijunwan
 * 
 */
public class ErrStatBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private ErrSummaryBean errorMaximums;

    private ErrBucketBean[] ports;

    public ErrStatBean() {
        super();
    }

    public ErrStatBean(ErrSummaryBean errorMaximums, ErrBucketBean[] ports) {
        super();
        this.errorMaximums = errorMaximums;
        this.ports = ports;
    }

    /**
     * @return the errorMaximums
     */
    public ErrSummaryBean getErrorMaximums() {
        return errorMaximums;
    }

    /**
     * @param errorMaximums
     *            the errorMaximums to set
     */
    public void setErrorMaximums(ErrSummaryBean errorMaximums) {
        this.errorMaximums = errorMaximums;
    }

    /**
     * @return the ports
     */
    public ErrBucketBean[] getPorts() {
        return ports;
    }

    /**
     * @param ports
     *            the ports to set
     */
    public void setPorts(ErrBucketBean[] ports) {
        if (ports.length != PAConstants.PM_ERR_BUCKETS)
            throw new IllegalArgumentException("Invalid data length. Expect "
                    + PAConstants.PM_ERR_BUCKETS + ", got " + ports.length);

        this.ports = ports;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ErrStatsBean [errorMaximums=" + errorMaximums + ", ports="
                + Arrays.toString(ports) + "]";
    }

}
