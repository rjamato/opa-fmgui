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
 *  File Name: DatabaseContextImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/08/17 18:49:34  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/11 21:40:34  fernande
 *  Archive Log:    DatabaseContext.getSubnet should throw an ConfigurationException if the specified subnet is not defined in the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/11 21:11:29  fernande
 *  Archive Log:    Adding support to remove a subnet (logical delete) from the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/06 15:04:47  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/11/04 14:21:57  fernande
 *  Archive Log:    NoticeManager improvements. Added new methods in support of batch processing of notices and removed methods not used anymore because they were used for individual updates. Improved BaseDAO so that the DatabaseContext is available from within a DAO and therefore other DAOs are available within a DAO.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/12 20:01:12  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/28 14:56:59  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/12 20:30:45  jijunwan
 *  Archive Log:    applied Failure Management on DB Manager and FEC Driver
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/20 16:58:25  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import static com.intel.stl.common.STLMessages.STL30022_SUBNET_NOT_FOUND;

import javax.persistence.EntityManager;

import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.failure.IFailureManagement;
import com.intel.stl.datamanager.SubnetRecord;
import com.intel.stl.dbengine.ConfigurationDAO;
import com.intel.stl.dbengine.DatabaseContext;
import com.intel.stl.dbengine.GroupDAO;
import com.intel.stl.dbengine.NoticeDAO;
import com.intel.stl.dbengine.PerformanceDAO;
import com.intel.stl.dbengine.SubnetDAO;

public class DatabaseContextImpl implements DatabaseContext {

    private final EntityManager em;

    private ConfigurationDAO configurationDAO;

    private GroupDAO groupDAO;

    private NoticeDAO noticeDAO;

    private final SubnetDAO subnetDAO;

    private final PerformanceDAO performanceDAO;

    private final IFailureManagement failureMgr;

    private long lastUsed;

    public DatabaseContextImpl(EntityManager em, IFailureManagement failureMgr) {
        this.em = em;
        this.failureMgr = failureMgr;
        this.subnetDAO = new SubnetDAOImpl(em, this);
        this.performanceDAO = new PerformanceDAOImpl(em, this);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public IFailureManagement getFailureManagement() {
        return failureMgr;
    }

    @Override
    public SubnetRecord getSubnet(String subnetName) {
        SubnetRecord subnet = subnetDAO.getSubnet(subnetName);
        if (subnet == null) {
            ConfigurationException ce =
                    new ConfigurationException(STL30022_SUBNET_NOT_FOUND,
                            subnetName);
            throw ce;
        }
        return subnet;
    }

    @Override
    public ConfigurationDAO getConfigurationDAO() {
        if (configurationDAO == null) {
            configurationDAO = new ConfigurationDAOImpl(em, this);
        }
        return configurationDAO;
    }

    @Override
    public GroupDAO getGroupDAO() {
        if (groupDAO == null) {
            groupDAO = new GroupDAOImpl(em, this);
        }
        return groupDAO;
    }

    @Override
    public NoticeDAO getNoticeDAO() {
        if (noticeDAO == null) {
            noticeDAO = new NoticeDAOImpl(em, this);
        }
        return noticeDAO;
    }

    @Override
    public SubnetDAO getSubnetDAO() {
        return subnetDAO;
    }

    @Override
    public PerformanceDAO getPerformanceDAO() {
        return performanceDAO;
    }

    protected long getLastUsed() {
        return lastUsed;
    }

    protected void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }
}
