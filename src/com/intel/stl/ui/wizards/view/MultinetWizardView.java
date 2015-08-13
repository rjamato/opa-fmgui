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
 *  File Name: MultinetWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.28.2.2  2015/08/12 15:26:49  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.28.2.1  2015/05/06 19:39:17  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/05/01 21:29:09  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/04/30 21:26:09  rjtierne
 *  Archive Log:    Remove code to set subnet name to lower case.  Duplicate subnet testing
 *  Archive Log:    is done when saving the subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/04/29 19:14:28  rjtierne
 *  Archive Log:    - Make subnet name a "restricted" text field to limit the number of characters that can be
 *  Archive Log:    handled by the database.
 *  Archive Log:    - Fix problem with Apply/Reset buttons become disabled, and warning dialog not coming up
 *  Archive Log:    when a subnet is changed and then abandoned
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/04/28 22:08:59  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/04/28 14:39:39  rjtierne
 *  Archive Log:    PR 128358 - Fabric Viewer not Working:
 *  Archive Log:    - Added panel to the Welcome card layout to display subnet error information when
 *  Archive Log:    user clicks on subnet buttons that catch an exception
 *  Archive Log:    - in onSubnetButtonClick(), call onSubnetError() method to carry out display of error
 *  Archive Log:    message if exception is caught
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/04/27 21:47:53  rjtierne
 *  Archive Log:    PR 128358 - Fabric Viewer not Working:
 *  Archive Log:    In showWizard(), catch IllegalArgumentException caused by getCurrentFE() in onSubnetButtonClick()
 *  Archive Log:    and show an error
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/04/21 21:18:13  rjtierne
 *  Archive Log:    - Added boolean manualSelect to take the place of btnWelcomeOk.setSelected().  This value indicates
 *  Archive Log:    that the onSubnetButtonClick() method is being called manually, and not by means of a button click,
 *  Archive Log:    to ensure the proper behavior when the "current" subnet button is clicked.
 *  Archive Log:    - Added enableReset() to enable/disable the reset button
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/04/18 01:39:57  fisherma
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss.  The code changes address issue #2 reported in the bug.  Adding common dialog to display errors.  Needs further appearance improvements.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/04/17 19:13:37  rjtierne
 *  Archive Log:    Klocwork Issue: Added null pointer protection for btnSelected in onSubnetButtonClick()
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/04/17 18:36:56  rjtierne
 *  Archive Log:    - Klockwork Issue: Added null pointer protection for buttonSource in onSubnetButtonClick()
 *  Archive Log:    - Fixed broken OK button on Welcome screen
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/04/15 20:40:02  rjtierne
 *  Archive Log:    - If subnet is updated and another subnet button is clicked before saving changes,
 *  Archive Log:    ask the user if the changes should be abandoned.
 *  Archive Log:    - Removed feature to ignore button clicks during new subnet configuration
 *  Archive Log:    - Moved setting of newWizardInProgress=false to newWizardCleanup()
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/04/07 20:17:43  jijunwan
 *  Archive Log:    second round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/04/06 22:53:49  jijunwan
 *  Archive Log:    first round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/04/06 11:14:09  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/03 14:43:41  rjtierne
 *  Archive Log:    Fixed bug when closing the wizard when Run is clicked; saves data without warning
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/01 17:04:51  rjtierne
 *  Archive Log:    Added OK button to welcome panel
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/31 20:42:49  fisherma
 *  Archive Log:    Minor UI appearance changes to the setup wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/31 17:49:21  rjtierne
 *  Archive Log:    - Changed K3022_HOST_REACHABILITY to K3022_HOST_CONNECTIVITY
 *  Archive Log:    - In onSubnetButtonClick(), using buttonSource to get current subnet
 *  Archive Log:    - Only clearing key factories for secure subnets
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/30 15:13:04  rjtierne
 *  Archive Log:    - Increased width of wizard dialog
 *  Archive Log:    - Added note labels to each section of the welcome status window
 *  Archive Log:    - Updated panel backgrounds to use static variable
 *  Archive Log:    - Restructured the way the welcome status panel is created to accommodate the notes label
 *  Archive Log:    - Checking for duplicate subnets in onFinish()
 *  Archive Log:    - Updating subnet button with new name in OnFinish() instead of in onNext()
 *  Archive Log:    - Added new method hasDuplicateSubnets()
 *  Archive Log:    - Reduced severity of some messages on welcome panel; now uses warning icon
 *  Archive Log:    - Added message to notes label on welcome panel when task fails
 *  Archive Log:    - Enforcing one subnet configuration at a time
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/25 17:56:48  rjtierne
 *  Archive Log:    - Reorganized panels to fix scrollpane on navigation panel
 *  Archive Log:    - Make call to clear key factories when subnet button clicked
 *  Archive Log:    - Disable buttons during welcome panel
 *  Archive Log:    - Disallow more than one subnet form entry at a time
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/20 21:07:41  rjtierne
 *  Archive Log:    - Made the setup wizard dialog wider to accommodate new host info
 *  Archive Log:    - Update the "Next" button depending on what tab is selected
 *  Archive Log:    - Fixed synchronization problem with selected subnet button
 *  Archive Log:    - Cleaned up test results when switching between subnets
 *  Archive Log:    - Check for duplicate hosts in a single subnet
 *  Archive Log:    - Fix problem with Apply/Run button enable
 *  Archive Log:    - Implemented stopSubnetConnectionTest()
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/16 17:46:53  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/11 15:25:27  rjtierne
 *  Archive Log:    - Multinet Wizard: Add Welcome Panel to provide instructions and report status information during
 *  Archive Log:    subnet configuration.
 *  Archive Log:    - Moved the subnet name field from the subnet view to this view.
 *  Archive Log:    - Added welcome panel to provide instructions and status
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/26 23:56:25  rjtierne
 *  Archive Log:    - Updated setMultinetTasks() so the first tab (Subnet)
 *  Archive Log:    will always appear when the wizard is first opened
 *  Archive Log:    - Removed window listener to fix NullPointerExceptions; wizard
 *  Archive Log:    window is only closed by the user via Close or Run button. If
 *  Archive Log:    application crashes, wizard goes down with it.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/26 16:21:07  fernande
 *  Archive Log:    Changed showSetupWizard so that the wizard can show its view centered on the calling frame.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/25 18:00:56  rjtierne
 *  Archive Log:    - Implemented subnet deletion
 *  Archive Log:    - Using new WizardType enumeration to specify model to update to improve efficiency
 *  Archive Log:    - Simplified method onClose()
 *  Archive Log:    - Standardized warning dialogue into one showWarningDialog()
 *  Archive Log:    - Added null pointer protection
 *  Archive Log:    - Removed button redundancy in maps
 *  Archive Log:    - Using current subnet to select the correct subnet sub-wizards to display
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/23 15:07:12  rjtierne
 *  Archive Log:    Added method to return new wizard status
 *  Archive Log:    Cleared navigation panel and sub-wizard tabs if changes are abandoned
 *  Archive Log:    Added protective null pointer checks
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/20 21:16:10  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/16 05:24:33  jijunwan
 *  Archive Log:    changed dialog size. Will polish the UI after we wizard has reasonable behaviors.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:30:26  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.VerticalLayout;

import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IntelButtonUI;
import com.intel.stl.ui.common.view.IntelTabbedPaneUI;
import com.intel.stl.ui.common.view.ProgressPanel;
import com.intel.stl.ui.common.view.RoundedCornersBorder;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.wizards.impl.ConfigTaskStatus;
import com.intel.stl.ui.wizards.impl.ConfigTaskType;
import com.intel.stl.ui.wizards.impl.IMultinetWizardListener;
import com.intel.stl.ui.wizards.impl.IMultinetWizardTask;
import com.intel.stl.ui.wizards.impl.IWizardListener;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.WizardType;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.subnet.SubnetModel;

