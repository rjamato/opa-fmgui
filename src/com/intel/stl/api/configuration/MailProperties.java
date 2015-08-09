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
 *  File Name: MailProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/04 21:29:37  jijunwan
 *  Archive Log:    added Mail Manager
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.configuration;

import java.io.Serializable;
import java.util.Arrays;

public class MailProperties implements Serializable {
    private static final long serialVersionUID = -2664394989972287473L;

    private String smtpServer;

    private int smtpPort;

    private String userName;

    private char[] password;

    private String fromAddr;

    private String toAddr;

    private boolean authEnabled;

    private boolean tlsEnabled;

    /**
     * @return the smtpServer
     */
    public String getSmtpServer() {
        return smtpServer;
    }

    /**
     * @param smtpServer
     *            the smtpServer to set
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    /**
     * @return the smtpPort
     */
    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * @param smtpPort
     *            the smtpPort to set
     */
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(char[] password) {
        this.password = password;
    }

    /**
     * @return the fromAddr
     */
    public String getFromAddr() {
        return fromAddr;
    }

    /**
     * @param fromAddr
     *            the fromAddr to set
     */
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    /**
     * @return the toAddr
     */
    public String getToAddr() {
        return toAddr;
    }

    /**
     * @param toAddr
     *            the toAddr to set
     */
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    /**
     * @return the authEnabled
     */
    public boolean isAuthEnabled() {
        return authEnabled;
    }

    /**
     * @param authEnabled
     *            the authEnabled to set
     */
    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    /**
     * @return the tlsEnabled
     */
    public boolean isTlsEnabled() {
        return tlsEnabled;
    }

    /**
     * @param tlsEnabled
     *            the tlsEnabled to set
     */
    public void setTlsEnabled(boolean tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (authEnabled ? 1231 : 1237);
        result = prime * result + smtpPort;
        result =
                prime * result
                        + ((smtpServer == null) ? 0 : smtpServer.hashCode());
        result = prime * result + (tlsEnabled ? 1231 : 1237);
        result =
                prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MailProperties other = (MailProperties) obj;
        if (authEnabled != other.authEnabled) {
            return false;
        }
        if (smtpPort != other.smtpPort) {
            return false;
        }
        if (smtpServer == null) {
            if (other.smtpServer != null) {
                return false;
            }
        } else if (!smtpServer.equals(other.smtpServer)) {
            return false;
        }
        if (tlsEnabled != other.tlsEnabled) {
            return false;
        }
        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MailProperties [smtpServer=" + smtpServer + ", smtpPort="
                + smtpPort + ", userName=" + userName + ", password="
                + Arrays.toString(password) + ", fromAddr=" + fromAddr
                + ", toAddr=" + toAddr + ", authEnabled=" + authEnabled
                + ", tlsEnabled=" + tlsEnabled + "]";
    }

}
