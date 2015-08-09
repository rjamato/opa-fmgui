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
 *  File Name: DBCacheType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/09/17 16:44:03  fernande
 *  Archive Log:    Refactored CacheManager to load caches according to what's defined in enums MemCacheType and DBCacheType, to make it more dynamic and more extensible. Changed code so that Exception caught during refreshCache is converted into a RuntimeException and it's rethrown every time getCachedObject is invoked. This to reflect that the caller has not much to do with the original error.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.configuration;

import com.intel.stl.api.subnet.impl.DBLinkCacheImpl;
import com.intel.stl.api.subnet.impl.DBNodeCacheImpl;

public enum DBCacheType {
    NODE(DBNodeCacheImpl.class, MemCacheType.NODE),

    LINK(DBLinkCacheImpl.class, MemCacheType.LINK);

    private final Class<? extends ManagedCache> implementingClass;

    private final MemCacheType memCacheType;

    private DBCacheType(Class<? extends ManagedCache> implementingClass,
            MemCacheType memCacheType) {
        this.implementingClass = implementingClass;
        this.memCacheType = memCacheType;
    }

    public String getImplementingClassName() {
        return implementingClass.getCanonicalName();
    }

    public MemCacheType getMemCacheType() {
        return memCacheType;
    }

    public ManagedCache getInstance(CacheManager cacheManager) throws Exception {
        ManagedCache cache =
                implementingClass.getConstructor(CacheManager.class)
                        .newInstance(cacheManager);
        return cache;
    }
}
