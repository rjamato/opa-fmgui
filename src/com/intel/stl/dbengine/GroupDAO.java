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
 *  File Name: SubnetDAO.java
 *
 *  Archive Source: $Source$
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.dbengine;

import java.util.List;

import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.GroupListBean;
import com.intel.stl.api.performance.PerformanceDataNotFoundException;
import com.intel.stl.api.performance.PortConfigBean;
import com.intel.stl.datamanager.GroupConfigId;
import com.intel.stl.datamanager.GroupConfigRecord;
import com.intel.stl.datamanager.SubnetRecord;

public interface GroupDAO {

    void saveGroupList(SubnetRecord subnet, List<GroupListBean> groupList);

    GroupConfigRecord saveGroupConfig(SubnetRecord subnet, String groupName,
            List<PortConfigBean> ports);

    List<PortConfigBean> getGroupConfig(GroupConfigId configId)
            throws PerformanceDataNotFoundException;

    List<PortConfigBean> getPortConfig(SubnetRecord subnet)
            throws PerformanceDataNotFoundException;

    void saveGroupInfos(SubnetRecord subnet, List<GroupInfoBean> groupInfoBeans);

    List<GroupInfoBean> getGroupInfoList(SubnetRecord subnet, String groupName,
            long startTime, long stopTime)
            throws PerformanceDataNotFoundException;

}
