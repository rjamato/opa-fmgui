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
 *  File Name: IModelListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/09 12:54:07  fernande
 *  Archive Log:    Added support for PropertyChange events bubbling up from an SwingWorker, thru an AbstractTask into the controller.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 21:48:13  fernande
 *  Archive Log:    Changes to framework to support model update lifecycle by notifying views of changes
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.framework;

public interface IModelListener<M extends IModel> {

    /**
     * 
     * Description: invoked by the model when a notifyModelChanged() is
     * requested
     * 
     * @param model
     *            the model that was changed
     */
    void modelChanged(M model);

    /**
     * 
     * Description: invoked by the model when a notifyModelUpdateFailed() is
     * requested
     * 
     * @param caught
     *            the error caught during model update
     */
    void modelUpdateFailed(M model, Throwable caught);
}
