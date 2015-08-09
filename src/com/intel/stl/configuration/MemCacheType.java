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
 *  File Name: CacheType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/23 22:24:35  jijunwan
 *  Archive Log:    added GroupConfCache
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/31 17:41:16  jypak
 *  Archive Log:    1. CableInfo updates (Moved the QSFP interpretation logic to backend etc.)
 *  Archive Log:    2. SC2SL updates.
 *  Archive Log:    3. SC2VLt updates.
 *  Archive Log:    4. SC3VLnt updates.
 *  Archive Log:    Some of the SubnetApi, CachedSubnetApi updates should be undone when the FE supports cable info, SC2SL, SC2VLt, SC2VLnt.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/18 16:25:40  jypak
 *  Archive Log:    Cable Info updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/18 20:48:50  fernande
 *  Archive Log:    Enabling GroupInfo saving after fixing issues in the application
 *  Archive Log:
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

import com.intel.stl.api.performance.impl.GroupCacheImpl;
import com.intel.stl.api.performance.impl.GroupConfCacheImpl;
import com.intel.stl.api.subnet.impl.CableCacheImpl;
import com.intel.stl.api.subnet.impl.LFTCacheImpl;
import com.intel.stl.api.subnet.impl.LinkCacheImpl;
import com.intel.stl.api.subnet.impl.MFTCacheImpl;
import com.intel.stl.api.subnet.impl.NodeCacheImpl;
import com.intel.stl.api.subnet.impl.PKeyTableCacheImpl;
import com.intel.stl.api.subnet.impl.PortCacheImpl;
import com.intel.stl.api.subnet.impl.SC2SLMTCacheImpl;
import com.intel.stl.api.subnet.impl.SC2VLNTMTCacheImpl;
import com.intel.stl.api.subnet.impl.SC2VLTMTCacheImpl;
import com.intel.stl.api.subnet.impl.SMCacheImpl;
import com.intel.stl.api.subnet.impl.SwitchCacheImpl;
import com.intel.stl.api.subnet.impl.VLArbTableCacheImpl;

/**
 * This enumeration is used by CacheManager to create all the caches used in the
 * application. Please note all cache implementations must have a constructor
 * that accepts a CacheManager as unique parameter; using this parameter, you
 * should be able to get references to the DatabaseManager, the
 * ProcessingService and the helpers that you might need in the cache
 * implementation.
 */
public enum MemCacheType {

    NODE(NodeCacheImpl.class),

    LINK(LinkCacheImpl.class),

    PORT(PortCacheImpl.class),

    SWITCH(SwitchCacheImpl.class),

    LFT(LFTCacheImpl.class),

    MFT(MFTCacheImpl.class),

    CABLE(CableCacheImpl.class),

    PKEYTABLE(PKeyTableCacheImpl.class),

    VLARBTABLE(VLArbTableCacheImpl.class),

    SM(SMCacheImpl.class),

    GROUP(GroupCacheImpl.class),

    GROUP_CONF(GroupConfCacheImpl.class),

    SC2SL(SC2SLMTCacheImpl.class),

    SC2VLT(SC2VLTMTCacheImpl.class),

    SC2VLNT(SC2VLNTMTCacheImpl.class);

    private final Class<? extends ManagedCache> implementingClass;

    private MemCacheType(Class<? extends ManagedCache> implementingClass) {
        this.implementingClass = implementingClass;
    }

    public String getImplementingClassName() {
        return implementingClass.getCanonicalName();
    }

    public ManagedCache getInstance(CacheManager cacheManager) throws Exception {
        ManagedCache cache =
                implementingClass.getConstructor(CacheManager.class)
                        .newInstance(cacheManager);
        return cache;
    }
}
