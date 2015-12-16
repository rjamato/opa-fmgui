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
 *  File Name: SubnetController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.27  2015/08/17 18:54:04  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/07/17 20:48:25  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - introduced isEditValid to allow us check whether we have valid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/05/11 12:37:06  rjtierne
 *  Archive Log:    PR 128585 - Fix errors found by Klocwork and FindBugs
 *  Archive Log:    In constructor, added null pointer protection to view since invocation of this
 *  Archive Log:    class flagged potential null error when SubnetWizardView is created
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/29 19:14:16  rjtierne
 *  Archive Log:    Handle case in validateUserEntry() to not update the model if a connection test is being
 *  Archive Log:    peformed.
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/03 14:43:11  rjtierne
 *  Archive Log:    Removed print statement
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/03/31 17:48:32  rjtierne
 *  Archive Log:    Added/Implemented setConnectable()
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/03/30 15:12:09  rjtierne
 *  Archive Log:    - Changed subnet wizard tab name to "Hosts"
 *  Archive Log:    - Created new subnet to test based on host info in connectActionPerformed()
 *  Archive Log:    - Passing new subnet to tryToConnect() in connectActionPerformed()
 *  Archive Log:    - Setting connection status in subnet wizard view using backup host name
 *  Archive Log:    - No longer setting primary master in updateModel
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/03/25 17:56:27  rjtierne
 *  Archive Log:    Clear subnet key factories in CertAssistant in connectActionPerformed() test
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/03/20 21:06:46  rjtierne
 *  Archive Log:    - Modified validateUserEntry() to validate all backup hosts
 *  Archive Log:    - During subnet test, don't update the subnet view
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/03/16 17:46:17  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/03/11 15:24:53  rjtierne
 *  Archive Log:    Multinet Wizard: In method connectActionPerformed() call local validateUserEntry() to verify entries and throw
 *  Archive Log:    WizardValidationException if necessary.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/02/27 15:34:33  rjtierne
 *  Archive Log:    Fixed preferences model/view sync issue when creating new subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/02/25 17:59:37  rjtierne
 *  Archive Log:    Updated subnetModel with the user's selection for auto-connect
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/20 21:13:27  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting synchronization of all sub-wizard data with selected subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/13 21:31:58  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/09 21:57:17  jijunwan
 *  Archive Log:    put tasks on SwingWorker so we do not query DB or FE on EDT
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/06 17:36:56  fernande
 *  Archive Log:    Fixed UserSettings not being saved off to the database in the SetupWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/06 15:12:54  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/02 20:38:26  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/01/30 20:28:59  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/21 21:21:20  rjtierne
 *  Archive Log:    Supplying preferences wizard with sweep interval through Context
 *  Archive Log:    for comparison with refresh rate supplied by user input. Also providing
 *  Archive Log:    task scheduler to preferences wizard so user supplied refresh rate can
 *  Archive Log:    be updated.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/20 19:13:14  rjtierne
 *  Archive Log:    Changed onApply() to return a boolean to indicate success/failure
 *  Archive Log:    Removed requirement to test connection before adding it to the DB
 *  Archive Log:    Apply button is no longer greyed out
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/11 21:48:15  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/23 18:34:42  rjtierne
 *  Archive Log:    Added setDirty() method
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/19 18:54:46  rjtierne
 *  Archive Log:    Disable the Apply button on startup so the user is forced to attempt subnet connection before proceeding.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 20:01:12  rjtierne
 *  Archive Log:    Removed the disabling of Next/Apply buttons to make Wizard functional again
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:02  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Controller for the Subnet Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl.subnet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.main.ISubnetManager;
import com.intel.stl.ui.wizards.impl.IMultinetWizardListener;
import com.intel.stl.ui.wizards.impl.IMultinetWizardTask;
import com.intel.stl.ui.wizards.impl.IWizardListener;
import com.intel.stl.ui.wizards.impl.InteractionType;
import com.intel.stl.ui.wizards.impl.WizardValidationException;
import com.intel.stl.ui.wizards.model.IModelChangeListener;
import com.intel.stl.ui.wizards.model.IWizardModel;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.subnet.SubnetModel;
import com.intel.stl.ui.wizards.view.subnet.HostInfoPanel;
import com.intel.stl.ui.wizards.view.subnet.SubnetWizardView;

