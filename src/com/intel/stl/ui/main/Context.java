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
 *  File Name: Context.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.37  2015/04/29 19:13:51  rjtierne
 *  Archive Log:    When saving a subnet to the database in method close(), run on the EDT to prevent
 *  Archive Log:    collision with deletion of the subnet when running in a window.
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/04/28 22:13:18  jijunwan
 *  Archive Log:    1) introduced component owner to Context, so when we have errors in data collection, preparation etc, we now there the error dialog should go
 *  Archive Log:    2) improved TaskScheduler to show error message on proper frame
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/04/21 21:19:20  fernande
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss. TaskScheduler is shut down when a connection is lost to stop generating requests until failover is complete. At that point, it is restarted.
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/04/20 15:59:47  fernande
 *  Archive Log:    Fix for NPE when failover occurs when a subnet is being started and the TaskScheduler has not been initialized yet.
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/15 18:51:16  fernande
 *  Archive Log:    Improved handling of TimeoutExceptions during failover: TaskScheduler is shutdown during failover and timeouts during failover do not trigger an additional failover process. Refresh functionality has been fixed
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/08 15:20:39  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/06 21:20:28  fernande
 *  Archive Log:    Context adds itself as a SubnetEventListener to be notified by the SubnetContext of issues with the subnet. In particular, when failover fails, the TaskScheduler is shutdown as well as the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/04/02 13:32:54  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/03/31 16:21:47  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/03/30 18:34:40  jypak
 *  Archive Log:    Introduce a UserSettingsProcessor to handle different use cases for user settings via Setup Wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/03/27 20:50:03  fernande
 *  Archive Log:    Adding support for failover
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/03/26 12:04:56  jypak
 *  Archive Log:    Updates for passing correct refresh rate to task scheduler. When the subnet is connected, compare the sweep interval from the subnet and the refresh rate provided by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/03/26 11:10:02  jypak
 *  Archive Log:    PR 126613 Event (State) Severity based on user configuration via setup wizard.
 *  Archive Log:    -The Notice Api retrieves the latest user configuration for the severity through the UserSettings and set the severity when the EventDescription is generated.
 *  Archive Log:    -The Event Calculator clear out event description contents before posting new ones based on new notices with the severities configured by user.
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/03/25 11:53:20  jypak
 *  Archive Log:    Header comments changed
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/03/25 11:52:27  jypak
 *  Archive Log:    Header comments changed
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.ui.main;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.ISubnetEventListener;
import com.intel.stl.api.SubnetContext;
import com.intel.stl.api.SubnetEvent;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.configuration.UserNotFoundException;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.notice.INoticeApi;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.SubnetDescription.Status;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.UserPreference;
import com.intel.stl.ui.publisher.EventCalculator;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.UserSettingsProcessor;

/**
 * @author jijunwan
 * 
 */
public class Context implements ISubnetEventListener {
    private static final Logger log = LoggerFactory.getLogger(Context.class);

    private final SubnetContext subnetContext;

    private TaskScheduler scheduler;

    private EventCalculator evtCal;

    private final String userName;

    private final UserPreference userPreference;

    private UserSettingsProcessor userSettingsProcessor;

    private Component owner;

    public Context(SubnetContext subnetContext, String userName) {
        this.subnetContext = subnetContext;
        this.userName = userName;
        refreshUserSettings();
        UserSettings userSettings = getUserSettings();
        Properties properties = null;
        if (userSettings != null) {
            properties = userSettings.getUserPreference();
        }
        userPreference = new UserPreference(properties);
        this.subnetContext.addSubnetEventListener(this);
    }

