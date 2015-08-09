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
 *  File Name: ConsoleAppender.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/08 22:40:29  fernande
 *  Archive Log:    Logback configuration support
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/17 21:16:41  fernande
 *  Archive Log:    Backend changes to update log config.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import org.w3c.dom.Node;

public class ConsoleAppender extends AppenderConfig {

    private static final long serialVersionUID = 1L;

    @Override
    public void updateNode(Node node, ILogConfigFactory factory) {
        factory.updateNode(node, this);
    }

    @Override
    public Node createNode(ILogConfigFactory factory) {
        return factory.createNode(this);
    }

    @Override
    public void populateFromNode(Node node, ILogConfigFactory factory) {
        factory.populateFromNode(node, this);
    }

    @Override
    public String toString() {
        return "ConsoleAppender [name=" + getName() + ", threshold="
                + getThreshold() + ", pattern=" + getConversionPattern() + "]";
    }
}
