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
package com.intel.stl.api.subnet.impl;

import java.util.List;

import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.LFTRecordBean;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.MFTRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.P_KeyTableRecordBean;
import com.intel.stl.api.subnet.PathRecordBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SC2SLMTRecordBean;
import com.intel.stl.api.subnet.SC2VLMTRecordBean;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.api.subnet.TraceRecordBean;
import com.intel.stl.api.subnet.VLArbTableRecordBean;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.impl.FEHelper;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;
import com.intel.stl.fecdriver.messages.command.InputGidPair;
import com.intel.stl.fecdriver.messages.command.InputLid;
import com.intel.stl.fecdriver.messages.command.InputPortGid;
import com.intel.stl.fecdriver.messages.command.InputPortGuid;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetCable;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetLFT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetLink;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetMFT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetNode;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetPKeyTable;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetPath;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetPortInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2SLMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2VLMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2VLNTMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2VLTMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSMInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSwitchInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetTrace;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetVLArb;

/**
 * @author jijunwan
 * 
 */
public class SAHelper extends FEHelper {
    public SAHelper(IConnection conn) {
        super(conn);
    }

    public SubnetDescription getSubnetDescription() {
        return this.conn.getConnectionDescription();
    }

    public List<NodeRecordBean> getNodes() throws Exception {
        FVCmdGetNode cmd = new FVCmdGetNode();
        List<NodeRecordBean> allNodes = getResponses(cmd);
        return allNodes;
    }

    public NodeRecordBean getNode(int lid) throws Exception {
        FVCmdGetNode cmd = new FVCmdGetNode(new InputLid(lid));
        NodeRecordBean node = getSingleResponse(cmd);
        return node;
    }

    public NodeRecordBean getNode(long portGuid) throws Exception {
        FVCmdGetNode cmd = new FVCmdGetNode(new InputPortGuid(portGuid));
        NodeRecordBean node = getSingleResponse(cmd);
        return node;
    }

    public List<LinkRecordBean> getLinks() throws Exception {
        FVCmdGetLink cmd = new FVCmdGetLink();
        List<LinkRecordBean> allLinks = getResponses(cmd);
        return allLinks;
    }

    public List<LinkRecordBean> getLinks(int lid) throws Exception {
        FVCmdGetLink cmd = new FVCmdGetLink(new InputLid(lid));
        List<LinkRecordBean> allLinks = getResponses(cmd);
        return allLinks;
    }

    public List<PortRecordBean> getPorts() throws Exception {
        FVCmdGetPortInfo cmd = new FVCmdGetPortInfo();
        List<PortRecordBean> allPorts = getResponses(cmd);
        return allPorts;
    }

    public List<PortRecordBean> getPorts(int lid) throws Exception {
        FVCmdGetPortInfo cmd = new FVCmdGetPortInfo(new InputLid(lid));
        List<PortRecordBean> allPorts = getResponses(cmd);
        return allPorts;
    }

    public List<SwitchRecordBean> getSwitches() throws Exception {
        FVCmdGetSwitchInfo cmd = new FVCmdGetSwitchInfo();
        List<SwitchRecordBean> allSwitchs = getResponses(cmd);
        return allSwitchs;
    }

    public SwitchRecordBean getSwitch(int lid) throws Exception {
        FVCmdGetSwitchInfo cmd = new FVCmdGetSwitchInfo(new InputLid(lid));
        SwitchRecordBean sw = getSingleResponse(cmd);
        return sw;
    }

    public List<LFTRecordBean> getLFTs() throws Exception {
        FVCmdGetLFT cmd = new FVCmdGetLFT();
        List<LFTRecordBean> allLFTs = getResponses(cmd);
        return allLFTs;
    }

    public List<LFTRecordBean> getLFTs(int lid) throws Exception {
        FVCmdGetLFT cmd = new FVCmdGetLFT(new InputLid(lid));
        List<LFTRecordBean> allLFTs = getResponses(cmd);
        return allLFTs;
    }

