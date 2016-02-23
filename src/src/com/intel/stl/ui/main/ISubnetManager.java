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
 *  File Name: ISubnetManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.15  2015/10/29 12:11:43  robertja
 *  Archive Log:    PR 131014 MailNotifier is now updated if user changes events or recipients in wizard after start-up.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/17 18:53:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/08/10 17:30:41  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/06/10 19:24:48  rjtierne
 *  Archive Log:    PR 128975 - Can not setup application log
 *  Archive Log:    Changed references of List<AppenderConfig> to LoggingConfiguration
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/31 19:54:54  fisherma
 *  Archive Log:    Added method to get app info for the about dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/31 17:47:24  rjtierne
 *  Archive Log:    - Added/implemented methods getHostIp() and isHostConnectable()
 *  Archive Log:    - Removed call to checkConnectivity() in startSubnet()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/25 17:56:18  rjtierne
 *  Archive Log:    Added maps to keep track of secure certificate files for each subnet
 *  Archive Log:    and clear key factories at appropriate times
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/26 22:50:56  rjtierne
 *  Archive Log:    Added method isHostReachable() to test for the existence of a host
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/24 14:47:19  fernande
 *  Archive Log:    Changes to the UI to display only subnet with the Autoconnect option at startup. If no subnet is defined as Autoconnect, then a blank screen is shown.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/12 22:09:02  fernande
 *  Archive Log:    SubnetDescription
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/10 19:34:41  fernande
 *  Archive Log:    Changes to handle SubnetConnectionExceptions
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/06 17:35:56  fernande
 *  Archive Log:    Fixed UserSettings not being saved off to the database in the SetupWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/06 15:10:16  fernande
 *  Archive Log:    Changes to use the new subnetId for SubnetDescription instead of the subnet name
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/02 20:36:35  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/30 20:27:16  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.util.List;

import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.LoggingConfiguration;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.main.view.SplashScreen;
import com.intel.stl.ui.publisher.TaskScheduler;

public interface ISubnetManager {

    boolean isFirstRun();

    List<SubnetDescription> getSubnets();

    SubnetDescription getNewSubnet();

    UserSettings getUserSettings(String subnetName, String userName)
            throws UserNotFoundException;

    PMConfigBean getPMConfig(SubnetDescription subnet);

    TaskScheduler getTaskScheduler(SubnetDescription subnet);

    SubnetDescription saveSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException;

    void removeSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException;

    void saveUserSettings(String subnetName, UserSettings userSettings);

    boolean tryToConnect(SubnetDescription subnet)
            throws SubnetConnectionException;

    void startSubnet(String subnetName) throws SubnetConnectionException;

    void stopSubnet(String subnetName, boolean forceWindowClose);

    void selectSubnet(String subnetName) throws SubnetConnectionException;

    void showSetupWizard(String subnetName, IFabricController controller);

    void init(boolean isFirstRun);

    void startSubnets(SplashScreen splashScreen);

    void cleanup();

    void saveLoggingConfiguration(LoggingConfiguration loggingConfig);

    LoggingConfiguration getLoggingConfig();

    public String getHostIp(String hostName) throws SubnetConnectionException;

    public boolean isHostReachable(String hostName);

    public boolean isHostConnectable(SubnetDescription subnet)
            throws ConfigurationException;

    public void clearSubnetFactories(SubnetDescription subnet);

    public IConfigurationApi getConfigurationApi();

    /**
     * <i>Description:</i>
     * 
     * @param subnetName
     * @return
     */
    SubnetDescription getSubnet(String subnetName);

    /**
     * <i>Description: Send test email to addresses listed in recipients.</i>
     * 
     * @param recipients
     */
    void onEmailTest(String recipients);
    
    /**
     * <i>Description: Get Context object.</i>
     * 
     * @param subnetDescription
     * @return
     */
     public Context getContext(SubnetDescription subnetDescription);

}
