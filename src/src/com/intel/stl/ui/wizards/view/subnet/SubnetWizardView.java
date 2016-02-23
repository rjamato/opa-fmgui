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
 *  File Name: LoggingWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.30  2015/11/09 20:56:20  fernande
 *  Archive Log:    PR130852 - The 1st subnet in the Subnet Wizard displays "Abandon Changes" message when no changes are made. Added special listener for dirty state
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/10/06 20:21:48  fernande
 *  Archive Log:    PR130749 - FM GUI virtual fabric information doesn't match opafm.xml file. Removed external access to textfield
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/08/17 18:54:33  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/07/22 19:12:15  jijunwan
 *  Archive Log:    PR 129647 - unremovable duplicate hosts in setup wizard
 *  Archive Log:    - fixed to only make the first one unremovable
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/07/17 20:48:22  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - introduced isEditValid to allow us check whether we have valid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/05/01 21:29:13  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/29 19:14:53  rjtierne
 *  Archive Log:    Don't update the model when running a connection test
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/28 22:09:02  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/04/27 21:48:06  rjtierne
 *  Archive Log:    - Call setDirty() to enable the Apply & Reset buttons if the auto-connect button state changes
 *  Archive Log:    - PR 128358 - Fabric Viewer not Working:
 *  Archive Log:    In method update() catch IllegalArgumentException when calling getCurrentFE() and display error
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/04/21 21:19:08  rjtierne
 *  Archive Log:    Set enable for Reset button in setDirty()
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/04/07 20:17:42  jijunwan
 *  Archive Log:    second round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/04/06 22:53:48  jijunwan
 *  Archive Log:    first round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/03 14:44:37  rjtierne
 *  Archive Log:    Added showFileChooser() method to center file browser over the wizard window
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/03/30 15:13:51  rjtierne
 *  Archive Log:    - Moved connection test panel to HostInfoPanel
 *  Archive Log:    - Updated panel backgrounds to use static variable
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/03/25 17:56:57  rjtierne
 *  Archive Log:    Move JFileChooser to the subnet wizard view level so previously traversed
 *  Archive Log:    directory locations are remembered
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/03/20 21:08:22  rjtierne
 *  Archive Log:    Changed user interface to support multiple backup hosts in a single subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/16 17:47:13  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/11 15:26:42  rjtierne
 *  Archive Log:    Multinet Wizard: Removed title from titled border
 *  Archive Log:    Using text field creation methods in ComponentFactory with input verifiers
 *  Archive Log:    Added panel to test connection
 *  Archive Log:    Removed subnet name text field and replaced with references to the one in the MultinetWizardView
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/26 22:55:04  rjtierne
 *  Archive Log:    Restored AutoConnect
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/25 18:01:13  rjtierne
 *  Archive Log:    Added back the auto-connect feature
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/20 21:16:07  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/13 21:31:55  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/09 22:00:05  jijunwan
 *  Archive Log:    temporary solution - treat new subnet name as intend to define a new subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/06 15:13:43  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/20 19:14:32  rjtierne
 *  Archive Log:    Temporarily disabled button to test connectivity; subnets can be added without testing
 *  Archive Log:    the connection. Connection test will change to a checkbox in the future.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/11 21:48:19  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/19 19:48:37  rjtierne
 *  Archive Log:    Added array of Objects to showMessage()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/11 20:01:08  rjtierne
 *  Archive Log:    Removed the disabling of Next/Apply buttons to make Wizard functional again
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 18:50:00  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:07  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: View for the Subnet Wizard
 *
 *  @author: jypak, rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.view.subnet;

// Java imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.DocumentDirtyListener;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.JScrollablePanel;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.subnet.ISubnetControl;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.subnet.SubnetModel;
import com.intel.stl.ui.wizards.view.AbstractTaskView;
import com.intel.stl.ui.wizards.view.IMultinetWizardView;
import com.intel.stl.ui.wizards.view.IWizardView;
import com.intel.stl.ui.wizards.view.MultinetWizardView;

