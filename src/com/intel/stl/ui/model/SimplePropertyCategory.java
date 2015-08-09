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
 *  File Name: SimplePropertyCategory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/22 02:05:18  jijunwan
 *  Archive Log:    made property model more general
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimplePropertyCategory implements
        IPropertyCategory<PropertyItem<SimplePropertyKey>> {
    private final String keyHeader;

    private final String valueHeader;

    private boolean showHeader;

    private final List<PropertyItem<SimplePropertyKey>> items;

    public SimplePropertyCategory() {
        this(null, null);
        showHeader = false;
    }

    /**
     * Description:
     * 
     * @param keyHeader
     * @param valueHeader
     */
    public SimplePropertyCategory(String keyHeader, String valueHeader) {
        super();
        this.keyHeader = keyHeader;
        this.valueHeader = valueHeader;
        items = new ArrayList<PropertyItem<SimplePropertyKey>>();
    }

    /**
     * @return the showHeader
     */
    public boolean isShowHeader() {
        return showHeader;
    }

    /**
     * @param showHeader
     *            the showHeader to set
     */
    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.model.IPropertyCategory#getKeyHeader()
     */
    @Override
    public String getKeyHeader() {
        return showHeader ? keyHeader : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.model.IPropertyCategory#getValueHeader()
     */
    @Override
    public String getValueHeader() {
        return showHeader ? valueHeader : null;
    }

    public void addItem(PropertyItem<SimplePropertyKey> item) {
        items.add(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.model.IPropertyCategory#getItems()
     */
    @Override
    public Collection<PropertyItem<SimplePropertyKey>> getItems() {
        return items;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.model.IPropertyCategory#size()
     */
    @Override
    public int size() {
        return items.size();
    }

}
