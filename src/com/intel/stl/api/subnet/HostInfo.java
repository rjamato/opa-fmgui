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
 *  File Name: HostInfo.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/10/06 15:51:14  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Updated toString() to include the new hostType attribute.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/10/01 21:27:36  fernande
 *  Archive Log:    PR130409 - [Dell]: FMGUI Admin Console login fails when switch is configured without username and password. Added HostType to HostInfo bean
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/17 18:48:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/07/28 18:20:31  fisherma
 *  Archive Log:    PR 129219 - Admin page login dialog improvement.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/06/08 15:59:44  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code. Adding IConnectionAssistant field so that other layers can provide an assistant, otherwise it falls back to the DefaultConnectionAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/05/04 22:22:30  jijunwan
 *  Archive Log:    set CertsDescription to be transient by mistake. Change it back now, and make it to be Serializable since HostInfo is Serializable
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/01 21:40:40  jijunwan
 *  Archive Log:    fixed Serializable issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/08 15:17:11  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/30 15:09:27  rjtierne
 *  Archive Log:    Added constructor to allow CertsDescription to be passed
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/16 17:37:45  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/09 13:51:30  fernande
 *  Archive Log:    Bean to hold host information for primary SM and backup SMs
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.io.Serializable;
import java.net.InetAddress;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.IConnectionAssistant;

public class HostInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String host;

    private HostType hostType;

    private int port = 3245;

    private boolean secureConnect;

    private CertsDescription certsDescription = new CertsDescription();

    private InetAddress inetAddress;

    private IConnectionAssistant connectionAssistant;

    private String sshUserName = "";

    private int sshPortNum = 22;

    public HostInfo() {
    }

    public HostInfo(String host, int port) {
        this.host = host;
        this.port = port;
        this.secureConnect = false;
    }

    public HostInfo(String host, int port, CertsDescription certsDescription) {
        this.host = host;
        this.port = port;
        this.certsDescription = certsDescription;
        this.secureConnect = true;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public HostType getHostType() {
        return hostType;
    }

    public void setHostType(HostType hostType) {
        this.hostType = hostType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSecureConnect() {
        return secureConnect;
    }

    public void setSecureConnect(boolean secureConnect) {
        this.secureConnect = secureConnect;
    }

    public CertsDescription getCertsDescription() {
        return certsDescription;
    }

    public void setCertsDescription(CertsDescription certsDescription) {
        this.certsDescription = certsDescription;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public IConnectionAssistant getConnectionAssistant() {
        return connectionAssistant;
    }

    public void setConnectionAssistant(IConnectionAssistant connectionAssistant) {
        this.connectionAssistant = connectionAssistant;
    }

    public HostInfo copy() {
        HostInfo newHostInfo = new HostInfo();
        newHostInfo.setHost(host);
        newHostInfo.setPort(port);
        newHostInfo.setSecureConnect(secureConnect);
        if (certsDescription != null) {
            CertsDescription newCertsDescription =
                    new CertsDescription(certsDescription.getKeyStoreFile(),
                            certsDescription.getTrustStoreFile());
            newCertsDescription.setKeyStorePwd(certsDescription
                    .getKeyStorePwd());
            newCertsDescription.setTrustStorePwd(certsDescription
                    .getTrustStorePwd());
            newHostInfo.setCertsDescription(newCertsDescription);
        } else {
            newHostInfo.setCertsDescription(null);
        }
        newHostInfo.setConnectionAssistant(connectionAssistant);
        newHostInfo.setSshUserName(sshUserName);
        newHostInfo.setSshPortNum(sshPortNum);

        return newHostInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((certsDescription == null) ? 0 : certsDescription
                                .hashCode());
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        result = prime * result + (secureConnect ? 1231 : 1237);
        return result;
    }

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
        HostInfo other = (HostInfo) obj;
        if (certsDescription == null) {
            if (other.certsDescription != null) {
                return false;
            }
        } else if (!certsDescription.equals(other.certsDescription)) {
            return false;
        }
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        if (secureConnect != other.secureConnect) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HostInfo [host=" + host + ", hostType=" + hostType + ", port="
                + port + ", secureConnect=" + secureConnect
                + ", certsDescription=" + certsDescription + ", sshUserName="
                + sshUserName + ", sshPortNum=" + sshPortNum + "]";
    }

    // If sslUserName is not set, an empty string is returned.
    public String getSshUserName() {
        return sshUserName;
    }

    public void setSshUserName(String sslUserName) {
        this.sshUserName = sslUserName;
    }

    public int getSshPortNum() {
        return sshPortNum;
    }

    public void setSshPortNum(int sslPortNum) {
        this.sshPortNum = sslPortNum;
    }

}
