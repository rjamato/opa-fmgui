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
 *  File Name: ImageInfoRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/06 15:03:04  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/12 19:58:38  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 15:37:20  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/27 17:04:20  fernande
 *  Archive Log:    Database changes to add Notice and ImageInfo tables to the schema database
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.intel.stl.api.performance.ImageInfoBean;

/**
 * ImageIdBean contains a field called imageNumber; this is a number that goes
 * up as the FM sweeps the fabric, but it's not unique, it restarts when the FM
 * is restarted. So we use sweepStart as the id for ImageInfoRecord (it's a
 * timestamp); however, we will need an index to access by image number, which
 * won't be unique. When querying by this index you might get more than one
 * ImageInfoRecord.
 */
@Entity
@Table(name = "IMAGE_INFOS", indexes = { @Index(name = "IDX_IMAGE_NUM",
        columnList = "imageNumber", unique = false) })
public class ImageInfoRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ImageInfoId id = new ImageInfoId();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subnetId", insertable = false, updatable = false)
    private SubnetRecord subnet;

    @Embedded
    private ImageInfoBean imageInfo;

    public ImageInfoRecord() {
    }

    public ImageInfoRecord(long subnetId, ImageInfoBean imageInfo) {
        this.id.setFabricId(subnetId);
        setImageInfoFields(imageInfo);
        this.imageInfo = imageInfo;
    }

    public ImageInfoId getId() {
        return id;
    }

    public void setId(ImageInfoId id) {
        this.id = id;
    }

    public SubnetRecord getSubnet() {
        return subnet;
    }

    public void setSubnet(SubnetRecord subnet) {
        this.subnet = subnet;
    }

    public ImageInfoBean getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfoBean imageInfo) {
        setImageInfoFields(imageInfo);
        this.imageInfo = imageInfo;
    }

    private void setImageInfoFields(ImageInfoBean imageInfo) {
        id.setSweepTimestamp(imageInfo.getSweepStart());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImageInfoRecord other = (ImageInfoRecord) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
