/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *  
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: ImageInfoBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/09/26 03:21:08  jijunwan
 *  Archive Log:    PR 130522 - OtherPorts doesn't report a value
 *  Archive Log:    - fixed mistake on hasChange
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/09/20 22:30:40  jijunwan
 *  Archive Log:    PR 130522 - OtherPorts doesn't report a value
 *  Archive Log:    - introduced hasChange to ImageInfo to detect if there is any statistics changes between two ImageInfo
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/17 18:48:41  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/06/25 21:04:02  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improvement on data randomization that ensure an attribute at the same time point get the same data where
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/09 03:29:21  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/12 19:30:00  jijunwan
 *  Archive Log:    introduced interface ITimestamped, and all timimg attributes implemented it, so we can easily know which attribute is associated with timestamp
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/04 21:37:53  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.performance;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.intel.stl.api.ITimestamped;
import com.intel.stl.api.Utils;

/**
 * @author jijunwan
 * 
 */
public class ImageInfoBean implements ITimestamped, Serializable {
    private static final long serialVersionUID = 1L;

    private ImageIdBean imageId;

    private long sweepStart;

    private int sweepDuration;

    private int numHFIPorts; // promote to handle unsigned short

    private int numSwitchNodes; // promote to handle unsigned short

    private long numSwitchPorts; // unsigned int

    private long numLinks; // unsigned int

    private int numSMs; // should be fine

    private int numFailedNodes; // should be fine

    private long numFailedPorts; // unsigned int

    private int numSkippedNodes;

    private long numSkippedPorts; // unsigned int

    private long numUnexpectedClearPorts; // unsigned int

    private SMInfoDataBean[] SMInfo;

    /**
     * @return the imageId
     */
    public ImageIdBean getImageId() {
        return imageId;
    }

    /**
     * @param imageId
     *            the imageId to set
     */
    public void setImageId(ImageIdBean imageId) {
        this.imageId = imageId;
    }

    /**
     * Note that sweepStart is Unix time (seconds since Jan 1st, 1970)
     * 
     * @return the sweepStart
     */
    public long getSweepStart() {
        return sweepStart;
    }

    /**
     * 
     * <i>Description:</i> returns sweepStart as Date
     * 
     * @return sweepStart converted to Date
     */
    public Date getSweepStartDate() {
        return Utils.convertFromUnixTime(sweepStart);
    }

    /**
     * Note that sweepStart is Unix time (seconds since Jan 1st, 1970)
     * 
     * @param sweepStart
     *            the sweepStart to set
     */
    public void setSweepStart(long sweepStart) {
        this.sweepStart = sweepStart;
    }

    /**
     * @return the sweepDuration
     */
    public int getSweepDuration() {
        return sweepDuration;
    }

    /**
     * @param sweepDuration
     *            the sweepDuration to set
     */
    public void setSweepDuration(int sweepDuration) {
        this.sweepDuration = sweepDuration;
    }

    /**
     * @return the numHCAPorts
     */
    public int getNumHFIPorts() {
        return numHFIPorts;
    }

    /**
     * @param numHCAPorts
     *            the numHCAPorts to set
     */
    public void setNumHFIPorts(short numHCAPorts) {
        this.numHFIPorts = numHCAPorts & 0xffff;
    }

    /**
     * @return the numSwitchNodes
     */
    public int getNumSwitchNodes() {
        return numSwitchNodes;
    }

    /**
     * @param numSwitchNodes
     *            the numSwitchNodes to set
     */
    public void setNumSwitchNodes(short numSwitchNodes) {
        this.numSwitchNodes = numSwitchNodes & 0xffff;
    }

    /**
     * @return the numSwitchPorts
     */
    public long getNumSwitchPorts() {
        return numSwitchPorts;
    }

    /**
     * @param numSwitchPorts
     *            the numSwitchPorts to set
     */
    public void setNumSwitchPorts(long numSwitchPorts) {
        this.numSwitchPorts = numSwitchPorts;
    }

    /**
     * @param numSwitchPorts
     *            the numSwitchPorts to set
     */
    public void setNumSwitchPorts(int numSwitchPorts) {
        this.numSwitchPorts = Utils.unsignedInt(numSwitchPorts);
    }

    /**
     * @return the numLinks
     */
    public long getNumLinks() {
        return numLinks;
    }

    /**
     * @param numLinks
     *            the numLinks to set
     */
    public void setNumLinks(long numLinks) {
        this.numLinks = numLinks;
    }

    /**
     * @param numLinks
     *            the numLinks to set
     */
    public void setNumLinks(int numLinks) {
        this.numLinks = Utils.unsignedInt(numLinks);
    }

    /**
     * @return the numSMs
     */
    public int getNumSMs() {
        return numSMs;
    }

    /**
     * @param numSMs
     *            the numSMs to set
     */
    public void setNumSMs(int numSMs) {
        this.numSMs = numSMs;
    }

    /**
     * @return the numFailedNodes
     */
    public int getNumFailedNodes() {
        return numFailedNodes;
    }

