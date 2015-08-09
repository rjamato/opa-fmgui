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
 *  File Name: PortLinkModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/04/16 19:43:47  jijunwan
 *  Archive Log:    updated to handle a bug on DC firmware that provides value 7 not specified in spec, and the meaning of 7 is excellent
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/05 22:32:16  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/26 20:07:40  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/05 21:48:55  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 18:27:29  jijunwan
 *  Archive Log:    PR 126371 - STL1 Spec inconsistencies - LinkQuality indicator
 *  Archive Log:    added LinkQuality, updated PortCounter data structure
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/18 21:31:22  fernande
 *  Archive Log:    Adding more properties for display
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.EnumMap;

import javax.swing.Icon;

import com.intel.stl.api.configuration.LinkQuality;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;

public enum LinkQualityViz {
    UNKNOWN(LinkQuality.UNKNOWN, STLConstants.K1610_QUALITY_EXCELLENT
            .getValue(), STLConstants.K1616_QUALITY_EXCELLENT_DESC.getValue(),
            UIImages.LINK_QUALITY_EXCELLENT.getImageIcon()),
    EXCELLENT(LinkQuality.EXCELLENT, STLConstants.K1610_QUALITY_EXCELLENT
            .getValue(), STLConstants.K1616_QUALITY_EXCELLENT_DESC.getValue(),
            UIImages.LINK_QUALITY_EXCELLENT.getImageIcon()),
    VERY_GOOD(LinkQuality.VERY_GOOD, STLConstants.K1611_QUALITY_VERY_GOOD
            .getValue(), STLConstants.K1617_QUALITY_VERY_GOOD_DESC.getValue(),
            UIImages.LINK_QUALITY_VERY_GOOD.getImageIcon()),
    GOOD(LinkQuality.GOOD, STLConstants.K1612_QUALITY_GOOD.getValue(),
            STLConstants.K1618_QUALITY_GOOD_DESC.getValue(),
            UIImages.LINK_QUALITY_GOOD.getImageIcon()),
    POOR(LinkQuality.POOR, STLConstants.K1613_QUALITY_POOR.getValue(),
            STLConstants.K1619_QUALITY_POOR_DESC.getValue(),
            UIImages.LINK_QUALITY_POOR.getImageIcon()),
    BAD(LinkQuality.BAD, STLConstants.K1614_QUALITY_BAD.getValue(),
            STLConstants.K1620_QUALITY_BAD_DESC.getValue(),
            UIImages.LINK_QUALITY_BAD.getImageIcon()),
    NONE(LinkQuality.NONE, STLConstants.K1615_QUALITY_NONE.getValue(),
            STLConstants.K1621_QUALITY_NONE_DESC.getValue(),
            UIImages.LINK_QUALITY_NONE.getImageIcon());

    private final static EnumMap<LinkQuality, LinkQualityViz> linkQualityMap =
            new EnumMap<LinkQuality, LinkQualityViz>(LinkQuality.class);
    static {
        for (LinkQualityViz lqz : LinkQualityViz.values()) {
            linkQualityMap.put(lqz.linkQuality, lqz);
        }
    };

    private final LinkQuality linkQuality;

    private final String value;

    private final String description;

    private final Icon icon;

    private LinkQualityViz(LinkQuality portLinkMode, String value,
            String description, Icon icon) {
        this.linkQuality = portLinkMode;
        this.value = value;
        this.description = description;
        this.icon = icon;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    public static String getLinkQualityStr(LinkQuality quality) {
        return getLinkQualityViz(quality).getValue();
    }

    public static String getLinkQualityStr(byte value) {
        LinkQuality quality = LinkQuality.getLinkQuality(value);
        return getLinkQualityViz(quality).getValue();
    }

    public static String getLinkQualityDescription(LinkQuality quality) {
        return getLinkQualityViz(quality).getDescription();
    }

    public static String getLinkQualityDescription(byte value) {
        LinkQuality quality = LinkQuality.getLinkQuality(value);
        return getLinkQualityViz(quality).getDescription();
    }

    public static Icon getLinkQualityIcon(byte value) {
        LinkQuality quality = LinkQuality.getLinkQuality(value);
        return getLinkQualityViz(quality).getIcon();
    }

    public static LinkQualityViz getLinkQualityViz(LinkQuality quality) {
        LinkQualityViz res = linkQualityMap.get(quality);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find LinkQualityViz for " + quality);
        }
    }
}
