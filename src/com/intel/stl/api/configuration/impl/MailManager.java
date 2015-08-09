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
 *  File Name: MailManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/22 16:51:39  fernande
 *  Archive Log:    Reorganized the startup sequence so that the UI plugin could initialize its own CertsAssistant. This way, autoconnect subnets would require a password using the UI CertsAssistant instead of the default CertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/04 21:29:36  jijunwan
 *  Archive Log:    added Mail Manager
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import static com.intel.stl.common.STLMessages.STL10019_MAIL_COMPONENT;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.MailProperties;
import com.intel.stl.configuration.AppComponent;
import com.intel.stl.configuration.AppConfigurationException;
import com.intel.stl.configuration.AppSettings;

public class MailManager implements AppComponent {
    private static Logger log = LoggerFactory.getLogger(MailManager.class);

    private final Map<MailProperties, MailSender> mailSenders =
            new HashMap<MailProperties, MailSender>();

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.configuration.AppComponent#initialize(com.intel.stl.
     * configuration.AppSettings)
     */
    @Override
    public void initialize(AppSettings settings)
            throws AppConfigurationException {
        // for now, nothing to do. In the future may improve to include message
        // type, such as plain text, html etc.
    }

    @Override
    public String getComponentDescription() {
        return STL10019_MAIL_COMPONENT.getDescription();
    }

    @Override
    public int getInitializationWeight() {
        return 5;
    }

    public void registerMailProperties(MailProperties properties) {
        if (properties == null) {
            return;
        }

        synchronized (mailSenders) {
            if (!mailSenders.containsKey(properties)) {
                MailSender sender = new MailSender(properties);
                mailSenders.put(properties, sender);
            }
        }
    }

    public void deregisterMailProperties(MailProperties properties) {
        if (properties == null) {
            return;
        }

        synchronized (mailSenders) {
            mailSenders.remove(properties);
        }
    }

    public void updateMailProperties(MailProperties oldProperties,
            MailProperties newProperties) {
        if (oldProperties == null) {
            registerMailProperties(newProperties);
        } else if (newProperties == null) {
            deregisterMailProperties(oldProperties);
        } else {
            synchronized (mailSenders) {
                MailSender sender = mailSenders.remove(oldProperties);
                if (sender != null) {
                    sender.setProperties(newProperties);
                } else {
                    sender = new MailSender(newProperties);
                }
                mailSenders.put(newProperties, sender);
            }
        }
    }

    public void submitMessage(MailProperties properties, String subject,
            String body) {
        MailSender sender = null;
        synchronized (mailSenders) {
            sender = mailSenders.get(properties);
            if (sender == null) {
                sender = new MailSender(properties);
                mailSenders.put(properties, sender);
            }
        }

        sender.submitMessage(properties.getFromAddr(), properties.getToAddr(),
                subject, body);
    }

    public void shutdown() {
        for (MailSender sender : mailSenders.values()) {
            try {
                sender.shutdown();
            } catch (Throwable e) {
                log.warn(StringUtils.getErrorMessage(e), e);
            }
        }
        mailSenders.clear();
    }

}
