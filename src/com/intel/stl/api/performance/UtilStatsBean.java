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
 *  File Name: UtilStatsBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/04 21:37:53  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance;

import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class UtilStatsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalMBps; // MB per sec

    private long totalKPps; // K pkts per sec

    private long avgMBps; // unsigned int

    private long minMBps; // unsigned int

    private long maxMBps; // unsigned int

    private int numBWBuckets; // this should be fine, no need to promote to long

    // java.sql.Blob, Byte[], byte[] and serializable type (Integer) will be
    // persisted in a Blob.
    private Integer[] bwBuckets; // this should be fine, no need to promote to
                                 // long

    private long avgKPps; // unsigned int

    private long minKPps; // unsigned int

    private long maxKPps; // unsigned int

    /**
     * @return the totalMBps
     */
    public long getTotalMBps() {
        return totalMBps;
    }

    /**
     * @param totalMBps
     *            the totalMBps to set
     */
    public void setTotalMBps(long totalMBps) {
        this.totalMBps = totalMBps;
    }

    /**
     * @return the totalKPps
     */
    public long getTotalKPps() {
        return totalKPps;
    }

    /**
     * @param totalKPps
     *            the totalKPps to set
     */
    public void setTotalKPps(long totalKPps) {
        this.totalKPps = totalKPps;
    }

    /**
     * @return the avgMBps
     */
    public long getAvgMBps() {
        return avgMBps;
    }

    /**
     * @param avgMBps
     *            the avgMBps to set
     */
    public void setAvgMBps(long avgMBps) {
        this.avgMBps = avgMBps;
    }

    /**
     * @param avgMBps
     *            the avgMBps to set
     */
    public void setAvgMBps(int avgMBps) {
        this.avgMBps = Utils.unsignedInt(avgMBps);
    }

    /**
     * @return the minMBps
     */
    public long getMinMBps() {
        return minMBps;
    }

    /**
     * @param minMBps
     *            the minMBps to set
     */
    public void setMinMBps(long minMBps) {
        this.minMBps = minMBps;
    }

    /**
     * @param minMBps
     *            the minMBps to set
     */
    public void setMinMBps(int minMBps) {
        this.minMBps = Utils.unsignedInt(minMBps);
    }

    /**
     * @return the maxMBps
     */
    public long getMaxMBps() {
        return maxMBps;
    }

    /**
     * @param maxMBps
     *            the maxMBps to set
     */
    public void setMaxMBps(long maxMBps) {
        this.maxMBps = maxMBps;
    }

    /**
     * @param maxMBps
     *            the maxMBps to set
     */
    public void setMaxMBps(int maxMBps) {
        this.maxMBps = Utils.unsignedInt(maxMBps);
    }

    /**
     * @return the numBWBuckets
     */
    public int getNumBWBuckets() {
        return numBWBuckets;
    }

    /**
     * @param numBWBuckets
     *            the numBWBuckets to set
     */
    public void setNumBWBuckets(int numBWBuckets) {
        this.numBWBuckets = numBWBuckets;
    }

    /**
     * @return the bwBuckets
     */
    public Integer[] getBwBuckets() {
        return bwBuckets;
    }

    /**
     * @param bwBuckets
     *            the bwBuckets to set
     */
    public void setBwBuckets(Integer[] bwBuckets) {
        if (bwBuckets.length != PAConstants.STL_PM_UTIL_BUCKETS) {
            throw new IllegalArgumentException("Invalid data length. Expect "
                    + PAConstants.STL_PM_UTIL_BUCKETS + ", got "
                    + bwBuckets.length);
        }

        this.bwBuckets = bwBuckets;
    }

    /**
     * @return the avgKPps
     */
    public long getAvgKPps() {
        return avgKPps;
    }

    /**
     * @param avgKPps
     *            the avgKPps to set
     */
    public void setAvgKPps(long avgKPps) {
        this.avgKPps = avgKPps;
    }

    /**
     * @param avgKPps
     *            the avgKPps to set
     */
    public void setAvgKPps(int avgKPps) {
        this.avgKPps = Utils.unsignedInt(avgKPps);
    }

    /**
     * @return the minKPps
     */
    public long getMinKPps() {
        return minKPps;
    }

    /**
     * @param minKPps
     *            the minKPps to set
     */
    public void setMinKPps(long minKPps) {
        this.minKPps = minKPps;
    }

    /**
     * @param minKPps
     *            the minKPps to set
     */
    public void setMinKPps(int minKPps) {
        this.minKPps = Utils.unsignedInt(minKPps);
    }

    /**
     * @return the maxKPps
     */
    public long getMaxKPps() {
        return maxKPps;
    }

    /**
     * @param maxKPps
     *            the maxKPps to set
     */
    public void setMaxKPps(long maxKPps) {
        this.maxKPps = maxKPps;
    }

    /**
     * @param maxKPps
     *            the maxKPps to set
     */
    public void setMaxKPps(int maxKPps) {
        this.maxKPps = Utils.unsignedInt(maxKPps);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UtilStatsBean [totalMBps=" + totalMBps + ", totalKPps="
                + totalKPps + ", avgMBps=" + avgMBps + ", minMBps=" + minMBps
                + ", maxMBps=" + maxMBps + ", numBWBuckets=" + numBWBuckets
                + ", bwBuckets=" + Arrays.toString(bwBuckets) + ", avgKPps="
                + avgKPps + ", minKPps=" + minKPps + ", maxKPps=" + maxKPps
                + "]";
    }

}
