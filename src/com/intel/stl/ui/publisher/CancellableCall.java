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
 *  File Name: ICancellableCall.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/02 14:20:23  jijunwan
 *  Archive Log:    improvement to CancellableCall to handle null cancel indicator and indicator is set after the caller already ran
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/09 21:30:48  jijunwan
 *  Archive Log:    promote cancelIndicator to protected
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/05 13:42:43  jijunwan
 *  Archive Log:    added special caller and callback to support cancellation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.util.concurrent.Callable;

import com.intel.stl.ui.common.ICancelIndicator;

public abstract class CancellableCall<V> implements Callable<V> {
    protected ICancelIndicator cancelIndicator;

    /**
     * @param cancelIndicator
     *            the cancelIndicator to set
     */
    public void setCancelIndicator(ICancelIndicator cancelIndicator) {
        this.cancelIndicator = cancelIndicator;
    }

    /**
     * @return the cancelIndicator
     */
    public ICancelIndicator getCancelIndicator() {
        return cancelIndicator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public V call() throws Exception {
        // a wrapper of cancelIndicator to handle the following case:
        // 1) cancelIndicator is null
        // 2) cancelIndicator is set after the call already ran
        return call(new ICancelIndicator() {

            @Override
            public boolean isCancelled() {
                return cancelIndicator != null && cancelIndicator.isCancelled();
            }

        });
    }

    public abstract V call(ICancelIndicator cancelIndicator) throws Exception;
}
