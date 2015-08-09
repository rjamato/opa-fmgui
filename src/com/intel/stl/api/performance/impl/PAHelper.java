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
 *  File Name: PAHelper.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2015/04/09 03:29:24  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/27 20:35:44  fernande
 *  Archive Log:    Changed to use the interface IConnection instead of the concrete implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/02 15:28:05  jypak
 *  Archive Log:    History query has been done with current live image ID '0' which isn't correct. Updates here are:
 *  Archive Log:    1. Get the image ID from current image.
 *  Archive Log:    2. History queries are done with this image ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/12 19:31:26  jijunwan
 *  Archive Log:    1) improvement on get imagestamp from image info
 *  Archive Log:    2) fixed an mistake on group info history query
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/04 21:38:00  jijunwan
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

package com.intel.stl.api.performance.impl;

import java.util.List;

import com.intel.stl.api.performance.FocusPortsRspBean;
import com.intel.stl.api.performance.GroupConfigRspBean;
import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.GroupListBean;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFConfigRspBean;
import com.intel.stl.api.performance.VFFocusPortsRspBean;
import com.intel.stl.api.performance.VFInfoBean;
import com.intel.stl.api.performance.VFListBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.api.subnet.Selection;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.impl.FEHelper;
import com.intel.stl.fecdriver.messages.command.InputFocus;
import com.intel.stl.fecdriver.messages.command.InputGroupName;
import com.intel.stl.fecdriver.messages.command.InputImageId;
import com.intel.stl.fecdriver.messages.command.InputLidPortNumber;
import com.intel.stl.fecdriver.messages.command.InputVFName;
import com.intel.stl.fecdriver.messages.command.InputVFNameFocus;
import com.intel.stl.fecdriver.messages.command.InputVFNamePort;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetFocusPort;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetGroupConfig;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetGroupInfo;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetGroupList;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetImageInfo;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetPMConfig;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetPortCounters;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetVFConfig;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetVFFocusPort;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetVFInfo;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetVFList;
import com.intel.stl.fecdriver.messages.command.pa.FVCmdGetVFPortCounters;

/**
 * @author jijunwan
 * 
 */
public class PAHelper extends FEHelper {
    public PAHelper(IConnection conn) {
        super(conn);
    }

    public SubnetDescription getSubnetDescription() {
        return this.conn.getConnectionDescription();
    }

    public ImageInfoBean getImageInfo(ImageIdBean imageId) throws Exception {
        return getImageInfo(imageId.getImageNumber(), imageId.getImageOffset());
    }

    public ImageInfoBean getImageInfo(long imageNumber, int imageOffset)
            throws Exception {
        FVCmdGetImageInfo cmd =
                new FVCmdGetImageInfo(
                        new InputImageId(imageNumber, imageOffset));
        ImageInfoBean res = getSingleResponse(cmd);
        return res;
    }

    public List<GroupListBean> getGroupList() throws Exception {
        FVCmdGetGroupList cmd = new FVCmdGetGroupList();
        List<GroupListBean> res = getResponses(cmd);
        return res;
    }

    public GroupInfoBean getGroupInfo(String name) throws Exception {
        FVCmdGetGroupInfo cmd = new FVCmdGetGroupInfo(new InputGroupName(name));
        GroupInfoBean res = getSingleResponse(cmd);
        return res;
    }

    public GroupInfoBean getGroupInfoHistory(String name, long imageID,
            int offset) throws Exception {
        FVCmdGetGroupInfo cmd =
                new FVCmdGetGroupInfo(new InputGroupName(name, imageID, offset));
        GroupInfoBean res = getSingleResponse(cmd);
        return res;
    }

    public List<GroupConfigRspBean> getGroupConfig(String name)
            throws Exception {
        FVCmdGetGroupConfig cmd =
                new FVCmdGetGroupConfig(new InputGroupName(name));
        List<GroupConfigRspBean> res = getResponses(cmd);
        return res;
    }

    public List<FocusPortsRspBean> getFocusPort(String group,
            Selection seclection, int range) throws Exception {
        InputFocus input = new InputFocus(group, seclection);
        input.setRange(range);
        FVCmdGetFocusPort cmd = new FVCmdGetFocusPort(input);
        List<FocusPortsRspBean> res = getResponses(cmd);
        return res;
    }

    public PortCountersBean getPortCounter(int lid, short portNum)
            throws Exception {
        FVCmdGetPortCounters cmd =
                new FVCmdGetPortCounters(new InputLidPortNumber(lid,
                        (byte) portNum));
        PortCountersBean res = getSingleResponse(cmd);
        return res;
    }

    public PortCountersBean getPortCounterHistory(int lid, short portNum,
            long imageID, int imageOffset) throws Exception {
        FVCmdGetPortCounters cmd =
                new FVCmdGetPortCounters(new InputLidPortNumber(lid,
                        (byte) portNum, imageID, imageOffset));
        PortCountersBean res = getSingleResponse(cmd);
        return res;
    }

    public List<VFListBean> getVFList() throws Exception {
        FVCmdGetVFList cmd = new FVCmdGetVFList();
        List<VFListBean> res = getResponses(cmd);
        return res;
    }

    /**
     * Description:
     * 
     * @param name
     * @return
     */
    public VFInfoBean getVFInfo(String name) throws Exception {
        FVCmdGetVFInfo cmd = new FVCmdGetVFInfo(new InputVFName(name));
        VFInfoBean res = getSingleResponse(cmd);
        return res;
    }

    public VFInfoBean getVFInfoHistory(String name, long imageID,
            int imageOffset) throws Exception {
        FVCmdGetVFInfo cmd =
                new FVCmdGetVFInfo(new InputVFName(name, imageID, imageOffset));
        VFInfoBean res = getSingleResponse(cmd);
        return res;
    }

    /**
     * Description:
     * 
     * @param name
     * @return
     */
    public List<VFConfigRspBean> getVFConfig(String name) throws Exception {
        FVCmdGetVFConfig cmd = new FVCmdGetVFConfig(new InputVFName(name));
        List<VFConfigRspBean> res = getResponses(cmd);
        return res;
    }

    /**
     * Description:
     * 
     * @param vfName
     * @param selection
     * @param n
     * @return
     */
    public List<VFFocusPortsRspBean> getVFFocusPort(String vfName,
            Selection selection, int n) throws Exception {
        InputVFNameFocus input = new InputVFNameFocus(vfName, selection);
        input.setRange(n);
        FVCmdGetVFFocusPort cmd = new FVCmdGetVFFocusPort(input);
        List<VFFocusPortsRspBean> res = getResponses(cmd);
        return res;
    }

    /**
     * Description:
     * 
     * @param lid
     * @param portNum
     * @return
     */
    public VFPortCountersBean getVFPortCounter(String vfName, int lid,
            short portNum) throws Exception {
        FVCmdGetVFPortCounters cmd =
                new FVCmdGetVFPortCounters(new InputVFNamePort(vfName, lid,
                        (byte) portNum));
        VFPortCountersBean res = getSingleResponse(cmd);
        return res;
    }

    public VFPortCountersBean getVFPortCounterHistory(String vfName, int lid,
            short portNum, long imageID, int imageOffset) throws Exception {
        FVCmdGetVFPortCounters cmd =
                new FVCmdGetVFPortCounters(new InputVFNamePort(vfName, lid,
                        (byte) portNum, imageID, imageOffset));
        VFPortCountersBean res = getSingleResponse(cmd);
        return res;
    }

    public PMConfigBean getPMConfig() throws Exception {
        FVCmdGetPMConfig cmd = new FVCmdGetPMConfig();
        return getSingleResponse(cmd);
    }
}