public class SubnetWizardView extends AbstractTaskView implements ISubnetView,
        IHostInfoListener {

    private static final long serialVersionUID = -4237965084816535532L;

    private static Logger log = LoggerFactory.getLogger(SubnetWizardView.class);

    private JPanel pnlHostContainer;

    private final List<HostInfoPanel> hostPanelList =
            new ArrayList<HostInfoPanel>();

    private JCheckBox chkboxAutoConnect;

    private boolean dirty;

    private ISubnetControl subnetControlListener;

    @SuppressWarnings("unused")
    private IWizardTask subnetWizardControlListener;

    private DocumentListener isDirtyListener;

    private DocumentListener setDirtyListener;

    @SuppressWarnings("unused")
    private IWizardView wizardViewListener = null;

    private IMultinetWizardView multinetWizardViewListener = null;

    private final IHostInfoListener hostInfoListener = this;

    private final JFileChooser chooser;

    /**
     * Main constructor. Creates a subnet dialog and sets this objects tree to
     * the parents tree.
     * 
     * @param frame
     *            parent Frame
     * @param title
     *            title for this dialog
     * @param tree
     *            parent tree to add subnet to
     * @param modal
     *            modality of this dialog.
     */
    public SubnetWizardView(IWizardView wizardViewListener) {
        super("");
        this.wizardViewListener = wizardViewListener;
        this.chooser = new JFileChooser();
        dirty = false;
    }

    public SubnetWizardView(IMultinetWizardView wizardViewListener) {
        super("");
        this.multinetWizardViewListener = wizardViewListener;
        this.chooser = new JFileChooser();
        dirty = false;

        createDocumentListener();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.AbstractTaskView#getOptionComponent()
     */
    @Override
    protected JComponent getOptionComponent() {

        JLabel lblConnectionType =
                ComponentFactory.getH5Label(
                        STLConstants.K3034_CONNECTION_TYPE.getValue(),
                        Font.BOLD);

        chkboxAutoConnect =
                ComponentFactory
                        .getIntelCheckBox(STLConstants.K0604_AUTO_CONNECT
                                .getValue());
        chkboxAutoConnect.setText(STLConstants.K0604_AUTO_CONNECT.getValue());
        chkboxAutoConnect.setFont(UIConstants.H5_FONT.deriveFont(Font.PLAIN));
        chkboxAutoConnect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setDirty();
            }
        });

        // Create the optionPane
        JPanel optionPane = new JPanel(new BorderLayout());
        optionPane.setOpaque(true);
        optionPane.setBackground(MultinetWizardView.WIZARD_COLOR);

        // // Add a title panel
        // optionPane.add(getTitlePanel(), BorderLayout.NORTH);

        // Create the container to hold the host info
        pnlHostContainer = new JScrollablePanel();
        pnlHostContainer.setLayout(new VerticalLayout(10));
        pnlHostContainer.setBackground(MultinetWizardView.WIZARD_COLOR);
        pnlHostContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlHostContainer.add(HostAdderPanel.getInstance(this));

        // Put the host info container panel on a scrollpane
        JScrollPane scrpnHostInfo = new JScrollPane(pnlHostContainer);
        scrpnHostInfo.getViewport().setBackground(
                MultinetWizardView.WIZARD_COLOR);
        scrpnHostInfo
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrpnHostInfo.getViewport().setViewSize(new Dimension(100, 100));
        scrpnHostInfo.setBorder(null);
        // scrpnHostInfo.setBorder(BorderFactory.createLineBorder(
        // UIConstants.INTEL_BORDER_GRAY, 1));
        optionPane.add(scrpnHostInfo, BorderLayout.CENTER);

        // Create the panel with the connection type
        JPanel pnlConnectionType = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlConnectionType.setBackground(MultinetWizardView.WIZARD_COLOR);
        pnlConnectionType.add(lblConnectionType);
        pnlConnectionType.add(chkboxAutoConnect);

        // Create the connection panel and add the connection test and type
        JPanel pnlConnection = new JPanel();
        pnlConnection.setLayout(new BoxLayout(pnlConnection, BoxLayout.Y_AXIS));
        pnlConnection.add(pnlConnectionType);
        optionPane.add(pnlConnection, BorderLayout.SOUTH);

        return optionPane;
    }

    protected JPanel getTitlePanel() {
        JPanel pnlTitle = new JPanel();
        pnlTitle.setBackground(MultinetWizardView.WIZARD_COLOR);
        pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.X_AXIS));

        pnlTitle.add(Box.createHorizontalStrut(45));
        pnlTitle.add(ComponentFactory.getH5Label(
                STLConstants.K3037_FE_CONNECTION.getValue(), Font.BOLD));

        pnlTitle.add(Box.createHorizontalStrut(98));
        pnlTitle.add(ComponentFactory.getH5Label(
                STLConstants.K3038_SECURITY_INFO.getValue(), Font.BOLD));

        pnlTitle.add(Box.createHorizontalStrut(74));
        pnlTitle.add(ComponentFactory.getH5Label(
                STLConstants.K3033_CONNECTION_TEST.getValue(), Font.BOLD));

        return pnlTitle;
    }

    public void stopConnectionTest() {
        for (HostInfoPanel pnl : hostPanelList) {
            pnl.stopConnectionTest();
        }
    }

    public void setConnectionStatus(String subnetName, String status) {

        boolean found = false;
        HostInfoPanel hostInfoPanel = null;
        Iterator<HostInfoPanel> it = hostPanelList.iterator();

        while (!found && it.hasNext()) {
            hostInfoPanel = it.next();
            found = hostInfoPanel.getHostName().equals(subnetName);
        }

        if (found) {
            hostInfoPanel.setConnectionStatus(status);
        }
    }

    /**
     * 
     * <i>Description: Document listeners to detect when changes occur to the
     * subnet wizard fields</i>
     * 
     */
    protected void createDocumentListener() {
        isDirtyListener = new DocumentDirtyListener() {

            @Override
            public void setDirty(DocumentEvent e) {
                dirty = true;
            }

        };

        setDirtyListener = new DocumentDirtyListener() {

            @Override
            public void setDirty(DocumentEvent e) {
                setDirtyFlag();
            }

        };
    }

    private void setDirtyFlag() {
        setDirty();
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#getDocumentListeners
     * ()
     */
    @Override
    public DocumentListener[] getDocumentListeners() {
        return new DocumentListener[] { isDirtyListener, setDirtyListener };
    }

    // Overridden in case caller tries to make us visible
    // after we've been killed:
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public void enableNext(boolean enable) {
    }

    public boolean isAutoConnected() {
        return chkboxAutoConnect.isSelected();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#resetPanel()
     */
    @Override
    public void setSubnet(SubnetDescription subnet) {

    }

    @Override
    public void setDirty() {
        dirty = true;

        int hostNameCount = 0;
        int portNumCount = 0;
        int secureCount = 0;
        int keyStoreCount = 0;
        int trustStoreCount = 0;
        int hostPanelCount = hostPanelList.size();
        for (HostInfoPanel pnl : hostPanelList) {
            if (pnl.isHostNamePopulated()) {
                hostNameCount++;
            }

            if (pnl.isPortNumPopulated()) {
                portNumCount++;
            }

            if (pnl.isSecureConnection()) {
                secureCount++;
                if (pnl.isKeyStorePopulated()) {
                    keyStoreCount++;
                }

                if (pnl.isTrustStorePopulated()) {
                    trustStoreCount++;
                }
            }
        }

        boolean basicRequirement =
                ((hostNameCount == hostPanelCount) && (portNumCount == hostPanelCount));

        boolean secureRequirement =
                ((keyStoreCount == secureCount) && (trustStoreCount == secureCount));

        if ((multinetWizardViewListener.getSubnetName().length() > 0)
                && (hostPanelCount > 0)
                && (basicRequirement && secureRequirement)) {

            multinetWizardViewListener.enableNext(true);
            multinetWizardViewListener.enableApply(true);
            multinetWizardViewListener.enableReset(true);
        } else {
            multinetWizardViewListener.enableNext(false);
            multinetWizardViewListener.enableApply(false);
            multinetWizardViewListener.enableReset(true);
        }
    }

    public void clearPanel() {
        pnlHostContainer.removeAll();

        dirty = false;
    }

    @Override
    public void resetPanel() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#isDirty()
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        multinetWizardViewListener.enableApply(dirty);
        multinetWizardViewListener.enableReset(dirty);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#update(com.intel.stl.ui.wizards
     * .model.MultinetWizardModel)
     */
    @Override
    public void updateView(final MultinetWizardModel model) {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                SubnetModel subnetModel = model.getSubnetModel();
                SubnetDescription subnet = subnetModel.getSubnet();

                if (subnet != null) {
                    update(subnetModel);
                }
                dirty = false;
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.ISubnetView#update(com.intel.stl
     * .ui.wizards.model.subnet.SubnetModel)
     */
    @Override
    public void update(SubnetModel subnetModel) {
        HostInfo host = null;
        SubnetDescription subnet = subnetModel.getSubnet();

        try {
            host = subnet.getCurrentFE();
        } catch (IllegalArgumentException e) {
            Util.showError(this, e);
        }

        hostPanelList.clear();
        for (HostInfo hostInfo : subnet.getFEList()) {
            HostInfoPanel pnlHostInfo = new HostInfoPanel(this, chooser);
            pnlHostInfo.setCurrentMaster(hostInfo.equals(host));
            pnlHostInfo.setHostName(hostInfo.getHost());
            pnlHostInfo.setPortNum(String.valueOf(hostInfo.getPort()));
            pnlHostInfo.setKeyStoreFile(hostInfo.getCertsDescription()
                    .getKeyStoreFile());
            pnlHostInfo.setTrustFileFile(hostInfo.getCertsDescription()
                    .getTrustStoreFile());
            pnlHostInfo.setSecureConnection(hostInfo.isSecureConnect());
            hostPanelList.add(pnlHostInfo);
        }
        refresh();
        chkboxAutoConnect.setSelected(subnet.isAutoConnect());

        dirty = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.ISubnetView#showMessage(java.lang
     * .String, int)
     */
    @Override
    public void showMessage(String message, int messageType) {
        showMessage(message, null, messageType, (Object[]) null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.ISubnetView#setControlListener(com
     * .intel.stl.ui.wizards.impl.subnet.ISubnetControl)
     */
    @Override
    public void setControlListener(ISubnetControl listener) {

        subnetControlListener = listener;
    }

    @Override
    public void setWizardListener(IWizardTask listener) {
        super.setWizardListener(listener);
        subnetWizardControlListener = listener;
    }

    protected void refresh() {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                pnlHostContainer.removeAll();
                boolean isFirst = true;
                for (HostInfoPanel pnl : hostPanelList) {
                    pnlHostContainer.add(pnl);
                    if (pnl.getHostName().equals("")) {
                        pnl.setFocus();
                    }

                    if (isFirst) {
                        pnl.enableRemove(false);
                        isFirst = false;
                    }
                }

                pnlHostContainer.add(HostAdderPanel
                        .getInstance(hostInfoListener));
                ((JScrollPane) pnlHostContainer.getParent().getParent())
                        .scrollRectToVisible(pnlHostContainer.getBounds());
                pnlHostContainer.scrollRectToVisible(new Rectangle(0,
                        (int) pnlHostContainer.getPreferredSize().getHeight(),
                        10, 10));
                setDirty();
                repaint();
                revalidate();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#addHost()
     */
    @Override
    public void addHost() {
        hostPanelList.add(new HostInfoPanel(this, chooser));
        refresh();
        multinetWizardViewListener.enableRun(false);
        dirty = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#removeHost(com
     * .intel.stl.ui.wizards.view.subnet.HostInfoPanel)
     */
    @Override
    public void removeHost(HostInfoPanel pnlHostInfo) {

        boolean found = false;
        Iterator<HostInfoPanel> it = hostPanelList.iterator();

        while (!found && it.hasNext()) {
            HostInfoPanel pnl = it.next();
            if (pnl.equals(pnlHostInfo)) {
                hostPanelList.remove(pnl);
                found = true;
            }
        }
        refresh();
    }

    public List<HostInfoPanel> getHostPanelList() {
        return hostPanelList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#isDuplicate
     * ()
     */
    @Override
    public boolean hasDuplicateHosts() {

        Set<HostInfoPanel> hostPanelSet = new HashSet<HostInfoPanel>();

        // If there are duplicate panels, they can't be added to the set
        for (HostInfoPanel pnl : hostPanelList) {
            if (!hostPanelSet.add(pnl)) {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#runConnectionTest
     * (com.intel.stl.ui.wizards.view.subnet.HostInfoPanel)
     */
    @Override
    public void runConnectionTest(HostInfoPanel hostInfoPanel) {

        HostInfo hostInfo;
        if (hostInfoPanel.isSecureConnection()) {
            CertsDescription certsDescription =
                    new CertsDescription(hostInfoPanel.getKeyStoreFile(),
                            hostInfoPanel.getTrustStoreFile());
            hostInfo =
                    new HostInfo(hostInfoPanel.getHostName(),
                            Integer.valueOf(hostInfoPanel.getPortNum()),
                            certsDescription);
        } else {
            hostInfo =
                    new HostInfo(hostInfoPanel.getHostName(),
                            Integer.valueOf(hostInfoPanel.getPortNum()));
        }

        subnetControlListener.connectActionPerformed(hostInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#showMessage(String
     * )
     */
    @Override
    public void showErrorMessage(String errorMessage) {
        Util.showErrorMessage(this, errorMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.IHostInfoListener#showFileChooser()
     */
    @Override
    public int showFileChooser() {

        // Open file chooser centered over the wizard
        return chooser.showOpenDialog(this.getParent().getParent().getParent()
                .getParent());
    }

    public boolean isEditValid() {
        for (HostInfoPanel hip : hostPanelList) {
            if (!hip.isEditValid()) {
                return false;
            }
        }
        return true;
    }
}
