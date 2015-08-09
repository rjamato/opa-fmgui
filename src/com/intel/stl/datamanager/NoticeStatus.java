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
 *  File Name: NoticeStatus.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/24 18:50:22  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/18 14:18:34  jypak
 *  Archive Log:    1. When  shutdown notice manager, remove it from the listener list of the STLConnection so that the blocking queue doesn't fill up.
 *  Archive Log:    2. Removed unncessary print out statements.
 *  Archive Log:    3. Port cache now has null check for memory cache.
 *  Archive Log:    4. For FE errors, still process the notice and set the NoticeStatus to FEERROR.
 *  Archive Log:    5. Junit updates for NoticeStatus.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/27 17:04:20  fernande
 *  Archive Log:    Database changes to add Notice and ImageInfo tables to the schema database
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

/**
 * This is used to keep track of the status of a notice while being processed.
 * Please keep in mind that the name of the enumeration is used to save the
 * status in the database, so changing the names here will require a database
 * reorganization.
 */
public enum NoticeStatus {
    RECEIVED,
    PROCESSED,
    INFLIGHT,
    FEERROR;
}
