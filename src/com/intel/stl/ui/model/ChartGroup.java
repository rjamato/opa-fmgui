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
 *  File Name: ChartsGroup.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/07/21 17:03:02  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:09:01  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.ui.common.view.ChartsView;

/**
 * Organize charts by a group
 */
public class ChartGroup {
    /**
     * Name of this ChartsGroup
     */
    private String name;
    /**
     * The chart view that represents this group. it can be a special view
     * that represents this group, or just one of the members.  
     */
    private ChartsView chartView;
    /**
     * The member of this group. When this list is null or empty, this group
     * is actually a leaf node represents a <code>chartView</code> with the 
     * name <code>name</code>
     */
    private List<ChartGroup> members;
    
    /**
     * Description: 
     *
     * @param name
     * @param chartView 
     */
    public ChartGroup(String name, ChartsView chartView) {
        super();
        this.name = name;
        this.chartView = chartView;
    }
    
    public ChartGroup(ChartsView chartView) {
        this(chartView==null ? null : chartView.getTitle(), chartView);
    }
    
    public void addMember(ChartGroup member) {
        if (members==null) {
            members = new ArrayList<ChartGroup>();
        }
        members.add(member);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the chartView
     */
    public ChartsView getChartView() {
        return chartView;
    }

    /**
     * @return the members
     */
    public List<ChartGroup> getMembers() {
        return members;
    }
    
    public boolean hasMembers() {
        return members!=null && !members.isEmpty();
    }
    
    public int numMembers() {
        return members==null ? 0 : members.size();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ChartsGroup [name=" + name + ", chartView=" + chartView
                + ", members=" + members + "]";
    }
    
}
