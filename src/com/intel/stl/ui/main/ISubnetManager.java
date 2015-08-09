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
 *  File Name: ISubnetManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.IConfigurationApi;
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

    void saveLoggingConfiguration(List<AppenderConfig> appenders);

    List<AppenderConfig> getLoggingConfig();

    public String getHostIp(String hostName) throws SubnetConnectionException;

    public boolean isHostReachable(String hostName);

    public boolean isHostConnectable(SubnetDescription subnet)
            throws ConfigurationException;

    public void clearSubnetFactories(SubnetDescription subnet);

    public IConfigurationApi getConfigurationApi();

}
