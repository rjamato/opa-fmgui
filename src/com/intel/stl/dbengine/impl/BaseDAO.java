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
 *  File Name: BaseDAO.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/11/04 14:21:57  fernande
 *  Archive Log:    NoticeManager improvements. Added new methods in support of batch processing of notices and removed methods not used anymore because they were used for individual updates. Improved BaseDAO so that the DatabaseContext is available from within a DAO and therefore other DAOs are available within a DAO.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/05 15:39:14  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/23 19:47:14  fernande
 *  Archive Log:    Saving topology to database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 19:11:06  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 16:16:04  fernande
 *  Archive Log:    Implementing DAOs to persist EventRule and SubnetDescription
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import javax.persistence.EntityManager;

import com.intel.stl.dbengine.DatabaseContext;

public class BaseDAO {
    protected final EntityManager em;

    protected final DatabaseContext databaseCtx;

    public BaseDAO(EntityManager entityManager) {
        this(entityManager, null);
    }

    public BaseDAO(EntityManager entityManager, DatabaseContext databaseCtx) {
        this.em = entityManager;
        this.databaseCtx = databaseCtx;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public DatabaseContext getDatabaseContext() {
        return databaseCtx;
    }

    public void startTransaction() {
        em.getTransaction().begin();
    }

    public void commitTransaction() {
        em.getTransaction().commit();
    }

    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    public void flush() {
        em.flush();
    }

    public void clear() {
        em.clear();
    }
}
