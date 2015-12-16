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
 *  File Name: NoticeBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/09/10 15:28:05  fernande
 *  Archive Log:    PR 129940 - Persisted notices have no time stamp data. Added timestamp to NoticeBean
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:48:43  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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

    private final long receiveTimestamp;

    public NoticeBean() {
        this(false);
    }

    public NoticeBean(boolean generateId) {
        this.receiveTimestamp = System.currentTimeMillis();
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

    /**
     * @return the receive timestamp
     */
    public long getReceiveTimestamp() {
        return receiveTimestamp;
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
