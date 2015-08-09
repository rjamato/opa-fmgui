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
 *  File Name: DeviceGroupRendererModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/30 14:40:52  jijunwan
 *  Archive Log:    added subclasses for ListRenderer to avoid the compiler problem on Hudson server
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/30 14:25:42  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.virtualfabrics;

import java.util.LinkedHashMap;
import java.util.Map;

import com.intel.stl.api.management.XMLConstants;
import com.intel.stl.api.management.virtualfabrics.ApplicationName;
import com.intel.stl.api.management.virtualfabrics.LimitedMember;
import com.intel.stl.api.management.virtualfabrics.Member;
import com.intel.stl.ui.admin.impl.IRendererModel;
import com.intel.stl.ui.admin.view.IAttrRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.ApplicationRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.BandwidthRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.BaseSLRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.FlowControlDisableRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.HighPriorityRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.HoqLifeRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.LimitedMemberRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.MaxMtuRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.MaxRateRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.MemberRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.PKeyRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.PktLifeTimeMultRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.PreemptRankRenderer;
import com.intel.stl.ui.admin.view.virtualfabrics.StandbyRenderer;

public class VirtualFabricRendererModel implements IRendererModel {
    private final static Map<String, Class<? extends IAttrRenderer<?>>> map =
            new LinkedHashMap<String, Class<? extends IAttrRenderer<?>>>() {
                private static final long serialVersionUID =
                        7007977245246746722L;

                {
                    put(XMLConstants.PKEY, PKeyRenderer.class);
                    put(XMLConstants.MAX_MTU, MaxMtuRenderer.class);
                    put(XMLConstants.MAX_RATE, MaxRateRenderer.class);
                    put(XMLConstants.STANDBY, StandbyRenderer.class);
                    put(XMLConstants.HIGH_PRIORITY, HighPriorityRenderer.class);
                    put(XMLConstants.BANDWIDTH, BandwidthRenderer.class);
                    put(XMLConstants.PKT_LT_MULT, PktLifeTimeMultRenderer.class);
                    put(XMLConstants.BASE_SL, BaseSLRenderer.class);
                    put(XMLConstants.FLOW_CONTR_DISABLE,
                            FlowControlDisableRenderer.class);
                    put(XMLConstants.PREEMPT_RANK, PreemptRankRenderer.class);
                    put(XMLConstants.HOQ_LIFE, HoqLifeRenderer.class);
                    put(XMLConstants.MEMBER, MemberRenderer.class);
                    put(XMLConstants.LIMITED_MEMBER,
                            LimitedMemberRenderer.class);
                    put(XMLConstants.APPLICATION, ApplicationRenderer.class);
                }
            };

    private String[] appNames;

    private String[] dgNames;

    /**
     * @param appNames
     *            the appNames to set
     */
    public void setAppNames(String[] appNames) {
        this.appNames = appNames;
    }

    public void setDgNames(String[] dgNames) {
        this.dgNames = dgNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.IRendererModel#getRendererNames()
     */
    @Override
    public String[] getRendererNames() {
        return map.keySet().toArray(new String[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.IRendererModel#getRenderer(java.lang.String)
     */
    @Override
    public IAttrRenderer<?> getRenderer(String name) throws Exception {
        Class<? extends IAttrRenderer<?>> klass = map.get(name);
        if (klass != null) {
            IAttrRenderer<?> res = klass.newInstance();
            initRenderer(name, res);
            return res;
        } else {
            throw new IllegalArgumentException("Unknown renderer '" + name
                    + "'");
        }
    }

    protected void initRenderer(String name, IAttrRenderer<?> renderer) {
        if (name.equals(XMLConstants.MEMBER)) {
            MemberRenderer memebers = (MemberRenderer) renderer;
            Member[] sns = new Member[dgNames.length];
            for (int i = 0; i < sns.length; i++) {
                sns[i] = new Member(dgNames[i]);
            }
            memebers.setList(sns);
        } else if (name.equals(XMLConstants.LIMITED_MEMBER)) {
            LimitedMemberRenderer limitedMembers =
                    (LimitedMemberRenderer) renderer;
            LimitedMember[] sns = new LimitedMember[dgNames.length];
            for (int i = 0; i < sns.length; i++) {
                sns[i] = new LimitedMember(dgNames[i]);
            }
            limitedMembers.setList(sns);
        } else if (name.equals(XMLConstants.APPLICATION)) {
            ApplicationRenderer appMemebers = (ApplicationRenderer) renderer;
            ApplicationName[] sns = new ApplicationName[appNames.length];
            for (int i = 0; i < sns.length; i++) {
                sns[i] = new ApplicationName(appNames[i]);
            }
            appMemebers.setList(sns);
        }
    }

    public boolean isRepeatabledAttr(String name) {
        return name.equals(XMLConstants.MEMBER)
                || name.equals(XMLConstants.LIMITED_MEMBER)
                || name.equals(XMLConstants.APPLICATION);
    }

}