    /**
     * @return the owner
     */
    public Component getOwner() {
        return owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(Component owner) {
        if (this.owner != null && owner != this.owner) {
            log.warn("Context " + this + "("
                    + subnetContext.getSubnetDescription().getName()
                    + ") is set to a new owner " + owner);
        }
        this.owner = owner;
    }

    public EventCalculator getEvtCal() {
        return evtCal;
    }

    public void initialize() throws SubnetConnectionException {
        subnetContext.initialize();

        IConfigurationApi configApi = subnetContext.getConfigurationApi();
        PMConfigBean pmConf =
                configApi.getPMConfig(subnetContext.getSubnetDescription());
        int refreshRate = userPreference.getRefreshRateInSeconds();
        if (pmConf != null) {
            int sweepInterval = pmConf.getSweepInterval();
            if (refreshRate > sweepInterval) {
                this.scheduler = new TaskScheduler(this, refreshRate);
            } else {
                this.scheduler = new TaskScheduler(this, sweepInterval);
            }
        } else {
            this.scheduler = new TaskScheduler(this, refreshRate);
        }

        int totalNodes = 0;
        try {
            totalNodes =
                    this.subnetContext.getSubnetApi().getNodes(false).size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        evtCal =
                new EventCalculator(userPreference.getTimeWindowInSeconds(),
                        totalNodes, userPreference.getNumWorstNodes());
        this.subnetContext.getNoticeApi().addEventListener(evtCal);
    }

    public IConfigurationApi getConfigurationApi() {
        return subnetContext.getConfigurationApi();
    }

    public ISubnetApi getSubnetApi() {
        return subnetContext.getSubnetApi();
    }

    public IPerformanceApi getPerformanceApi() {
        return subnetContext.getPerformanceApi();
    }

    public INoticeApi getNoticeApi() {
        return subnetContext.getNoticeApi();
    }

    public IManagementApi getManagementApi() {
        return subnetContext.getManagementApi();
    }

    public UserSettings getUserSettings() {
        UserSettings userSettings = null;
        try {
            userSettings = subnetContext.getUserSettings(userName);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return userSettings;
    }

    public void refreshUserSettings() {
        try {
            subnetContext.refreshUserSettings(userName);

            if (evtCal != null) {
                UserSettings userSettings = getUserSettings();
                if (userSettingsProcessor == null) {
                    userSettingsProcessor =
                            new UserSettingsProcessor(userSettings, evtCal);
                } else {
                    userSettingsProcessor.process(userSettings);
                }
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRandom(boolean random) {
        subnetContext.setRandom(random);
    }

    public void addSubnetEventListener(ISubnetEventListener listener) {
        subnetContext.addSubnetEventListener(listener);
    }

    public void removeSubnetEventListener(ISubnetEventListener listener) {
        subnetContext.removeSubnetEventListener(listener);
    }

    public void addFailoverProgressListener(PropertyChangeListener listener) {
        subnetContext.addFailoverProgressListener(listener);
    }

    public void removeFailoverProgressListener(PropertyChangeListener listener) {
        subnetContext.removeFailoverProgressListener(listener);
    }

    /**
     * @return the apiBroker
     */
    public TaskScheduler getTaskScheduler() {
        return scheduler;
    }

    public SubnetDescription getSubnetDescription() {
        return subnetContext.getSubnetDescription();
    }

    public void close() {
        final Status status =
                subnetContext.isValid() ? Status.VALID : Status.INVALID;
        final IConfigurationApi confApi = subnetContext.getConfigurationApi();
        SubnetDescription subnet = subnetContext.getSubnetDescription();
        final long subnetId = subnet.getSubnetId();

        // Saving the subnet description to the database is done on the EDT
        // to prevent collision with deletion of the subnet while it is running
        // in a window. The EDT will serialize these two operations so there is
        // no resource contention.
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                try {
                    SubnetDescription subnetDesc = confApi.getSubnet(subnetId);
                    subnetDesc.setLastStatus(status);
                    confApi.updateSubnet(subnetDesc);
                } catch (SubnetDataNotFoundException e) {
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });
        cleanup();
    }

    public void cleanup() {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            subnetContext.cleanup();
        }
    }

    public boolean isValid() {
        return subnetContext.isValid();
    }

    @Override
    public void onFailoverCompleted(SubnetEvent event) {
        // Restart the scheduler
        if (scheduler != null) {
            // if the scheduler has been initialized already
            log.debug("Restarting the task scheduler");
            int rate = scheduler.getRefreshRate();
            scheduler.updateRefreshRate(rate);
        }
    }

    @Override
    public void onFailoverFailed(SubnetEvent event) {
        log.debug("Context is cleaning up after failover failed");
        cleanup();
    }

    @Override
    public void onSubnetManagerConnectionLost(SubnetEvent event) {
        log.debug("Stopping the task scheduler");
        // Stop the TaskScheduler when a connection is lost to avoid more
        // requests until failover is completed
        try {
            scheduler.shutdown();
        } catch (Exception e) {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.ISubnetEventListener#onSubnetManagerConnected(com.intel
     * .stl.api.SubnetEvent)
     */
    @Override
    public void onSubnetManagerConnected(SubnetEvent arg0) {
        // TODO Auto-generated method stub

    }
}
