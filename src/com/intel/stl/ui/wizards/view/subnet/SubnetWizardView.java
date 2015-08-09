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
 *  File Name: LoggingWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.24.2.1  2015/05/06 19:39:20  jijunwan
 *  Archive Log:    changed to directly show exception(s)
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
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

        try {
            dirty = false;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public SubnetWizardView(IMultinetWizardView wizardViewListener) {
        super("");
        this.multinetWizardViewListener = wizardViewListener;
        this.chooser = new JFileChooser();

        try {
            dirty = false;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        createDocumentListener();
        multinetWizardViewListener.assignDocumentListeners(isDirtyListener,
                setDirtyListener);
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

    protected JTextField createTextField(String txt) {
        if (isDirtyListener == null || setDirtyListener == null) {
            createDocumentListener();
        }

        JTextField txtField = new JTextField(txt);
        txtField.getDocument().addDocumentListener(setDirtyListener);
        txtField.getDocument().addDocumentListener(isDirtyListener);
        return txtField;
    }

    /**
     * 
     * <i>Description: Document listeners to detect when changes occur to the
     * subnet wizard fields</i>
     * 
     */
    protected void createDocumentListener() {
        isDirtyListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                dirty = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                dirty = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                dirty = true;
            }

        };

        setDirtyListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                setDirty();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setDirty();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setDirty();
            }
        };
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

        if ((multinetWizardViewListener.getTxtfldSubnetName().getText()
                .length() > 0)
                && (hostPanelCount > 0)
                && (basicRequirement && secureRequirement)) {

            multinetWizardViewListener.enableNext(true);
            multinetWizardViewListener.enableApply(true);
            multinetWizardViewListener.enableReset(true);
        } else {
            multinetWizardViewListener.enableNext(false);
            multinetWizardViewListener.enableApply(false);
            multinetWizardViewListener.enableReset(false);
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
        multinetWizardViewListener.setTxtfldSubnetName(subnet.getName());

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.subnet.ISubnetView#setSubnetName(java.lang
     * .String)
     */
    @Override
    public void setSubnetName(String subnetName) {
        multinetWizardViewListener.setTxtfldSubnetName(subnetName);

        dirty = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.subnet.ISubnetView#getSubnetName()
     */
    @Override
    public String getSubnetName() {
        return multinetWizardViewListener.getTxtfldSubnetName().getText();
    }

    protected void refresh() {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                pnlHostContainer.removeAll();
                for (HostInfoPanel pnl : hostPanelList) {
                    pnlHostContainer.add(pnl);
                    if (pnl.getHostName().equals("")) {
                        pnl.setFocus();
                    }

                    if (hostPanelList.indexOf(pnl) == 0) {
                        pnl.enableRemove(false);
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
}