public class MultinetWizardView extends JDialog implements IMultinetWizardView {

    private static final long serialVersionUID = 6898168781808453167L;

    public static final Color CONTENT_COLOR = UIConstants.INTEL_WHITE;

    public static final Color WIZARD_COLOR = UIConstants.INTEL_WHITE;

    public static final int DIALOG_WIDTH = 800;

    public static final int DIALOG_HEIGHT = 500;

    private final int MAX_SUBNET_NAME_LEN = 56;

    private IMultinetWizardListener wizardListener;

    private final MultinetWizardModel wizardModel;

    private JPanel pnlNavigation;

    private JPanel pnlNavigationButtons;

    private JPanel pnlSubwizard;

    private JPanel pnlMainCtrl;

    private JButton btnAddSubnet;

    private JButton btnDeleteSubnet;

    private JButton btnApply;

    private JButton btnReset;

    private JButton btnPrevious;

    private JButton btnNext;

    private JButton btnRun;

    private JButton btnClose;

    private JTabbedPane tabbedPane;

    private JPanel pnlMain;

    private JPanel pnlHeading;

    private JPanel pnlSubwizardCtrl;

    private JPanel pnlExistingSubnet;

    private JTextField txtFldSubnetName;

    private boolean nextSelected = false;

    private boolean subnetBtnSelected = false;

    private JButton btnSelected;

    private boolean manualSelect = false;

    private String selectedButtonName;

    private boolean newWizardInProgress = false;

    private List<IMultinetWizardTask> tasks;

    private final CardLayout wizardLayout = new CardLayout();

    private final CardLayout controlLayout = new CardLayout();

    private final CardLayout welcomeLayout = new CardLayout();

    private JPanel pnlWelcomeContent;

    private final LinkedHashMap<String, JButton> subnetButtonMap =
            new LinkedHashMap<String, JButton>();

    private final LinkedHashMap<JButton, SubnetDescription> subnetMap =
            new LinkedHashMap<JButton, SubnetDescription>();

    private ProgressPanel progressPanel;

    private JLabel lblEntryValidation;

    private JLabel lblEntryValidationIcon;

    private JLabel lblEntryValidationStatus;

    private JLabel lblEntryValidationNotes;

    private JLabel lblHostReachability;

    private JLabel lblHostReachabilityIcon;

    private JLabel lblHostReachabilityStatus;

    private JLabel lblHostReachabilityNotes;

    private JLabel lblDatabaseUpdate;

    private JLabel lblDatabaseUpdateIcon;

    private JLabel lblDatabaseUpdateStatus;

    private JLabel lblDatabaseUpdateNotes;

    private JButton btnWelcomeOk;

    private JLabel lblWelcomeError;

