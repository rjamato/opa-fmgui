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
 *  File Name: MailProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/08/12 15:21:40  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
