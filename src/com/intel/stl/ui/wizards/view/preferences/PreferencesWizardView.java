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
 *  File Name: PreferencesWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import static com.intel.stl.api.configuration.UserSettings.PROPERTY_NUM_WORST_NODES;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_REFRESH_RATE;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_REFRESH_RATE_UNITS;
import static com.intel.stl.api.configuration.UserSettings.PROPERTY_TIMING_WINDOW;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.Validator;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.preferences.IPreferencesControl;
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

    private final String[] defaultRefreshRates = new String[] { "10", "20",
            "30" };

    private JComboBox<String> cboxRefreshRate;

    private JComboBox<String> cboxRefreshRateUnits;

    private JTextField txtFldTimingWindow;

    private JTextField txtFldNumWorstNodes;

    private DocumentListener isDirtyListener;

    private DocumentListener setDirtyListener;

    @SuppressWarnings("unused")
    private IWizardView wizardViewListener = null;

    private IMultinetWizardView multinetWizardViewListener = null;

    private IPreferencesControl preferencesControlListener;

    private PreferencesModel preferencesModel;

    private boolean dirty;

    public PreferencesWizardView(IWizardView wizardViewListener) {

        super("");
        this.wizardViewListener = wizardViewListener;

        try {
            // Set the document listeners again since they weren't available
            // when the super class created these fields
            createDocumentListener();
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
            }
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
            }
            dirty = false;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.AbstractTaskView#getOptionComponent()
     */
    @Override
    protected JComponent getOptionComponent() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBackground(MultinetWizardView.WIZARD_COLOR);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 2, 3, 2);
        gc.weighty = 1;

        cboxRefreshRate =
                ComponentFactory.createNumericComboBox(new String[] { "10",
                        "20", "30" }, setDirtyListener, isDirtyListener);
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

        cboxRefreshRateUnits =
                ComponentFactory.createComboBox(new String[] {
                        STLConstants.K0012_SECONDS.getValue(),
                        STLConstants.K0011_MINUTES.getValue() },
                        setDirtyListener, isDirtyListener);
        cboxRefreshRateUnits.setEditable(true);
        cboxRefreshRateUnits.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                setDirty();
            }
        });

        txtFldTimingWindow =
                ComponentFactory.createNumericTextField(isDirtyListener,
                        setDirtyListener);
        JLabel lblTimeWindow =
                ComponentFactory.getH5Label(
                        STLConstants.K3008_TIME_WINDOW.getValue(), Font.BOLD);
        JLabel lblSeconds =
                ComponentFactory.getH5Label(
                        STLConstants.K0012_SECONDS.getValue(), Font.BOLD);

        txtFldNumWorstNodes =
                ComponentFactory.createNumericTextField(isDirtyListener,
                        setDirtyListener);
        JLabel lblNumWorstNodes =
                ComponentFactory.getH5Label(
                        STLConstants.K3009_NUM_WORST_NODES.getValue(),
                        Font.BOLD);
        JLabel lblNodes =
                ComponentFactory.getH5Label(
                        STLConstants.K1024_NODE_RESOURCE.getValue(), Font.BOLD);

        gc.weightx = 0;
        gc.gridwidth = 1;
        mainPanel.add(lblRefreshRate, gc);
        gc.weightx = 1;
        mainPanel.add(cboxRefreshRate, gc);
        gc.weightx = 0;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        mainPanel.add(cboxRefreshRateUnits, gc);

        gc.weightx = 0;
        gc.gridwidth = 1;
        mainPanel.add(lblTimeWindow, gc);
        mainPanel.add(txtFldTimingWindow, gc);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        mainPanel.add(lblSeconds, gc);

        gc.weightx = 0;
        gc.gridwidth = 1;
        mainPanel.add(lblNumWorstNodes, gc);
        mainPanel.add(txtFldNumWorstNodes, gc);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        mainPanel.add(lblNodes, gc);

        return mainPanel;
    }

    @Override
    public void setWizardListener(IWizardTask listener) {
        super.setWizardListener(listener);
    }

    protected void addCBoxItem(String item, JComboBox<String> cbox) {
        for (int i = 0; i < cbox.getItemCount(); i++) {
            String history = cbox.getItemAt(i);
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
        }

        if (setDirtyListener == null) {
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
            multinetWizardViewListener.enableReset(false);
        }
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

        if (preferencesControlListener != null) {

            UserSettings userSettings =
                    preferencesControlListener.getUserSettings();
            Properties preferences = null;
            try {
                preferences = userSettings.getUserPreference();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (preferences != null) {

                // Reinitialize combo box
                for (String str : defaultRefreshRates) {
                    addCBoxItem(str, cboxRefreshRate);
                }

                // Select the units previously stored
                String unitStr =
                        preferences.getProperty(PROPERTY_REFRESH_RATE_UNITS);
                TimeUnit unit = TimeUnit.valueOf(unitStr.toUpperCase());
                unitStr = PreferencesInputValidator.getTimeUnitString(unit);
                cboxRefreshRateUnits.setSelectedItem(unitStr);

                // If units is in minutes, convert the refresh rate value
                String storeValue =
                        preferences.getProperty(PROPERTY_REFRESH_RATE);
                int refreshRate =
                        storeValue == null ? 5 : Integer.parseInt(storeValue);

                // Add the refresh rate from storage if it's not in the list
                if (Validator.integerInRange(refreshRate, MIN_REFRESH_RATE,
                        MAX_REFRESH_RATE)) {
                    addCBoxItem(String.valueOf(refreshRate), cboxRefreshRate);
                    cboxRefreshRate.setSelectedItem(refreshRate);
                }

                // Display the timing window and # worst nodes
                txtFldTimingWindow.setText(preferences
                        .getProperty(PROPERTY_TIMING_WINDOW));
                txtFldNumWorstNodes.setText(preferences
                        .getProperty(PROPERTY_NUM_WORST_NODES));

                closeStatusPanel();
                dirty = false;
            }
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
     * @see
     * com.intel.stl.ui.wizards.view.preferences.IPreferencesView#setControlListener
     * (com.intel.stl.ui.wizards.impl.preferences.IPreferencesControl)
     */
    @Override
    public void setControlListener(IPreferencesControl listener) {

        this.preferencesControlListener = listener;
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

                cboxRefreshRate.setSelectedItem(Integer
                        .valueOf(preferencesModel.getRefreshRateInSeconds()));

                cboxRefreshRateUnits.setSelectedItem(preferencesModel
                        .getRefreshRateUnits());

                txtFldTimingWindow.setText(preferencesModel
                        .getTimingWindowInSeconds());

                txtFldNumWorstNodes.setText(preferencesModel.getNumWorstNodes());
            }
        });
    }
}
