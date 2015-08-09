/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: HelpAction.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/04/07 18:08:21  jypak
 *  Archive Log:    Online Help updates for additional panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/02 13:32:54  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/26 17:38:23  jypak
 *  Archive Log:    Online Help updates for additional panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/11 16:44:05  jypak
 *  Archive Log:    File name parameter not needed.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/10 18:43:15  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.help.CSH;
import javax.help.DefaultHelpBroker;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

public class HelpAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = -1868097381130706771L;

    // Help ID strings
    private final String DEFAULT = "GUID-4B372184-0331-4BEA-9958-578F7E030F39";

    private final String SUBNET_SUMMARY =
            "GUID-A3DCE301-7E40-445A-81F0-B2CADEAEAF0A";

    private final String SUBNET_NAME =
            "GUID-F3CB56AD-4135-4F26-A1B2-07AB07D3A3C3";

    private final String STATUS = "GUID-96555ADB-F524-41F4-A803-FFB0942DB3FC";

    private final String HEALTH_TREND =
            "GUID-5F45E9FD-D356-4631-BC5B-8854FD28CC0E";

    private final String WORST_NODES =
            "GUID-1686A817-3EC8-4C32-B6E8-90B804418596";

    private final String SUBNET_PERFORMANCE =
            "GUID-0F8B013E-084E-4F34-8D8F-1E6679EB85FE";

    private final String TREND = "GUID-13874FF6-49CF-4EE9-A937-40254947787B";

    private final String TOP_N = "GUID-13874FF6-49CF-4EE9-A937-40254947787B";

    private final String PERF_SUBNET_SUMMARY =
            "GUID-4A0DC71A-2224-41C4-8BF8-47DDB59AD963";

    private final String STATISTICS =
            "GUID-D67D3699-28D5-4488-A235-3040CF9350AF";

    private final String EVENTS = "GUID-79008C3C-15F7-45FE-B795-DE29C8C5B9E9";

    private final String GENERAL_SUMMARY =
            "GUID-2B9BFA98-8AAB-4620-9F23-92E06346A11A";

    private final String PERF_TREND =
            "GUID-0A2A33E6-DDBD-4323-9721-40B53A60EAC5";

    private final String HISTOGRAM =
            "GUID-9AF58D84-4FCE-49B4-BCA3-03C91FB1FD34";

    private final String PERF_TOP_N =
            "GUID-4C9DB627-99EA-4475-B0FD-677ACEF0D89D";

    private final String PORT_PERF =
            "GUID-F4DBA2C1-FFC5-43CA-A499-388060815F2D";

    private final String PORT_RCV_PKTS =
            "GUID-2F075726-6E27-4776-A6F6-0BF920097B51";

    private final String PORT_TRAN_PKTS =
            "GUID-2F075726-6E27-4776-A6F6-0BF920097B51";

    private final String NODE_PERF =
            "GUID-70C75041-ECD6-4300-A93F-F1E121B6D98C";

    private final String NODE_RCV_PKTS =
            "GUID-70C75041-ECD6-4300-A93F-F1E121B6D98C";

    private final String NODE_TRAN_PKTS =
            "GUID-70C75041-ECD6-4300-A93F-F1E121B6D98C";

    private final String COUNTERS = "GUID-2F075726-6E27-4776-A6F6-0BF920097B51";

    private final String RECEIVE_COUNTERS =
            "GUID-2F075726-6E27-4776-A6F6-0BF920097B51";

    private final String OTHER_COUNTERS =
            "GUID-2F075726-6E27-4776-A6F6-0BF920097B51";

    // Topology page
    private final String NAME_OF_SUBNET =
            "GUID-30A92411-3312-45AA-A09F-56AD35F29567";

    private final String TOPOLOGY_NODE =
            "GUID-30A92411-3312-45AA-A09F-56AD35F29567";

    private final String OVERALL_SUMMARY =
            "GUID-CD12ECBC-7E28-4B82-9419-BF16256A012F";

    private final String TOPOLOGY_SUMMARY =
            "GUID-CD12ECBC-7E28-4B82-9419-BF16256A012F";

    private final String LINKS = "GUID-4D0C7193-5343-4D40-AB29-8A1A0D4684FD";

    private final String DEVICE_GROUP =
            "GUID-14B1543F-C9DC-4B01-B2E0-BCAE320CB212";

    private final String MFT = "GUID-B3A46E21-3C2B-4B86-9ED0-0DA29AEAF597";

    private final String LFT = "GUID-509C627F-D08E-4104-8D86-9EB3430D426D";

    private final String PERF_NODE_PORTS_TABLE =
            "GUID-C5A734FF-34F0-4A54-A40F-FA80703B10FB";

    // HFI Node level properties
    private final String NODE_GENERAL =
            "GUID-BCC5875D-76FA-414C-A0DC-DFC3EB1640B9";// ResourceCategory.NODE_INFO

    private final String SC2SL = "GUID-3A2A68C1-3DD3-4C91-ABE7-2B3F96EDADAE";

    // Switch Node level properties
    private final String SWITCH_INFORMATION =
            "GUID-4E451160-BD28-48E7-AA1E-5C8117B39B49";

    private final String ROUTING_INFORMATION =
            "GUID-AF9B8778-17F8-4BEA-8979-C9119DE9AC2C";

    // Port level properties
    private final String PORT_DEVICE_INFO =
            "GUID-80DD968E-049F-49FA-9BA8-107446DF2F71";// ResourceCategory.PORT_INFO

    private final String PORT_LINK =
            "GUID-37EC9203-4C4F-4E82-A4C5-ED151C5379FE";

    private final String PORT_LINK_CONN =
            "GUID-E3841E8C-4026-4050-9A25-2E2670327BC5";

    private final String PORT_CAPABILITY =
            "GUID-0C771533-6402-4BC7-93E4-35D5F4203CC9";

    private final String VL = "GUID-6B2C80A1-89CF-4DCC-AE7D-5B8B11F4B9DC";

    private final String DIAGNOSTICS =
            "GUID-5FB5A987-8A67-4B2F-AD09-51C694C0ED9B";

    private final String PARTITION =
            "GUID-83CC80FB-B0C4-4D03-AD39-00BD7210B688";

    private final String MANAGEMENT =
            "GUID-19E75083-19D2-4837-AC64-F7654F04842C";

    private final String FLIT_CONTROL =
            "GUID-9ABCBE5B-D5FE-49B8-8EF3-6FA0E7808CE0";

    // Java Help system navigator type enum.
    private final JavaHelpNavType view;

    private static final String FILE_NAME =
            "GUID-E6C6D081-FE56-4EB5-8287-9873E65B45E6.hs";

    private DefaultHelpBroker helpBroker;

    private HelpSet helpSet;

    private ActionListener displayListener;

    private static HelpAction instance = null;

    public static HelpAction getInstance() {
        if (instance == null) {
            instance = new HelpAction();
        }
        return instance;
    }

    protected HelpAction() {
        view = JavaHelpNavType.TOC;
        initHelpSystem();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // If not 1st time, other ID will be displayed. Set to default ID
        // anyway.
        helpBroker.setCurrentView(view.toString());
        CSH.setHelpIDString((JMenuItem) event.getSource(), DEFAULT);
        displayListener.actionPerformed(event);
    }

    private void initHelpSystem() {
        if (helpBroker != null && helpSet != null) {
            return;
        }

        helpSet = initHelpSet();
        if (helpSet != null) {
            helpBroker = (DefaultHelpBroker) helpSet.createHelpBroker();
        }

        displayListener = new CSH.DisplayHelpFromSource(helpBroker);
    }

    protected HelpSet initHelpSet() {

        URL hsURL = HelpSet.findHelpSet(null, FILE_NAME);

        HelpSet helpSet = null;
        try {
            helpSet = new HelpSet(null, hsURL);
        } catch (HelpSetException e) {
            e.printStackTrace();
        }

        return helpSet;
    }

    public void enableHelpKey(JFrame frame) {
        helpBroker.enableHelpKey(frame.getRootPane(), DEFAULT, helpSet);
    }

    public HelpBroker getHelpBroker() {
        return helpBroker;
    };

    public HelpSet getHelpSet() {
        return helpSet;
    }

    public String getDefault() {
        return DEFAULT;
    }

    /**
     * @return the subnetSummary
     */
    public String getSubnetSummary() {
        return SUBNET_SUMMARY;
    }

    /**
     * @return the subnetName
     */
    public String getSubnetName() {
        return SUBNET_NAME;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return STATUS;
    }

    /**
     * @return the healthTrend
     */
    public String getHealthTrend() {
        return HEALTH_TREND;
    }

    /**
     * @return the worstNodes
     */
    public String getWorstNodes() {
        return WORST_NODES;
    }

    /**
     * @return the subnetPerformance
     */
    public String getSubnetPerformance() {
        return SUBNET_PERFORMANCE;
    }

    /**
     * @return the trend
     */
    public String getTrend() {
        return TREND;
    }

    /**
     * @return the topN
     */
    public String getTopN() {
        return TOP_N;
    }

    /**
     * @return the perfSubnetSummary
     */
    public String getPerfSubnetSummary() {
        return PERF_SUBNET_SUMMARY;
    }

    /**
     * @return the statistics
     */
    public String getStatistics() {
        return STATISTICS;
    }

    /**
     * @return the events
     */
    public String getEvents() {
        return EVENTS;
    }

    /**
     * @return the generalSummary
     */
    public String getGeneralSummary() {
        return GENERAL_SUMMARY;
    }

    /**
     * @return the perfTrend
     */
    public String getPerfTrend() {
        return PERF_TREND;
    }

    /**
     * @return the histogram
     */
    public String getHistogram() {
        return HISTOGRAM;
    }

    /**
     * @return the perfTopN
     */
    public String getPerfTopN() {
        return PERF_TOP_N;
    }

    /**
     * @return the performance
     */
    public String getPortPerf() {
        return PORT_PERF;
    }

    /**
     * @return the receivedPackets
     */
    public String getPortRcvPkts() {
        return PORT_RCV_PKTS;
    }

    /**
     * @return the transmittedPackets
     */
    public String getPortTranPkts() {
        return PORT_TRAN_PKTS;
    }

    /**
     * @return the counters
     */
    public String getCounters() {
        return COUNTERS;
    }

    /**
     * @return the receiveCounters
     */
    public String getReceiveCounters() {
        return RECEIVE_COUNTERS;
    }

    /**
     * @return the otherCounters
     */
    public String getOtherCounters() {
        return OTHER_COUNTERS;
    }

    /**
     * @return the NODE_PERFORMANCE
     */
    public String getNodePerf() {
        return NODE_PERF;
    }

    /**
     * @return the NODE_RECEIVED_PACKETS
     */
    public String getNodeRcvPkts() {
        return NODE_RCV_PKTS;
    }

    /**
     * @return the nODE_TRANSMITTED_PACKETS
     */
    public String getNodeTranPkts() {
        return NODE_TRAN_PKTS;
    }

    /**
     * @return the nameOfSubnet
     */
    public String getNameOfSubnet() {
        return NAME_OF_SUBNET;
    }

    /**
     * @return the tOPOLOGY_NODE
     */
    public String getTopologyNode() {
        return TOPOLOGY_NODE;
    }

    /**
     * @return the overallSummary
     */
    public String getOverallSummary() {
        return OVERALL_SUMMARY;
    }

    /**
     * @return the topologySummary
     */
    public String getTopologySummary() {
        return TOPOLOGY_SUMMARY;
    }

    /**
     * @return the links
     */
    public String getLinks() {
        return LINKS;
    }

    /**
     * @return the switchInformation
     */
    public String getSwitchInformation() {
        return SWITCH_INFORMATION;
    }

    /**
     * @return the routingInformation
     */
    public String getRoutingInformation() {
        return ROUTING_INFORMATION;
    }

    /**
     * @return the deviceGroup
     */
    public String getDeviceGroup() {
        return DEVICE_GROUP;
    }

    /**
     * @return the mft
     */
    public String getMft() {
        return MFT;
    }

    /**
     * @return the lft
     */
    public String getLft() {
        return LFT;
    }

    /**
     * @return the PERF_NODE_PORTS_TABLE
     */
    public String getPerfNodePortsTable() {
        return PERF_NODE_PORTS_TABLE;
    }

    public String getNodeGeneral() {
        return NODE_GENERAL;
    }

    /**
     * @return the sC2SL
     */
    public String getSC2SL() {
        return SC2SL;
    }

    /**
     * @return the pORT_DEVICE_INFO
     */
    public String getPortDevInfo() {
        return PORT_DEVICE_INFO;
    }

    /**
     * @return the pORT_LINK
     */
    public String getPortLink() {
        return PORT_LINK;
    }

    /**
     * @return the pORT_LINK_CONN
     */
    public String getPortLinkConn() {
        return PORT_LINK_CONN;
    }

    /**
     * @return the pORT_CAPABILITY
     */
    public String getPortCap() {
        return PORT_CAPABILITY;
    }

    /**
     * @return the vL
     */
    public String getVL() {
        return VL;
    }

    /**
     * @return the dIAGNOSTICS
     */
    public String getDiagnostics() {
        return DIAGNOSTICS;
    }

    /**
     * @return the pARTITION
     */
    public String getPartition() {
        return PARTITION;
    }

    /**
     * @return the mANAGEMENT
     */
    public String getManagement() {
        return MANAGEMENT;
    }

    /**
     * @return the fLIT_CONTROL
     */
    public String getFlitControl() {
        return FLIT_CONTROL;
    }

}
