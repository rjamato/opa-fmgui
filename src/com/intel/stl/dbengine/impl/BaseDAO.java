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
 *  File Name: BaseDAO.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/17 18:49:34  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
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
