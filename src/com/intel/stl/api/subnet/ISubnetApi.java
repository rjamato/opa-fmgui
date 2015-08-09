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
 *  File Name: ISubnetApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.20  2015/04/21 17:45:53  jypak
 *  Archive Log:    Header comments fixed.
 *  Archive Log:
 *  
 *  Overview: 
 *  
 *  Reference: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.api.subnet;

import java.util.EnumMap;
import java.util.List;

import com.intel.stl.api.IErrorSupport;

public interface ISubnetApi extends IErrorSupport {
    SubnetDescription getConnectionDescription();

    // node related queries
    List<NodeRecordBean> getNodes(boolean includeInactive)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException;

    NodeRecordBean getNode(long portGuid) throws SubnetDataNotFoundException;

    EnumMap<NodeType, Integer> getNodesTypeDist(boolean includeInactive)
            throws SubnetDataNotFoundException;

    // link related queries
    List<LinkRecordBean> getLinks(boolean includeInactive)
            throws SubnetDataNotFoundException;

    LinkRecordBean getLinkBySource(int lid, short portNum)
            throws SubnetDataNotFoundException;

    LinkRecordBean getLinkByDestination(int lid, short portNum)
            throws SubnetDataNotFoundException;

    // port related queries
    List<PortRecordBean> getPorts() throws SubnetDataNotFoundException;

    PortRecordBean getPortByPortNum(int lid, short portNum)
            throws SubnetDataNotFoundException;

    PortRecordBean getPortByLocalPortNum(int lid, short localPortNum)
            throws SubnetDataNotFoundException;

    boolean hasPort(int lid, short portNum);

    boolean hasLocalPort(int lid, short localPortNum);

    EnumMap<NodeType, Long> getPortsTypeDist(boolean countInternalMgrPort)
            throws SubnetDataNotFoundException;

    // switch related queries
    List<SwitchRecordBean> getSwitches() throws SubnetException;

    SwitchRecordBean getSwitch(int lid) throws SubnetException;

    // LFT related queries
    List<LFTRecordBean> getLFTs() throws SubnetException;

    List<LFTRecordBean> getLFT(int lid) throws SubnetException;

    // MFT related queries
    List<MFTRecordBean> getMFTs() throws SubnetException;

    List<MFTRecordBean> getMFT(int lid) throws SubnetException;

    // Cable related queries
    List<CableRecordBean> getCables() throws SubnetException;

    List<CableRecordBean> getCable(int lid) throws SubnetException;

    List<CableRecordBean> getCable(int lid, short portNum)
            throws SubnetException;

    // P Key related queries
    List<P_KeyTableRecordBean> getPKeyTables() throws SubnetException;

    // VLArb related queries
    List<VLArbTableRecordBean> getVLArbTables() throws SubnetException;

    // SM related queries
    List<SMRecordBean> getSMs() throws SubnetException;

    SMRecordBean getSM(int lid) throws SubnetException;

    // Path/Trace related queries
    List<PathRecordBean> getPath(int lid) throws SubnetException;

    List<TraceRecordBean> getTrace(int sourceLid, int targetLid)
            throws SubnetException;

    List<SC2SLMTRecordBean> getSC2SLMTs() throws SubnetException;

    SC2SLMTRecordBean getSC2SLMT(int lid) throws SubnetException;

    List<SC2VLMTRecordBean> getSC2VLTMTs() throws SubnetException;

    List<SC2VLMTRecordBean> getSC2VLTMT(int lid) throws SubnetException;

    SC2VLMTRecordBean getSC2VLTMT(int lid, short portNum)
            throws SubnetException;

    List<SC2VLMTRecordBean> getSC2VLNTMTs() throws SubnetException;

    List<SC2VLMTRecordBean> getSC2VLNTMT(int lid) throws SubnetException;

    SC2VLMTRecordBean getSC2VLNTMT(int lid, short portNum)
            throws SubnetException;

    void cleanup();
}
