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
 *  File Name: NoticeBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/10/24 18:47:34  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/22 21:37:44  fernande
 *  Archive Log:    Adding support for saving notices and imageinfos to the database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/12 20:07:42  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:16:47  jijunwan
 *  Archive Log:    moved sharable class to com.intel.stl.api
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 15:07:33  jijunwan
 *  Archive Log:    added support to FE notice
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import java.io.Serializable;
import java.util.Arrays;

import com.intel.stl.api.IdGenerator;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.GIDGlobal;

public class NoticeBean implements Serializable {
    private static final long serialVersionUID = -8870079256941040218L;

    private long id;

    private NoticeAttrBean attributes;

    private boolean toggle;

    private short noticeCount;

    private int issuerLID;

    private GIDGlobal issuerGID;

    private byte[] data;

    private byte[] classData;

    public NoticeBean() {
        this(false);
    }

    public NoticeBean(boolean generateId) {
        super();
        if (generateId) {
            id = IdGenerator.id();
        }
    }

    /**
     * @return the attributes
     */
    public NoticeAttrBean getAttributes() {
        return attributes;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(NoticeAttrBean attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the toggle
     */
    public boolean isToggle() {
        return toggle;
    }

    /**
     * @param toggle
     *            the toggle to set
     */
    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    /**
     * @return the count
     */
    public short getNoticeCount() {
        return noticeCount;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setNoticeCount(short count) {
        this.noticeCount = count;
    }

    /**
     * @return the issuerLID
     */
    public int getIssuerLID() {
        return issuerLID;
    }

    /**
     * @param issuerLID
     *            the issuerLID to set
     */
    public void setIssuerLID(int issuerLID) {
        this.issuerLID = issuerLID;
    }

    /**
     * @return the issuerGID
     */
    public GIDGlobal getIssuerGID() {
        return issuerGID;
    }

    /**
     * @param issuerGID
     *            the issuerGID to set
     */
    public void setIssuerGID(GIDGlobal issuerGID) {
        this.issuerGID = issuerGID;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the classData
     */
    public byte[] getClassData() {
        return classData;
    }

    /**
     * @param classData
     *            the classData to set
     */
    public void setClassData(byte[] classData) {
        this.classData = classData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NoticeBean [id=" + id + ", attributes=" + attributes
                + ", toggle=" + toggle + ", count=" + noticeCount
                + ", issuerLID=" + StringUtils.intHexString(issuerLID)
                + ", issuerGID=" + issuerGID + ", data="
                + Arrays.toString(data) + ", classData="
                + Arrays.toString(classData) + "]";
    }
}
