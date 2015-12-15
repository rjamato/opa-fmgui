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
 *  File Name: IManagementApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/10/06 15:50:48  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Added cleanup() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/09/28 13:53:23  fisherma
 *  Archive Log:    PR 130425 - added cancel button to allow user to cancel out of hung or slow ssh logins.  Cancel action terminates sftp connection and closes remote ssh session.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/17 18:49:02  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/17 17:30:47  jijunwan
 *  Archive Log:    PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log:    - improved FmConfHelper to get ride of ILoginAssistence and deploy with password
 *  Archive Log:    - added tmp FM conf helper that deal with conf file with temporary connection
 *  Archive Log:    - renamed testConnection to fetchConfigFile
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/07/28 18:20:25  fisherma
 *  Archive Log:    PR 129219 - Admin page login dialog improvement.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/07/09 17:55:44  jijunwan
 *  Archive Log:    PR 129509 - Shall refresh UI after failover completed
 *  Archive Log:    - added reset method to ManagermentApi so we can reset after failover completed
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/28 21:55:00  jijunwan
 *  Archive Log:    improved LoginAssistant to support setting owner
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/25 19:10:09  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 22:02:28  jijunwan
 *  Archive Log:    Added #getType to LongNode
 *  Archive Log:    Added devicegroup to management api
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:41:43  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:37  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

import com.intel.stl.api.management.applications.IApplicationManagement;
import com.intel.stl.api.management.devicegroups.IDeviceGroupManagement;
import com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;

public interface IManagementApi extends IApplicationManagement,
        IDeviceGroupManagement, IVirtualFabricManagement {

    /**
     * 
     * <i>Description:</i> reset the ManagementApi so that when we need to get
     * opafm.xml it will try to get a fresh copy from FM. Typical use case is
     * that after a fail over, we reset this ManagementApi.
     * 
     */
    void reset();

    /**
     * 
     * <i>Description:</i> indicate whether there are changes on opafm.xml
     * 
     * @return
     */
    boolean hasChanges();

    /**
     * 
     * <i>Description:</i> deploy local opafm.xml to current SM
     * 
     * @param password
     * @param restart
     *            indicate whether we restart FM after copy file to SMs
     */
    void deploy(char[] password, boolean restart) throws Exception;

    /**
     * 
     * <i>Description:</i> deploy conf changes to a specified SM
     * 
     * @param password
     * @param target
     * @throws Exception
     */
    void deployTo(char[] password, HostInfo target) throws Exception;

    /**
     * 
     * <i>Description:</i> get SubnetDescription
     */
    public SubnetDescription getSubnetDescription();

    /**
     * 
     * <i>Description:</i> returns true if opafm.xml config file from server is
     * present in the FMConfHelper
     */
    public boolean isConfigReady();

    /**
     * 
     * <i>Description:</i> return true if we already have a valid ssh connection
     * with the subnet
     * 
     * @return
     */
    public boolean hasSession();

    /**
     * 
     * <i>Description:</i> Try to ssh to the server and cache opafm.xml in
     * FMConfHelper
     * 
     * @param password
     *            password to use for the ssh connection
     */
    public void fetchConfigFile(char[] password) throws Exception;

    /**
     * <i>Description:</i> call this method on login cancellation by user to
     * terminate ssh/ftp/sftp connection which might be still in progress
     * 
     */
    public void onCancelFetchConfig(SubnetDescription subnet);

    /**
     * 
     * <i>Description: Shut down the session when the subnet is closed </i>
     * 
     */
    public void cleanup();

}
