/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: IHostInfoListener.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/21 21:18:59  rjtierne
 *  Archive Log:    Added setDirty() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/03 14:44:37  rjtierne
 *  Archive Log:    Added showFileChooser() method to center file browser over the wizard window
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/30 15:13:28  rjtierne
 *  Archive Log:    - Changed method name to hasDuplicateHosts()
 *  Archive Log:    - Added methods runConnectTest() and showErrorMessage()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/20 21:07:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view.subnet;

import javax.swing.event.DocumentListener;

public interface IHostInfoListener {

    public void addHost();

    public void removeHost(HostInfoPanel pnlHostInfo);

    public DocumentListener[] getDocumentListeners();

    public void setDirty();

    public void setDirty(boolean dirty);

    public boolean hasDuplicateHosts();

    public void runConnectionTest(HostInfoPanel hostInfoPanel);

    public void showErrorMessage(String errorMessage);

    public int showFileChooser();

}
