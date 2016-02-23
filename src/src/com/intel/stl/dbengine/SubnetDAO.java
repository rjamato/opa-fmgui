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
 *  File Name: SubnetDAO.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19  2015/08/17 18:48:35  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/25 20:41:12  fernande
 *  Archive Log:    Fix for autoconnect not being set on the database
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/23 22:26:29  jijunwan
 *  Archive Log:    added querying links by lid
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/11 21:11:00  fernande
 *  Archive Log:    Adding support to remove a subnet (logical delete) from the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/02/06 15:04:21  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/04 21:38:02  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/11/04 14:19:44  fernande
 *  Archive Log:    NoticeManager improvements. Added new methods in support of batch processing of notices and removed methods not used anymore because they were used for individual updates and not batches.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/09/22 11:35:58  jypak
 *  Archive Log:    Added a new CopyTopologyTask and related updates for the subnet DAO and the cache manager.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/17 13:17:39  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/15 15:15:47  jypak
 *  Archive Log:    Notice Manager JUnit tests and relevant fixes.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/05 15:38:44  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/28 14:57:02  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/20 16:57:41  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/19 20:08:07  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/17 13:12:37  jypak
 *  Archive Log:    Renamed ports to links.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/11 22:09:00  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/23 19:46:18  fernande
 *  Archive Log:    Saving topology to database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 19:10:38  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 16:15:10  fernande
 *  Archive Log:    Implementing DAOs to persist EventRule and SubnetDescription
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine;

import java.util.List;
import java.util.Set;

import com.intel.stl.api.FMException;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.datamanager.SubnetRecord;
import com.intel.stl.datamanager.TopologyLinkRecord;
import com.intel.stl.datamanager.TopologyNodeRecord;
import com.intel.stl.datamanager.TopologyRecord;

public interface SubnetDAO {

    List<SubnetDescription> getSubnets();

    SubnetRecord getSubnet(String name);

    SubnetRecord getSubnet(long subnetId);

    SubnetDescription defineSubnet(SubnetDescription subnet);

    void updateSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException;

    void removeSubnet(long subnetId) throws SubnetDataNotFoundException;

    TopologyRecord saveTopology(String subnetName, List<NodeRecordBean> nodes,
            List<LinkRecordBean> links) throws SubnetDataNotFoundException;

    TopologyRecord copyTopology(SubnetRecord subnet,
            Set<TopologyNodeRecord> newNodes, Set<TopologyNodeRecord> updNodes,
            Set<TopologyLinkRecord> newLinks, Set<TopologyLinkRecord> updLinks);

    TopologyRecord getTopology(String subnetName)
            throws SubnetDataNotFoundException;

    List<NodeRecordBean> getNodes(String subnetName)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNode(long guid) throws SubnetDataNotFoundException;

    NodeRecordBean getNode(String subnetName, long guid)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNode(String subnetName, int lid)
            throws SubnetDataNotFoundException;

    NodeRecordBean getNodeByPortGUID(String subnetName, long portGuid)
            throws SubnetDataNotFoundException;

    LinkRecordBean getLinkBySource(String subnetName, int lid, short portNum)
            throws FMException;

    LinkRecordBean getLinkByDestination(String subnetName, int lid,
            short portNum) throws FMException;

    void insertNode(NodeRecordBean node);

    void updateNode(NodeRecordBean node) throws SubnetDataNotFoundException;

    List<LinkRecordBean> getLinks(String subnetName)
            throws SubnetDataNotFoundException;

    List<LinkRecordBean> getLinks(String subnetName, int lid)
            throws SubnetDataNotFoundException;

    TopologyNodeRecord getTopologyNodeRecord(long topologyId, int lid);

    List<TopologyLinkRecord> getTopologyLinkRecords(long topologyId,
            long nodeGuid);

}
