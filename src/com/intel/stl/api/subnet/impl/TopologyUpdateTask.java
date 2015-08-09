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
 *  File Name: TopologyUpdateTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/12/11 18:33:59  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/03 21:35:54  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/23 15:15:15  fernande
 *  Archive Log:    Fix for switching subnets: tryConnect is now a transient connection.
 *  Archive Log:    Last accessed subnet is now saved to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/20 16:54:32  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/19 20:04:08  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.subnet.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.configuration.AsyncTask;
import com.intel.stl.datamanager.DatabaseManager;

public class TopologyUpdateTask extends AsyncTask<Void> {
    private static Logger log = LoggerFactory.getLogger(TopologyUpdateTask.class);

    private final DatabaseManager dbMgr;

    private final SAHelper helper;

    public TopologyUpdateTask(SAHelper helper, DatabaseManager dbMgr) {
        this.helper = helper;
        this.dbMgr = dbMgr;
    }

    @Override
    public Void process() throws Exception {
        SubnetDescription subnet = helper.getSubnetDescription();
        String subnetName = subnet.getName();
        log.info("Updating topology in the background for subnet: "
                + subnetName);
        List<NodeRecordBean> allNodes = helper.getNodes();
        List<LinkRecordBean> allLinks = helper.getLinks();
        if (allNodes == null || allLinks == null) {
            log.error("Subnet manager for subnet " + subnetName
                    + " returned no nodes or no links");
        } else {
            dbMgr.saveTopology(subnet.getName(), allNodes, allLinks);
            log.info("Topology has been updated");
        }
        return null;
    }

}
