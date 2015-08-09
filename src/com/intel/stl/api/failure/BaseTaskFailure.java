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
 *  File Name: BaseFailureItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/08/12 20:28:40  jijunwan
 *  Archive Log:    init version Failure Management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.failure;

public abstract class BaseTaskFailure<E> implements ITaskFailure<E> {
    private final Object id;

    private final IFailureEvaluator evaluator;

    public BaseTaskFailure(Object id, IFailureEvaluator evaluator) {
        super();
        this.id = id;
        this.evaluator = evaluator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.failure.IFailureItem#getId()
     */
    @Override
    public Object getTaskId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.failure.IFailureItem#getFailureType(java.lang.Throwable
     * )
     */
    @Override
    public FailureType getFailureType(Throwable error) {
        if (evaluator == null) {
            throw new RuntimeException("No FailureEvaluator provided!");
        }

        return evaluator.getType(error);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseTaskFailure other = (BaseTaskFailure) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
