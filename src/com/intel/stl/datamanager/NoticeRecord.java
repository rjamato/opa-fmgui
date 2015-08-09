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
 *  File Name: NoticeRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/02/06 15:03:04  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/24 18:50:22  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/30 15:37:20  fernande
 *  Archive Log:    Changed hashCode methods to use generated code by Eclipse
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/18 14:18:34  jypak
 *  Archive Log:    1. When  shutdown notice manager, remove it from the listener list of the STLConnection so that the blocking queue doesn't fill up.
 *  Archive Log:    2. Removed unncessary print out statements.
 *  Archive Log:    3. Port cache now has null check for memory cache.
 *  Archive Log:    4. For FE errors, still process the notice and set the NoticeStatus to FEERROR.
 *  Archive Log:    5. Junit updates for NoticeStatus.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/15 15:15:39  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/28 14:56:52  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/27 17:04:40  fernande
 *  Archive Log:    Database changes to add Notice and ImageInfo tables to the schema database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 21:39:58  fernande
 *  Archive Log:    Adding support for saving notices and imageinfos to the database
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.intel.stl.api.notice.NoticeAttrBean;
import com.intel.stl.api.notice.NoticeBean;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "noticeType",
        discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "NOTICES", indexes = { @Index(name = "IDX_NOTICE_STATUS",
        columnList = "noticeStatus") })
public abstract class NoticeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private NoticeId id = new NoticeId();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subnetId", insertable = false, updatable = false)
    private SubnetRecord subnet;

    @Column(name = "noticeType", insertable = false, updatable = false)
    private int noticeType;

    @Column(length = 32)
    @Enumerated(STRING)
    private NoticeStatus noticeStatus;

    @Embedded
    private NoticeBean notice;

    public NoticeRecord() {
    }

    public NoticeRecord(NoticeBean notice) {
        setNoticeFields(notice);
        this.notice = notice;
    }

    public NoticeId getId() {
        return id;
    }

    public void setId(NoticeId id) {
        this.id = id;
    }

    public SubnetRecord getSubnet() {
        return subnet;
    }

    public void setSubnet(SubnetRecord subnet) {
        this.id.setFabricId(subnet.getId());
        this.subnet = subnet;
    }

    /**
     * 
     * Description: returns the notice discriminator value.
     * 
     * @return either 1 (Generic) or 2 (Vendor) - See specific concrete classes
     *         extending this abstract class
     */
    public int getNoticeType() {
        return noticeType;
    }

    /**
     * 
     * Description: sets the notice type. Please note that this value is set
     * internally by Hibernate according to the concrete implementation being
     * saved, so there is no point on setting it.
     * 
     * @param noticeType
     */
    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * @return the noticeStatus
     */
    public NoticeStatus getNoticeStatus() {
        return noticeStatus;
    }

    /**
     * @param noticeStatus
     *            the noticeStatus to set
     */
    public void setNoticeStatus(NoticeStatus noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public NoticeBean getNotice() {
        NoticeAttrBean attributes = getNoticeAttributes();
        // The following fields were transient, so, need to
        // be filled with NoticeRecord info.
        notice.setAttributes(attributes);
        notice.setId(id.getNoticeId());
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        setNoticeFields(notice);
        this.notice = notice;
    }

    public abstract NoticeAttrBean getNoticeAttributes();

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
        NoticeRecord other = (NoticeRecord) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    private void setNoticeFields(NoticeBean notice) {
        NoticeAttrBean attributes = notice.getAttributes();
        if (attributes == null) {
            // TODO Create message for this
            throw new IllegalArgumentException(
                    "No NoticeAttrBean attached to NoticeBean");
        }
        this.id.setNoticeId(notice.getId());
    }
}
