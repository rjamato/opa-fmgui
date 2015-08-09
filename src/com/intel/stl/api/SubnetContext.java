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
 *  File Name: SubnetContext.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.16  2015/04/27 21:45:12  rjtierne
 *  Archive Log:    Removed method setUserSettings() from interface
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/15 17:04:46  rjtierne
 *  Archive Log:    Updated the interface to include new method setUserSettings()
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/31 16:13:35  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/27 20:30:11  fernande
 *  Archive Log:    Adding support for failover. The UI can now setup objects to listen to SubnetEvents the the ISubnetEventListener. Listener register in the SubnetContext
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/26 11:10:00  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/25 11:26:57  jypak
 *  Archive Log:    Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    The Event Calculator and the Event Summary Table clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/05 17:30:40  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/02 22:29:53  fernande
 *  Archive Log:    Changed AppContext to allow for changes of the subnet name and still retrieve the corresponding SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/10 19:28:31  fernande
 *  Archive Log:    SubnetContext are now created after a successful connection is made to the Fabric Executive, otherwise a SubnetConnectionException is triggered. Also, waitForConnect has been postponed until the UI invokes SubnetContext.initialize (thru Context.initialize). This way the UI shows up faster and the UI progress bar reflects more closely the process.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/06 14:59:03  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/14 20:44:52  jijunwan
 *  Archive Log:    1) improved to set SubnetContext invalid when we have network connection issues
 *  Archive Log:    2) improved to recreate SubnetContext when we query for it and the current one is invalid. We also clean up (include shutdown) the old context before we replace it with a new one
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/14 11:26:53  jypak
 *  Archive Log:    UI related updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/19 18:11:00  jijunwan
 *  Archive Log:    introduced cleanup method to do cleanup before we shutdown an app
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/06 15:17:32  jijunwan
 *  Archive Log:    updated configuration and context to include notice
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/17 22:14:10  jijunwan
 *  Archive Log:    Added subnetDescription to SubnetContext
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/15 20:27:02  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.beans.PropertyChangeListener;

import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDescription;

public interface SubnetContext {

    public IConfigurationApi getConfigurationApi();

    public ISubnetApi getSubnetApi();

    public IPerformanceApi getPerformanceApi();

    public INoticeApi getNoticeApi();

    public IManagementApi getManagementApi();

    public SubnetDescription getSubnetDescription();

    public UserSettings getUserSettings(String userName)
            throws UserNotFoundException;

    // retrieve user setting fresh out of database
    public void refreshUserSettings(String userName)
            throws UserNotFoundException;

    public void initialize() throws SubnetConnectionException;

    public void setRandom(boolean random);

    void addSubnetEventListener(ISubnetEventListener listener);

    void removeSubnetEventListener(ISubnetEventListener listener);

    void addFailoverProgressListener(PropertyChangeListener listener);

    void removeFailoverProgressListener(PropertyChangeListener listener);

    public void cleanup();

    public boolean isValid();

    boolean isClosed();
}
