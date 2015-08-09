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
 *  File Name: PropertyCategory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/05/06 19:20:31  jijunwan
 *  Archive Log:    fixed Serializable issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/05/01 21:34:29  jijunwan
 *  Archive Log:    fixed Serializable issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/13 21:00:49  fernande
 *  Archive Log:    Added support for valueHeader attribute in UserOptions XML.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/29 18:53:56  fernande
 *  Archive Log:    Changing UserSettings to support Properties Display options
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.io.Serializable;

public class PropertyCategory implements Serializable {
    private static final long serialVersionUID = 6614578247875201296L;

    private ResourceCategory resourceCategory;

    private boolean showHeader = true;

    private String keyHeader;

    private String valueHeader;

    public ResourceCategory getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(ResourceCategory resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public String getKeyHeader() {
        return keyHeader;
    }

    public void setKeyHeader(String header) {
        this.keyHeader = header;
    }

    public String getValueHeader() {
        return valueHeader;
    }

    public void setValueHeader(String valueHeader) {
        this.valueHeader = valueHeader;
    }

}
