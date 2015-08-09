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
 *  File Name: DatabaseContextImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
