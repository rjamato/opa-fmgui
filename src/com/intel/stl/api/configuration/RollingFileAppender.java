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

package com.intel.stl.api.configuration;

import org.w3c.dom.Node;

/**
 * @author jypak
 * 
 */
public class RollingFileAppender extends AppenderConfig {
    private static final long serialVersionUID = 1L;

    private String fileLocation;

    private String fileNamePattern;

    private String maxFileSize;

    private String maxNumOfBackup;

    /**
     * @return the maxFileSize
     */
    public String getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * @param maxFileSize
     *            the maxFileSize to set
     */
    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * @return the maxNumOfBackUp
     */
    public String getMaxNumOfBackUp() {
        return maxNumOfBackup;
    }

    /**
     * @param maxNumOfBackup
     *            the maxNumOfBackup to set
     */
    public void setMaxNumOfBackUp(String maxNumOfBackup) {
        this.maxNumOfBackup = maxNumOfBackup;
    }

    /**
     * @return the fileLocation
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * @param fileLocation
     *            the fileLocation to set
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * @return the fileNamePattern
     */
    public String getFileNamePattern() {
        return fileNamePattern;
    }

    /**
     * @param fileNamePattern
     *            the fileNamePattern to set
     */
    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

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
        factory.populateFomNode(node, this);
    }

    @Override
    public String toString() {
        return "RollingFileAppender [name=" + getName() + ", threshold="
                + getThreshold() + ", pattern=" + getConversionPattern()
                + ", file=" + fileLocation + ", maxFileSize=" + maxFileSize
                + ", maxNumOfBackup=" + maxNumOfBackup + "]";
    }
}
