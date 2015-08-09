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
 *  File Name: ManagementApi.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/04/28 21:54:59  jijunwan
 *  Archive Log:    improved LoginAssistant to support setting owner
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/25 19:10:05  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 22:01:05  jijunwan
 *  Archive Log:    changed package name from application to applications, and from devicegroup to devicegroups
 *  Archive Log:    Added #getType to ServiceID, MGID, LongNode and their subclasses,
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:41:42  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:35  jijunwan
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

package com.intel.stl.api.management.impl;

import java.util.List;
import java.util.Set;

import com.intel.stl.api.ILoginAssistant;
import com.intel.stl.api.management.FMConfHelper;
import com.intel.stl.api.management.IManagementApi;
import com.intel.stl.api.management.applications.Application;
import com.intel.stl.api.management.applications.ApplicationException;
import com.intel.stl.api.management.applications.IApplicationManangement;
import com.intel.stl.api.management.applications.impl.ApplicationManagement;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.devicegroups.DeviceGroupException;
import com.intel.stl.api.management.devicegroups.IDeviceGroupManagement;
import com.intel.stl.api.management.devicegroups.impl.DeviceGroupManagement;
import com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement;
import com.intel.stl.api.management.virtualfabrics.VirtualFabric;
import com.intel.stl.api.management.virtualfabrics.VirtualFabricException;
import com.intel.stl.api.management.virtualfabrics.impl.VirtualFabricManagement;
import com.intel.stl.api.subnet.SubnetDescription;

/**
 * This class provides access to FM configuration management functions. It
 * intend to be stateless. It's the caller's responsibility to do the
 * synchronizations etc based on business logic. This class also provide safe
 * check to ensure the data to be change is valid.
 */
public class ManagementApi implements IManagementApi {
    private final FMConfHelper confHelper;

    private final IApplicationManangement appMgt;

    private final IDeviceGroupManagement groupMgt;

    private final IVirtualFabricManagement vfMgt;

    public ManagementApi(SubnetDescription subnet) {
        confHelper = FMConfHelper.getInstance(subnet);
        appMgt = new ApplicationManagement(confHelper);
        groupMgt = new DeviceGroupManagement(confHelper);
        vfMgt = new VirtualFabricManagement(confHelper);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.IManagementApi#setLoginAssistant(com.intel
     * .stl.api.ILoginAssistant)
     */
    @Override
    public void setLoginAssistant(ILoginAssistant loginAssistant) {
        confHelper.setLoginAssistant(loginAssistant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IManagementApi#getLoginAssistant()
     */
    @Override
    public ILoginAssistant getLoginAssistant() {
        return confHelper.getLoginAssistant();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IManagementApi#refresh()
     */
    @Override
    public void refresh() throws Exception {
        confHelper.getConfFile(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.IManagementApi#deplaoy(boolean)
     */
    @Override
    public void deploy(boolean restart) throws Exception {
        confHelper.deployConf();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.applications.IApplicationManangement#
     * getReservedApplications()
     */
    @Override
    public Set<String> getReservedApplications() {
        return appMgt.getReservedApplications();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.IApplicationManangement#
     * getApplications()
     */
    @Override
    public List<Application> getApplications() throws ApplicationException {
        return appMgt.getApplications();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.IApplicationManangement#
     * getApplication(java.lang.String)
     */
    @Override
    public Application getApplication(String name) throws ApplicationException {
        return appMgt.getApplication(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.IApplicationManangement#
     * addApplication(com.intel.stl.api.management.application.Application)
     */
    @Override
    public void addApplication(Application app) throws ApplicationException {
        appMgt.addApplication(app);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.IApplicationManangement#
     * removeApplication(java.lang.String)
     */
    @Override
    public void removeApplication(String appName) throws ApplicationException {
        appMgt.removeApplication(appName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.IApplicationManangement#
     * updateApplication(com.intel.stl.api.management.application.Application)
     */
    @Override
    public void updateApplication(String oldName, Application app)
            throws ApplicationException {
        appMgt.updateApplication(oldName, app);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.application.IApplicationManangement#
     * addOrUpdateApplication
     * (com.intel.stl.api.management.application.Application)
     */
    @Override
    public void addOrUpdateApplication(String oldName, Application app)
            throws ApplicationException {
        appMgt.addOrUpdateApplication(oldName, app);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * getReservedDeviceGroups()
     */
    @Override
    public Set<String> getReservedDeviceGroups() {
        return groupMgt.getReservedDeviceGroups();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * getDeviceGroups()
     */
    @Override
    public List<DeviceGroup> getDeviceGroups() throws DeviceGroupException {
        return groupMgt.getDeviceGroups();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * getDeviceGroup(java.lang.String)
     */
    @Override
    public DeviceGroup getDeviceGroup(String name) throws DeviceGroupException {
        return groupMgt.getDeviceGroup(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * addDeviceGroup(com.intel.stl.api.management.devicegroups.DeviceGroup)
     */
    @Override
    public void addDeviceGroup(DeviceGroup group) throws DeviceGroupException {
        groupMgt.addDeviceGroup(group);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * removeDeviceGroup(java.lang.String)
     */
    @Override
    public void removeDeviceGroup(String name) throws DeviceGroupException {
        groupMgt.removeDeviceGroup(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * updateDeviceGroup(java.lang.String,
     * com.intel.stl.api.management.devicegroups.DeviceGroup)
     */
    @Override
    public void updateDeviceGroup(String oldName, DeviceGroup group)
            throws DeviceGroupException {
        groupMgt.updateDeviceGroup(oldName, group);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * addOrUpdateDeviceGroup(java.lang.String,
     * com.intel.stl.api.management.devicegroups.DeviceGroup)
     */
    @Override
    public void addOrUpdateDeviceGroup(String oldName, DeviceGroup group)
            throws DeviceGroupException {
        groupMgt.addOrUpdateDeviceGroup(oldName, group);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * getReservedVirtualFabrics()
     */
    @Override
    public Set<String> getReservedVirtualFabrics() {
        return vfMgt.getReservedVirtualFabrics();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * getVirtualFabrics()
     */
    @Override
    public List<VirtualFabric> getVirtualFabrics()
            throws VirtualFabricException {
        return vfMgt.getVirtualFabrics();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * getVirtualFabric(java.lang.String)
     */
    @Override
    public VirtualFabric getVirtualFabric(String name)
            throws VirtualFabricException {
        return vfMgt.getVirtualFabric(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * addVirtualFabric
     * (com.intel.stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void addVirtualFabric(VirtualFabric vf)
            throws VirtualFabricException {
        vfMgt.addVirtualFabric(vf);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * removeVirtualFabric(java.lang.String)
     */
    @Override
    public void removeVirtualFabric(String name) throws VirtualFabricException {
        vfMgt.removeVirtualFabric(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * updateVirtualFabric(java.lang.String,
     * com.intel.stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void updateVirtualFabric(String oldName, VirtualFabric vf)
            throws VirtualFabricException {
        vfMgt.updateVirtualFabric(oldName, vf);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.management.virtualfabrics.IVirtualFabricManagement#
     * addOrUpdateVirtualFabric(java.lang.String,
     * com.intel.stl.api.management.virtualfabrics.VirtualFabric)
     */
    @Override
    public void addOrUpdateVirtualFabric(String oldName, VirtualFabric vf)
            throws VirtualFabricException {
        vfMgt.addOrUpdateVirtualFabric(oldName, vf);
    }

}
