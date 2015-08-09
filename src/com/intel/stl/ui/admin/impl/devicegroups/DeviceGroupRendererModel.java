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
 *  Archive Log:    Revision 1.2  2015/03/30 14:45:43  jijunwan
 *  Archive Log:    added subclasses for ListRenderer to avoid the compiler problem on Hudson server
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/30 14:25:40  jijunwan
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

package com.intel.stl.ui.admin.impl.devicegroups;

import java.util.LinkedHashMap;
import java.util.Map;

import com.intel.stl.api.management.XMLConstants;
import com.intel.stl.api.management.devicegroups.IncludeGroup;
import com.intel.stl.ui.admin.impl.IRendererModel;
import com.intel.stl.ui.admin.view.IAttrRenderer;
import com.intel.stl.ui.admin.view.devicegroups.DGSelectRenderer;
import com.intel.stl.ui.admin.view.devicegroups.IncludeGroupRenderer;
import com.intel.stl.ui.admin.view.devicegroups.NodeDescRenderer;
import com.intel.stl.ui.admin.view.devicegroups.NodeGUIDRenderer;
import com.intel.stl.ui.admin.view.devicegroups.NodeTypeRenderer;
import com.intel.stl.ui.admin.view.devicegroups.PortGUIDRenderer;
import com.intel.stl.ui.admin.view.devicegroups.SystemImageGUIDRenderer;

public class DeviceGroupRendererModel implements IRendererModel {
    private final static Map<String, Class<? extends IAttrRenderer<?>>> map =
            new LinkedHashMap<String, Class<? extends IAttrRenderer<?>>>() {
                private static final long serialVersionUID =
                        -4676175893529648601L;

                {
                    put(XMLConstants.SI_GUID, SystemImageGUIDRenderer.class);
                    put(XMLConstants.NODE_GUID, NodeGUIDRenderer.class);
                    put(XMLConstants.PORT_GUID, PortGUIDRenderer.class);
                    put(XMLConstants.NODE_DESC, NodeDescRenderer.class);
                    put(XMLConstants.NODE_TYPE, NodeTypeRenderer.class);
                    put(XMLConstants.SELECT, DGSelectRenderer.class);
                    put(XMLConstants.INCLUDE_GROUP, IncludeGroupRenderer.class);
                }
            };

    private String[] dgNames;

    /**
     * @param dgNames
     *            the dgNames to set
     */
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
        if (name.equals(XMLConstants.INCLUDE_GROUP)) {
            IncludeGroupRenderer lsRenderer = (IncludeGroupRenderer) renderer;
            lsRenderer.setList(IncludeGroup.toArry(dgNames));
        }
    }
}
