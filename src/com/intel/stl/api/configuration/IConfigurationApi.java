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
package com.intel.stl.api.configuration;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.api.subnet.SubnetConnectionException;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;

/**
 * @author jijunwan
 * 
 */
public interface IConfigurationApi {
    boolean tryToConnect(SubnetDescription subnet)
            throws SubnetConnectionException;

    void registerCertsAssistant(ICertsAssistant providor);

    AppInfo getAppInfo();

    void saveAppProperties(Map<String, Properties> appProperties);

    List<SubnetDescription> getSubnets();

    SubnetDescription defineSubnet(SubnetDescription subnet);

    void updateSubnet(SubnetDescription subnet)
            throws SubnetDataNotFoundException;;

    void removeSubnet(long subnetId) throws SubnetDataNotFoundException;

    SubnetDescription getSubnet(String subnetName)
            throws SubnetDataNotFoundException;

    SubnetDescription getSubnet(long subnetId);

    List<AppenderConfig> getLoggingConfig() throws ConfigurationException;

    void saveLoggingConfiguration(List<AppenderConfig> config)
            throws ConfigurationException;

    List<EventRule> getEventRules() throws EventNotFoundException;

    void saveEventRules(List<EventRule> rules) throws ConfigurationException;

    String getLogPropertyPath();

    UserSettings getUserSettings(String subnetName, String userName)
            throws UserNotFoundException;

    void saveUserSettings(String subnetName, UserSettings settings);

    void startSimulatedFailover(String subnetName);

    void cleanup();

    PMConfigBean getPMConfig(SubnetDescription subnet);

    String getHostIp(String hostName) throws SubnetConnectionException;

    boolean isHostReachable(String hostName);

    boolean isHostConnectable(SubnetDescription subnet)
            throws ConfigurationException;

    /**
     * 
     * <i>Description:</i> register a mail properties, so we can use it to send
     * messages
     * 
     * @param properties
     */
    public void registerMailProperties(MailProperties properties);

    /**
     * 
     * <i>Description:</i> remove a mail properties if we do not need it anymore
     * 
     * @param properties
     */
    public void deregisterMailProperties(MailProperties properties);

    /**
     * 
     * <i>Description:</i> change mail properties to new value. this will impact
     * the mail sender to create a new transport
     * 
     * @param oldProperties
     * @param newProperties
     */
    public void updateMailProperties(MailProperties oldProperties,
            MailProperties newProperties);

    /**
     * 
     * <i>Description:</i> schedule sending a email with the specified mail
     * properties. cache strategy may apply to share transport among messages
     * with the same mail properties (see {@link MailProperties#equals}).
     * 
     * @param properties
     * @param subject
     * @param message
     */
    public void submitMessage(MailProperties properties, String subject,
            String body);
}
