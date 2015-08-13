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
 *  File Name: PortLinkModeViz.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:38  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
