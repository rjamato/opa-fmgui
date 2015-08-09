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

package com.intel.stl.dbengine.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.failure.FailureManager;
import com.intel.stl.api.failure.IFailureManagement;
import com.intel.stl.dbengine.DatabaseContext;
import com.intel.stl.dbengine.DatabaseEngine;
import com.intel.stl.dbengine.DatabaseServer;

public class DatabaseServerImpl implements DatabaseServer {

    private static Logger log = LoggerFactory.getLogger(DatabaseServerImpl.class);

    protected static final long UNUSED_THRESHOLD = 600000L;

    private final DatabaseEngine engine;

    private final Map<String, DatabaseContextImpl> dbContexts =
            new ConcurrentHashMap<String, DatabaseContextImpl>(8, 0.9f, 1);

    public DatabaseServerImpl(DatabaseEngine engine) {
        this.engine = engine;
    }

    @Override
    public DatabaseContext getDatabaseContext(String contextName) {
        DatabaseContextImpl ctx = dbContexts.get(contextName);
        if (ctx == null) {
            ctx = createDatabaseContext(contextName);
        }
        ctx.setLastUsed(System.currentTimeMillis());
        return ctx;
    }

    @Override
    public void addDatabaseContext(String contextName) {
        Iterator<Entry<String, DatabaseContextImpl>> iterator =
                dbContexts.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, DatabaseContextImpl> entry = iterator.next();
            if (entry.getKey().equals(contextName)) {
                entry.getValue().getEntityManager().close();
                iterator.remove();
            } else {
                long now = System.currentTimeMillis();
                long lastUsed = entry.getValue().getLastUsed();
                if ((now - lastUsed) > UNUSED_THRESHOLD) {
                    entry.getValue().getEntityManager().close();
                    iterator.remove();
                }
            }
        }
        createDatabaseContext(contextName);
    }

    private DatabaseContextImpl createDatabaseContext(String contextName) {
        EntityManager em = null;
        try {
            em = engine.getEntityManager();
        } catch (DatabaseException e) {
            log.error(e.getMessage(), e);
        }
        IFailureManagement failureMgr = FailureManager.getManager();
        DatabaseContextImpl ctx = new DatabaseContextImpl(em, failureMgr);
        ctx.setLastUsed(System.currentTimeMillis());
        dbContexts.put(contextName, ctx);
        return ctx;
    }

    protected Map<String, DatabaseContextImpl> getDbContexts() {
        return dbContexts;
    }
}
