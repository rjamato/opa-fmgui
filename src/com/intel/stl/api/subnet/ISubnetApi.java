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
 *  Archive Log:    Revision 1.25  2015/09/26 06:17:07  jijunwan
 *  Archive Log:    130487 - FM GUI: Topology refresh required after enabling Fabric Simulator
 *  Archive Log:    - added reset to clear all caches and update DB topology
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/09/25 20:47:18  fernande
 *  Archive Log:    PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/08/17 18:48:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/08/11 17:37:21  jijunwan
 *  Archive Log:    PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log:    - improved to get distribution data with argument "refresh". When it's true, calculate distribution rather than get it from cache
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/05/26 15:35:11  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
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

public interface ISubnetApi {
    SubnetDescription getConnectionDescription();

    // node related queries
    List<NodeRecordBean> getNodes(boolean includeInactive)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException;

    NodeRecordBean getNode(long portGuid) throws SubnetDataNotFoundException;

    EnumMap<NodeType, Integer> getNodesTypeDist(boolean includeInactive,
            boolean refresh) throws SubnetDataNotFoundException;

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

    EnumMap<NodeType, Long> getPortsTypeDist(boolean countInternalMgrPort,
            boolean refresh) throws SubnetDataNotFoundException;

    // switch related queries
    List<SwitchRecordBean> getSwitches();

    SwitchRecordBean getSwitch(int lid);

    // LFT related queries
    List<LFTRecordBean> getLFTs();

    List<LFTRecordBean> getLFT(int lid);

    // MFT related queries
    List<MFTRecordBean> getMFTs();

    List<MFTRecordBean> getMFT(int lid);

    // Cable related queries
    List<CableRecordBean> getCables();

    List<CableRecordBean> getCable(int lid);

    List<CableRecordBean> getCable(int lid, short portNum);

    // P Key related queries
    List<P_KeyTableRecordBean> getPKeyTables();

    // VLArb related queries
    List<VLArbTableRecordBean> getVLArbTables();

    // SM related queries
    List<SMRecordBean> getSMs();

    SMRecordBean getSM(int lid);

    // Path/Trace related queries
    List<PathRecordBean> getPath(int lid);

    List<TraceRecordBean> getTrace(int sourceLid, int targetLid);

    List<SC2SLMTRecordBean> getSC2SLMTs();

    SC2SLMTRecordBean getSC2SLMT(int lid);

    List<SC2VLMTRecordBean> getSC2VLTMTs();

    List<SC2VLMTRecordBean> getSC2VLTMT(int lid);

    SC2VLMTRecordBean getSC2VLTMT(int lid, short portNum);

    List<SC2VLMTRecordBean> getSC2VLNTMTs();

    List<SC2VLMTRecordBean> getSC2VLNTMT(int lid);

    SC2VLMTRecordBean getSC2VLNTMT(int lid, short portNum);

    FabricInfoBean getFabricInfo();

    /**
     * 
     * <i>Description:</i> refresh all data from scratch
     * 
     */
    void reset();

    void cleanup();
}