    /**
     * 
     * Description: Constructor for the MultinetWizardView
     * 
     * @param owner
     *            top level window of the application to center this dialog
     *            around
     */
    public MultinetWizardView(IFabricView owner, MultinetWizardModel wizardModel) {

        super((JFrame) owner,
                STLConstants.K0667_CONFIG_SETUP_WIZARD.getValue(), true);
        this.wizardModel = wizardModel;

        initComponents();
        this.addWindowListener(new WindowAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent
             * )
             */
            @Override
            public void windowClosed(WindowEvent e) {
                onClose(false);
            }

        });
    }

    /**
     * 
     * <i>Description: Initializes the GUI components for this view</i>
     * 
     */
    protected void initComponents() {
        // Dialog is not resizable
        this.setResizable(false);

        // Configure the layout of the content pane
        JPanel pnlContent = (JPanel) getContentPane();
        pnlContent.setLayout(new BorderLayout(5, 0));
        pnlContent.setBorder(BorderFactory.createLineBorder(
                UIConstants.INTEL_BORDER_GRAY, 2, true));
        pnlContent.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        // Create the subnet navigation panel
        JPanel pnlNavigation = getNavigationPanel();
        pnlContent.add(pnlNavigation, BorderLayout.WEST);

        // Create a main panel on the right side to hold the wizard views and
        // the main control panel
        JPanel pnlMain = getConfPanel();
        pnlContent.add(pnlMain, BorderLayout.CENTER);

        // Add the main control panel
        JPanel pnlMainCtrl = getControlPanel();
        pnlContent.add(pnlMainCtrl, BorderLayout.SOUTH);
    }

    /**
     * 
     * <i>Description:</i> the subnet navigation panel
     * 
     * @return
     */
    protected JPanel getNavigationPanel() {
        if (pnlNavigation == null) {
            pnlNavigation = new JPanel(new BorderLayout(5, 5));
            pnlNavigation.setBackground(UIConstants.INTEL_WHITE);
            pnlNavigation.setBorder(new RoundedCornersBorder(
                    UIConstants.INTEL_BORDER_GRAY, 2, 5));

            // Create the head panel to hold the "Subnets" title
            JLabel pnlNavigationHeader =
                    ComponentFactory.getH4Label(
                            STLConstants.K3013_SUBNETS.getValue(), Font.BOLD);
            pnlNavigationHeader.setHorizontalAlignment(JLabel.CENTER);
            pnlNavigationHeader.setBorder(BorderFactory.createMatteBorder(0, 0,
                    2, 0, UIConstants.INTEL_ORANGE));
            pnlNavigation.add(pnlNavigationHeader, BorderLayout.NORTH);

            // Create the Navigation Button Panel
            pnlNavigationButtons = new JPanel(new VerticalLayout(2));

            // Create the navigation panel on a scroll pane with a minor tweak
            // to
            // scrollbar widths w/out creating a new ScrollBarUI
            JScrollPane scrpnNavigation =
                    new JScrollPane(pnlNavigationButtons,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrpnNavigation.setBorder(null);
            scrpnNavigation.getVerticalScrollBar().setPreferredSize(
                    new Dimension(10, 0));
            scrpnNavigation.getHorizontalScrollBar().setPreferredSize(
                    new Dimension(0, 10));
            scrpnNavigation.setPreferredSize(new Dimension(
                    (int) (DIALOG_WIDTH * .2f), DIALOG_HEIGHT));
            pnlNavigation.add(scrpnNavigation, BorderLayout.CENTER);
        }
        return pnlNavigation;
    }

    protected JPanel getConfPanel() {
        if (pnlMain == null) {
            pnlMain = new JPanel(wizardLayout);
            pnlMain.setBorder(BorderFactory.createLineBorder(
                    UIConstants.INTEL_BORDER_GRAY, 2, true));
            JPanel wizardPanel = getWizardPanel();
            pnlMain.add(wizardPanel, WizardViewType.WIZARD.getName());

            // Create the Welcome panel and add it to the main panel
            JPanel pnlWelcome = createWelcomePanel();
            pnlMain.add(pnlWelcome, WizardViewType.WELCOME.getName());
        }
        return pnlMain;
    }

    protected JPanel getWizardPanel() {

        if (pnlSubwizard == null) {
            pnlSubwizard = new JPanel(new BorderLayout());
            pnlSubwizard.setBorder(BorderFactory.createLineBorder(
                    UIConstants.INTEL_WHITE, 5));

            // Add the subnet name label/text field to the top of the wizard
            // panel
            JLabel lblSubnetName =
                    ComponentFactory
                            .getH5Label(STLConstants.K2111_NAME.getValue()
                                    + ":", Font.BOLD);
            txtFldSubnetName =
                    ComponentFactory
                            .createRestrictedTextField(MAX_SUBNET_NAME_LEN);

            pnlHeading = new JPanel();
            pnlHeading.setBackground(CONTENT_COLOR);
            pnlHeading.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(3, 2, 5, 2);
            gc.weighty = 1;
            gc.weightx = 0;
            gc.gridwidth = 1;
            pnlHeading.add(lblSubnetName, gc);
            gc.weightx = 1;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            pnlHeading.add(txtFldSubnetName, gc);
            pnlSubwizard.add(pnlHeading, BorderLayout.NORTH);

            // Create a tabbed pane
            tabbedPane = new JTabbedPane();
            tabbedPane.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {

                    // Update the "Next" button depending on what tab is
                    // selected
                    String str;
                    int maxIndex = tabbedPane.getTabCount() - 1;
                    if (tabbedPane.getSelectedIndex() == maxIndex) {
                        btnPrevious.setEnabled(true);
                        str = STLConstants.K0627_FINISH.getValue();
                    } else {
                        str = STLConstants.K0622_NEXT.getValue();
                        if (tabbedPane.getSelectedIndex() == 0) {
                            btnPrevious.setEnabled(false);
                        }
                    }
                    updateNextButton(str);

                    // Only do this if the tab was selected
                    if ((!nextSelected && !subnetBtnSelected)
                            && (subnetButtonMap.size() > 0)) {
                        wizardListener.setCurrentTask(tabbedPane
                                .getSelectedIndex());
                        wizardListener.onTab();
                    }
                }
            });
            tabbedPane.setUI(new IntelTabbedPaneUI());
            tabbedPane.setBorder(BorderFactory.createLineBorder(
                    UIConstants.INTEL_BORDER_GRAY, 2));

            pnlSubwizard.add(tabbedPane);

            // Create a card layout panel to hold two control panels; one for
            // subnet creation and the other for existing subnets
            pnlSubwizardCtrl = new JPanel(controlLayout);
            pnlSubwizardCtrl.setBackground(WIZARD_COLOR);
            pnlSubwizardCtrl.add(creationControlPanel(),
                    WizardControlType.CREATION.getName());
            pnlSubwizardCtrl.add(existingControlPanel(),
                    WizardControlType.EXISTING.getName());

            // Add the subwizard panel to the main panel
            pnlSubwizard.add(pnlSubwizardCtrl, BorderLayout.SOUTH);
        }
        return pnlSubwizard;
    }

    protected JPanel createWelcomePanel() {

        // Create the main welcome panel
        JPanel pnlWelcome = new JPanel(new BorderLayout());
        pnlWelcome.setBackground(WIZARD_COLOR);
        JLabel lblWelcome =
                ComponentFactory.getH2Label(
                        STLConstants.K3019_WELCOME_MESSAGE.getValue(),
                        Font.ITALIC);
        lblWelcome.setHorizontalAlignment(JLabel.CENTER);
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pnlWelcome.add(lblWelcome, BorderLayout.NORTH);

        // The Welcome Content panel has a CardLayout, and contains either an
        // Instruction panel if there are no subnets, or a Status panel if
        // saving a configuration
        pnlWelcomeContent = new JPanel(welcomeLayout);
        pnlWelcome.add(pnlWelcomeContent, BorderLayout.CENTER);

        // Create the Instructions panel
        JPanel pnlInstructions = createWelcomeInstructionsPanel();
        pnlWelcomeContent.add(pnlInstructions,
                WizardWelcomeType.INSTRUCTIONS.getName());

        // Create the Status panel
        JPanel pnlStatus = createWelcomeStatusPanel();
        pnlWelcomeContent.add(pnlStatus, WizardWelcomeType.STATUS.getName());

        // Create the Error panel
        JPanel pnlError = createWelcomeErrorPanel();
        pnlWelcomeContent.add(pnlError, WizardWelcomeType.ERROR.getName());

        return pnlWelcome;
    }

    protected JPanel createWelcomeInstructionsPanel() {

        // Create the instructions panel
        JPanel pnlInstructions = new JPanel(new GridBagLayout());
        pnlInstructions.setBackground(WIZARD_COLOR);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.weightx = 1;
        JLabel lblInstructions =
                ComponentFactory.getH3Label(
                        UILabels.STL50083_WELCOME_MESSAGE.getDescription(),
                        Font.ITALIC);
        lblInstructions.setHorizontalAlignment(JLabel.CENTER);
        pnlInstructions.add(lblInstructions, gc);

        return pnlInstructions;
    }

    protected JPanel createWelcomeStatusPanel() {

        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBackground(WIZARD_COLOR);

        // Create the status panel
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(WIZARD_COLOR);

        // Add a progress panel to the status panel
        progressPanel = new ProgressPanel(false);
        progressPanel
                .setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        pnlContent.add(progressPanel);

        pnlContent.add(Box.createVerticalStrut((int) (DIALOG_HEIGHT * .05)));

        // Host Check Status
        lblHostReachabilityIcon = new JLabel(UIImages.DASH.getImageIcon());
        lblHostReachability =
                ComponentFactory.getH3Label(
                        STLConstants.K3022_HOST_CONNECTIVITY.getValue(),
                        Font.ITALIC);
        lblHostReachability.setHorizontalAlignment(JLabel.LEFT);
        lblHostReachabilityStatus =
                ComponentFactory.getH3Label("**********", Font.ITALIC);
        lblHostReachabilityNotes = ComponentFactory.getH5Label("", Font.BOLD);
        pnlContent.add(getStatusPanel(lblHostReachabilityIcon,
                lblHostReachability, lblHostReachabilityStatus,
                lblHostReachabilityNotes));
        pnlContent.add(Box.createVerticalStrut((int) (DIALOG_HEIGHT * .1)));

        // Validation Status
        lblEntryValidationIcon = new JLabel(UIImages.DASH.getImageIcon());
        lblEntryValidation =
                ComponentFactory.getH3Label(
                        STLConstants.K3023_ENTRY_VALIDATION.getValue(),
                        Font.ITALIC);
        lblEntryValidation.setHorizontalAlignment(JLabel.LEFT);
        lblEntryValidationStatus =
                ComponentFactory.getH3Label("**********", Font.ITALIC);
        lblEntryValidationNotes = ComponentFactory.getH5Label("", Font.BOLD);
        pnlContent.add(getStatusPanel(lblEntryValidationIcon,
                lblEntryValidation, lblEntryValidationStatus,
                lblEntryValidationNotes));
        pnlContent.add(Box.createVerticalStrut((int) (DIALOG_HEIGHT * .1)));

        // Database Update Status
        lblDatabaseUpdateIcon = new JLabel(UIImages.DASH.getImageIcon());
        lblDatabaseUpdate =
                ComponentFactory.getH3Label(
                        STLConstants.K3024_DATABASE_UPDATE.getValue(),
                        Font.ITALIC);
        lblDatabaseUpdate.setHorizontalAlignment(JLabel.LEFT);
        lblDatabaseUpdateStatus =
                ComponentFactory.getH3Label("**********", Font.ITALIC);
        lblDatabaseUpdateNotes = ComponentFactory.getH5Label("", Font.BOLD);
        pnlContent.add(getStatusPanel(lblDatabaseUpdateIcon, lblDatabaseUpdate,
                lblDatabaseUpdateStatus, lblDatabaseUpdateNotes));
        pnlContent.add(Box.createVerticalStrut((int) (DIALOG_HEIGHT * .1)));
        pnlStatus.add(pnlContent, BorderLayout.NORTH);

        // Control Panel
        JPanel pnlControl = new JPanel(new BorderLayout());
        pnlControl.setBackground(WIZARD_COLOR);
        pnlControl.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 2));
        btnWelcomeOk =
                ComponentFactory.getIntelActionButton(STLConstants.K0645_OK
                        .getValue());
        btnWelcomeOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                manualSelect = true;
                onSubnetButtonClick(btnSelected);
            }

        });
        pnlControl.add(btnWelcomeOk, BorderLayout.EAST);
        pnlStatus.add(pnlControl, BorderLayout.SOUTH);

        return pnlStatus;
    }

    protected JPanel createWelcomeErrorPanel() {

        // Create the error panel
        JPanel pnlError = new JPanel(new GridBagLayout());
        pnlError.setBackground(WIZARD_COLOR);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.weightx = 1;
        lblWelcomeError =
                ComponentFactory.getH3Label(
                        UILabels.STL50094_WELCOME_ERROR.getDescription(),
                        Font.ITALIC);
        lblWelcomeError.setHorizontalAlignment(JLabel.CENTER);
        lblWelcomeError.setVerticalAlignment(JLabel.TOP);
        pnlError.add(lblWelcomeError, gc);

        return pnlError;
    }

    protected JPanel getControlPanel() {
        if (pnlMainCtrl == null) {
            pnlMainCtrl = new JPanel();
            pnlMainCtrl.setLayout(new BoxLayout(pnlMainCtrl, BoxLayout.X_AXIS));

            pnlMainCtrl.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));

            // Add +, -, Ok, Cancel, and Apply buttons to the main control panel
            // Add a single space character on the left and on the right of the
            // '+'
            btnAddSubnet =
                    ComponentFactory.getIntelActionButton(" "
                            + STLConstants.K3011_PLUS.getValue() + " ");
            btnAddSubnet.setBorder(new RoundedCornersBorder(
                    UIConstants.INTEL_BLUE, 1, 4));
            btnAddSubnet.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnAddSubnet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onAddSubnet();
                }
            });
            pnlMainCtrl.add(btnAddSubnet);
            pnlMainCtrl.add(Box.createHorizontalStrut(2));

            // Add a single space character on the left and on the right of the
            // '-'
            btnDeleteSubnet =
                    ComponentFactory.getIntelActionButton(" "
                            + STLConstants.K3012_MINUS.getValue() + " ");
            btnDeleteSubnet.setBorder(new RoundedCornersBorder(
                    UIConstants.INTEL_BLUE, 1, 4));
            btnDeleteSubnet.setAlignmentX(Component.LEFT_ALIGNMENT);

            btnDeleteSubnet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onDeleteSubnet();
                }
            });
            pnlMainCtrl.add(btnDeleteSubnet);

            ComponentFactory.makeSameWidthButtons(new JButton[] { btnAddSubnet,
                    btnDeleteSubnet });

            pnlMainCtrl.add(Box.createGlue());

            // Add the Run button
            btnRun =
                    ComponentFactory
                            .getIntelActionButton(STLConstants.K3014_RUN
                                    .getValue());
            btnRun.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    onRun();
                }

            });

            // Add a mouse listener to ensure the Run button gets the focus
            btnRun.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JButton) e.getSource()).requestFocusInWindow();
                }
            });
            pnlMainCtrl.add(btnRun);
            pnlMainCtrl.add(Box.createHorizontalStrut(5));

            // Add the Close button
            btnClose =
                    ComponentFactory
                            .getIntelActionButton(STLConstants.K0740_CLOSE
                                    .getValue());
            btnClose.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    onClose(false);
                }

            });
            pnlMainCtrl.add(btnClose);
            ComponentFactory.makeSameWidthButtons(new JButton[] { btnRun,
                    btnClose });
        }
        return pnlMainCtrl;
    }

    public void setWelcomeOkEnabled(boolean enable) {
        btnWelcomeOk.setEnabled(enable);
    }

    protected JPanel getStatusPanel(JLabel... labels) {

        int numLabels = labels.length;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WIZARD_COLOR);

        // Put all but the last label horizontally on the status panel
        JPanel pnlStatus = new JPanel();
        pnlStatus.setBackground(WIZARD_COLOR);
        pnlStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlStatus.setLayout(new BoxLayout(pnlStatus, BoxLayout.X_AXIS));
        for (int i = 0; i < numLabels - 1; i++) {
            labels[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            labels[i].setBorder(BorderFactory.createEmptyBorder(5, 60, 5, 65));
            pnlStatus.add(labels[i]);
        }
        mainPanel.add(pnlStatus, BorderLayout.NORTH);

        // Add the last label vertically under the status panel
        JPanel pnlNotes = new JPanel();
        pnlNotes.setBackground(WIZARD_COLOR);
        pnlNotes.setLayout(new BoxLayout(pnlNotes, BoxLayout.X_AXIS));
        pnlNotes.add(Box.createHorizontalStrut(60));
        pnlNotes.add(labels[numLabels - 1]);
        mainPanel.add(pnlNotes, BorderLayout.CENTER);

        return mainPanel;
    }

    protected void addGlue(JPanel panel, int direction) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = direction;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(2, 2, 2, 2);
        gc.weightx = 1;
        gc.weighty = 1;

        switch (direction) {
            case GridBagConstraints.VERTICAL:
                panel.add(Box.createVerticalGlue(), gc);
                break;

            case GridBagConstraints.HORIZONTAL:
                panel.add(Box.createVerticalGlue(), gc);
                break;
        }

    }

    protected JPanel existingControlPanel() {
        // Add the sub-wizard control panel containing buttons pertaining
        // to all sub-wizards
        pnlExistingSubnet = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        pnlExistingSubnet.setOpaque(false);

        // Add the Apply button
        btnApply =
                ComponentFactory.getIntelActionButton(STLConstants.K0672_APPLY
                        .getValue());
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabbedPane.getSelectedIndex() >= 0) {
                    wizardListener.setCurrentTask(tabbedPane.getSelectedIndex());
                }
                onFinish();
            }
        });
        // Add a mouse listener to ensure the Apply button gets the focus
        btnApply.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).requestFocusInWindow();
            }
        });
        pnlExistingSubnet.add(btnApply, BorderLayout.EAST);

        // Add the Reset button
        btnReset =
                ComponentFactory.getIntelActionButton(STLConstants.K1006_RESET
                        .getValue());
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onReset();
            }
        });
        // Add a mouse listener to ensure the Reset button gets the focus
        btnReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).requestFocusInWindow();
            }
        });
        pnlExistingSubnet.add(btnReset, BorderLayout.EAST);

        return pnlExistingSubnet;
    }

    protected JPanel creationControlPanel() {
        // Add the sub-wizard control panel containing buttons pertaining
        // to all sub-wizards
        JPanel pnlCreateSubnet =
                new JPanel(new FlowLayout(FlowLayout.TRAILING));
        pnlCreateSubnet.setOpaque(false);

        // Add the Back button
        btnPrevious =
                ComponentFactory.getIntelActionButton(STLConstants.K0624_BACK
                        .getValue());
        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPrevious();
            }
        });
        pnlCreateSubnet.add(btnPrevious, BorderLayout.EAST);

        // Add the Next button
        btnNext =
                ComponentFactory.getIntelActionButton(STLConstants.K0622_NEXT
                        .getValue());
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNext();
            }
        });
        pnlCreateSubnet.add(btnNext, BorderLayout.EAST);

        return pnlCreateSubnet;
    }

    public JButton getBtnSelected() {
        return btnSelected;
    }

    @Override
    public void initButtonMap(List<SubnetDescription> subnets,
            SubnetDescription currentSubnet) {

        for (SubnetDescription subnet : subnets) {
            if (subnet.getName() != null) {

                // Create the button if it doesn't exist in the map
                if (!isSubnetInMap(subnet)) {
                    createSubnetButton(subnet);
                }

            }
        }
        // Select the first button in the list
        if (subnetMap.size() > 0) {
            updateSelectedButton(subnetButtonMap.get(currentSubnet.getName()));

            if (btnSelected == null) {
                updateSelectedButton((JButton) subnetMap.keySet().toArray()[0]);
            }
            controlLayout.show(pnlSubwizardCtrl,
                    WizardControlType.EXISTING.getName());
            highlightButtons();

            // Update the model
            wizardModel.getSubnetModel().setSubnet(subnetMap.get(btnSelected));
            wizardModel.notifyModelChange(WizardType.SUBNET);
        }
    }

    protected boolean isSubnetInMap(SubnetDescription subnet) {

        boolean found = false;
        Iterator<SubnetDescription> it = subnetMap.values().iterator();

        while (!found && it.hasNext()) {
            SubnetDescription mappedSubnet = it.next();
            found = (subnet.getSubnetId() == mappedSubnet.getSubnetId());
        }

        return found;
    }

    public void setNewWizardInProgress(boolean newWizardInProgress) {
        this.newWizardInProgress = newWizardInProgress;
    }

    public boolean getNewWizardStatus() {
        return this.newWizardInProgress;
    }

    public SubnetDescription getSelectedSubnet() {

        return subnetMap.get(btnSelected);
    }

    /**
     * 
     * <i>Description: Create button for new subnet </i>
     * 
     * @param control
     * @return
     */
    protected JButton createSubnetButton() {
        JButton btnUnknown =
                new JButton(STLConstants.K3018_UNKNOWN_SUBNET.getValue());
        btnUnknown.setUI(new IntelButtonUI(UIConstants.INTEL_MEDIUM_BLUE,
                UIConstants.INTEL_PALE_BLUE));
        btnUnknown.setHorizontalAlignment(JButton.LEADING);
        btnUnknown.setFont(UIConstants.H4_FONT);
        btnUnknown.setRolloverEnabled(false);
        btnUnknown.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.INTEL_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)));

        btnUnknown.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                JButton buttonSource = (JButton) e.getSource();
                if (buttonSource.isEnabled()) {
                    subnetBtnSelected = true;
                    onSubnetButtonClick(buttonSource);
                }
                subnetBtnSelected = false;
            }
        });
        updateSelectedButton(btnUnknown);

        return btnUnknown;
    }

    /**
     * 
     * <i>Description: Create button for existing subnet</i>
     * 
     * @param control
     * @param subnet
     */
    protected JButton createSubnetButton(SubnetDescription subnet) {

        // Create a new button and update with the subnet name
        JButton btn = createSubnetButton();
        btn.setText(subnet.getName());

        // Add the new button to the button map and assign it as selected
        updateMaps(btn, subnet, STLConstants.K3018_UNKNOWN_SUBNET.getValue());
        updateSelectedButton(btn);

        return btn;
    }

    public void updateSubnetMap(SubnetDescription subnet) {

        // Locate the button with the corresponding subnet name
        // and use it to update the subnet map
        if ((subnet != null) && !subnet.getName().equals("")) {
            JButton btn = subnetButtonMap.get(subnet.getName());
            if (btn != null) {
                subnetMap.put(btn, subnet);
            }
        }
    }

    protected void updateMaps(JButton btn, SubnetDescription subnet, String key) {
        subnetMap.put(btn, subnet);
        subnetButtonMap.remove(key);
        subnetButtonMap.put(subnet.getName(), btn);
    }

    public void updateMaps(SubnetDescription subnet) {
        subnetMap.put(btnSelected, subnet);

        if (selectedButtonName != null) {
            if (!selectedButtonName.equals(btnSelected.getText())) {
                subnetButtonMap.remove(selectedButtonName);
                subnetButtonMap.put(subnet.getName(), btnSelected);
            }
        }
    }

    protected void highlightButtons() {
        for (JButton btn : subnetMap.keySet()) {
            if (btn.equals(btnSelected)) {
                btn.setBackground(UIConstants.INTEL_LIGHT_BLUE);
                btn.setForeground(UIConstants.INTEL_WHITE);
            } else {
                btn.setBackground(UIConstants.INTEL_WHITE);
                btn.setForeground(UIConstants.INTEL_DARK_GRAY);
            }
        }
    }

    protected synchronized void addSubnetButtons() {
        // Iterate through the button map and put the buttons back on the panel
        for (JButton btn : subnetMap.keySet()) {
            pnlNavigationButtons.add(btn);
        }

        // Update button highlighting
        highlightButtons();
    }

    public synchronized void onSubnetButtonClick(final JButton buttonSource) {

        if (buttonSource == null) {
            return;
        }

        // If the current subnet button is selected, and this wasn't caused by
        // clicking on the Welcome screen's OK button - then do nothing
        if (!manualSelect && buttonSource.equals(btnSelected)) {
            return;
        }
        manualSelect = false;

        // If a new subnet is being configured or an existing one is changed
        // and a different subnet button is clicked, display a message asking
        // the user if the changes are to be abandoned.
        if (newWizardInProgress || haveUnsavedChanges()) {
            int result =
                    showWarningDialog(UILabels.STL50081_ABANDON_CHANGES_MESSAGE
                            .getDescription());

            if (result == JOptionPane.NO_OPTION) {
                return;
            } else {
                // Reset all the wizards back to their original state
                onReset();
                btnAddSubnet.setEnabled(true);
                if ((btnSelected != null)
                        && btnSelected.getText().equals(
                                STLConstants.K3018_UNKNOWN_SUBNET.getValue())) {
                    newWizardCleanup();
                }
            }
        }

        try {
            // Clear the subnet certificate factories
            SubnetDescription subnet = subnetMap.get(buttonSource);
            HostInfo currentHost = null;
            if (subnet != null) {
                currentHost = subnet.getCurrentFE();
            }
            if ((currentHost != null) && currentHost.isSecureConnect()) {
                wizardListener.clearSubnetFactories(subnet);
            }

            Util.runInEDT(new Runnable() {

                @Override
                public void run() {

                    btnRun.setEnabled(true);
                    btnApply.setEnabled(true);

                    // Stop the connection test
                    stopSubnetConnectionTest();

                    // Make the heading panel visible
                    pnlHeading.setVisible(true);

                    // Show wizard view
                    wizardLayout.show(pnlMain, WizardViewType.WIZARD.getName());
                    updateSelectedButton(buttonSource);
                    highlightButtons();

                    // Update the subnet attribute in the controller
                    wizardListener.setCurrentSubnet(getSelectedSubnet());

                    // Determine if this is a new "unknown" button
                    boolean unknownButton =
                            (btnSelected.getText()
                                    .equals(STLConstants.K3018_UNKNOWN_SUBNET
                                            .getValue()));

                    // Set control layout depending on whether button is new or
                    // not
                    WizardControlType controlType =
                            unknownButton ? WizardControlType.CREATION
                                    : WizardControlType.EXISTING;
                    controlLayout.show(pnlSubwizardCtrl, controlType.getName());

                    // Set the tabs
                    setTabs(wizardListener.getTasks(), unknownButton, false);

                    // Update the models
                    wizardListener.updateModels(getSelectedSubnet());

                    // Assume no changes have been made yet
                    for (IMultinetWizardTask task : tasks) {
                        task.setDirty(false);
                    }
                }
            });
        } catch (Exception e) {
            onSubnetError(buttonSource);
        }
    }

    protected void onSubnetError(final JButton subnetButton) {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                btnRun.setEnabled(false);
                String subnetName = subnetButton.getText();
                updateSelectedButton(subnetButton);
                highlightButtons();
                lblWelcomeError.setText(UILabels.STL50094_WELCOME_ERROR
                        .getDescription(subnetName));
                welcomeLayout.show(pnlWelcomeContent,
                        WizardWelcomeType.ERROR.getName());
                wizardLayout.show(pnlMain, WizardViewType.WELCOME.getName());
            }
        });
    }

    public void enableNavPanel(boolean enable) {

        for (Component c : pnlNavigationButtons.getComponents()) {
            c.setEnabled(enable);
        }
    }

    public void enableSubnetModifiers(boolean enable) {
        btnAddSubnet.setEnabled(enable);
        btnDeleteSubnet.setEnabled(enable);
        btnRun.setEnabled(enable);
    }

    // Button control begins
    protected void onAddSubnet() {

        // Stop the connection test
        stopSubnetConnectionTest();

        // Disable the '+' button so only one subnet can be a added at a time
        btnAddSubnet.setEnabled(false);

        // Show wizard view
        wizardLayout.show(pnlMain, WizardViewType.WIZARD.getName());

        // Clear the header subnet name
        pnlHeading.setVisible(true);
        setSubnetName("");

        // Disable all the control buttons
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);
        btnRun.setEnabled(false);

        // Add a new subnet, display new empty tabs and subnet button
        // This subnet will be updated when the subnet controller updates
        // the model with a subnet from the subnetMgr
        SubnetDescription subnet =
                new SubnetDescription(
                        STLConstants.K3018_UNKNOWN_SUBNET.getValue(),
                        STLConstants.K3018_UNKNOWN_SUBNET.getValue(),
                        SubnetModel.DEFAULT_PORT_NUM);
        JButton btn = createSubnetButton();
        updateMaps(btn, subnet, STLConstants.K3018_UNKNOWN_SUBNET.getValue());
        controlLayout.show(pnlSubwizardCtrl,
                WizardControlType.CREATION.getName());
        updateNavPanel();

        // Initialize the tasks
        wizardListener.clearTasks();

        // Update the Subnet Model with the new subnet
        wizardModel.getSubnetModel().setSubnet(subnet);
        wizardModel.notifyModelChange(WizardType.SUBNET);
        setTabs(wizardListener.getTasks(), true, false);

        txtFldSubnetName.requestFocusInWindow();
        newWizardInProgress = true;
    }

    protected void onDeleteSubnet() {
        // Delete selected subnet and button and change selected button
        // to next in list
        int position = 0;
        boolean found = false;
        boolean deleted = false;
        Iterator<Entry<JButton, SubnetDescription>> it =
                subnetMap.entrySet().iterator();

        while (!found && it.hasNext()) {
            JButton btn = it.next().getKey();
            if (btn.equals(btnSelected)) {

                // Warn the user first
                int result =
                        showWarningDialog(UILabels.STL50082_DELETE_SUBNET_MESSAGE
                                .getDescription());
                if (result == JOptionPane.YES_OPTION) {
                    newWizardInProgress = false;
                    if (!btnSelected.getText().equals(
                            STLConstants.K3018_UNKNOWN_SUBNET.getValue())) {
                        wizardListener.deleteSubnet(subnetMap.get(btnSelected));
                    }
                    subnetMap.remove(btn);
                    subnetButtonMap.remove(btn.getText());
                    pnlHeading.setVisible(false);

                    // Enable the '+' button so more subnets can be added
                    btnAddSubnet.setEnabled(true);

                    deleted = true;
                }
                found = true;

            } else {
                position++;
            }
        }

        // Assign the selected button
        // Update the tabbed pane with the selected subnet or if no subnet
        // is selected, then remove all tabs
        if (deleted && (position >= 0)) {

            position = (position == 0) ? position : position - 1;
            updateSelectedButton((subnetMap.size() == 0) ? null
                    : (JButton) subnetMap.keySet().toArray()[position]);

            updateNavPanel();
            setTabs(tasks, false, false);

            // Update the view as though the selected button was clicked
            if (btnSelected != null) {
                manualSelect = true;
                onSubnetButtonClick(btnSelected);
            }
        }

        // When all subnets have been removed, clear the tabbed pane, disable
        // buttons
        if (subnetMap.size() == 0) {
            tabbedPane.removeAll();
            newWizardInProgress = false;
            btnApply.setEnabled(false);
            btnReset.setEnabled(false);
            btnNext.setEnabled(false);
            btnRun.setEnabled(false);
            welcomeLayout.show(pnlWelcomeContent,
                    WizardWelcomeType.INSTRUCTIONS.getName());
            wizardLayout.show(pnlMain, WizardViewType.WELCOME.getName());
        }
    }

    protected void onApply(boolean finished) {

        // Capture the selected button name
        selectedButtonName = btnSelected.getText();
        wizardModel.getSubnetModel().getSubnet()
                .setName(txtFldSubnetName.getText());
        wizardModel.notifyModelChange(WizardType.SUBNET);

        wizardListener.onFinish();
    }

    protected void onReset() {
        wizardListener.onReset();
    }

    protected void onPrevious() {
        wizardListener.onPrevious();

    }

    protected void onNext() {

        if (isNextButton()) {

            // Check for duplicates before doing anything else
            if (wizardListener.getSubnetView().hasDuplicateHosts()) {
                Util.showErrorMessage(this,
                        UILabels.STL50086_DUPLICATE_HOSTS.getDescription());
                return;
            }

            nextSelected = true;
            boolean success = wizardListener.onNext();

            if (success) {
                // Re-enable the '+' button so more subnets can be added
                btnAddSubnet.setEnabled(true);
            }
        } else {
            onFinish();
        }
        nextSelected = false;
    }

    protected void onFinish() {

        // Check for duplicate subnets
        if (hasDuplicateSubnets()) {
            Util.showErrorMessage(this,
                    UILabels.STL50087_DUPLICATE_SUBNETS.getDescription());
            return;
        }

        // Check for duplicates backup hosts
        if (wizardListener.getSubnetView().hasDuplicateHosts()) {
            Util.showErrorMessage(this,
                    UILabels.STL50086_DUPLICATE_HOSTS.getDescription());
            return;
        }

        // Update the button in case the subnet name changed
        String oldName = btnSelected.getText();
        String newName = txtFldSubnetName.getText();
        btnSelected.setText(newName);
        subnetButtonMap.remove(oldName);
        subnetButtonMap.put(newName, btnSelected);
        updateNavPanel();

        // Disable the buttons on the Navigation panel and the +/- buttons
        enableNavPanel(false);
        enableSubnetModifiers(false);

        // Prepare the Welcome panel
        initConfigLabels();
        welcomeLayout.show(pnlWelcomeContent,
                WizardWelcomeType.STATUS.getName());
        wizardLayout.show(pnlMain, WizardViewType.WELCOME.getName());
        wizardListener.onFinish();

    }

    protected boolean hasDuplicateSubnets() {

        Set<String> subnetNameSet = new HashSet<String>();

        // If there are subnet names, they can't be added to the set
        for (SubnetDescription subnet : subnetMap.values()) {
            if (!subnetNameSet.add(subnet.getName().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    protected void initConfigLabels() {

        // Reset the progress panel
        progressPanel.setProgress(0);
        progressPanel.setProgressNote("");

        // Reset the config status labels
        lblHostReachabilityIcon.setIcon(UIImages.DASH.getImageIcon());
        lblHostReachabilityStatus.setText("**********");
        lblHostReachabilityNotes.setText("");

        lblEntryValidationIcon.setIcon(UIImages.DASH.getImageIcon());
        lblEntryValidationStatus.setText("**********");
        lblEntryValidationNotes.setText("");

        lblDatabaseUpdateIcon.setIcon(UIImages.DASH.getImageIcon());
        lblDatabaseUpdateStatus.setText("**********");
        lblDatabaseUpdateNotes.setText("");
    }

    public void updateConfigStatus(ConfigTaskStatus status) {

        ImageIcon icon =
                (status.isSuccess()) ? UIImages.CHECK_MARK.getImageIcon()
                        : UIImages.X_MARK.getImageIcon();

        String resultStr =
                status.isSuccess() ? STLConstants.K3026_SUCCESSFUL.getValue()
                        : STLConstants.K0020_FAILED.getValue();

        switch (status.getType()) {

            case CHECK_HOST:
                icon =
                        (status.isSuccess()) ? UIImages.CHECK_MARK
                                .getImageIcon() : UIImages.WARNING2_ICON
                                .getImageIcon();
                lblHostReachabilityIcon.setIcon(icon);
                lblHostReachabilityStatus.setText(resultStr);

                if (!status.isSuccess()) {
                    lblHostReachabilityNotes
                            .setText(UILabels.STL50088_HOST_NOT_FOUND
                                    .getDescription());
                }
                break;

            case VALIDATE_ENTRY:
                icon =
                        (status.isSuccess()) ? UIImages.CHECK_MARK
                                .getImageIcon() : UIImages.WARNING2_ICON
                                .getImageIcon();
                lblEntryValidationIcon.setIcon(icon);
                lblEntryValidationStatus.setText(resultStr);

                if (!status.isSuccess()) {
                    lblEntryValidationNotes
                            .setText(UILabels.STL50089_UNABLE_TO_VALIDATE
                                    .getDescription());
                }
                break;

            case UPDATE_DATABASE:
                lblDatabaseUpdateIcon.setIcon(icon);
                lblDatabaseUpdateStatus.setText(resultStr);
                if (!status.isSuccess()) {
                    lblDatabaseUpdateNotes
                            .setText(UILabels.STL50090_DB_SAVE_FAILURE
                                    .getDescription());

                }
                setProgress(ConfigTaskType.CONFIGURATION_COMPLETE);
                break;

            default:
                break;
        }
    }

    public void setProgress(ConfigTaskType taskType) {

        switch (taskType) {

            case CHECK_HOST:
                progressPanel.setProgress(33);
                break;

            case VALIDATE_ENTRY:
                progressPanel.setProgress(66);
                break;

            case UPDATE_DATABASE:
                progressPanel.setProgress(99);
                break;

            case CONFIGURATION_COMPLETE:
                progressPanel.setProgress(100);
                break;
        }

        progressPanel.setProgressNote(taskType.getName());
    }

    protected void onRun() {

        // If there are unsaved changes when Run is clicked, save them to the DB
        if (haveUnsavedChanges()) {
            onApply(true);
        }

        if (newWizardInProgress) {
            // Update the control panel
            controlLayout.show(pnlSubwizardCtrl,
                    WizardControlType.EXISTING.getName());
        }

        // Close the wizard if run was successful
        if (wizardListener.onRun()) {
            onClose(true);
        }
    }

    protected void onClose(boolean running) {

        boolean okayToClose = true;

        if (newWizardInProgress) {
            int result =
                    showWarningDialog(UILabels.STL50081_ABANDON_CHANGES_MESSAGE
                            .getDescription());

            // Remove the new button/subnet and close the wizard
            // otherwise leave the wizard open
            if (result == JOptionPane.YES_OPTION) {
                newWizardCleanup();
                okayToClose = true;
            } else {
                okayToClose = false;
            }
        } else if (!running && haveUnsavedChanges()) {
            // Reinitialize each task and close the wizard
            // otherwise leave the wizard open
            int result =
                    showWarningDialog(UILabels.STL50081_ABANDON_CHANGES_MESSAGE
                            .getDescription());

            if (result == JOptionPane.YES_OPTION) {
                for (IWizardTask task : tasks) {
                    task.onReset();
                }
                newWizardCleanup();
                okayToClose = true;
            } else {
                okayToClose = false;
            }
        }

        // Close the status panels and wizard
        if (okayToClose) {

            // Enable any buttons that were grayed out when the welcome panel
            // was displayed
            enableNavPanel(true);
            enableSubnetModifiers(true);

            wizardListener.closeStatusPanels();
            closeWizard();

            // Shutdown the configuration thread
            if (!running) {
                wizardListener.cancelConfiguration();
            }
        }
    }

    protected boolean haveUnsavedChanges() {

        // Check if any of the wizards have been edited
        byte editInProgress = 0;

        if ((tasks != null) && (tasks.size() > 0)) {
            for (IWizardTask task : tasks) {
                if (task.isDirty()) {
                    editInProgress++;
                }
            }
        }

        return (editInProgress > 0);
    }

    protected int showWarningDialog(String message) {
        int result = Util.showConfirmDialog(this, message);
        return result;
    }

    protected void newWizardCleanup() {

        if (subnetButtonMap.containsKey(STLConstants.K3018_UNKNOWN_SUBNET
                .getValue())) {
            subnetMap.remove(subnetButtonMap
                    .get(STLConstants.K3018_UNKNOWN_SUBNET.getValue()));
            subnetButtonMap
                    .remove(STLConstants.K3018_UNKNOWN_SUBNET.getValue());
        } else {
            if (btnSelected != null) {
                subnetMap.remove(btnSelected);
                subnetButtonMap.remove(btnSelected.getText());
            }
        }

        // Clear the navigation panel
        updateNavPanel();

        // Remove the tabs
        tabbedPane.removeAll();

        // Clear all the models
        for (IMultinetWizardTask task : tasks) {
            task.clear();
        }

        newWizardInProgress = false;
    }

    public boolean nextTab() {

        boolean result = false;
        int currentIndex = tabbedPane.getSelectedIndex();
        int maxIndex = tabbedPane.getTabCount() - 1;

        if (currentIndex == maxIndex) {
            result = false;
        } else if ((0 <= currentIndex) && (currentIndex < maxIndex)) {
            // Enable and move to the next tab
            int newIndex = currentIndex + 1;
            tabbedPane.setEnabledAt(newIndex, true);
            tabbedPane.setSelectedIndex(newIndex);
            result = true;
        }

        return result;
    }

    public boolean previousTab() {

        boolean result = false;
        int currentIndex = tabbedPane.getSelectedIndex();

        if (currentIndex == 0) {
            result = true;
        } else if (currentIndex > 0) {
            // Move to the previous tab
            int newIndex = currentIndex - 1;
            tabbedPane.setSelectedIndex(newIndex);

            // If the first tab has been reached, disable the "Back" button
            if (newIndex == 0) {
                btnPrevious.setEnabled(false);
            }

            result = true;
        }

        return result;
    }

    /**
     * 
     * <i>Description: Center the wizard dialog relative to the main frame</i>
     * 
     */
    private void centerDialog(IFabricView mainFrame) {
        pack();
        Point dialogLocation = new Point(0, 0);

        dialogLocation.x =
                mainFrame.getScreenPosition().x
                        + mainFrame.getScreenSize().width / 2 - getWidth() / 2;

        dialogLocation.y =
                mainFrame.getScreenPosition().y
                        + mainFrame.getScreenSize().height / 2 - getHeight()
                        / 2;

        setLocation(dialogLocation);
    } // centerDialog

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#showWizard(boolean)
     */
    @Override
    public void showWizard(boolean enable) {
        // Used in single subnet wizard only
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IWizardView#showWizard(SubnetDescription,
     * boolean)
     */
    @Override
    public void showWizard(SubnetDescription subnet, boolean isFirstRun,
            IFabricView mainFrame) {

        String type =
                (wizardListener.getSubnets().size() > 0) ? WizardViewType.WIZARD
                        .getName() : WizardViewType.WELCOME.getName();
        wizardLayout.show(pnlMain, type);

        // Initially assume no subnets and disable buttons
        btnNext.setEnabled(false);
        btnRun.setEnabled(false);
        pnlHeading.setVisible(false);

        try {
            // Clear the tabbed pane and navigation panels
            // and reinitialize them
            if ((tasks != null) && (subnetMap.size() > 0)) {

                pnlHeading.setVisible(true);
                if (subnet != null) {
                    updateSelectedButton(subnetButtonMap.get(subnet.getName()));
                }
                wizardModel.getSubnetModel().setSubnet(
                        subnetMap.get(btnSelected));

                // Update the UI as though the first subnet button were clicked
                // This will clear the key factories in certAssistant
                manualSelect = true;
                onSubnetButtonClick(btnSelected);

                // Set the tabs and update the navigation panel
                setTabs(tasks, false, true);
                updateNavPanel();

                // Loop through the tasks and reset the dirty variable
                for (IMultinetWizardTask task : tasks) {
                    task.setDirty(false);
                }

                // Enable buttons since there are subnets
                btnNext.setEnabled(true);
                btnRun.setEnabled(true);
            }

        } catch (IllegalArgumentException e) {
            Util.showError(this, e);
        } finally {
            centerDialog(mainFrame);
            this.setVisible(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#setTasks(java.util.List)
     */
    @Override
    public void setTasks(List<IWizardTask> tasks) {

        for (IWizardTask task : tasks) {
            tabbedPane.add(task.getName(), task.getView());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IWizardView#showTaskView(java.lang.String)
     */
    @Override
    public void showTaskView(String name) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enablePrevious(boolean)
     */
    @Override
    public void enablePrevious(boolean enable) {
        btnPrevious.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableNext(boolean)
     */
    @Override
    public void enableNext(boolean enable) {
        btnNext.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableApply(boolean)
     */
    @Override
    public void enableApply(boolean enable) {
        btnApply.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableReset(boolean)
     */
    @Override
    public void enableReset(boolean enable) {
        btnReset.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableRun(boolean)
     */
    @Override
    public void enableRun(boolean enable) {
        btnRun.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#closeWizard()
     */
    @Override
    public void closeWizard() {
        this.setVisible(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IWizardView#updateNextButton(java.lang.
     * String)
     */
    @Override
    public void updateNextButton(String name) {
        btnNext.setText(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#isNextButton()
     */
    @Override
    public boolean isNextButton() {

        return btnNext.getText().equals(STLConstants.K0622_NEXT.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IWizardView#update(com.intel.stl.ui.wizards
     * .model.MultinetWizardModel)
     */
    @Override
    public void update(MultinetWizardModel model) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#update()
     */
    @Override
    public void updateNavPanel() {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {

                // Clear panel and put back the subnet label without the glue
                pnlNavigationButtons.removeAll();

                // Add one button for each subnet
                addSubnetButtons();

                revalidate();
                // Repaint the panel with the new components
                pnlNavigationButtons.repaint();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IWizardView#setWizardListener(com.intel
     * .stl.ui.wizards.impl.IWizardListener)
     */
    @Override
    public void setWizardListener(IWizardListener listener) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#setWizardListener(com
     * .intel.stl.ui.wizards.impl.IMultinetWizardListener)
     */
    @Override
    public void setWizardListener(IMultinetWizardListener listener) {
        this.wizardListener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#showErrorMessage(java
     * .lang.String, java.lang.String[])
     */
    @Override
    public void showErrorMessage(String title, String... msgs) {

        StringBuffer message = new StringBuffer();
        for (String msg : msgs) {
            if (message.length() == 0) {
                message.append(msg);
            } else {
                message.append("\n" + msg);
            }
        }

        Util.showErrorMessage(this, message.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#showErrorMessage(java
     * .lang.String, java.lang.Throwable[])
     */
    @Override
    public void showErrorMessage(String title, Throwable... errors) {
        Util.showErrors(this, Arrays.asList(errors));
    }

    @Override
    public synchronized void updateSelectedButtonText(String name) {

        if (btnSelected != null) {
            btnSelected.setText(name);
        }
    }

    protected synchronized void updateSelectedButton(JButton btn) {
        btnSelected = btn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#setMultinetTasks(java
     * .util.List)
     */
    @Override
    public void setTabs(final List<IMultinetWizardTask> tasks,
            final boolean newSubnet, final boolean wizardStartup) {

        this.tasks = tasks;

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                int currentTab = tabbedPane.getSelectedIndex();

                // Add the tasks to the tabbed pane first
                for (int i = 0; i < tasks.size(); i++) {

                    // Get the current task
                    IMultinetWizardTask task = tasks.get(i);

                    // Add this task to the tabbed pane
                    tabbedPane.add(task.getName(), task.getView());
                }

                // Once the tabs are present, set their enables
                for (int i = 0; i < tasks.size(); i++) {

                    // Clear the dirty flag
                    tasks.get(i).setDirty(false);

                    // Disable all but the first tab
                    if (newSubnet && (i > 0)) {
                        tabbedPane.setEnabledAt(i, false);
                    }
                }

                if (wizardStartup || newSubnet) {
                    wizardListener.setCurrentTask(0);
                } else {
                    if ((0 <= currentTab)
                            && (currentTab < tabbedPane.getTabCount())) {
                        tabbedPane.setSelectedIndex(currentTab);
                        wizardListener.setCurrentTask(currentTab);
                    }
                }

                // Make sure the creation control panel has a "Next" button
                updateNextButton(STLConstants.K0622_NEXT.getValue());

                // Disable all the control buttons
                btnPrevious.setEnabled(false);
                btnNext.setEnabled(false);
                // btnRun.setEnabled(false);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#updateSubnetName(java
     * .lang.String)
     */
    @Override
    public void setSubnetName(String name) {

        txtFldSubnetName.setText(name);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IMultinetWizardView#getSubnetName()
     */
    @Override
    public String getSubnetName() {

        return txtFldSubnetName.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#assignDocumentListeners
     * (javax.swing.event.DocumentListener[])
     */
    @Override
    public void assignDocumentListeners(DocumentListener... docListeners) {

        for (DocumentListener docListener : docListeners) {
            txtFldSubnetName.getDocument().addDocumentListener(docListener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#setTxtfldSubnetName
     * (java.lang.String)
     */
    @Override
    public void setTxtfldSubnetName(String value) {

        txtFldSubnetName.setText(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#getTxtfldSubnetName()
     */
    @Override
    public JTextField getTxtfldSubnetName() {

        return txtFldSubnetName;
    }

    public void markTasksClean() {

        for (IMultinetWizardTask task : tasks) {
            task.setDirty(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IMultinetWizardView#stopSubnetConnectionTest
     * ()
     */
    @Override
    public void stopSubnetConnectionTest() {
        wizardListener.getSubnetView().stopConnectionTest();
    }
}