    public List<MFTRecordBean> getMFTs() throws Exception {
        FVCmdGetMFT cmd = new FVCmdGetMFT();
        List<MFTRecordBean> allMFTs = getResponses(cmd);
        return allMFTs;
    }

    public List<MFTRecordBean> getMFTs(int lid) throws Exception {
        FVCmdGetMFT cmd = new FVCmdGetMFT(new InputLid(lid));
        List<MFTRecordBean> allMFTs = getResponses(cmd);
        return allMFTs;
    }

    public List<CableRecordBean> getCables() throws Exception {
        FVCmdGetCable cmd = new FVCmdGetCable();
        List<CableRecordBean> allCables = getResponses(cmd);
        return allCables;
    }

    public List<CableRecordBean> getCables(int lid) throws Exception {
        FVCmdGetCable cmd = new FVCmdGetCable(new InputLid(lid));
        List<CableRecordBean> allCables = getResponses(cmd);
        return allCables;
    }

    public List<SC2SLMTRecordBean> getSC2SLMTs() throws Exception {
        FVCmdGetSC2SLMT cmd = new FVCmdGetSC2SLMT();
        List<SC2SLMTRecordBean> allSC2SLs = getResponses(cmd);
        return allSC2SLs;
    }

    public SC2SLMTRecordBean getSC2SLMT(int lid) throws Exception {
        FVCmdGetSC2SLMT cmd = new FVCmdGetSC2SLMT(new InputLid(lid));
        SC2SLMTRecordBean sc2sl = getSingleResponse(cmd);
        return sc2sl;
    }

    public List<SC2VLMTRecordBean> getSC2VLTMTs() throws Exception {
        FVCmdGetSC2VLTMT cmd = new FVCmdGetSC2VLTMT();
        List<SC2VLMTRecordBean> allSC2VLTs = getResponses(cmd);
        return allSC2VLTs;
    }

    public List<SC2VLMTRecordBean> getSC2VLTMT(int lid) throws Exception {
        FVCmdGetSC2VLTMT cmd = new FVCmdGetSC2VLTMT(new InputLid(lid));
        List<SC2VLMTRecordBean> sc2vlts = getResponses(cmd);
        return sc2vlts;
    }

    public List<SC2VLMTRecordBean> getSC2VLNTMTs() throws Exception {
        FVCmdGetSC2VLNTMT cmd = new FVCmdGetSC2VLNTMT();
        List<SC2VLMTRecordBean> allSC2VLNTs = getResponses(cmd);
        return allSC2VLNTs;
    }

    public List<SC2VLMTRecordBean> getSC2VLNTMT(int lid) throws Exception {
        FVCmdGetSC2VLMT cmd = new FVCmdGetSC2VLNTMT(new InputLid(lid));
        List<SC2VLMTRecordBean> sc2vlnts = getResponses(cmd);
        return sc2vlnts;
    }

    public List<P_KeyTableRecordBean> getPKeyTables() throws Exception {
        FVCmdGetPKeyTable cmd = new FVCmdGetPKeyTable();
        List<P_KeyTableRecordBean> allPKTs = getResponses(cmd);
        return allPKTs;
    }

    public List<VLArbTableRecordBean> getVLArbTables() throws Exception {
        FVCmdGetVLArb cmd = new FVCmdGetVLArb();
        List<VLArbTableRecordBean> allVLArbs = getResponses(cmd);
        return allVLArbs;
    }

    public List<SMRecordBean> getSMs() throws Exception {
        FVCmdGetSMInfo cmd = new FVCmdGetSMInfo();
        List<SMRecordBean> allSMs = getResponses(cmd);
        return allSMs;
    }

    public List<PathRecordBean> getPath(GID.Global gid) throws Exception {
        FVCmdGetPath cmd = new FVCmdGetPath(new InputPortGid(gid));
        List<PathRecordBean> pathes = getResponses(cmd);
        return pathes;
    }

    public List<TraceRecordBean> getTrace(GID.Global source, GID.Global target)
            throws Exception {
        FVCmdGetTrace cmd = new FVCmdGetTrace(new InputGidPair(source, target));
        List<TraceRecordBean> traces = getResponses(cmd);
        return traces;
    }
}
