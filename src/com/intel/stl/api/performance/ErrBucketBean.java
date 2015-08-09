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
package com.intel.stl.api.performance;

import java.io.Serializable;

/**
 * @author jijunwan
 * 
 */
public class ErrBucketBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int integrityErrors;

    private int congestionErrors;

    private int smaCongestionErrors;

    private int bubbleErrors;

    private int securityErrors;

    private int routingErrors;

    public ErrBucketBean() {
        super();
    }

    public ErrBucketBean(int integrityErrors, int congestionErrors,
            int smaCongestionErrors, int bubbleErrors, int securityErrors,
            int routingErrors) {
        super();
        this.integrityErrors = integrityErrors;
        this.congestionErrors = congestionErrors;
        this.smaCongestionErrors = smaCongestionErrors;
        this.bubbleErrors = bubbleErrors;
        this.securityErrors = securityErrors;
        this.routingErrors = routingErrors;
    }

    /**
     * @return the integrityErrors
     */
    public int getIntegrityErrors() {
        return integrityErrors;
    }

    /**
     * @param integrityErrors
     *            the integrityErrors to set
     */
    public void setIntegrityErrors(int integrityErrors) {
        this.integrityErrors = integrityErrors;
    }

    /**
     * @return the congestionErrors
     */
    public int getCongestionErrors() {
        return congestionErrors;
    }

    /**
     * @param congestionErrors
     *            the congestionErrors to set
     */
    public void setCongestionErrors(int congestionErrors) {
        this.congestionErrors = congestionErrors;
    }

    /**
     * @return the smaCongestionErrors
     */
    public int getSmaCongestionErrors() {
        return smaCongestionErrors;
    }

    /**
     * @param smaCongestionErrors
     *            the smaCongestionErrors to set
     */
    public void setSmaCongestionErrors(int smaCongestionErrors) {
        this.smaCongestionErrors = smaCongestionErrors;
    }

    /**
     * @return the bubbleErrors
     */
    public int getBubbleErrors() {
        return bubbleErrors;
    }

    /**
     * @param bubbleErrors
     *            the bubbleErrors to set
     */
    public void setBubbleErrors(int bubbleErrors) {
        this.bubbleErrors = bubbleErrors;
    }

    /**
     * @return the securityErrors
     */
    public int getSecurityErrors() {
        return securityErrors;
    }

    /**
     * @param securityErrors
     *            the securityErrors to set
     */
    public void setSecurityErrors(int securityErrors) {
        this.securityErrors = securityErrors;
    }

    /**
     * @return the routingErrors
     */
    public int getRoutingErrors() {
        return routingErrors;
    }

    /**
     * @param routingErrors
     *            the routingErrors to set
     */
    public void setRoutingErrors(int routingErrors) {
        this.routingErrors = routingErrors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ErrBucketBean [integrityErrors=" + integrityErrors
                + ", congestionErrors=" + congestionErrors
                + ", smaCongestionErrors=" + smaCongestionErrors
                + ", bubbleErrors=" + bubbleErrors + ", securityErrors="
                + securityErrors + ", routingErrors=" + routingErrors + "]";
    }

}
