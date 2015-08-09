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
 *  File Name: LastUpdateListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/06/20 16:58:25  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:14  fernande
 *  Archive Log:    Saving topology to database
 *  Archive Log:
 *
 *  Overview: This is an entity listener configured in JPA for all entities that
 *  extend IVoid. It's defined in the mapped superclass and it updates the 
 *  lastUpdate field used as version for Hibernate.
 *
 *  @author: Fernando Fernandez
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import java.util.Date;

import com.intel.stl.datamanager.DatabaseRecord;

public class LastUpdateListener {

    public void setLastUpdate(final DatabaseRecord entity) {
        final Date now = new Date();
        entity.setLastUpdate(now);
    }

}
