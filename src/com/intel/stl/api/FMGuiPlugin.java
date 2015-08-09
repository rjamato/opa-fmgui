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
 *  File Name: FMGUIPlugin.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/04/22 16:51:16  fernande
 *  Archive Log:    Reorganized the startup sequence so that the UI plugin could initialize its own CertsAssistant. This way, autoconnect subnets would require a password using the UI CertsAssistant instead of the default CertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/16 21:09:29  jijunwan
 *  Archive Log:    added single app instance check
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 17:54:02  jijunwan
 *  Archive Log:    made setup wizard works within our app
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/15 20:27:02  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/14 17:10:20  fernande
 *  Archive Log:    Passed flag to indicate first run
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:20:23  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;

public abstract class FMGuiPlugin implements FMGui {

    private AppContext appContext;

    @Override
    public void init(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public AppContext getAppContext() {
        return appContext;
    }
}
