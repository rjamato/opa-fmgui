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
