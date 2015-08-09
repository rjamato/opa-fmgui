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

import static com.intel.stl.common.STLMessages.STL30055_NODE_NOT_FOUND_IN_CACHE_LID;

import java.util.EnumMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.BaseApi;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.LFTRecordBean;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.MFTRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.P_KeyTableRecordBean;
import com.intel.stl.api.subnet.PathRecordBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SC2SLMTRecordBean;
import com.intel.stl.api.subnet.SC2VLMTRecordBean;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SubnetException;
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.api.subnet.TraceRecordBean;
import com.intel.stl.api.subnet.VLArbTableRecordBean;
import com.intel.stl.common.STLMessages;
import com.intel.stl.configuration.CacheManager;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

/**
 * @author jijunwan
 * 
 */
public class SubnetApi extends BaseApi implements ISubnetApi {
    private static Logger log = LoggerFactory.getLogger(SubnetApi.class);

    private final CacheManager cacheMgr;

    private SAHelper helper;

    public SubnetApi(CacheManager cacheMgr) {
        super();
        this.cacheMgr = cacheMgr;
        this.helper = cacheMgr.getSAHelper();
        helper.setConnectionEventListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getConnectionDescription()
     */
    @Override
    public SubnetDescription getConnectionDescription() {
        return helper.getSubnetDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getNodes()
     */
    @Override
    public List<NodeRecordBean> getNodes(boolean includeInactive)
            throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNodes(includeInactive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getNode(int)
     */
    @Override
    public NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNode(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getNode(long)
     */
    @Override
    public NodeRecordBean getNode(long portGuid)
            throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNode(portGuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getNodesTypeDist()
     */
    @Override
    public EnumMap<NodeType, Integer> getNodesTypeDist(boolean includeInactive)
            throws SubnetDataNotFoundException {
        NodeCache nodeCache = cacheMgr.acquireNodeCache();
        return nodeCache.getNodesTypeDist(includeInactive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getLinks()
     */
    @Override
    public List<LinkRecordBean> getLinks(boolean includeInactive)
            throws SubnetDataNotFoundException {
        LinkCache linkCache = cacheMgr.acquireLinkCache();
        return linkCache.getLinks(includeInactive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getFromLink(int)
     */
    @Override
    public LinkRecordBean getLinkBySource(int lid, short portNum)
            throws SubnetDataNotFoundException {
        LinkCache linkCache = cacheMgr.acquireLinkCache();
        return linkCache.getLinkBySource(lid, portNum);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getToLink(int)
     */
    @Override
    public LinkRecordBean getLinkByDestination(int lid, short portNum)
            throws SubnetDataNotFoundException {
        LinkCache linkCache = cacheMgr.acquireLinkCache();
        return linkCache.getLinkByDestination(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getPorts()
     */
    @Override
    public List<PortRecordBean> getPorts() throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPorts();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getSwitchPort(int, short)
     */
    @Override
    public PortRecordBean getPortByPortNum(int lid, short portNum)
            throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPortByPortNum(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getPort(int, short)
     */
    @Override
    public PortRecordBean getPortByLocalPortNum(int lid, short localPortNum)
            throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPortByLocalPortNum(lid, localPortNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#hasPort(int, short)
     */
    @Override
    public boolean hasPort(int lid, short portNum) {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.hasPort(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#hasLocalPort(int, short)
     */
    @Override
    public boolean hasLocalPort(int lid, short localPortNum) {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.hasLocalPort(lid, localPortNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getPortsTypeDist()
     */
    @Override
    public EnumMap<NodeType, Long> getPortsTypeDist(boolean countInternalMgrPort)
            throws SubnetDataNotFoundException {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getPortsTypeDist(countInternalMgrPort);
    }

    public long getSubnetPrefix() {
        PortCache portCache = cacheMgr.acquirePortCache();
        return portCache.getSubnetPrefix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSwitches()
     */
    @Override
    public List<SwitchRecordBean> getSwitches() throws SubnetException {
        SwitchCache switchCache = cacheMgr.acquireSwitchCache();
        return switchCache.getSwitches();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getSwitch(int)
     */
    @Override
    public SwitchRecordBean getSwitch(int lid) throws SubnetException {
        SwitchCache switchCache = cacheMgr.acquireSwitchCache();
        return switchCache.getSwitch(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getLFTs()
     */
    @Override
    public List<LFTRecordBean> getLFTs() throws SubnetException {
        LFTCache lftCache = cacheMgr.acquireLFTCache();
        return lftCache.getLFTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getLFT(int)
     */
    @Override
    public List<LFTRecordBean> getLFT(int lid) throws SubnetException {
        LFTCache lftCache = cacheMgr.acquireLFTCache();
        return lftCache.getLFT(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getMFTs()
     */
    @Override
    public List<MFTRecordBean> getMFTs() throws SubnetException {
        MFTCache mftCache = cacheMgr.acquireMFTCache();
        return mftCache.getMFTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getMFT(int)
     */
    @Override
    public List<MFTRecordBean> getMFT(int lid) throws SubnetException {
        MFTCache mftCache = cacheMgr.acquireMFTCache();
        return mftCache.getMFT(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getPKeyTables()
     */
    @Override
    public List<P_KeyTableRecordBean> getPKeyTables() throws SubnetException {
        PKeyTableCache pkeyTableCache = cacheMgr.acquirePKeyTableCache();
        return pkeyTableCache.getPKeyTables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getVLArbTables()
     */
    @Override
    public List<VLArbTableRecordBean> getVLArbTables() throws SubnetException {
        VLArbTableCache vlarbTableCache = cacheMgr.acquireVLArbTableCache();
        return vlarbTableCache.getVLArbTables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SMRecordBean> getSMs() throws SubnetException {
        // the UptimeInSeconds field in SMInfo is dynamic. So we should always
        // directly query from FE
        try {
            return helper.getSMs();
        } catch (Exception e) {
            throw getSubnetException(e);
        }

        // SMCache smCache = cacheMgr.acquireSMCache();
        // return smCache.getSMs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public SMRecordBean getSM(int lid) throws SubnetException {
        // the UptimeInSeconds field in SMInfo is dynamic. So we should always
        // directly query from FE
        try {
            List<SMRecordBean> all = helper.getSMs();
            for (SMRecordBean sm : all) {
                if (sm.getLid() == lid) {
                    return sm;
                }
            }
            throw new SubnetDataNotFoundException(
                    STL30055_NODE_NOT_FOUND_IN_CACHE_LID, lid);
        } catch (Exception e) {
            throw getSubnetException(e);
        }

        // SMCache smCache = cacheMgr.acquireSMCache();
        // return smCache.getSM(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<CableRecordBean> getCables() throws SubnetException {
        CableCache cableCache = cacheMgr.acquireCableCache();
        return cableCache.getCables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public List<CableRecordBean> getCable(int lid) throws SubnetException {
        CableCache cableCache = cacheMgr.acquireCableCache();
        return cableCache.getCable(lid);
    }

    @Override
    public List<CableRecordBean> getCable(int lid, short portNum)
            throws SubnetException {
        CableCache cableCache = cacheMgr.acquireCableCache();
        return cableCache.getCable(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SC2SLMTRecordBean> getSC2SLMTs() throws SubnetException {
        SC2SLMTCache sc2slCache = cacheMgr.acquireSC2SLMTCache();
        return sc2slCache.getSC2SLMTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public SC2SLMTRecordBean getSC2SLMT(int lid) throws SubnetException {
        SC2SLMTCache sc2slCache = cacheMgr.acquireSC2SLMTCache();
        return sc2slCache.getSC2SLMT(lid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLTMTs() throws SubnetException {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLTMT(int lid) throws SubnetException {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMT(lid);

    }

    @Override
    public SC2VLMTRecordBean getSC2VLTMT(int lid, short portNum)
            throws SubnetException {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMT(lid, portNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSMs()
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLNTMTs() throws SubnetException {
        SC2VLNTMTCache sc2vlntCache = cacheMgr.acquireSC2VLNTMTCache();
        return sc2vlntCache.getSC2VLNTMTs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.api.ISubnetApi#getSM(int)
     */
    @Override
    public List<SC2VLMTRecordBean> getSC2VLNTMT(int lid) throws SubnetException {
        SC2VLNTMTCache sc2vlntCache = cacheMgr.acquireSC2VLNTMTCache();
        return sc2vlntCache.getSC2VLNTMT(lid);

    }

    @Override
    public SC2VLMTRecordBean getSC2VLNTMT(int lid, short portNum)
            throws SubnetException {
        SC2VLTMTCache sc2vltCache = cacheMgr.acquireSC2VLTMTCache();
        return sc2vltCache.getSC2VLTMT(lid, portNum);
    }

    /*
     * This method is used only in testing
     */
    protected void setHelper(SAHelper helper) {
        this.helper = helper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getPath(int)
     */
    @Override
    public List<PathRecordBean> getPath(int lid) throws SubnetException {
        List<PathRecordBean> res = null;
        try {
            res = helper.getPath(getGid(lid));
        } catch (Exception e) {
            e.printStackTrace();
            throw getSubnetException(e);
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#getTrace(int, int)
     */
    @Override
    public List<TraceRecordBean> getTrace(int sourceLid, int targetLid)
            throws SubnetException {
        List<TraceRecordBean> res = null;
        try {
            res = helper.getTrace(getGid(sourceLid), getGid(targetLid));
        } catch (Exception e) {
            e.printStackTrace();
            throw getSubnetException(e);
        }
        return res;
    }

    public GID.Global getGid(int lid) throws SubnetDataNotFoundException {
        NodeRecordBean node = getNode(lid);
        return new GID.Global(getSubnetPrefix(), node.getNodeInfo()
                .getPortGUID());
    }

    public static SubnetException getSubnetException(Exception e) {
        SubnetException se =
                new SubnetException(STLMessages.STL60002_SUBNET_DATA_FAILURE,
                        e, StringUtils.getErrorMessage(e));
        log.error(StringUtils.getErrorMessage(se), e);
        return se;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.subnet.ISubnetApi#cleanup()
     */
    @Override
    public void cleanup() {
        helper.close();
    }

}
