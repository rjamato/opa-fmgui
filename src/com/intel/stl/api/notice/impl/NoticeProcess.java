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
 *  File Name: NoticeProcess.java
 *  
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/06 20:44:02  jypak
 *  Archive Log:    Header comments fixed for archive log to be updated.
 *  Archive Log:
 *
 *  Overview: Notice wrapper class.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.notice.impl;

import java.io.Serializable;
import java.util.List;

import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.TrapType;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.PortRecordBean;

public class NoticeProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lid;

    private TrapType trapType;

    private NodeRecordBean node;

    private List<PortRecordBean> ports;

    private List<LinkRecordBean> links;

    private final NoticeBean notice;

    public NoticeProcess(NoticeBean notice) {
        this.notice = notice;
    }

    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /**
     * @return the node
     */
    public NodeRecordBean getNode() {
        return node;
    }

    /**
     * @return the ports
     */
    public List<PortRecordBean> getPorts() {
        return ports;
    }

    /**
     * @return the links
     */
    public List<LinkRecordBean> getLinks() {
        return links;
    }

    /**
     * @return the notice
     */
    public NoticeBean getNotice() {
        return notice;
    }

    /**
     * @param lid
     *            the lid to set
     */
    public void setLid(int lid) {
        this.lid = lid;
    }

    /**
     * @return the trap type
     */
    public TrapType getTrapType() {
        return trapType;
    }

    /**
     * @param trapType
     *            the trap type to set
     */
    public void setTrapType(TrapType trapType) {
        this.trapType = trapType;
    }

    /**
     * @param node
     *            the node to set
     */
    public void setNode(NodeRecordBean node) {
        this.node = node;
    }

    /**
     * @param ports
     *            the ports to set
     */
    public void setPorts(List<PortRecordBean> ports) {
        this.ports = ports;
    }

    /**
     * @param links
     *            the links to set
     */
    public void setLinks(List<LinkRecordBean> links) {
        this.links = links;
    }

}
