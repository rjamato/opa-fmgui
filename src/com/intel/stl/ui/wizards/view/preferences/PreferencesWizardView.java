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
 *  File Name: PreferencesWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.23  2015/12/09 16:06:07  jijunwan
 *  Archive Log:    PR 131944 - If "# Worst Nodes" is <10 or >100, there is a Entry Validation warning for the Refresh Rate
 *  Archive Log:
 *  Archive Log:    - added value range to "# Worst Nodes" text field
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/11/09 20:51:57  fernande
 *  Archive Log:    PR130852 - The 1st subnet in the Subnet Wizard displays "Abandon Changes" message when no changes are made. Added special listener for dirty state
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/09/14 19:04:34  jijunwan
 *  Archive Log:    PR 130229 - The text component of all editable combo boxes should provide validation of the input
 *  Archive Log:    - added exception check just in case
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/09/14 16:06:58  jijunwan
 *  Archive Log:    PR 130229 - The text component of all editable combo boxes should provide validation of the input
 *  Archive Log:    - apply FormattedComboBoxEditor on ComponentFactory and PreferenceWizard
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/09/01 12:56:16  rjtierne
 *  Archive Log:    PR 130174 - Inconsistent checking for blank subnet "Name" field in wizard
 *  Archive Log:    - On subnet creation, set the default subnet name to match the subnet button.
 *  Archive Log:    Then there is no possibility to move to the next wizard with a blank subnet name.
 *  Archive Log:    Since all fields are populated, if a field is made blank, the Next/Apply button is
 *  Archive Log:    disabled. If any field is invalid, then clicking on Next/Apply results in an error message.
 *  Archive Log:    - Changed cboxRefreshRateUnits to be non-editable
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/08/17 18:53:48  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/08/12 20:41:55  fisherma
 *  Archive Log:    PR 129747 - Time Window failed when updating recommended value in Time Window.  Updated the validation and upper limit on the time property.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/08/12 19:28:13  fisherma
 *  Archive Log:    Store/retrieve SMTP settings in/from SECTION_PREFERENCE properties.  Cleanup unused interface.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/08/10 22:25:05  fisherma
 *  Archive Log:    PR 128974 - Email notification functionality.  Updated layout in the preferences tab.  Added strings to localization file.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/10 17:55:48  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/07/17 20:48:27  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - introduced isEditValid to allow us check whether we have valid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/05/11 12:37:44  rjtierne
 *  Archive Log:    PR 128585 - Fix errors found by Klocwork and FindBugs
 *  Archive Log:    - Added new method addDocumentListeners() which provides null pointer protection
 *  Archive Log:    to the view form fields.
 *  Archive Log:    - Invoked addDocumentListeners() in both constructors.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/21 21:18:41  rjtierne
 *  Archive Log:    Fixed document listeners on form fields so the Apply and Reset buttons are enabled only
 *  Archive Log:    when the fields change.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/03/30 15:12:49  rjtierne
 *  Archive Log:    Updated panel backgrounds to use static variable
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/03/11 15:26:25  rjtierne
 *  Archive Log:    Multinet Wizard: Removed title from titled border
 *  Archive Log:    Using text field creation methods in ComponentFactory with input verifiers
 *  Archive Log:    Removed print statements
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/27 15:34:28  rjtierne
 *  Archive Log:    Fixed preferences model/view sync issue when creating new subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/23 15:06:53  rjtierne
 *  Archive Log:    Replaced hard coded string constants with STLConstants
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/20 21:16:09  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/13 21:31:56  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/10 23:18:05  jijunwan
 *  Archive Log:    changed to store refreshRate rather than refreshRateinSeconds, store TimeUnit by name rather than ordinary
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/20 19:14:20  rjtierne
 *  Archive Log:    Moved all computational logic to controller; view only deals with String values.
 *  Archive Log:    Corrected document listeners for wizard fields
 *  Archive Log:    Minor L&F changes
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/15 19:09:45  rjtierne
 *  Archive Log:    Create fields for refresh rate, refresh rate units, timing window, and # worst nodes.
 *  Archive Log:    Store user-entered values in user settings for storage in database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/13 19:01:51  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the User Preferences Wizard
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view.preferences;

