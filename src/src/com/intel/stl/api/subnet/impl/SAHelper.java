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
 *  File Name: SAHelper.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.12  2015/09/26 06:17:57  jijunwan
 *  Archive Log: 130487 - FM GUI: Topology refresh required after enabling Fabric Simulator
 *  Archive Log: - added more log info
 *  Archive Log:
 *  Archive Log: Revision 1.11  2015/09/25 20:47:43  fernande
 *  Archive Log: PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log: Revision 1.10  2015/08/17 18:48:53  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.9  2015/06/10 19:36:39  jijunwan
 *  Archive Log: PR 129153 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: jijunwan
 * 
 ******************************************************************************/
package com.intel.stl.api.subnet.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.FabricInfoBean;
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
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.api.subnet.TraceRecordBean;
import com.intel.stl.api.subnet.VLArbTableRecordBean;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.impl.FEHelper;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;
import com.intel.stl.fecdriver.messages.command.InputGidPair;
import com.intel.stl.fecdriver.messages.command.InputLid;
import com.intel.stl.fecdriver.messages.command.InputPortGid;
import com.intel.stl.fecdriver.messages.command.InputPortGuid;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetCable;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetFabricInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetLFT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetLink;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetMFT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetNode;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetNodes;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetPKeyTable;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetPath;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetPortInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2SLMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2SLMTs;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2VLNTMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSC2VLTMT;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSMInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSwitchInfo;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetSwitches;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetTrace;
import com.intel.stl.fecdriver.messages.command.sa.FVCmdGetVLArb;

/**
 * @author jijunwan
 * 
 */
public class SAHelper extends FEHelper {
    private final static Logger log = LoggerFactory.getLogger(SAHelper.class);

    public SAHelper(IStatement statement) {
        super(statement);
    }

    public List<NodeRecordBean> getNodes() throws Exception {
        FVCmdGetNodes cmd = new FVCmdGetNodes();
        List<NodeRecordBean> res = statement.execute(cmd);
        log.info("Get " + res.size() + " nodes from FE");
        return res;
    }

    public NodeRecordBean getNode(int lid) throws Exception {
        FVCmdGetNode cmd = new FVCmdGetNode(new InputLid(lid));
        return statement.execute(cmd);
    }

    public NodeRecordBean getNode(long portGuid) throws Exception {
        FVCmdGetNode cmd = new FVCmdGetNode(new InputPortGuid(portGuid));
        return statement.execute(cmd);
    }

    public List<LinkRecordBean> getLinks() throws Exception {
        FVCmdGetLink cmd = new FVCmdGetLink();
        List<LinkRecordBean> res = statement.execute(cmd);
        log.info("Get " + res.size() + " links from FE");
        return res;
    }

    public List<LinkRecordBean> getLinks(int lid) throws Exception {
        FVCmdGetLink cmd = new FVCmdGetLink(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<PortRecordBean> getPorts() throws Exception {
        FVCmdGetPortInfo cmd = new FVCmdGetPortInfo();
        List<PortRecordBean> res = statement.execute(cmd);
        log.info("Get " + res.size() + " ports from FE");
        return res;
    }

    public List<PortRecordBean> getPorts(int lid) throws Exception {
        FVCmdGetPortInfo cmd = new FVCmdGetPortInfo(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<SwitchRecordBean> getSwitches() throws Exception {
        FVCmdGetSwitches cmd = new FVCmdGetSwitches();
        return statement.execute(cmd);
    }

    public SwitchRecordBean getSwitch(int lid) throws Exception {
        FVCmdGetSwitchInfo cmd = new FVCmdGetSwitchInfo(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<LFTRecordBean> getLFTs() throws Exception {
        FVCmdGetLFT cmd = new FVCmdGetLFT();
        return statement.execute(cmd);
    }

    public List<LFTRecordBean> getLFTs(int lid) throws Exception {
        FVCmdGetLFT cmd = new FVCmdGetLFT(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<MFTRecordBean> getMFTs() throws Exception {
        FVCmdGetMFT cmd = new FVCmdGetMFT();
        return statement.execute(cmd);
    }

    public List<MFTRecordBean> getMFTs(int lid) throws Exception {
        FVCmdGetMFT cmd = new FVCmdGetMFT(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<CableRecordBean> getCables() throws Exception {
        FVCmdGetCable cmd = new FVCmdGetCable();
        return statement.execute(cmd);
    }

    public List<CableRecordBean> getCables(int lid) throws Exception {
        FVCmdGetCable cmd = new FVCmdGetCable(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<SC2SLMTRecordBean> getSC2SLMTs() throws Exception {
        FVCmdGetSC2SLMTs cmd = new FVCmdGetSC2SLMTs();
        return statement.execute(cmd);
    }

    public SC2SLMTRecordBean getSC2SLMT(int lid) throws Exception {
        FVCmdGetSC2SLMT cmd = new FVCmdGetSC2SLMT(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<SC2VLMTRecordBean> getSC2VLTMTs() throws Exception {
        FVCmdGetSC2VLTMT cmd = new FVCmdGetSC2VLTMT();
        return statement.execute(cmd);
    }

    public List<SC2VLMTRecordBean> getSC2VLTMT(int lid) throws Exception {
        FVCmdGetSC2VLTMT cmd = new FVCmdGetSC2VLTMT(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<SC2VLMTRecordBean> getSC2VLNTMTs() throws Exception {
        FVCmdGetSC2VLNTMT cmd = new FVCmdGetSC2VLNTMT();
        return statement.execute(cmd);
    }

    public List<SC2VLMTRecordBean> getSC2VLNTMT(int lid) throws Exception {
        FVCmdGetSC2VLNTMT cmd = new FVCmdGetSC2VLNTMT(new InputLid(lid));
        return statement.execute(cmd);
    }

    public List<P_KeyTableRecordBean> getPKeyTables() throws Exception {
        FVCmdGetPKeyTable cmd = new FVCmdGetPKeyTable();
        return statement.execute(cmd);
    }

    public List<VLArbTableRecordBean> getVLArbTables() throws Exception {
        FVCmdGetVLArb cmd = new FVCmdGetVLArb();
        return statement.execute(cmd);
    }

    public List<SMRecordBean> getSMs() throws Exception {
        FVCmdGetSMInfo cmd = new FVCmdGetSMInfo();
        return statement.execute(cmd);
    }

    public List<PathRecordBean> getPath(GID.Global gid) throws Exception {
        FVCmdGetPath cmd = new FVCmdGetPath(new InputPortGid(gid));
        return statement.execute(cmd);
    }

    public List<TraceRecordBean> getTrace(GID.Global source, GID.Global target)
            throws Exception {
        FVCmdGetTrace cmd = new FVCmdGetTrace(new InputGidPair(source, target));
        return statement.execute(cmd);
    }

    public FabricInfoBean getFabricInfo() throws Exception {
        FVCmdGetFabricInfo cmd = new FVCmdGetFabricInfo();
        return statement.execute(cmd);
    }
}
