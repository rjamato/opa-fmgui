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
 *  File Name: GenericNoticeRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/09/15 15:15:39  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.NoticeAttrBean;

@Entity
@DiscriminatorValue("1")
public class GenericNoticeRecord extends NoticeRecord {

    private static final long serialVersionUID = 1L;

    private GenericNoticeAttrBean genericNoticeAttr;

    public GenericNoticeAttrBean getGenericNoticeAttr() {
        return genericNoticeAttr;
    }

    public void setGenericNoticeAttr(GenericNoticeAttrBean genericNoticeAttr) {
        this.genericNoticeAttr = genericNoticeAttr;
    }

    @Override
    public NoticeAttrBean getNoticeAttributes() {
        // isGeneric transient field should be set here.
        genericNoticeAttr.setGeneric(true);
        return genericNoticeAttr;
    }

}
