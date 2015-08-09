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
 *  File Name: DevicegroupSelectionController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/06 11:14:07  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 17:46:10  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.devicegroups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.NumberNode;
import com.intel.stl.api.management.devicegroups.DGSelect;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.devicegroups.IncludeGroup;
import com.intel.stl.api.management.devicegroups.NodeDesc;
import com.intel.stl.api.management.devicegroups.NodeGUID;
import com.intel.stl.api.management.devicegroups.NodeTypeAttr;
import com.intel.stl.api.management.devicegroups.PortGUID;
import com.intel.stl.api.management.devicegroups.SystemImageGUID;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.admin.view.devicegroups.DevicegroupSelectionPanel;
import com.intel.stl.ui.admin.view.devicegroups.DevicesPanel;
import com.intel.stl.ui.admin.view.devicegroups.ListPanel;
import com.intel.stl.ui.main.Context;

public class DevicegroupSelectionController {
    private final DevicegroupSelectionPanel view;

    private final Map<String, IResourceSelector> selectors =
            new LinkedHashMap<String, IResourceSelector>();

    private final Map<Class<? extends IAttribute>, Set<IResourceSelector>> typeSelectors =
            new HashMap<Class<? extends IAttribute>, Set<IResourceSelector>>();

    private DevicesSelector devicesSelector;

    private SelectSelector selectSelector;

    private IncludeSelector includeSelector;

    /**
     * Description:
     * 
     * @param view
     */
    public DevicegroupSelectionController(DevicegroupSelectionPanel view) {
        super();
        this.view = view;
        installSelectors();
        view.setSelectors(selectors.values());
    }

    @SuppressWarnings("unchecked")
    protected void installSelectors() {
        selectors.clear();

        DevicesPanel dp = new DevicesPanel();
        devicesSelector = new DevicesSelector(dp);
        registerSelector(devicesSelector, SystemImageGUID.class,
                NodeGUID.class, PortGUID.class, NodeDesc.class,
                NodeTypeAttr.class);

        ListPanel<DGSelect> selectView = new ListPanel<DGSelect>();
        selectSelector = new SelectSelector(selectView);
        registerSelector(selectSelector, DGSelect.class);

        ListPanel<IncludeGroup> includeView = new ListPanel<IncludeGroup>();
        includeSelector = new IncludeSelector(includeView);
        registerSelector(includeSelector, IncludeGroup.class);
    }

    @SuppressWarnings("unchecked")
    protected void registerSelector(IResourceSelector selector,
            Class<? extends IAttribute>... types) {
        selectors.put(selector.getName(), selector);

        for (Class<? extends IAttribute> type : types) {
            Set<IResourceSelector> selectors = typeSelectors.get(type);
            if (selectors == null) {
                selectors = new HashSet<IResourceSelector>();
                typeSelectors.put(type, selectors);
            }
            selectors.add(selector);
        }
    }

    protected void registerSelector(Class<? extends IAttribute> type,
            IResourceSelector selector) {
        Set<IResourceSelector> selectors = typeSelectors.get(type);
        if (selectors == null) {
            selectors = new HashSet<IResourceSelector>();
            typeSelectors.put(type, selectors);
        }
        selectors.add(selector);
    }

    /**
     * <i>Description:</i>
     * 
     * @param context
     */
    public void setContext(Context context) {
        for (IResourceSelector selector : selectors.values()) {
            selector.setContext(context, null);
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param item
     * @param items
     */
    public void setItem(Item<DeviceGroup> item, Item<DeviceGroup>[] items) {
        DeviceGroup dg = item.getObj();

        devicesSelector.clear();

        List<NumberNode> ids = dg.getIDs();
        if (ids != null) {
            fireAddAttrs(ids, devicesSelector);
        }
        List<NodeDesc> descs = dg.getNodeDesc();
        if (descs != null) {
            fireAddAttrs(descs, devicesSelector);
        }
        List<NodeTypeAttr> types = dg.getNodeTypes();
        if (types != null) {
            fireAddAttrs(types, devicesSelector);
        }

        selectSelector.clear();
        List<DGSelect> sels = dg.getSelects();
        if (sels != null) {
            fireAddAttrs(sels, selectSelector);
        }

        includeSelector.clear();
        List<IncludeGroup> allGroups =
                new ArrayList<IncludeGroup>(items.length);
        for (int i = 0; i < items.length; i++) {
            allGroups.add(new IncludeGroup(items[i].getName()));
        }
        includeSelector.setGroups(allGroups);
        List<IncludeGroup> includeGroups = dg.getIncludeGroups();
        if (includeGroups != null) {
            fireAddAttrs(includeGroups, includeSelector);
        }
    }

    protected void fireAddAttrs(List<? extends IAttribute> attr,
            IResourceSelector... selectors) {
        for (IResourceSelector selector : selectors) {
            selector.setModelSelections(attr);
        }
    }

    public List<IAttribute> getSelections(String selectorName) {
        IResourceSelector selector = selectors.get(selectorName);
        if (selector != null) {
            return selector.getViewSelections();
        } else {
            // shouldn't happen
            throw new IllegalArgumentException(
                    "Couldn't find ResourceSelector '" + selectorName + "'");
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param attrs
     */
    public void addSelections(String selectorName, List<IAttribute> attrs) {
        IResourceSelector selector = selectors.get(selectorName);
        if (selector != null) {
            fireAddAttrs(attrs, selector);
        } else {
            // shouldn't happen
            throw new IllegalArgumentException(
                    "Couldn't find ResourceSelector '" + selectorName + "'");
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param attr
     */
    public void removeSelection(IAttribute attr) {
        Set<IResourceSelector> selectors = typeSelectors.get(attr.getClass());
        if (selectors != null) {
            fireRemoveAttrs(attr, selectors.toArray(new IResourceSelector[0]));
        }
    }

    protected void fireRemoveAttrs(IAttribute attr,
            IResourceSelector... selectors) {
        for (IResourceSelector selector : selectors) {
            selector.removeModelSelection(attr);
        }
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void clearViewSelections(String selectorName) {
        IResourceSelector selector = selectors.get(selectorName);
        if (selector != null) {
            selector.clearViewSelections();
        }
    }

}
