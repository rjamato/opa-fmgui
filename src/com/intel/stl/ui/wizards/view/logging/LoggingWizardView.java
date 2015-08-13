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
 *  Archive Log:    Revision 1.11.2.1  2015/08/12 15:27:05  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/02 13:33:01  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/20 21:14:34  rjtierne
 *  Archive Log:    Multinet Wizard: Kept logging wizard up to date with its interface, but will be moved to the configuration menu later.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/13 21:31:59  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/01/20 19:15:07  rjtierne
 *  Archive Log:    Fixed document listener to detect changes in user entries
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/13 19:01:26  rjtierne
 *  Archive Log:    Removed warnings by specifying generic types
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/11 21:48:20  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/19 19:46:03  rjtierne
 *  Archive Log:    Rewrote Logging Wizard view to remove inherited panels, now only supporting rolling file appender.  Left some commented code which can be reactivated if case new appenders are added
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak, rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.view.logging;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.LoggingThreshold;
import com.intel.stl.api.configuration.RollingFileAppender;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IntelComboBoxUI;
import com.intel.stl.ui.model.LoggingThresholdViz;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.logging.ILoggingControl;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.view.AbstractTaskView;
import com.intel.stl.ui.wizards.view.ITaskView;
import com.intel.stl.ui.wizards.view.IWizardView;

public class LoggingWizardView extends AbstractTaskView implements ITaskView {

    private static final long serialVersionUID = -5605031349189530174L;

    private DocumentListener isDirtyListener;

    private DocumentListener setDirtyListener;

    private boolean dirty;

    private JTextField txtfldConversionPattern;

    private JFileChooser fileChooser;

    private JComboBox<String> cboxThresholdValue;

    private JTextField txtfldMaxFileSize;

    private JTextField txtfldMaxBackUpIndex;

    private JTextField txtfldFileLocation;

    private final String byteStr = "B";

    private JComboBox<String> cboxFileSizeUnit;

    private final IWizardView wizardViewListener;

    @SuppressWarnings("unused")
    private IWizardTask loggingWizardController;

    @SuppressWarnings("unused")
    private ILoggingControl loggingControlListener;

    private RollingFileAppender rollingFileAppender;

