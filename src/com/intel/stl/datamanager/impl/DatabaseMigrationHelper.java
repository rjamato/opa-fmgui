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
 *  File Name: DatabaseMigrationHelper.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/16 17:38:14  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/17 19:40:52  fernande
 *  Archive Log:    Changed database migration logic invoked during a schema upgrade to save UserSettings together with Subnets
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.datamanager.SubnetRecord;
import com.intel.stl.datamanager.UserRecord;

public class DatabaseMigrationHelper {
    private static Logger log = LoggerFactory
            .getLogger(DatabaseMigrationHelper.class);

    private static final String INSERT_SUBNETRECORD =
            "INSERT INTO SUBNETS (subnetId, uniqueName, port, autoConnect, secureConnect, statusTimestamp) VALUES(?, ?, ?, ?, ?, ?)";

    private static final String INSERT_USERRECORD =
            "INSERT INTO USERS (subnetId, userName, lastUpdate, userDescription, userOptionXml) VALUES(?, ?, ?, ?, ?)";

    public static List<SubnetRecord> getSubnetRecords(EntityManager em) {
        TypedQuery<SubnetRecord> query =
                em.createNamedQuery("Subnet.All", SubnetRecord.class);
        return query.getResultList();
    }

    public static List<UserRecord> getUserRecords(EntityManager em) {
        TypedQuery<UserRecord> query =
                em.createNamedQuery("User.All", UserRecord.class);
        return query.getResultList();
    }

    public static SubnetRecord insertSubnetRecord(EntityManager em,
            SubnetRecord subnet) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String subnetName = subnet.getSubnetDescription().getName();
        String uniqueName = subnet.getUniqueName();
        em.persist(subnet);
        try {
            tx.commit();
            TypedQuery<SubnetRecord> query =
                    em.createNamedQuery("Subnet.findByName", SubnetRecord.class);
            query.setParameter("subnetName", uniqueName);
            return query.getSingleResult();
        } catch (Exception e) {
            log.error("Could not save previously defined subnet '" + subnetName
                    + "': " + StringUtils.getErrorMessage(e), e);
            return null;
        }
    }

    public static void insertUserRecord(EntityManager em, UserRecord user) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(user);
        try {
            tx.commit();
        } catch (Exception e) {
            log.error("Could not save previously defined user settings '"
                    + user.getId() + "': " + StringUtils.getErrorMessage(e), e);
        }
    }

    public static void insertSpecialRecord(EntityManager em, SubnetRecord subnet) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNativeQuery(INSERT_SUBNETRECORD);
        query.setParameter(1, subnet.getId());
        query.setParameter(2, subnet.getUniqueName());
        SubnetDescription desc = subnet.getSubnetDescription();
        query.setParameter(3, desc.getCurrentFE().getPort());
        query.setParameter(4, desc.isAutoConnect());
        query.setParameter(5, desc.getCurrentFE().isSecureConnect());
        query.setParameter(6, desc.getStatusTimestamp());
        try {
            System.out.println("Inserting special record");
            query.executeUpdate();
            System.out.println("Execute done");
            tx.commit();
        } catch (Exception e) {
            log.error("Could not insert special record '" + subnet.getId()
                    + "': " + StringUtils.getErrorMessage(e), e);
        }
    }
}
