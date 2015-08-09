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
 *  File Name: LoginBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2014/10/28 22:19:47  rjtierne
 *  Archive Log:    Added copy constructor
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/23 19:47:00  rjtierne
 *  Archive Log:    Integration of Gritty for Java Console
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/09 20:03:28  rjtierne
 *  Archive Log:    Added default login bean to console dialog to reduce typing
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/05 21:56:28  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/04 21:05:06  rjtierne
 *  Archive Log:    Added password and portNum fields
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/22 19:53:57  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Java bean containing the information related to a single SSH
 *  console connection
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.console;

import com.jcraft.jsch.Session;

/**
 * @author rjtierne
 * 
 */
public class LoginBean {

    private String userName;

    private String hostName;

    private String password;

    private String portNum = "22";

    private Session session;

    public LoginBean() {

    }

    public LoginBean(String userName, String hostName, String portNum) {
        this.userName = userName;
        this.hostName = hostName;
        this.portNum = portNum;
    }

    /**
     * Copy Constructor
     */
    public LoginBean(LoginBean bean) {
        this(bean.getUserName(), bean.getHostName(), bean.getPortNum());
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
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the portNum
     */
    public String getPortNum() {
        return portNum;
    }

    /**
     * @param portNum
     *            the portNum to set
     */
    public void setPortNum(String portNum) {
        this.portNum = portNum;
    }

    @Override
    public String toString() {
        return "LoginBean [userName=" + userName + ", hostName=" + hostName
                + ", portNum = " + portNum + "]";

    }

}
