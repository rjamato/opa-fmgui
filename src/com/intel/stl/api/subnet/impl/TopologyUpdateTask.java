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
 *  File Name: TopologyUpdateTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:48:53  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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