    /**
     * @param numFailedNodes
     *            the numFailedNodes to set
     */
    public void setNumFailedNodes(int numFailedNodes) {
        this.numFailedNodes = numFailedNodes;
    }

    /**
     * @return the numFailedPorts
     */
    public long getNumFailedPorts() {
        return numFailedPorts;
    }

    /**
     * @param numFailedPorts
     *            the numFailedPorts to set
     */
    public void setNumFailedPorts(long numFailedPorts) {
        this.numFailedPorts = numFailedPorts;
    }

    /**
     * @param numFailedPorts
     *            the numFailedPorts to set
     */
    public void setNumFailedPorts(int numFailedPorts) {
        this.numFailedPorts = Utils.unsignedInt(numFailedPorts);
    }

    /**
     * @return the numSkippedNodes
     */
    public int getNumSkippedNodes() {
        return numSkippedNodes;
    }

    /**
     * @param numSkippedNodes
     *            the numSkippedNodes to set
     */
    public void setNumSkippedNodes(int numSkippedNodes) {
        this.numSkippedNodes = numSkippedNodes;
    }

    /**
     * @return the numSkippedPorts
     */
    public long getNumSkippedPorts() {
        return numSkippedPorts;
    }

    /**
     * @param numSkippedPorts
     *            the numSkippedPorts to set
     */
    public void setNumSkippedPorts(long numSkippedPorts) {
        this.numSkippedPorts = numSkippedPorts;
    }

    /**
     * @param numSkippedPorts
     *            the numSkippedPorts to set
     */
    public void setNumSkippedPorts(int numSkippedPorts) {
        this.numSkippedPorts = Utils.unsignedInt(numSkippedPorts);
    }

    /**
     * @return the numUnexpectedClearPorts
     */
    public long getNumUnexpectedClearPorts() {
        return numUnexpectedClearPorts;
    }

    /**
     * @param numUnexpectedClearPorts
     *            the numUnexpectedClearPorts to set
     */
    public void setNumUnexpectedClearPorts(long numUnexpectedClearPorts) {
        this.numUnexpectedClearPorts = numUnexpectedClearPorts;
    }

    /**
     * @param numUnexpectedClearPorts
     *            the numUnexpectedClearPorts to set
     */
    public void setNumUnexpectedClearPorts(int numUnexpectedClearPorts) {
        this.numUnexpectedClearPorts =
                Utils.unsignedInt(numUnexpectedClearPorts);
    }

    /**
     * @return the sMInfo
     */
    public SMInfoDataBean[] getSMInfo() {
        return SMInfo;
    }

    /**
     * @param sMInfo
     *            the sMInfo to set
     */
    public void setSMInfo(SMInfoDataBean[] sMInfo) {
        SMInfo = sMInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ITimestamped#getTimestamp()
     */
    @Override
    public long getTimestamp() {
        return sweepStart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.ITimestamped#getTimestampDate()
     */
    @Override
    public Date getTimestampDate() {
        return Utils.convertFromUnixTime(sweepStart);
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
        result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
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
        ImageInfoBean other = (ImageInfoBean) obj;
        if (imageId == null) {
            if (other.imageId != null) {
                return false;
            }
        } else if (!imageId.equals(other.imageId)) {
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
        return "ImageInfoBean [imageId=" + imageId + ", sweepStart="
                + sweepStart + ", sweepDuration=" + sweepDuration
                + ", numHCAPorts=" + numHFIPorts + ", numSwitchNodes="
                + numSwitchNodes + ", numSwitchPorts=" + numSwitchPorts
                + ", numLinks=" + numLinks + ", numSMs=" + numSMs
                + ", numFailedNodes=" + numFailedNodes + ", numFailedPorts="
                + numFailedPorts + ", numSkippedNodes=" + numSkippedNodes
                + ", numSkippedPorts=" + numSkippedPorts
                + ", numUnexpectedClearPorts=" + numUnexpectedClearPorts
                + ", SMInfo=" + Arrays.toString(SMInfo) + "]";
    }

    public boolean hasChange(ImageInfoBean other) {
        if (this == other) {
            return false;
        }
        if (other == null) {
            return true;
        }
        if (numFailedNodes != other.numFailedNodes) {
            return true;
        }
        if (numFailedPorts != other.numFailedPorts) {
            return true;
        }
        if (numHFIPorts != other.numHFIPorts) {
            return true;
        }
        if (numLinks != other.numLinks) {
            return true;
        }
        if (numSMs != other.numSMs) {
            return true;
        }
        if (numSkippedNodes != other.numSkippedNodes) {
            return true;
        }
        if (numSkippedPorts != other.numSkippedPorts) {
            return true;
        }
        if (numSwitchNodes != other.numSwitchNodes) {
            return true;
        }
        if (numSwitchPorts != other.numSwitchPorts) {
            return true;
        }
        if (numUnexpectedClearPorts != other.numUnexpectedClearPorts) {
            return true;
        }
        return false;
    }

}
