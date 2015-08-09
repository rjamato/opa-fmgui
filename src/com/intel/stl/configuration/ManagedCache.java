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
 *  File Name: ManagedCache.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/11/04 14:14:41  fernande
 *  Archive Log:    NoticeManager performance improvements. Notices are now processed in batches and database update is done in parallel with cache updates. Changes to the management of caches; if a cache is not ready, no updates for a notice are carried out.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/17 13:17:35  jypak
 *  Archive Log:    Return boolean for each cache process notice operation to let CacheManager know whether it need to start the CopyTopologyTask.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/28 14:56:57  jypak
 *  Archive Log:    Notice Manager updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/12 13:08:42  fernande
 *  Archive Log:    Adding support for notice processing
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/03 21:34:19  fernande
 *  Archive Log:    Adding the CacheManager in support of APIs
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.configuration;

import com.intel.stl.api.notice.impl.NoticeProcess;

public interface ManagedCache {

    boolean isCacheReady();

    void setCacheReady(boolean ready);

    void updateCache();

    void processNotice(NoticeProcess notice) throws Exception;

}
