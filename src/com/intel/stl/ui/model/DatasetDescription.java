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
 *  File Name: DatasetDescription.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/17 23:29:05  jijunwan
 *  Archive Log:    minor change
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/17 23:22:17  jijunwan
 *  Archive Log:    PR 127106 - Suggest to use same bucket range for Group Err Summary as shown in "opatop" command to plot performance graphs in FV
 *  Archive Log:     - changed error histogram chart to bar chart to show the new data ranges: 0-25%, 25-50%, 50-75%, 75-100% and 100+%
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 19:11:01  jijunwan
 *  Archive Log:    fixed a bug
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 18:38:44  jijunwan
 *  Archive Log:    introduced DatasetDescription to support short name and full name (description) for a dataset
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.model;

import org.jfree.data.general.Dataset;

public class DatasetDescription {
    private final String name;

    private String fullName;

    private final Dataset dataset;

    private final boolean jumpable;

    /**
     * Description:
     * 
     * @param name
     * @param fullName
     * @param dataset
     */
    public DatasetDescription(String name, String fullName, Dataset dataset,
            boolean jumpable) {
        super();
        this.name = name;
        this.fullName = fullName;
        this.dataset = dataset;
        this.jumpable = jumpable;
    }

    /**
     * Description:
     * 
     * @param name
     * @param dataset
     */
    public DatasetDescription(String name, Dataset dataset) {
        this(name, name, dataset, false);
    }

    /**
     * @return the description
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName
     *            the description to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the dataset
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * @return the jumpable
     */
    public boolean isJumpable() {
        return jumpable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DatasetDescription [name=" + name + ", fullName=" + fullName
                + ", dataset=" + dataset + "]";
    }

}
