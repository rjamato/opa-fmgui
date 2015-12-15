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
 *  File Name: DatabaseMigrationHelper.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/18 21:04:41  fernande
 *  Archive Log:    PR 128703 - Fail over doesn't work on A0 Fabric. Fixed schema update because FE list is not copied over to new database
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/17 18:49:00  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.HostInfo;
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
        List<HostInfo> feList = subnet.getSubnetDescription().getFEList();
        List<HostInfo> newfeList = new ArrayList<HostInfo>(feList);
        subnet.getSubnetDescription().setFEList(newfeList);
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
