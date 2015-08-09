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
 *  File Name: IPerformanceApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.20  2015/04/09 03:29:21  jijunwan
 *  Archive Log:    updated to match FM 390
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/03/02 15:28:04  jypak
 *  Archive Log:    History query has been done with current live image ID '0' which isn't correct. Updates here are:
 *  Archive Log:    1. Get the image ID from current image.
 *  Archive Log:    2. History queries are done with this image ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/09 21:29:53  jijunwan
 *  Archive Log:    added clean up to APIs that intend to close STLConections
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/04 21:37:53  jijunwan
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

import java.util.List;

import com.intel.stl.api.IErrorSupport;
import com.intel.stl.api.IRandomable;
import com.intel.stl.api.subnet.Selection;
import com.intel.stl.api.subnet.SubnetDescription;

/**
 * @author jijunwan
 * 
 */
public interface IPerformanceApi extends IRandomable, IErrorSupport {
    SubnetDescription getConnectionDescription();

    ImageInfoBean getImageInfo(long imageNumber, int imageOffset);

    ImageInfoBean getLatestImageInfo();

    List<GroupListBean> getGroupList();

    GroupInfoBean getGroupInfo(String name);

    GroupInfoBean getGroupInfoHistory(String name, long imageID, int imageOffset);

    List<GroupConfigRspBean> getGroupConfig(String name);

    List<FocusPortsRspBean> getFocusPorts(String groupName,
            Selection selection, int n);

    List<FocusPortsRspBean> getTopBandwidth(String groupName, int n);

    List<FocusPortsRspBean> getTopCongestion(String groupName, int n);

    List<String> getDeviceGroup(int lid);

    PortCountersBean getPortCounters(int lid, short portNum);

    PortCountersBean getPortCountersHistory(int lid, short portNum,
            long imageID, int imageOffset);

    List<VFListBean> getVFList();

    VFInfoBean getVFInfo(String name);

    VFInfoBean getVFInfoHistory(String name, long imageID, int imageOffset);

    List<VFConfigRspBean> getVFConfig(String name);

    List<String> getVFNames(int lid);

    List<VFFocusPortsRspBean> getVFFocusPorts(String vfName,
            Selection selection, int n);

    VFPortCountersBean getVFPortCounters(String vfName, int lid, short portNum);

    VFPortCountersBean getVFPortCountersHistory(String vfName, int lid,
            short portNum, long imageID, int imageOffset);

    void saveGroupConfig(String groupName, List<PortConfigBean> ports);

    List<GroupConfigRspBean> getGroupConfig(String subnetName, String groupName)
            throws PerformanceDataNotFoundException;

    List<PortConfigBean> getPortConfig(String subnetName)
            throws PerformanceDataNotFoundException;

    List<GroupInfoBean> getGroupInfo(String subnetName, String groupName,
            long startTime, long stopTime)
            throws PerformanceDataNotFoundException;

    PMConfigBean getPMConfig();

    void cleanup();
}
