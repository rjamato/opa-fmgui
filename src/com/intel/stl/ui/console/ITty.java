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
 *  File Name: ITty.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/10 14:09:07  rjtierne
 *  Archive Log:    PR 126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added method closeChannel() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/09 21:13:23  rjtierne
 *  Archive Log:    126675 - User cannot execute commands on duplicate Console numbers beyond 10 consoles.
 *  Archive Log:    Added method closeChannel() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/28 22:22:23  rjtierne
 *  Archive Log:    Added remote host "history" to command dialog
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/23 19:46:16  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Interface for the IntelTty class
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.console;

import com.jcraft.jsch.Session;
import com.wittams.gritty.Tty;

// Added this comment to correct PR 126675 comment above
public interface ITty extends Tty {

    public boolean isConnected();

    public boolean initialize() throws Exception;

    public Session getSession();

    public void setSession(Session session);

    public boolean isEnableMsgListener();

    public void enableMsgListener(boolean enableMsgListener);

    public void closeChannel();

}