public class SubnetWizardController implements IMultinetWizardTask,
        ISubnetControl, IModelChangeListener<IWizardModel> {

    private static Logger log = LoggerFactory
            .getLogger(SubnetWizardController.class);

    private LinkedHashMap<String, SubnetDescription> subnets;

    private SubnetWizardView view = null;

    private final ISubnetManager subnetMgr;

    private boolean done;

    private IMultinetWizardListener multinetWizardController;

    private SubnetModel subnetModel;

    private SubnetDescription newSubnet;

    @SuppressWarnings("unused")
    private boolean connectable;

    private boolean connectionTest = false;

    /**
     * 
     * Description Constructor for the Subnet Wizard Controller:
     * 
     * @param view
     *            - View for the Subnet Wizard Controller
     * 
     * @param subnetMgr
     *            - subnet manager
     */
    public SubnetWizardController(SubnetWizardView view,
            ISubnetManager subnetMgr) {

        this.subnetMgr = subnetMgr;
        loadSubnets();

        if (view != null) {
            this.view = view;
            view.setControlListener(this);
            view.setWizardListener(this);
            view.setDirty(false);
        } else {
            log.error(STLConstants.K3045_SUBNET_VIEW_NULL.getValue());
        }
    }

    /**
     * 
     * Description: Constructor for the Multinet Subnet Wizard Controller
     * 
     * @param view
     *            - View for the Subnet Wizard Controller
     * 
     * @param subnetModel
     *            - data model for the subnet
     * 
     * @param subnetMgr
     *            - subnet manager
     */
    public SubnetWizardController(SubnetWizardView view,
            SubnetModel subnetModel, ISubnetManager subnetMgr) {

        this(view, subnetMgr);
        this.subnetModel = subnetModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.ISubnetControl#addSubnetListener(com.intel
     * .stl.ui.admin.ISubnetListener)
     */
    @Override
    public void addSubnetListener(ISubnetListener listener) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K0052_HOSTS.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#getView()
     */
    @Override
    public JComponent getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#init()
     */
    @Override
    public void init() {

        if (!multinetWizardController.isNewWizard()) {
            view.resetPanel();
        }

        view.setDirty(false);
        view.enableNext(false);
        done = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#setDone(boolean)
     */
    @Override
    public void setDone(boolean done) {

        this.done = done;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#isDone()
     */
    @Override
    public boolean isDone() {

        return done;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#onApply()
     */
    @Override
    public boolean validateUserEntry() throws WizardValidationException {

        boolean success = false;
        List<HostInfoPanel> hostPanelList = view.getHostPanelList();

        // Validate port entries
        for (HostInfoPanel pnl : hostPanelList) {

            try {
                int portNum = Integer.valueOf(pnl.getPortNum());
                if (portNum <= 0) {
                    throw new WizardValidationException(
                            UILabels.STL80002_INVALID_PORT_NUMBER, portNum);
                }
            } catch (NumberFormatException nfe) {
                WizardValidationException wve =
                        new WizardValidationException(
                                UILabels.STL80007_PORT_INVALID_FORMAT,
                                pnl.getPortNum());
                throw wve;
            }
        }

        // If not doing a connection test, update model with the valid entries
        // This makes it possible to reset the host values if changes need
        // to be abandoned
        if (!connectionTest) {
            updateModel();
        }

        // If we make it this far update the selected button
        if (!multinetWizardController.isNewWizard()) {
            multinetWizardController.getView().updateSelectedButtonText(
                    view.getSubnetName());
        }

        // If we've made it this far, it's a success
        done = true;
        success = true;
        return success;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.ISubnetControl#connectActionPerformed(String
     * )
     */
    @Override
    public void connectActionPerformed(final HostInfo hostInfo) {

        // Create a subnet for this host
        final SubnetDescription subnet =
                new SubnetDescription(hostInfo.getHost(), hostInfo.getHost(),
                        Integer.valueOf(hostInfo.getPort()));
        List<HostInfo> hostInfoList = new ArrayList<HostInfo>();
        hostInfoList.add(hostInfo);
        subnet.setFEList(hostInfoList);
        subnet.setCurrentFEIndex(0);

        // Clear the key factories for this subnet
        multinetWizardController.clearSubnetFactories(subnet);

        try {
            connectionTest = true;

            if (validateUserEntry()) {
                SwingWorker<Boolean, Void> worker =
                        new SwingWorker<Boolean, Void>() {

                            @Override
                            protected Boolean doInBackground() throws Exception {
                                boolean isConnected =
                                        multinetWizardController
                                                .tryToConnect(subnet);
                                return isConnected;
                            }

                            /*
                             * (non-Javadoc)
                             * 
                             * @see javax.swing.SwingWorker#done()
                             */
                            @Override
                            protected void done() {
                                Boolean isConnected = false;
                                Throwable error = null;
                                try {
                                    isConnected = get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                    error = e.getCause();
                                }
                                subnet.setLastStatus(isConnected ? SubnetDescription.Status.VALID
                                        : SubnetDescription.Status.INVALID);

                                if (isConnected) {
                                    // Enable the "Apply" and "Next" buttons
                                    multinetWizardController.getView()
                                            .enableApply(true);
                                    multinetWizardController.getView()
                                            .enableNext(true);
                                    view.setConnectionStatus(
                                            hostInfo.getHost(),
                                            STLConstants.K3031_PASS.getValue());
                                    view.logMessage(UILabels.STL50017_CONNECTION_EST
                                            .getDescription());
                                } else if (error != null) {
                                    view.setConnectionStatus(
                                            hostInfo.getHost(),
                                            STLConstants.K3032_FAIL.getValue());
                                    view.showMessage(
                                            UILabels.STL50059_CONNECTION_FAILURE_PROCEED
                                                    .getDescription(
                                                            subnet.getName(),
                                                            subnet.getCurrentFE()
                                                                    .getHost(),
                                                            StringUtils
                                                                    .getErrorMessage(error)),
                                            JOptionPane.WARNING_MESSAGE);
                                } else {
                                    view.setConnectionStatus(
                                            hostInfo.getHost(),
                                            STLConstants.K3032_FAIL.getValue());
                                    view.showMessage(
                                            UILabels.STL50059_CONNECTION_FAILURE_PROCEED
                                                    .getDescription(
                                                            subnet.getName(),
                                                            subnet.getCurrentFE()
                                                                    .getHost(),
                                                            STLConstants.K0016_UNKNOWN
                                                                    .getValue()),
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            }

                        };
                worker.execute();
            } else {
                view.setConnectionStatus(subnet.getName(),
                        STLConstants.K3032_FAIL.getValue());
            }
        } catch (WizardValidationException e) {
            e.printStackTrace();
        } finally {

            connectionTest = false;
        }
    }

    protected void loadSubnets() {
        List<SubnetDescription> dbSubnets = null;
        dbSubnets = subnetMgr.getSubnets();
        if (dbSubnets != null) {
            subnets = new LinkedHashMap<String, SubnetDescription>();
            for (SubnetDescription dbSubnet : dbSubnets) {
                subnets.put(dbSubnet.getName(), dbSubnet);
            }
            log.info("Subnets: " + subnets);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.subnet.ISubnetControl#getSubnet()
     */
    @Override
    public SubnetDescription getSubnet() {

        return subnetModel.getSubnet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#cleanup()
     */
    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#selectStep(java.lang.String)
     */
    @Override
    public void selectStep(String taskName) {

        multinetWizardController.selectStep(taskName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#onPrevious()
     */
    @Override
    public void onPrevious() {

        view.enableNext(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#onReset()
     */
    @Override
    public void onReset() {

        // view.resetPanel();
        view.update(subnetModel);
        view.setDirty(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#clear()
     */
    @Override
    public void clear() {
        view.clearPanel();
        subnetModel.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#isDirty()
     */
    @Override
    public boolean isDirty() {

        return view.isDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {

        view.setDirty(dirty);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#doInteractiveAction(com.intel
     * .stl.ui.wizards.impl.InteractionType, java.lang.Object)
     */
    @Override
    public void doInteractiveAction(InteractionType action, Object... data) {

        switch (action) {

            case CHANGE_WIZARDS:

                if (data == null) {
                    return;
                }

                String taskName = (String) data[0];

                // setDone(true);
                if (taskName != null) {
                    onReset();
                    view.closeStatusPanel();
                    selectStep(taskName);
                }
                break;

            case SAVE_LOGGING:
                // NOP
                break;

            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#updateModel()
     */
    @Override
    public synchronized void updateModel() {

        if (subnetModel == null) {
            return;
        }

        // Update the local subnet model
        if (subnetModel.getSubnet() != null) {

            // Get the subnet from the model
            newSubnet = subnetModel.getSubnet();
            newSubnet.setName(view.getSubnetName());
            newSubnet.setAutoConnect(view.isAutoConnected());

            // Loop through the backup hosts
            List<HostInfo> feList = new ArrayList<HostInfo>();
            for (HostInfoPanel hostInfoPanel : view.getHostPanelList()) {
                HostInfo hostInfo = new HostInfo();
                hostInfo.setHost(hostInfoPanel.getHostName());
                hostInfo.setPort(Integer.valueOf(hostInfoPanel.getPortNum()));
                hostInfo.setSecureConnect(hostInfoPanel.isSecureConnection());
                CertsDescription certs = new CertsDescription();
                certs.setKeyStoreFile(hostInfoPanel.getKeyStoreFile());
                certs.setTrustStoreFile(hostInfoPanel.getTrustStoreFile());
                hostInfo.setCertsDescription(certs);

                feList.add(hostInfo);
            }
            newSubnet.setFEList(feList);

            subnetModel.setSubnet(newSubnet);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.subnet.ISubnetControl#getNewsubnet()
     */
    @Override
    public SubnetDescription getNewSubnet() {
        return newSubnet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#promoteModel(com.intel.stl.
     * ui.wizards.model.MultinetWizardModel)
     */
    @Override
    public void promoteModel(MultinetWizardModel topModel) {

        // Promote the subnet model to the top model
        topModel.setSubnetModel(subnetModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.model.IModelChangeListener#onModelChange(com
     * .intel.stl.ui.wizards.model.IWizardModel)
     */
    @Override
    public void onModelChange(IWizardModel m) {

        MultinetWizardModel model = (MultinetWizardModel) m;
        view.updateView(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IWizardTask#setWizardController(com.intel
     * .stl.ui.wizards.impl.IWizardListener)
     */
    @Override
    public void setWizardController(IWizardListener controller) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.impl.IMultinetWizardTask#setWizardController
     * (com.intel.stl.ui.wizards.impl.IMultinetWizardListener)
     */
    @Override
    public void setWizardController(IMultinetWizardListener controller) {
        multinetWizardController = controller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.subnet.ISubnetControl#getSubnetModel()
     */
    @Override
    public SubnetModel getSubnetModel() {

        return subnetModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#setConnectable(boolean)
     */
    @Override
    public void setConnectable(boolean connectable) {
        this.connectable = connectable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.impl.IWizardTask#isEditValid()
     */
    @Override
    public boolean isEditValid() {
        return view.isEditValid();
    }

}
