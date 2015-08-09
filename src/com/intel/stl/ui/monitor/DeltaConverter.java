/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: DeltaConverter.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.2  2015/05/17 18:30:42  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/05/14 17:43:07  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import java.util.Date;

public class DeltaConverter {
    private boolean debug;

    private final String name;

    private Date latestTimestamp = null;

    private long latestCummValue;

    private Date earliestTimestamp = null;

    private long earliestCummValue;

    private final Delta delta = new Delta();

    /**
     * Description:
     * 
     * @param name
     */
    public DeltaConverter(String name) {
        super();
        this.name = name;
        // debug = name.equals("rxData");
    }

    public synchronized Delta addValue(long cummValue, Date timestamp) {
        if (debug) {
            System.out.println("[" + name + "] " + timestamp.getTime() + " "
                    + cummValue);
        }
        if (latestTimestamp == null) {
            latestTimestamp = timestamp;
            latestCummValue = cummValue;
        }
        if (earliestTimestamp == null) {
            earliestTimestamp = timestamp;
            earliestCummValue = cummValue;
        }

        if (timestamp.after(latestTimestamp)) {
            delta.setValue(cummValue - latestCummValue);
            delta.setTime(timestamp);
            delta.setPeriodInSec((timestamp.getTime() - latestTimestamp
                    .getTime()) / 1000);
            latestTimestamp = timestamp;
            latestCummValue = cummValue;
            if (debug) {
                System.out.println("[" + name + "]  " + delta);
            }
            return delta;
        }

        if (timestamp.before(earliestTimestamp)) {
            delta.setValue(earliestCummValue - cummValue);
            delta.setTime(earliestTimestamp);
            delta.setPeriodInSec((earliestTimestamp.getTime() - timestamp
                    .getTime()) / 1000);
            earliestTimestamp = timestamp;
            earliestCummValue = cummValue;
            if (debug) {
                System.out.println("[" + name + "]  " + delta);
            }
            return delta;
        }

        return null;
    }

    public synchronized void clear() {
        if (debug) {
            System.out.println("[" + name + "]  CLEAR");
        }
        latestTimestamp = null;
        earliestTimestamp = null;
    }

    public static class Delta {
        private long periodInSec;

        private Date time;

        private long value;

        /**
         * @return the time
         */
        public Date getTime() {
            return time;
        }

        /**
         * @param time
         *            the time to set
         */
        public void setTime(Date time) {
            this.time = time;
        }

        /**
         * @return the value
         */
        public long getValue() {
            return value;
        }

        public long getNormalizedValue(long timePeriodInSec) {
            return value * timePeriodInSec / periodInSec;
        }

        /**
         * @param value
         *            the value to set
         */
        public void setValue(long value) {
            this.value = value;
        }

        /**
         * @return the periodInSec
         */
        public long getPeriodInSec() {
            return periodInSec;
        }

        /**
         * @param periodInSec
         *            the periodInSec to set
         */
        public void setPeriodInSec(long periodInSec) {
            this.periodInSec = periodInSec;
        }

        public double getRate() {
            return value / periodInSec;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Delta [time=" + time + " (" + time.getTime() + "), value="
                    + value + "]";
        }

    }
}
