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
 *  File Name: PortUtils.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/03/16 21:56:30  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/05 17:30:40  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/28 23:15:20  jijunwan
 *  Archive Log:    added helpers to support unsigned byte, short and int
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 23:09:30  jijunwan
 *  Archive Log:    renamed PortUtils to Utils
 *  Archive Log:    moved convertFromUnixTime to Utils
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:47:22  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015.
 *  Archive Log:    adapt to changes introduced on FM side.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/21 22:14:14  jijunwan
 *  Archive Log:    Added utilities that is able to detect port status. We should always reuse these utilities when possible to ensure we have consistent results
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.intel.stl.api.configuration.LinkSpeedMask;
import com.intel.stl.api.configuration.LinkWidthMask;
import com.intel.stl.api.subnet.PortInfoBean;

public class Utils {
    public static boolean isExpectedSpeed(PortInfoBean port,
            LinkSpeedMask expectedSpeed) {
        short activeSpeedBit = port.getLinkSpeedActive();
        return activeSpeedBit == expectedSpeed.getSpeedMask();
    }

    public static boolean isExpectedWidth(PortInfoBean port,
            LinkWidthMask expectedWidth) {
        short activeWidthBit = port.getLinkWidthActive();
        return activeWidthBit == expectedWidth.getWidthMask();
    }

    public static boolean isExpectedWidthDowngrade(PortInfoBean port,
            LinkWidthMask expectedWidth) {
        short txActiveWidthDownBit = port.getLinkWidthDownTxActive();
        short rxActiveWidthDownBit = port.getLinkWidthDownRxActive();
        return txActiveWidthDownBit == expectedWidth.getWidthMask()
                && rxActiveWidthDownBit == expectedWidth.getWidthMask();
    }

    public static boolean isSlowPort(PortInfoBean port) {
        return isDegradedPort(port) || !isExpectedSpeed(port)
                || !isExpectedWidth(port) || !isExpectedWidthDowngrade(port);
    }

    public static boolean isExpectedSpeed(PortInfoBean port) {
        short activeSpeedBit = port.getLinkSpeedActive();
        float activeSpeed =
                LinkSpeedMask.getLinkSpeedMask(activeSpeedBit).getSpeedInGb();
        float expectedSpeed = getEnabledSpeed(port);
        return (activeSpeed >= expectedSpeed);
    }

    private static float getEnabledSpeed(PortInfoBean port) {
        float maxSupportedSpeed = 0;
        List<LinkSpeedMask> supportedSpeeds =
                LinkSpeedMask.getSpeedMasks(port.getLinkSpeedSupported());
        for (LinkSpeedMask speed : supportedSpeeds) {
            if (speed.getSpeedInGb() > maxSupportedSpeed) {
                maxSupportedSpeed = speed.getSpeedInGb();
            }
        }
        return maxSupportedSpeed;
    }

    public static boolean isExpectedWidth(PortInfoBean port) {
        short activeWidthBit = port.getLinkWidthActive();
        int activeWidth =
                LinkWidthMask.getLinkWidthMask(activeWidthBit).getWidth();
        int expectedWidth = getEnabledWidth(port);
        return (activeWidth >= expectedWidth);
    }

    private static int getEnabledWidth(PortInfoBean port) {
        int maxSupportedWidth = 0;
        List<LinkWidthMask> supportedWidths =
                LinkWidthMask.getWidthMasks(port.getLinkWidthSupported());
        for (LinkWidthMask speed : supportedWidths) {
            if (speed.getWidth() > maxSupportedWidth) {
                maxSupportedWidth = speed.getWidth();
            }
        }
        return maxSupportedWidth;
    }

    public static boolean isExpectedWidthDowngrade(PortInfoBean port) {
        short txActiveWidthDownBit = port.getLinkWidthDownTxActive();
        int txActiveWidthDown =
                LinkWidthMask.getLinkWidthMask(txActiveWidthDownBit).getWidth();
        short rxActiveWidthDownBit = port.getLinkWidthDownRxActive();
        int rxActiveWidthDown =
                LinkWidthMask.getLinkWidthMask(rxActiveWidthDownBit).getWidth();
        int expectedWidthDown = getEnabledWidthDowngrade(port);
        return (txActiveWidthDown >= expectedWidthDown)
                && (rxActiveWidthDown >= expectedWidthDown);
    }

    private static int getEnabledWidthDowngrade(PortInfoBean port) {
        int maxSupportedWidthDown = 0;
        List<LinkWidthMask> supportedWidthDowns =
                LinkWidthMask.getWidthMasks(port.getLinkWidthDownSupported());
        for (LinkWidthMask speed : supportedWidthDowns) {
            if (speed.getWidth() > maxSupportedWidthDown) {
                maxSupportedWidthDown = speed.getWidth();
            }
        }
        return maxSupportedWidthDown;
    }

    public static boolean isDegradedPort(PortInfoBean port) {
        short activeWidthBit = port.getLinkWidthActive();
        short txActiveWidthDownBit = port.getLinkWidthDownTxActive();
        short rxActiveWidthDownBit = port.getLinkWidthDownRxActive();
        return activeWidthBit != txActiveWidthDownBit
                || activeWidthBit != rxActiveWidthDownBit;
    }

    public static final Date convertFromUnixTime(long unixTime) {
        return new Date(unixTime * 1000);
    }

    public static short unsignedByte(byte val) {
        return (short) (val & 0xff);
    }

    public static int unsignedShort(short val) {
        return val & 0xffff;
    }

    public static long unsignedInt(int val) {
        return val & 0xffffffffL;
    }

    public static long toLong(String str) {
        if (str.startsWith("0x") || str.startsWith("0X")) {
            str = str.substring(2, str.length());
        }
        return new BigInteger(str, 16).longValue();
    }

    public static File getFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            ClassLoader classLoader = Utils.class.getClassLoader();
            file = new File(classLoader.getResource(fileName).getFile());
        }
        return file;
    }

}
