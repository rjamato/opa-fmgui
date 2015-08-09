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
 *  File Name: FabricModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/22 01:16:19  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/09 12:59:15  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.framework.AbstractModel;

public class FabricModel extends AbstractModel {

    private SubnetDescription currentSubnet;

    private SubnetDescription previousSubnet;

    private boolean addRandomValues;

    private String errorMessage;

    public SubnetDescription getCurrentSubnet() {
        return currentSubnet;
    }

    public void setCurrentSubnet(SubnetDescription currentSubnet) {
        this.currentSubnet = currentSubnet;
    }

    public SubnetDescription getPreviousSubnet() {
        return previousSubnet;
    }

    public void setPreviousSubnet(SubnetDescription previousSubnet) {
        this.previousSubnet = previousSubnet;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isAddRandomValues() {
        return addRandomValues;
    }

    public void setAddRandomValues(boolean addRandomValues) {
        this.addRandomValues = addRandomValues;
    }

}
