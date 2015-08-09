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
 *  File Name: TopologyTier.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.util.Arrays;

import com.intel.stl.ui.common.UILabels;

public class TopologyTier {
    private final String name;

    private final int index;

    private int numSwitches;

    private int numHFIs;

    private int numOtherPorts;

    private Quality upQuality;

    private Quality downQuality;

    /**
     * Description:
     * 
     * @param index
     */
    public TopologyTier(int index) {
        super();
        this.index = index;
        name = UILabels.STL10212_TIRE_N.getDescription(index);
    }

    /**
     * @return the nodes
     */
    public int getNumSwitches() {
        return numSwitches;
    }

    /**
     * @param nodes
     *            the nodes to set
     */
    public void setNumSwitches(int nodes) {
        this.numSwitches = nodes;
    }

    /**
     * @return the numHFIs
     */
    public int getNumHFIs() {
        return numHFIs;
    }

    /**
     * @param numHFIs
     *            the numHFIs to set
     */
    public void setNumHFIs(int numHFIs) {
        this.numHFIs = numHFIs;
    }

    /**
     * @return the otherPorts
     */
    public int getNumOtherPorts() {
        return numOtherPorts;
    }

    /**
     * @param otherPorts
     *            the otherPorts to set
     */
    public void setNumOtherPorts(int otherPorts) {
        this.numOtherPorts = otherPorts;
    }

    public int getTotalPorts() {
        return upQuality.getTotalPorts() + downQuality.getTotalPorts()
                + numOtherPorts;
    }

    public int getTotalActivePorts() {
        return upQuality.getTotalPorts() + downQuality.getTotalPorts();
    }

    /**
     * @return the upQuality
     */
    public Quality getUpQuality() {
        return upQuality;
    }

    /**
     * @param upQuality
     *            the upQuality to set
     */
    public void setUpQuality(Quality upQuality) {
        this.upQuality = upQuality;
    }

    /**
     * @return the downQuality
     */
    public Quality getDownQuality() {
        return downQuality;
    }

    /**
     * @param downQuality
     *            the downQuality to set
     */
    public void setDownQuality(Quality downQuality) {
        this.downQuality = downQuality;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TopologyTier [name=" + name + ", index=" + index + ", nodes="
                + numSwitches + ", upQuality=" + upQuality + ", downQuality="
                + downQuality + ", otherPorts=" + numOtherPorts + "]";
    }

    public static class Quality {
        private int totalPorts;

        private int slowPorts;

        private int degPorts;

        private int[] qualities;

        /**
         * @return the totalPorts
         */
        public int getTotalPorts() {
            return totalPorts;
        }

        /**
         * @param totalPorts
         *            the totalPorts to set
         */
        public void setTotalPorts(int totalPorts) {
            this.totalPorts = totalPorts;
        }

        public void increaseTotalPorts(int num) {
            totalPorts += num;
        }

        /**
         * @return the slowPorts
         */
        public int getSlowPorts() {
            return slowPorts;
        }

        /**
         * @param slowPorts
         *            the slowPorts to set
         */
        public void setSlowPorts(int slowPorts) {
            this.slowPorts = slowPorts;
        }

        public void increaseSlowPorts(int num) {
            slowPorts += num;
        }

        /**
         * @return the degPorts
         */
        public int getDegPorts() {
            return degPorts;
        }

        /**
         * @param degPorts
         *            the degPorts to set
         */
        public void setDegPorts(int degPorts) {
            this.degPorts = degPorts;
        }

        public void increaseDegPorts(int num) {
            degPorts += num;
        }

        /**
         * @return the qualities
         */
        public int[] getQualities() {
            return qualities;
        }

        /**
         * @param qualities
         *            the qualities to set
         */
        public void setQualities(int[] qualities) {
            this.qualities = qualities;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Quality [totalPorts=" + totalPorts + ", slowPorts="
                    + slowPorts + ", degPorts=" + degPorts + ", qualities="
                    + Arrays.toString(qualities) + "]";
        }

    }
}
