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
 *  File Name: MailManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:22:06  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
