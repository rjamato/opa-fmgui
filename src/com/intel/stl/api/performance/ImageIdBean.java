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
public class ImageIdBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long imageNumber;

    private int imageOffset;

    public ImageIdBean() {
        super();
    }

    public ImageIdBean(long imageNumber, int imageOffset) {
        super();
        this.imageNumber = imageNumber;
        this.imageOffset = imageOffset;
    }

    /**
     * @return the imageNumber
     */
    public long getImageNumber() {
        return imageNumber;
    }

    /**
     * @param imageNumber
     *            the imageNumber to set
     */
    public void setImageNumber(long imageNumber) {
        this.imageNumber = imageNumber;
    }

    /**
     * @return the imageOffset
     */
    public int getImageOffset() {
        return imageOffset;
    }

    /**
     * @param imageOffset
     *            the imageOffset to set
     */
    public void setImageOffset(int imageOffset) {
        this.imageOffset = imageOffset;
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
        result = prime * result + (int) (imageNumber ^ (imageNumber >>> 32));
        result = prime * result + imageOffset;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        ImageIdBean other = (ImageIdBean) obj;
        if (imageNumber != other.imageNumber) {
            return false;
        }
        if (imageOffset != other.imageOffset) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ImageId [imageNumber=0x" + Long.toHexString(imageNumber)
                + ", imageOffset=" + imageOffset + "]";
    }

}
