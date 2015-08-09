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
 *  File Name: SystemFunctions.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/09/29 18:55:28  fernande
 *  Archive Log:    Adding UserOptions XML and  saving it to the database. Includes XML schema validation. AppDataUtils has new method to retrieve default UserOptions XML.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/07 17:35:25  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: Fernando Fernandez
 *
 ******************************************************************************/

package com.intel.stl.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;

public interface SystemFunctions {

    String getSystemProperty(String property);

    String getEnvironmentVariable(String variable);

    URL getResource(String resource);

    BufferedReader getReader(InputStream stream);

}
