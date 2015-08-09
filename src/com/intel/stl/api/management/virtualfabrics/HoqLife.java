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
 *  File Name: HoqLife.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/27 15:42:32  jijunwan
 *  Archive Log:    added installVirtualFabric to IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/25 19:10:04  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.virtualfabrics;

import com.intel.stl.api.management.WrapperNode;
import com.intel.stl.api.management.XMLConstants;
import com.intel.stl.api.management.virtualfabrics.HoqLife.TimeOut;
import com.intel.stl.api.management.virtualfabrics.HoqLife.TimeOut.Unit;

/**
 * This timeout is specified per SM instance and may be overridden per Virtual
 * Fabric. If not specified per Virtual Fabric, default to the SM instance
 * values.
 */
public class HoqLife extends WrapperNode<TimeOut> {
    private static final long serialVersionUID = -3442071697440514266L;

    public HoqLife() {
        this(null);
    }

    /**
     * Description:
     * 
     * @param type
     * @param value
     */
    public HoqLife(TimeOut value) {
        super(XMLConstants.HOQ_LIFE, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.WrapperNode#valueOf(java.lang.String)
     */
    @Override
    protected TimeOut valueOf(String str) {
        str = str.trim();
        int pos = 0;
        while (pos < str.length() && Character.isDigit(str.charAt(pos))) {
            pos += 1;
        }
        if (pos < str.length() - 1) {
            String numStr = str.substring(0, pos);
            String unitStr = str.substring(pos).trim();
            Unit unit = Unit.valueOf(unitStr.toUpperCase());
            if (unit != null) {
                int num = Integer.parseInt(numStr);
                return new TimeOut(num, unit);
            }
        }
        throw new IllegalArgumentException("Invalid format '" + str + "'");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#valueString(java.lang.Object)
     */
    @Override
    protected String valueString(TimeOut value) {
        return value.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IAttribute#copy()
     */
    @Override
    public HoqLife copy() {
        return new HoqLife(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HoqLife [type=" + type + ", value=" + value + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.WrapperNode#installVirtualFabric(com.intel
     * .stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void installVirtualFabric(VirtualFabric vf) {
        vf.setHoqLife(this);
    }

    public static class TimeOut {
        public enum Unit {
            NS,
            US,
            MS,
            S,
            M,
            H
        };

        private final int value;

        private final Unit unit;

        /**
         * Description:
         * 
         * @param value
         * @param unit
         */
        public TimeOut(int value, Unit unit) {
            super();
            this.value = value;
            this.unit = unit;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @return the unit
         */
        public Unit getUnit() {
            return unit;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((unit == null) ? 0 : unit.hashCode());
            result = prime * result + value;
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TimeOut other = (TimeOut) obj;
            if (unit == null) {
                if (other.unit != null) {
                    return false;
                }
            } else if (!unit.equals(other.unit)) {
                return false;
            }
            if (value != other.value) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return value + unit.name().toLowerCase();
        }

    }

}
