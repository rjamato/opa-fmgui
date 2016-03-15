/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.21  2015/12/17 21:51:10  jijunwan
 *  Archive Log:    PR 132124 - Newly created VF not displayed after reboot of SM
 *  Archive Log:    - improved the arch to do cache reset
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/08/17 18:48:51  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/08/17 14:22:57  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/07/30 19:29:34  fernande
 *  Archive Log:    PR 129592 - removing a subnet a user is monitoring cause internal DB exception. Added flag to SubnetContext indicating the subnet has been deleted. If the flag is set, no saving of subnet information occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/07/09 18:39:44  fernande
 *  Archive Log:    PR 129447 - Database size increases a lot over a short period of time. Added method to expose application settings in the settings.xml file to higher levels in the app
 *  Archive Log:
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
import com.intel.stl.api.logs.ILogApi;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDescription;

public interface SubnetContext {

    /**
     * 
     * <i>Description:</i> returns the Configuration API
     * 
     * @return {@link com.intel.stl.api.configuration.IConfigurationApi}
     */
    IConfigurationApi getConfigurationApi();

    /**
     * 
     * <i>Description:</i> returns the Subnet API
     * 
     * @return {@link com.intel.stl.api.subnet.ISubnetApi}
     */
    ISubnetApi getSubnetApi();

    /**
     * 
     * <i>Description:</i> returns the Performance API
     * 
     * @return {@link com.intel.stl.api.performance.IPerformanceApi}
     */
    IPerformanceApi getPerformanceApi();

    /**
     * 
     * <i>Description:</i> returns the Notice API
     * 
     * @return {@link com.intel.stl.api.notice.INoticeApi}
     */
    INoticeApi getNoticeApi();

    /**
     * 
     * <i>Description:</i> returns the Management API
     * 
     * @return {@link com.intel.stl.api.management.IManagementApi}
     */
    IManagementApi getManagementApi();

    /**
     * 
     * <i>Description: returns the Log API</i>
     * 
     * @return {@link com.intel.stl.api.logs.ILogApi}
     */
    public ILogApi getLogApi();

    /**
     * 
     * <i>Description:</i> returns the subnet description for this context
     * 
     * @return {@link com.intel.stl.api.subnet.SubnetDescription}
     */
    SubnetDescription getSubnetDescription();

    /**
     * 
     * <i>Description:</i> returns the user settings for the specified user name
     * 
     * @param userName
     * @return {@link com.intel.stl.api.configuration.UserSettings}
     * @throws UserNotFoundException
     */
    UserSettings getUserSettings(String userName) throws UserNotFoundException;

    /**
     * 
     * <i>Description:</i> refreshes the user settings in this SubnetContext to
     * those of the specified user
     * 
     * @param userName
     *            the user name
     * @throws UserNotFoundException
     */
    void refreshUserSettings(String userName) throws UserNotFoundException;

    /**
     * 
     * <i>Description:</i> gets the specified application setting name for the
     * application, returning the provided default value if not defined.
     * Application settings are defined through the settings.xml file; they are
     * used to fine tune the application.
     * 
     * @param settingName
     * @param defaultValue
     *            the default value if the setting has not been defined
     * @return the setting value
     */
    String getAppSetting(String settingName, String defaultValue);

    /**
     * 
     * <i>Description:</i> initializes this SubnetContext. This method should be
     * invoked only once.
     * 
     * @throws SubnetConnectionException
     */
    void initialize() throws SubnetConnectionException;

    /**
     * 
     * <i>Description:</i> starts or stops the notice simulator to simulate
     * fabric activity and showcase the UI.
     * 
     * @param random
     *            a boolean; a value of true starts the notice simulator and a
     *            value of false stops it
     */
    void setRandom(boolean random);

    /**
     * 
     * <i>Description:</i> adds a subnet event listener interested on subnet
     * events
     * 
     * @param listener
     */
    void addSubnetEventListener(ISubnetEventListener listener);

    /**
     * 
     * <i>Description:</i> removes the subnet event listener
     * 
     * @param listener
     */
    void removeSubnetEventListener(ISubnetEventListener listener);

    /**
     * 
     * <i>Description:</i> adds a failover progress listener; this listener
     * receives events related to a failover. A failover occurs when the Subnet
     * Manager and/or its components become unresponsive and the application
     * needs to use one of the secondary managers.
     * 
     * @param listener
     */
    void addFailoverProgressListener(PropertyChangeListener listener);

    /**
     * 
     * <i>Description:</i> remove a failover progress listener.
     * 
     * @param listener
     */
    void removeFailoverProgressListener(PropertyChangeListener listener);

    /**
     * 
     * <i>Description:</i> cleans up this SubnetContext. This usually happens
     * when the UI viewer closes
     * 
     */
    void cleanup();

    /**
     * 
     * <i>Description:</i> sets the deleted flag
     * 
     * @param deleted
     */
    void setDeleted(boolean deleted);

    /**
     * 
     * <i>Description:</i> indicates whether this SubnetContext is valid.
     * Typically, communications errors with the FE may render a SubnetContext
     * invalid.
     * 
     * @return valid flag
     */
    boolean isValid();

    /**
     * 
     * <i>Description:</i> indicates whether the FE session has been closed.
     * This usually happens when the UI viewer for this subnet is closed.
     * 
     * @return closed flag
     */
    boolean isClosed();

    /**
     * 
     * <i>Description:</i> indicates whether the subnet definition associated
     * with this SubnetContext has been deleted. This happens when the user
     * deletes the definition through the Setup Wizard.
     * 
     * @return deleted flag
     */
    boolean isDeleted();

    /**
     * 
     * <i>Description:</i> reset to clear all cached data
     * 
     */
    void reset();
}
