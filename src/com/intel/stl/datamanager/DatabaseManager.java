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

package com.intel.stl.datamanager;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import com.intel.stl.api.configuration.AppInfo;
import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.impl.NoticeProcess;
import com.intel.stl.api.performance.GroupConfigRspBean;
import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.GroupListBean;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.PerformanceDataNotFoundException;
import com.intel.stl.api.performance.PortConfigBean;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;

public interface DatabaseManager {

    AppInfo getAppInfo();

    void saveAppProperties(Map<String, Properties> appProperties);

    List<SubnetDescription> getSubnets();

    SubnetDescription getSubnet(String subnetName);

    SubnetDescription getSubnet(long subnetId);

    SubnetDescription defineSubnet(SubnetDescription subnet);

    void updateSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException;

    void removeSubnet(long subnetId) throws SubnetDataNotFoundException;

    void saveEventRules(List<EventRule> rules);

    List<EventRule> getEventRules();

    List<NodeRecordBean> getNodes(String subnetName)
            throws SubnetDataNotFoundException;

    List<LinkRecordBean> getLinks(String subnetName)
            throws SubnetDataNotFoundException;

    List<LinkRecordBean> getLinks(String subnetName, int lid)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNode(String subnetName, int lid)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNode(String subnetName, long nodeGUID)
            throws SubnetDataNotFoundException;

    /**
     * Description: returns a node by its port GUID
     * 
     * @param subnetName
     * @param portGuid
     * @return
     */
    NodeRecordBean getNodeByPortGUID(String subnetName, long portGuid)
            throws SubnetDataNotFoundException;

    void saveTopology(String subnetName, List<NodeRecordBean> nodes,
            List<LinkRecordBean> links) throws SubnetDataNotFoundException;

    long getTopologyId(String subnetName) throws SubnetDataNotFoundException;

    /**
     * Description: returns the node type distribution for the subnet
     * 
     * @param subnetName
     * @return enumeration map of NodeType
     */
    EnumMap<NodeType, Integer> getNodeTypeDist(String subnetName)
            throws SubnetDataNotFoundException;

    void close();

    LinkRecordBean getLinkBySource(String subnetName, int lid, short portNum)
            throws SubnetDataNotFoundException;

    LinkRecordBean getLinkByDestination(String subnetName, int lid,
            short portNum) throws SubnetDataNotFoundException;

    UserSettings getUserSettings(String subnetName, String userName)
            throws UserNotFoundException;

    void saveUserSettings(String subnetName, UserSettings userSettings);

    void saveGroupInfos(String subnetName, List<GroupInfoBean> groupInfos);

    void saveImageInfos(String subnetName, List<ImageInfoBean> imageInfos);

    void saveGroupConfig(String subnetName, String groupName,
            List<PortConfigBean> ports);

    void saveGroupList(String subnetName, List<GroupListBean> groupList);

    List<GroupInfoBean> getGroupInfo(String subnetName, String groupName,
            long startTime, long stopTime)
            throws PerformanceDataNotFoundException;

    List<GroupConfigRspBean> getGroupConfig(String subnetName, String groupName)
            throws PerformanceDataNotFoundException;

    List<PortConfigBean> getPortConfig(String subnetName)
            throws PerformanceDataNotFoundException;

    void saveNotices(String subnetName, NoticeBean[] notices);

    Future<Boolean> processNotices(String subnetName,
            List<NoticeProcess> notices);

    void resetNotices(String subnetName);

    void updateNotice(String subnetName, long noticeId,
            NoticeStatus noticeStatus);

    List<NoticeBean> getNotices(String subnetName, NoticeStatus status);

    List<NoticeBean> getNotices(String subnetName, NoticeStatus status,
            NoticeStatus newStatus);

}