import static com.intel.stl.api.configuration.UserSettings.PROPERTY_MAIL_RECIPIENTS;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_NUM_WORST_NODES;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_REFRESH_RATE;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_REFRESH_RATE_UNITS;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_TIMING_WINDOW;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.DocumentDirtyListener;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.Validator;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.SafeNumberField;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.preferences.PreferencesInputValidator;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.preferences.PreferencesModel;
import com.intel.stl.ui.wizards.view.AbstractTaskView;
import com.intel.stl.ui.wizards.view.IMultinetWizardView;
import com.intel.stl.ui.wizards.view.IWizardView;
import com.intel.stl.ui.wizards.view.MultinetWizardView;

public class PreferencesWizardView extends AbstractTaskView implements
        IPreferencesView {

    private static final long serialVersionUID = 6356778995911484484L;

    private final int MIN_REFRESH_RATE = 1;

    private final int MAX_REFRESH_RATE = 1800;

    private static Logger log = LoggerFactory
            .getLogger(PreferencesWizardView.class);

    private Integer[] defaultRefreshRates;

    private JComboBox<Integer> cboxRefreshRate;

    private SafeNumberField<Integer> txtFldRefreshRate;

    private JComboBox<String> cboxRefreshRateUnits;

    private SafeNumberField<Integer> txtFldTimingWindow;

    private SafeNumberField<Integer> txtFldNumWorstNodes;

    private DocumentListener isDirtyListener;

    private DocumentListener setDirtyListener;

    @SuppressWarnings("unused")
    private IWizardView wizardViewListener = null;

    private IMultinetWizardView multinetWizardViewListener = null;

    private PreferencesModel preferencesModel;

    private boolean dirty;

    private JTextArea emailListArea;

    private JButton emailTestBtn;

    private Pattern pattern;

    private Matcher matcher;

    private JScrollPane scrollPane;

    public PreferencesWizardView(IWizardView wizardViewListener) {

        super("");
        this.wizardViewListener = wizardViewListener;

        try {
            // Set the document listeners again since they weren't available
            // when the super class created these fields
            createDocumentListener();

            // Add the document listeners to the fields
            addDocumentListeners();

            dirty = false;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public PreferencesWizardView(IMultinetWizardView wizardViewListener,
            PreferencesModel preferencesModel) {
        super("");
        this.multinetWizardViewListener = wizardViewListener;
        this.preferencesModel = preferencesModel;

        try {
            // Set the document listeners again since they weren't available
            // when the super class created these fields
            createDocumentListener();

            // Add the document listeners to the fields
            addDocumentListeners();

            dirty = false;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    protected void addDocumentListeners() {

        if (cboxRefreshRate != null) {
            JTextComponent tcRefreshRate =
                    (JTextComponent) cboxRefreshRate.getEditor()
                            .getEditorComponent();
            DocumentListener[] docListeners =
                    new DocumentListener[] { isDirtyListener, setDirtyListener };
            for (DocumentListener docListener : docListeners) {
                tcRefreshRate.getDocument().addDocumentListener(docListener);
                txtFldTimingWindow.getDocument().addDocumentListener(
                        docListener);
                txtFldNumWorstNodes.getDocument().addDocumentListener(
                        docListener);
                emailListArea.getDocument().addDocumentListener(docListener);
            }
        } else {
            log.error(STLConstants.K3044_REFRESH_FIELD_NULL.getValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.AbstractTaskView#getOptionComponent()
     */
    @Override
    protected JComponent getOptionComponent() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JPanel propertiesPanel = new JPanel(new GridBagLayout());
        // mainPanel.setOpaque(true);
        propertiesPanel.setBackground(MultinetWizardView.WIZARD_COLOR);
        propertiesPanel
                .setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 10, 5, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weighty = 1;

        txtFldRefreshRate =
                new SafeNumberField<Integer>(new DecimalFormat("###"),
                        MIN_REFRESH_RATE, true, MAX_REFRESH_RATE, true);
        txtFldRefreshRate.setValidChars(UIConstants.DIGITS);
        cboxRefreshRate =
                ComponentFactory.createComboBox(getDefaultRefreshRates(),
                        txtFldRefreshRate, setDirtyListener, isDirtyListener);
        cboxRefreshRate.setEditable(true);
        cboxRefreshRate.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                setDirty();
            }
        });

        JLabel lblRefreshRate =
                ComponentFactory.getH5Label(
                        STLConstants.K3007_REFRESH_RATE.getValue(), Font.BOLD);
        // Right-align the label:
        lblRefreshRate.setHorizontalAlignment(SwingConstants.RIGHT);

        cboxRefreshRateUnits =
                ComponentFactory.createComboBox(new String[] {
                        STLConstants.K0012_SECONDS.getValue(),
                        STLConstants.K0011_MINUTES.getValue() },
                        setDirtyListener, isDirtyListener);
        cboxRefreshRateUnits.setEditable(false);
        cboxRefreshRateUnits.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                setDirty();
            }
        });

        txtFldTimingWindow =
                ComponentFactory.createNumericTextField(
                        PreferencesInputValidator.getInstance()
                                .getMaxTimingLimit(), isDirtyListener,
                        setDirtyListener);
        JLabel lblTimeWindow =
                ComponentFactory.getH5Label(
                        STLConstants.K3008_TIME_WINDOW.getValue(), Font.BOLD);
        // Right-align the label:
        lblTimeWindow.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel lblSeconds =
                ComponentFactory.getH5Label(
                        STLConstants.K0012_SECONDS.getValue(), Font.BOLD);

        txtFldNumWorstNodes =
                new SafeNumberField<Integer>(new DecimalFormat("###"),
                        PreferencesInputValidator.getInstance()
                                .getMinNumWorstNode(), true,
                        PreferencesInputValidator.getInstance()
                                .getMaxNumWorstNode(), true);
        JLabel lblNumWorstNodes =
                ComponentFactory.getH5Label(
                        STLConstants.K3009_NUM_WORST_NODES.getValue(),
                        Font.BOLD);
        // Right-align the label:
        lblNumWorstNodes.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel lblNodes =
                ComponentFactory.getH5Label(
                        STLConstants.K1024_NODE_RESOURCE.getValue(), Font.BOLD);

        gc.gridwidth = 1;
        propertiesPanel.add(lblRefreshRate, gc);
        gc.weightx = 1;
        propertiesPanel.add(cboxRefreshRate, gc);
        gc.weightx = 0;
        propertiesPanel.add(cboxRefreshRateUnits, gc);

        gc.gridy = 1;
        propertiesPanel.add(lblTimeWindow, gc);
        gc.weightx = 1;
        propertiesPanel.add(txtFldTimingWindow, gc);
        gc.weightx = 0;
        propertiesPanel.add(lblSeconds, gc);

        gc.gridy = 2;
        propertiesPanel.add(lblNumWorstNodes, gc);
        gc.weightx = 1;
        propertiesPanel.add(txtFldNumWorstNodes, gc);
        gc.weightx = 0;
        propertiesPanel.add(lblNodes, gc);

        mainPanel.setOpaque(true);
        mainPanel.setBackground(UIConstants.INTEL_WHITE);
        gc.insets = new Insets(10, 5, 10, 5);
        mainPanel.add(propertiesPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(createEmailSettingsPanel());
        return mainPanel;
    }

    private JPanel createEmailSettingsPanel() {
        JLabel emailLbl =
                ComponentFactory.getH5Label(
                        STLConstants.K5007_WIZARD_EMAIL_PREFERENCES_LIST
                                .getValue(), Font.BOLD);

        // Email pattern to make sure user enters valid email
        pattern = Pattern.compile(Util.EMAIL_PATTERN);

        emailListArea = new JTextArea();
        emailListArea.setText("");
        emailListArea.setOpaque(true);
        emailListArea.setRows(3);
        emailListArea.setFont(UIConstants.H5_FONT.deriveFont(Font.PLAIN));
        emailListArea.setWrapStyleWord(true);
        emailListArea.setLineWrap(true);
        emailListArea.getDocument().putProperty("emailList", emailListArea);

        scrollPane = new JScrollPane(emailListArea);
        scrollPane.setBorder(BorderFactory
                .createLineBorder(UIConstants.INTEL_BORDER_GRAY));

        emailTestBtn =
                ComponentFactory.getImageButton(UIImages.PLAY.getImageIcon());
        emailTestBtn
                .setToolTipText(STLConstants.K5008_WIZARD_EMAIL_TEST_BTN_TOOLTIP
                        .getValue());
        emailTestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTestEmail();
            }
        });

        JLabel emailTestLbl =
                ComponentFactory.getH5Label(
                        STLConstants.K5009_WIZARD_EMAIL_TEST_LABEL_TEXT
                                .getValue(), Font.BOLD);
        emailTestLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel emailSettingsPanel = new JPanel();
        emailSettingsPanel.setOpaque(true);
        emailSettingsPanel.setBackground(UIConstants.INTEL_WHITE);

        emailSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0,
                10));
        emailSettingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 3, 0, 0);
        gbc.weightx = 1;
        emailSettingsPanel.add(emailLbl, gbc);
        gbc.weightx = 0;
        emailSettingsPanel.add(emailTestLbl, gbc);
        gbc.insets = new Insets(0, 3, 0, 10);
        emailSettingsPanel.add(emailTestBtn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        emailSettingsPanel.add(scrollPane, gbc);

        return emailSettingsPanel;
    }

    protected Integer[] getDefaultRefreshRates() {
        if (defaultRefreshRates == null) {
            defaultRefreshRates = new Integer[] { 10, 20, 30 };
        }
        return defaultRefreshRates;
    }

    public void sendTestEmail() {
        String recipients = emailListArea.getText();
        multinetWizardViewListener.onEmailTest(recipients);
    }

    @Override
    public void setWizardListener(IWizardTask listener) {
        super.setWizardListener(listener);
    }

    protected void addCBoxItem(Integer item, JComboBox<Integer> cbox) {
        for (int i = 0; i < cbox.getItemCount(); i++) {
            Integer history = cbox.getItemAt(i);
            if (item.equals(history)) {
                return;
            }
        }
        cbox.addItem(item);
    }

    /**
     * 
     * <i>Description: Document listeners to detect when changes occur to the
     * subnet wizard fields</i>
     * 
     */
    protected void createDocumentListener() {

        if (isDirtyListener == null) {
            isDirtyListener = new DocumentDirtyListener() {

                @Override
                public void setDirty(DocumentEvent e) {
                    dirty = true;
                }

            };
        }

        if (setDirtyListener == null) {
            setDirtyListener = new DocumentDirtyListener() {

                @Override
                public void setDirty(DocumentEvent e) {
                    doOnEdit(e);
                }
            };
        }
    }

    private void doOnEdit(DocumentEvent e) {
        Object emailListComp = e.getDocument().getProperty("emailList");
        if (emailListComp != null && emailListComp.equals(emailListArea)) {
            // If the change is for the email list, only set dirty
            // if typed information is correct
            if (isEmailListValid()) {
                setDirty();
            }
        } else {
            // For all other components call setDirty():
            setDirty();
        }
    }

    protected void setDirty() {

        dirty = true;

        final JTextComponent txtCompRefreshRate =
                (JTextComponent) cboxRefreshRate.getEditor()
                        .getEditorComponent();
        if ((txtCompRefreshRate.getText().length() > 0)
                && (txtFldTimingWindow.getText().length() > 0)
                && (txtFldNumWorstNodes.getText().length() > 0)) {

            multinetWizardViewListener.enableNext(true);
            multinetWizardViewListener.enableApply(true);
            multinetWizardViewListener.enableReset(true);
        } else {
            multinetWizardViewListener.enableNext(false);
            multinetWizardViewListener.enableApply(false);
            multinetWizardViewListener.enableReset(true);
        }
    }

    private boolean isEmailListValid() {
        boolean valid = false;
        if (isVisible()) {

            String emailListStr = emailListArea.getText().trim();
            if (emailListStr.isEmpty()) {
                // Set border to normal color as empty list is allowed
                scrollPane.setBorder(BorderFactory
                        .createLineBorder(UIConstants.INTEL_BORDER_GRAY));
                valid = true;
                return valid;
            }

            String[] emails = emailListStr.split(";");
            for (int i = 0; i < emails.length; i++) {
                matcher = pattern.matcher(emails[i].trim());
                if (matcher.matches()) {
                    // Set border to normal color
                    scrollPane.setBorder(BorderFactory
                            .createLineBorder(UIConstants.INTEL_BORDER_GRAY));
                    multinetWizardViewListener.enableApply(true);
                    emailTestBtn.setEnabled(true);
                    valid = true;
                } else {
                    // Set border to red color and disable 'Apply' button
                    scrollPane.setBorder(BorderFactory
                            .createLineBorder(UIConstants.INTEL_DARK_RED));
                    multinetWizardViewListener.enableApply(false);
                    emailTestBtn.setEnabled(false);
                    valid = false;
                }
            }
        }
        return valid;
    }

    public void clearPanel() {
        cboxRefreshRate.setSelectedIndex(0);
        cboxRefreshRateUnits.setSelectedIndex(0);
        txtFldTimingWindow.setText("");
        txtFldNumWorstNodes.setText("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#resetPanel()
     */
    @Override
    public void resetPanel() {
        // Get properties from the model
        Properties preferences = preferencesModel.getPreferences();

        if (preferences != null) {
            // Reinitialize combo box
            for (Integer value : getDefaultRefreshRates()) {
                addCBoxItem(value, cboxRefreshRate);
            }

            // Select the units previously stored
            String unitStr =
                    preferences.getProperty(PROPERTY_REFRESH_RATE_UNITS);
            TimeUnit unit = TimeUnit.valueOf(unitStr.toUpperCase());
            unitStr = PreferencesInputValidator.getTimeUnitString(unit);
            cboxRefreshRateUnits.setSelectedItem(unitStr);

            // If units is in minutes, convert the refresh rate value
            String storeValue = preferences.getProperty(PROPERTY_REFRESH_RATE);
            int refreshRate = 5;
            if (storeValue != null) {
                try {
                    refreshRate = Integer.parseInt(storeValue);
                } catch (NumberFormatException nfe) {
                    // shouldn't happen
                    log.error("Invalid refresh rate '" + storeValue
                            + "' from DB.", nfe);
                    nfe.printStackTrace();
                }
            }

            // Add the refresh rate from storage if it's not in the list
            if (Validator.integerInRange(refreshRate, MIN_REFRESH_RATE,
                    MAX_REFRESH_RATE)) {
                addCBoxItem(refreshRate, cboxRefreshRate);
                cboxRefreshRate.setSelectedItem(refreshRate);
            }

            // Display the timing window and # worst nodes
            txtFldTimingWindow.setText(preferences
                    .getProperty(PROPERTY_TIMING_WINDOW));
            txtFldNumWorstNodes.setText(preferences
                    .getProperty(PROPERTY_NUM_WORST_NODES));

            emailListArea.setText(preferences
                    .getProperty(PROPERTY_MAIL_RECIPIENTS));
            emailTestBtn.setEnabled(true);

            closeStatusPanel();
            dirty = false;
        }
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
     * @see com.intel.stl.ui.wizards.view.ITaskView#isDirty()
     */
    @Override
    public boolean isDirty() {

        return dirty;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#setSubnet(com.intel.stl.api.subnet
     * .SubnetDescription)
     */
    @Override
    public void setSubnet(SubnetDescription subnet) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.preferences.IPreferencesView#
     * getRefreshRate()
     */
    @Override
    public String getRefreshRate() {
        return cboxRefreshRate.getSelectedItem().toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.preferences.IPreferencesView#
     * getRefreshRateUnits()
     */
    @Override
    public String getRefreshRateUnits() {
        String unitStr = cboxRefreshRateUnits.getSelectedItem().toString();
        return unitStr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.preferences.IPreferencesView#
     * getTimeWindowInSeconds ()
     */
    @Override
    public String getTimeWindowInSeconds() {

        return txtFldTimingWindow.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.preferences.IPreferencesView#getNumWorstNodes
     * ()
     */
    @Override
    public String getNumWorstNodes() {

        return txtFldNumWorstNodes.getText();
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
                preferencesModel = model.getPreferencesModel();

                try {
                    Integer refreshRate =
                            Integer.valueOf(preferencesModel
                                    .getRefreshRateInSeconds());
                    addCBoxItem(refreshRate, cboxRefreshRate);
                    cboxRefreshRate.setSelectedItem(refreshRate);
                } catch (NumberFormatException nfe) {
                    // shouldn't happen
                    nfe.printStackTrace();
                    cboxRefreshRate.setSelectedIndex(0);
                }

                cboxRefreshRateUnits.setSelectedItem(preferencesModel
                        .getRefreshRateUnits());

                txtFldTimingWindow.setText(preferencesModel
                        .getTimingWindowInSeconds());

                txtFldNumWorstNodes.setText(preferencesModel.getNumWorstNodes());

                emailListArea.setText(preferencesModel.getMailRecipients());

                dirty = false;
            }
        });
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    public boolean isEditValid() {
        return txtFldTimingWindow.isEditValid()
                && txtFldNumWorstNodes.isEditValid()
                && txtFldRefreshRate.isEditValid();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.preferences.IPreferencesView#getEmailList()
     * 
     * Returns a String of email addresses separated by semicolon. This string
     * may contain white spaces.
     */
    @Override
    public String getEmailList() {
        return emailListArea.getText().trim();
    }
}
