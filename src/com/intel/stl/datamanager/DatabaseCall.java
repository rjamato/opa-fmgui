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
 *  File Name: DatabaseCall.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/04/29 14:26:32  fernande
 *  Archive Log:    Adding client stack trace to exceptions for supportability.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/05 15:37:20  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/30 17:01:03  fernande
 *  Archive Log:    Establishing overall exception handling strategy for the app
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/20 16:56:12  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/07 19:08:18  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/30 16:10:44  fernande
 *  Archive Log:    Adding base class (DatabaseCall) to simplify database operation implementation
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/25 20:31:42  fernande
 *  Archive Log:    Added support for Hibernate
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.datamanager;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.intel.stl.api.FMException;
import com.intel.stl.dbengine.DatabaseContext;
import com.intel.stl.dbengine.DatabaseServer;

public interface DatabaseCall<R> extends Callable<R> {
    R getResult();

    <E extends FMException> R getResult(Class<E> allowedException) throws E;

    <E extends FMException, F extends FMException> R getResult(
            Class<E> allowedException1, Class<F> allowedException2) throws E, F;

    Result<R> getResultAsync();

    R execute(DatabaseContext dbCtx) throws Exception;

    void setFuture(Future<R> future);

    void setDatabaseServer(DatabaseServer server);

    void setClientTrace(Exception exception);
}