    public LoggingWizardView(IWizardView wizardViewListener) {
        super(STLConstants.K0669_LOGGING.getValue());
        this.wizardViewListener = wizardViewListener;

        createDocumentListener();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.AbstractTaskView#getOptionComponent()
     */
    @Override
    protected JComponent getOptionComponent() {
        final JPanel pnlConfigAppender = new JPanel();
        pnlConfigAppender.setOpaque(true);
        pnlConfigAppender.setBackground(UIConstants.INTEL_WHITE);
        pnlConfigAppender.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Information Level Label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.weighty = 1;
        JLabel lblThreshold =
                ComponentFactory.getH5Label(
                        STLConstants.K0636_INFORMATION_LEVEL.getValue(),
                        Font.BOLD);
        pnlConfigAppender.add(lblThreshold, gc);

        // Information Level combo box
        gc.gridx = 1;
        gc.weightx = 1;
        String[] thresholdValues =
                new String[] { STLConstants.K0699_OFF.getValue(),
                        STLConstants.K0631_INFO.getValue(),
                        STLConstants.K0630_DEBUG.getValue(),
                        STLConstants.K3002_WARN.getValue(),
                        STLConstants.K0030_ERROR.getValue(),
                        STLConstants.K0632_FATAL.getValue(),
                        STLConstants.K0698_ALL.getValue(), };
        cboxThresholdValue = new JComboBox<String>(thresholdValues);
        cboxThresholdValue.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                // Disable the logging fields if the threshold level = OFF
                int selectedIndex =
                        ((JComboBox<?>) e.getSource()).getSelectedIndex();
                enableFields(selectedIndex);

                setDirty();
            }
        });
        cboxThresholdValue.setUI(new IntelComboBoxUI());
        pnlConfigAppender.add(cboxThresholdValue, gc);

        // Output Format Label
        gc.gridx = 0;
        gc.gridy++;
        gc.weightx = 0;
        JLabel lblConversionPattern =
                ComponentFactory.getH5Label(
                        STLConstants.K0637_OUTPUT_FORMAT.getValue(), Font.BOLD);
        pnlConfigAppender.add(lblConversionPattern, gc);

        // Output Format Text Field
        gc.gridx = 1;
        gc.weightx = 1;
        txtfldConversionPattern = new JTextField(20);
        txtfldConversionPattern.getDocument().addDocumentListener(
                isDirtyListener);
        txtfldConversionPattern.getDocument().addDocumentListener(
                setDirtyListener);
        pnlConfigAppender.add(txtfldConversionPattern, gc);

        // Output Format Help Button
        gc.gridx = 2;
        gc.weightx = 0;
        JButton btnConversionPatternHelp =
                ComponentFactory.getImageButton(UIImages.HELP_ICON
                        .getImageIcon());
        final ConversionPatternHelpDialog dlgPatternHelp =
                new ConversionPatternHelpDialog(pnlConfigAppender);
        btnConversionPatternHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlgPatternHelp.setVisible(true);
            }
        });
        pnlConfigAppender.add(btnConversionPatternHelp, gc);

        // The layout checkbox and combobox may be needed in the future
        // so it is left in the code as a place holder
        // Layout CheckBox
        // gc.gridx = 0;
        // gc.gridy++;
        // gc.fill = GridBagConstraints.HORIZONTAL;
        // chkBoxLayout = ComponentFactory.getIntelCheckBox("");
        // chkBoxLayout.addItemListener(new ItemListener() {
        // @Override
        // public void itemStateChanged(ItemEvent e) {
        // if (e.getStateChange() == ItemEvent.SELECTED) {
        // cboxLayoutValue.setEnabled(true);
        // txtfldConversionPattern.setEnabled(true);
        // } else if (e.getStateChange() == ItemEvent.DESELECTED) {
        // cboxLayoutValue.setEnabled(false);
        // txtfldConversionPattern.setEnabled(false);
        // }
        // dirty = true;
        // }
        // });
        // chkBoxLayout.setText(STLConstants.K0690_LAYOUT.getValue());
        // chkBoxLayout.setFont(UIConstants.H5_FONT.deriveFont(Font.BOLD));
        // chkBoxLayout.setForeground(UIConstants.INTEL_DARK_GRAY);
        // pnlConfigAppender.add(chkBoxLayout, gc);

        // Layout ComboBox
        // gc.gridx = 1;
        // String layoutValueArray[] =
        // new String[] { STLConstants.K0691_PATTERN_LAYOUT.getValue(),
        // STLConstants.K0692_ENHANCED_PATTERN.getValue() };
        // cboxLayoutValue = new JComboBox(layoutValueArray);
        // cboxLayoutValue.setUI(new IntelComboBoxUI());
        // cboxLayoutValue.addItemListener(new ItemListener() {
        // @Override
        // public void itemStateChanged(ItemEvent e) {
        // dirty = true;
        // }
        // });
        // pnlConfigAppender.add(cboxLayoutValue, gc);

        // Max File Size Label
        gc.gridx = 0;
        gc.gridy++;
        JLabel lblMaxFileSize =
                ComponentFactory.getH5Label(
                        STLConstants.K0638_MAX_FILE_SIZE.getValue(), Font.BOLD);
        pnlConfigAppender.add(lblMaxFileSize, gc);

        // Max File Size Text Field
        gc.gridx = 1;
        gc.weightx = 1;
        txtfldMaxFileSize = createTextField("");
        txtfldMaxFileSize.getDocument().addDocumentListener(setDirtyListener);
        txtfldMaxFileSize.getDocument().addDocumentListener(isDirtyListener);
        pnlConfigAppender.add(txtfldMaxFileSize, gc);

        // Max File Size Units ComboBox
        gc.gridx = 2;
        gc.weightx = 0;
        String unitValueArray[] =
                new String[] { STLConstants.K0697_BYTE.getValue(),
                        STLConstants.K0695_KB.getValue(),
                        STLConstants.K0722_MB.getValue(),
                        STLConstants.K0696_GB.getValue() };
        cboxFileSizeUnit = new JComboBox<String>(unitValueArray);
        cboxFileSizeUnit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dirty = true;
            }
        });
        cboxFileSizeUnit.setUI(new IntelComboBoxUI());
        pnlConfigAppender.add(cboxFileSizeUnit, gc);

        // Max # Files Label
        gc.gridx = 0;
        gc.gridy++;
        JLabel lblMaxBackupIndex =
                ComponentFactory.getH5Label(
                        STLConstants.K0640_MAX_NUM_FILE.getValue(), Font.BOLD);
        pnlConfigAppender.add(lblMaxBackupIndex, gc);

        // Max # Files Text Field
        gc.gridx = 1;
        txtfldMaxBackUpIndex = createTextField("");
        txtfldMaxBackUpIndex.getDocument()
                .addDocumentListener(setDirtyListener);
        txtfldMaxBackUpIndex.getDocument().addDocumentListener(isDirtyListener);
        pnlConfigAppender.add(txtfldMaxBackUpIndex, gc);

        // File Location Label
        gc.gridx = 0;
        gc.gridy++;
        JLabel lblFileLocation =
                ComponentFactory.getH5Label(
                        STLConstants.K0641_FILE_LOC.getValue(), Font.BOLD);
        pnlConfigAppender.add(lblFileLocation, gc);

        // File Location Text Field
        gc.gridx = 1;
        gc.weightx = 1;
        txtfldFileLocation = createTextField("");
        txtfldFileLocation.getDocument().addDocumentListener(setDirtyListener);
        txtfldFileLocation.getDocument().addDocumentListener(isDirtyListener);
        pnlConfigAppender.add(txtfldFileLocation, gc);

        // File Location Browser Button
        gc.fill = GridBagConstraints.NONE;
        gc.gridx = 2;
        gc.weightx = 0;
        JButton browseButton =
                ComponentFactory.getImageButton(UIImages.FOLDER_ICON
                        .getImageIcon());
        browseButton.setToolTipText(STLConstants.K0642_BROWSE.getValue());
        fileChooser = new JFileChooser();
        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle(STLConstants.K0643_SELECT_FILE
                        .getValue());
                File logLocation =
                        new File(rollingFileAppender.getFileLocation());
                fileChooser.setCurrentDirectory(logLocation);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(pnlConfigAppender);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    txtfldFileLocation.setText(file.getAbsolutePath());
                }
            }
        });
        pnlConfigAppender.add(browseButton, gc);

        // Add a component listener to this panel
        pnlConfigAppender.addAncestorListener(new AncestorListener() {

            @Override
            public void ancestorAdded(AncestorEvent event) {
                // Disable the logging fields if the threshold level = OFF
                int selectedIndex = cboxThresholdValue.getSelectedIndex();
                enableFields(selectedIndex);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        });
        return pnlConfigAppender;
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

    protected void enableFields(int selectedIndex) {

        // Disable the logging fields if the threshold level = OFF
        int offIndex = LoggingThreshold.OFF.getId();

        boolean enabled = (selectedIndex == offIndex) ? false : true;
        txtfldConversionPattern.setEnabled(enabled);
        txtfldMaxFileSize.setEnabled(enabled);
        txtfldMaxBackUpIndex.setEnabled(enabled);
        txtfldFileLocation.setEnabled(enabled);
    }

    protected void setDirty() {
        dirty = true;
        closeStatusPanel();
        wizardViewListener.enableApply(true);
        wizardViewListener.enableNext(true);

    }

    public void setDetails(RollingFileAppender appenderConfig) {

        txtfldConversionPattern.setText(appenderConfig.getConversionPattern());
        setLevel(appenderConfig.getThreshold());
        setMaxFileSize(appenderConfig.getMaxFileSize());
        txtfldMaxBackUpIndex.setText(String.valueOf(appenderConfig
                .getMaxNumOfBackUp()));
        txtfldFileLocation.setText(appenderConfig.getFileLocation());
    }

    private void setMaxFileSize(String maxFileSizeStr) {
        // There could be no unit string at the end. That is for unit Byte.
        // See if it's Byte by checking the last character.

        String unit =
                maxFileSizeStr.substring(maxFileSizeStr.length() - 2,
                        maxFileSizeStr.length());
        if (unit.equals(STLConstants.K0695_KB.getValue())) {
            cboxFileSizeUnit.setSelectedItem(STLConstants.K0695_KB.getValue());
            txtfldMaxFileSize.setText(maxFileSizeStr.substring(0,
                    maxFileSizeStr.length() - 2));
        } else if (unit.equals(STLConstants.K0722_MB.getValue())) {
            cboxFileSizeUnit.setSelectedItem(STLConstants.K0722_MB.getValue());
            txtfldMaxFileSize.setText(maxFileSizeStr.substring(0,
                    maxFileSizeStr.length() - 2));
        } else if (unit.equals(STLConstants.K0696_GB.getValue())) {
            cboxFileSizeUnit.setSelectedItem(STLConstants.K0696_GB.getValue());
            txtfldMaxFileSize.setText(maxFileSizeStr.substring(0,
                    maxFileSizeStr.length() - 2));
        } else if (!unit.contains(byteStr)) {
            cboxFileSizeUnit
                    .setSelectedItem(STLConstants.K0697_BYTE.getValue());
            txtfldMaxFileSize.setText(maxFileSizeStr);
        }
        ;
    }

    @Override
    public void setWizardListener(IWizardTask listener) {
        super.setWizardListener(listener);
        loggingWizardController = listener;
    }

    public void setLoggingControlListener(ILoggingControl listener) {
        this.loggingControlListener = listener;
    }

    public void setConversionPattern(String pattern) {
        txtfldConversionPattern.setText(pattern);
    }

    // public void initView(String[] appenderList) {
    public void initView(final HashMap<String, AppenderConfig> appenderConfigMap) {

        Runnable init = new Runnable() {

            @Override
            public void run() {

                // Get the rolling file appender
                rollingFileAppender =
                        (RollingFileAppender) appenderConfigMap
                                .get(STLConstants.K3003_ROLLING_FILE_APPENDER
                                        .getValue());

                // Populate the logging wizard form
                // If the threshold is null, initialize the combo box to default
                // ERROR
                if (rollingFileAppender != null) {
                    LoggingThreshold threshold =
                            rollingFileAppender.getThreshold();
                    if (threshold == null) {
                        cboxThresholdValue
                                .setSelectedIndex(LoggingThresholdViz.ERROR
                                        .getId());
                    } else {
                        cboxThresholdValue.setSelectedIndex(threshold.getId());
                    }

                    // Update output format
                    txtfldConversionPattern.setText(rollingFileAppender
                            .getConversionPattern());

                    // Set the max file size
                    setMaxFileSize(rollingFileAppender.getMaxFileSize());
                    txtfldMaxBackUpIndex.setText(String
                            .valueOf(rollingFileAppender.getMaxNumOfBackUp()));

                    // Set the logger's file location
                    txtfldFileLocation.setText(rollingFileAppender
                            .getFileLocation());
                }
            }
        };
        Util.runInEDT(init);
    }

    protected void setLevel(LoggingThreshold threshold) {

        // cboxThresholdValue.setSelectedIndex(threshold.getId());

        // if (threshold.equalsIgnoreCase(STLConstants.K0631_INFO.getValue())) {
        // cboxThresholdValue.setSelectedIndex(0);
        // } else if (threshold.equalsIgnoreCase(STLConstants.K0630_DEBUG
        // .getValue())) {
        // cboxThresholdValue.setSelectedIndex(1);
        // } else if (threshold.equalsIgnoreCase(STLConstants.K3002_WARN
        // .getValue())) {
        // cboxThresholdValue.setSelectedIndex(2);
        // } else if (threshold.equalsIgnoreCase(STLConstants.K0030_ERROR
        // .getValue())) {
        // cboxThresholdValue.setSelectedIndex(3);
        // } else if (threshold.equalsIgnoreCase(STLConstants.K0632_FATAL
        // .getValue())) {
        // cboxThresholdValue.setSelectedIndex(4);
        // } else if (threshold
        // .equalsIgnoreCase(STLConstants.K0698_ALL.getValue())) {
        // cboxThresholdValue.setSelectedIndex(5);
        // } else {
        // cboxThresholdValue.setSelectedIndex(6);
        // }

    }

    // protected void setLayoutType(AppenderConfig config) {
    // Map<String, String> settings = config.getSettings();
    // if (!settings.containsKey(LAYOUT.getValue())) {
    // txtfldConversionPattern.setEnabled(false);
    // chkBoxLayout.setSelected(false);
    // cboxLayoutValue.setEnabled(false);
    // } else {
    // // Settings should also include key for conversion patter,
    // // otherwise, prompt the user.
    // if (settings.containsKey(CONVERSION_PATTERN.getValue())) {
    // txtfldConversionPattern.setEnabled(true);
    // txtfldConversionPattern.setText(config.getConversionPattern());
    // chkBoxLayout.setSelected(true);
    // String layout = settings.get(LAYOUT.getValue());
    // if (layout.contains(STLConstants.K0691_PATTERN_LAYOUT
    // .getValue())) {
    // cboxLayoutValue
    // .setSelectedItem(STLConstants.K0691_PATTERN_LAYOUT
    // .getValue());
    // } else if (layout.contains(STLConstants.K0692_ENHANCED_PATTERN
    // .getValue())) {
    // cboxLayoutValue
    // .setSelectedItem(STLConstants.K0692_ENHANCED_PATTERN
    // .getValue());
    // }
    // }
    //
    // }
    //
    // }

    public void updateAppender(AppenderConfig appenderConfig) {

        try {

            if (appenderConfig instanceof RollingFileAppender) {

                rollingFileAppender.setName(appenderConfig.getName());

                rollingFileAppender.setThreshold(LoggingThresholdViz
                        .getLoggingThreshold((String) cboxThresholdValue
                                .getSelectedItem()));

                rollingFileAppender
                        .setConversionPattern(txtfldConversionPattern.getText());

                try {
                    rollingFileAppender.setFileLocation(txtfldFileLocation
                            .getText().trim());

                } catch (ConfigurationException e) {
                    showMessage(e.getMessage(), null,
                            JOptionPane.ERROR_MESSAGE, (Object[]) null);
                }

                String fileSizeUnitSelected =
                        (String) cboxFileSizeUnit.getSelectedItem();
                String maxFileSizestr = txtfldMaxFileSize.getText().trim();
                if (fileSizeUnitSelected.equals(STLConstants.K0697_BYTE
                        .getValue())) {
                    rollingFileAppender.setMaxFileSize(maxFileSizestr);
                } else {
                    String maxFileSizeInclUnitStr =
                            maxFileSizestr + fileSizeUnitSelected;
                    rollingFileAppender.setMaxFileSize(maxFileSizeInclUnitStr);
                }

                // Set the number of backups
                String maxNumBackupsStr = txtfldMaxBackUpIndex.getText().trim();
                rollingFileAppender.setMaxNumOfBackUp(maxNumBackupsStr);
            }
        } catch (Exception e) {
            showMessage(
                    UILabels.STL50057_LOGGING_CONFIG_SAVE_FAILURE
                            .getDescription(),
                    null, JOptionPane.ERROR_MESSAGE, (Object) null);
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

    @Override
    public void resetPanel() {
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
        resetPanel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#update(com.intel.stl.ui.wizards
     * .model.MultinetWizardModel)
     */
    @Override
    public void updateView(MultinetWizardModel model) {
        // TODO Auto-generated method stub

    }

    public void clearPanel() {
        cboxThresholdValue.setSelectedItem(STLConstants.K0699_OFF.getValue());
        cboxFileSizeUnit.setSelectedIndex(0);
        txtfldConversionPattern.setText("");
        txtfldFileLocation.setText("");
        txtfldMaxFileSize.setText("");
        txtfldMaxBackUpIndex.setText("");
    }
}
