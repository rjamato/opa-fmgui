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
 *  File Name: ISubnetApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.20.2.1  2015/08/12 15:21:42  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
