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
 *  File Name: HostInfoPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.23  2015/11/09 20:54:03  fernande
 *  Archive Log:    PR130852 - The 1st subnet in the Subnet Wizard displays "Abandon Changes" message when no changes are made. Fixed bug where setting the key store value would not dirty the wizard.
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/10/06 15:53:49  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Fixed Typo
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/08/17 18:54:33  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/08/11 13:57:01  jijunwan
 *  Archive Log:    PR 129925 - Error when entering IP address of host in Configuration setup Wizard screen
 *  Archive Log:    - changed to allow start with digital
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/07/17 21:20:56  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - improved CompomentFactory to create text field based on argument allowEmpty
 *  Archive Log:    - apply it on Log preference and subnet name on Setup Wizard to forbid empty string
 *  Archive Log:    - apply it on key file location on Setup Wizard to allow empty string
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/07/17 20:48:22  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - introduced isEditValid to allow us check whether we have valid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/07/16 21:28:14  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - restrict host name to follow node desc rules
 *  Archive Log:    - restrict port number must be positive integer
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/07/13 19:36:34  jijunwan
 *  Archive Log:    PR 129528 - input validation improvement
 *  Archive Log:    - In setup wizard, the max length of host name is 253, the max length for file path is 4096, and the host port number shall in range (0, 65535)
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/06/26 13:36:43  rjtierne
 *  Archive Log:    PR 129380 - Subnet Wizard "Next" button disabled when Key/Trust Store fields are filled in
 *  Archive Log:    In constructor for SecureStorage() pass the document listeners to the FieldPair superclass
 *  Archive Log:    so it recognizes when the fields are populated and enables the Next/Apply buttons.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/05/27 14:36:13  rjtierne
 *  Archive Log:    128874 - Eliminate login dialog from admin console and integrate into panel
 *  Archive Log:    - Moved inner class FieldPair to standalone class
 *  Archive Log:    - Added label width to FieldPair
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/05/11 12:36:41  rjtierne
 *  Archive Log:    PR 128585 - Fix errors found by Klocwork and FindBugs
 *  Archive Log:    Use == to compare string to null instead of equals()
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/04/29 19:14:42  rjtierne
 *  Archive Log:    Fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/04/21 21:18:50  rjtierne
 *  Archive Log:    In method setDirty(), calling setDirty() in SubnetwizardView to enable the Apply and Reset
 *  Archive Log:    buttons only when the fields change
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/20 15:22:18  rjtierne
 *  Archive Log:    Initialize port number to default when creating new hosts within a subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/09 21:14:44  rjtierne
 *  Archive Log:    In method testConnection(), prevent running a test on a host panel having no host name or port number
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/07 20:32:27  jijunwan
 *  Archive Log:    added null check
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/07 20:17:42  jijunwan
 *  Archive Log:    second round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/06 22:53:48  jijunwan
 *  Archive Log:    first round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/03 14:44:37  rjtierne
 *  Archive Log:    Added showFileChooser() method to center file browser over the wizard window
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/01 17:05:45  rjtierne
 *  Archive Log:    Added color to results when testing connection on host panels.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/30 15:13:18  rjtierne
 *  Archive Log:    Reorganized view to hold the connection test panel and remove the master radio button
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/25 17:56:57  rjtierne
 *  Archive Log:    Move JFileChooser to the subnet wizard view level so previously traversed
 *  Archive Log:    directory locations are remembered
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/20 21:07:59  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view.subnet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.FieldPair;
import com.intel.stl.ui.common.view.SafeNumberField;
import com.intel.stl.ui.common.view.SafeTextField;

public class HostInfoPanel extends JPanel {

    private static final long serialVersionUID = -7241402810197918958L;

    private final IHostInfoListener hostInfoListener;

    private final HostInfoPanel hostInfoPanel = this;

    private final int LABEL_WIDTH = 32;

    private boolean currentMaster;

    private JPanel hostPanel;

    private JButton btnRemove;

    private JFormattedTextField txtFldHostName;

    private JFormattedTextField txtFldPortNum;

    private JCheckBox chkboxSecureConnect;

    private JFormattedTextField txtFldKeyStoreFile;

    private JButton btnKeyStoreBrowser;

    private JFormattedTextField txtFldTrustStoreFile;

    private JButton btnTrustStoreBrowser;

    private JPanel pnlConnection;

    private JPanel pnlSecurity;

    private JPanel pnlHostEntry;

    private final JFileChooser chooser;

    private JLabel lblConnectionStatus;

    private JButton btnConnectionTest;

    private final HostInfoPanel thisPanel = this;

    private final Insets insets = new Insets(3, 2, 3, 2);

    private final Insets widthInsets = new Insets(3, 10, 3, 2);

    public HostInfoPanel(IHostInfoListener hostInfoListener,
            JFileChooser chooser) {
        this.hostInfoListener = hostInfoListener;
        this.chooser = chooser;
        initComponents();
        enableCerts(false);
    }

    protected void initComponents() {
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 1, 1, 4, UIConstants.INTEL_LIGHT_GRAY),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        add(getHostPanel(), BorderLayout.CENTER);

        // Remove Button
        btnRemove =
                ComponentFactory.getImageButton(UIImages.CLOSE_RED
                        .getImageIcon());
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hostInfoListener.removeHost(hostInfoPanel);
            }
        });
        add(btnRemove, BorderLayout.EAST);
    }

    protected JPanel getHostPanel() {
        if (hostPanel == null) {
            hostPanel = new JPanel(new GridBagLayout());
            hostPanel.setBackground(UIConstants.INTEL_WHITE);
            hostPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 2, 5));

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.BOTH;

            gc.gridwidth = 1;
            gc.insets = new Insets(7, 2, 3, 2);
            JLabel label =
                    ComponentFactory.getH5Label(
                            STLConstants.K3037_FE_CONNECTION.getValue(),
                            Font.BOLD);
            label.setHorizontalAlignment(JLabel.TRAILING);
            label.setVerticalAlignment(JLabel.TOP);
            hostPanel.add(label, gc);

            gc.weightx = 1;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.insets = widthInsets;
            JPanel panel = getConnectionPanel();
            hostPanel.add(panel, gc);

            gc.weightx = 0;
            gc.gridwidth = 1;
            gc.insets = insets;
            label =
                    ComponentFactory.getH5Label(
                            STLConstants.K3033_CONNECTION_TEST.getValue(),
                            Font.BOLD);
            label.setHorizontalAlignment(JLabel.TRAILING);
            hostPanel.add(label, gc);

            gc.weightx = 1;
            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.insets = widthInsets;
            panel = getConnTestPanel();
            hostPanel.add(panel, gc);
        }
        return hostPanel;
    }

    protected JPanel getConnectionPanel() {
        if (pnlConnection == null) {
            pnlConnection = new JPanel(new BorderLayout(5, 5));
            pnlConnection.setOpaque(false);
            pnlConnection.add(getHostEntryPanel(), BorderLayout.NORTH);
            pnlConnection.add(getSecurityPanel(), BorderLayout.CENTER);
        }
        return pnlConnection;
    }

    /**
     * 
     * <i>Description:</i>
     * 
     * see <a
     * href=https://en.wikipedia.org/wiki/Hostname>https://en.wikipedia.org
     * /wiki/Hostname</a> for valid hostname
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    protected JPanel getHostEntryPanel() {
        if (pnlHostEntry == null) {
            pnlHostEntry = new JPanel(new BorderLayout(5, 5));
            pnlHostEntry.setOpaque(false);

            JPanel panel = new JPanel(new GridLayout(1, 2, 10, 5));
            panel.setOpaque(false);
            String hostNameChars =
                    UIConstants.DIGITS + UIConstants.LETTERS + "-.";
            txtFldHostName = new SafeTextField(false, 253);
            ((SafeTextField) txtFldHostName).setValidChars(hostNameChars);
            for (DocumentListener listener : hostInfoListener
                    .getDocumentListeners()) {
                txtFldHostName.getDocument().addDocumentListener(listener);
            }
            FieldPair<JFormattedTextField> fp =
                    new FieldPair<JFormattedTextField>(
                            STLConstants.K0051_HOST.getValue(), LABEL_WIDTH,
                            txtFldHostName);
            panel.add(fp);

            txtFldPortNum =
                    new SafeNumberField<Integer>(new DecimalFormat("###"), 0,
                            false, 65535, false);
            // only positive integer
            ((SafeNumberField<Integer>) txtFldPortNum)
                    .setValidChars(UIConstants.DIGITS);
            for (DocumentListener listener : hostInfoListener
                    .getDocumentListeners()) {
                txtFldPortNum.getDocument().addDocumentListener(listener);
            }
            txtFldPortNum.setText(STLConstants.K3015_DEFAULT_PORT.getValue());
            fp =
                    new FieldPair<JFormattedTextField>(
                            STLConstants.K1035_CONFIGURATION_PORT.getValue(),
                            LABEL_WIDTH, txtFldPortNum);
            panel.add(fp);
            pnlHostEntry.add(panel, BorderLayout.CENTER);

            chkboxSecureConnect =
                    ComponentFactory
                            .getIntelCheckBox(STLConstants.K2003_SECURE_CONNECT
                                    .getValue());
            chkboxSecureConnect.setFont(UIConstants.H5_FONT
                    .deriveFont(Font.BOLD));
            chkboxSecureConnect.setForeground(UIConstants.INTEL_DARK_GRAY);
            chkboxSecureConnect.setHorizontalAlignment(JLabel.TRAILING);
            chkboxSecureConnect.setSelected(false);
            chkboxSecureConnect.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    hostInfoListener.setDirty();
                    boolean isSecureConnect = chkboxSecureConnect.isSelected();
                    enableCerts(isSecureConnect);
                }
            });
            pnlHostEntry.add(chkboxSecureConnect, BorderLayout.EAST);
        }
        return pnlHostEntry;
    }

    protected JPanel getSecurityPanel() {
        if (pnlSecurity == null) {
            pnlSecurity = new JPanel(new GridLayout(1, 2, 10, 5));
            pnlSecurity.setOpaque(false);
            pnlSecurity.setBorder(BorderFactory
                    .createTitledBorder(STLConstants.K3041_SSL.getValue()));

            txtFldKeyStoreFile =
                    ComponentFactory.createTextField(null, true, 4096,
                            hostInfoListener.getDocumentListeners());
            SecureStorage ss =
                    new SecureStorage(STLConstants.K2001_KEY_STORE.getValue(),
                            txtFldKeyStoreFile);
            btnKeyStoreBrowser = ss.getBtnStoreBrowser();
            pnlSecurity.add(ss);

            txtFldTrustStoreFile =
                    ComponentFactory.createTextField(null, true, 4096,
                            hostInfoListener.getDocumentListeners());
            ss =
                    new SecureStorage(
                            STLConstants.K2002_TRUST_STORE.getValue(),
                            txtFldTrustStoreFile);
            btnTrustStoreBrowser = ss.getBtnStoreBrowser();
            pnlSecurity.add(ss);
        }
        return pnlSecurity;
    }

    protected JPanel getConnTestPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = insets;

        gc.gridwidth = 1;
        btnConnectionTest =
                ComponentFactory.getImageButton(UIImages.PLAY.getImageIcon());
        btnConnectionTest.setToolTipText(STLConstants.K3027_TEST_CONNECTION
                .getValue());
        btnConnectionTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtFldHostName.isEditValid()
                        || !txtFldPortNum.isEditValid()) {
                    // do nothing if edit is invalid
                    return;
                }

                if (btnConnectionTest.isSelected()) {
                    stopConnectionTest();
                } else {
                    testConnection();
                }
            }
        });
        panel.add(btnConnectionTest, gc);

        gc.weightx = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        lblConnectionStatus =
                ComponentFactory.getH5Label(
                        STLConstants.K3028_NOT_TESTED.getValue(), Font.PLAIN);
        panel.add(lblConnectionStatus, gc);

        return panel;
    }

    protected void testConnection() {

        if (txtFldHostName.getText().equals("")
                || txtFldPortNum.getText().equals("")) {
            hostInfoListener
                    .showErrorMessage(STLConstants.K3042_HOST_PORT_BLANK
                            .getValue());
            return;
        }

        btnConnectionTest.setSelected(true);

        if (hostInfoListener.hasDuplicateHosts()) {
            hostInfoListener.showErrorMessage(UILabels.STL50086_DUPLICATE_HOSTS
                    .getDescription());
            return;
        }

        btnConnectionTest.setIcon(UIImages.STOP.getImageIcon());
        lblConnectionStatus.setIcon(UIImages.RUNNING.getImageIcon());
        setLabel(lblConnectionStatus, STLConstants.K0606_CONNECTING.getValue(),
                UIConstants.INTEL_DARK_GRAY);
        hostInfoListener.runConnectionTest(thisPanel);
    }

    public void enableRemove(boolean enable) {
        btnRemove.setEnabled(enable);
    }

    public void setFocus() {
        txtFldHostName.grabFocus();
    }

    public void stopConnectionTest() {
        btnConnectionTest.setIcon(UIImages.PLAY.getImageIcon());
        lblConnectionStatus.setIcon(null);
        setLabel(lblConnectionStatus, STLConstants.K3028_NOT_TESTED.getValue(),
                UIConstants.INTEL_DARK_GRAY);
        // TODO cancel task...
        btnConnectionTest.setSelected(false);
    }

    public void setConnectionStatus(String status) {
        btnConnectionTest.setIcon(UIImages.PLAY.getImageIcon());
        btnConnectionTest.setSelected(false);
        lblConnectionStatus.setIcon(null);
        Color color =
                (status.equals(STLConstants.K3031_PASS.getValue())) ? UIConstants.DARK_GREEN
                        : UIConstants.INTEL_RED;
        setLabel(lblConnectionStatus, status, color);
        btnConnectionTest.setIcon(UIImages.PLAY.getImageIcon());
    }

    protected void setLabel(JLabel lbl, String value, Color color) {
        lbl.setText(value);
        lbl.setForeground(color);
    }

    protected void enableCerts(boolean enable) {
        txtFldKeyStoreFile.setEnabled(enable);
        if (btnKeyStoreBrowser != null) {
            btnKeyStoreBrowser.setEnabled(enable);
        }
        txtFldTrustStoreFile.setEnabled(enable);
        if (btnTrustStoreBrowser != null) {
            btnTrustStoreBrowser.setEnabled(enable);
        }

        Color color = (enable) ? UIConstants.INTEL_WHITE : null;
        txtFldKeyStoreFile.setBackground(color);
        txtFldTrustStoreFile.setBackground(color);

        pnlSecurity.setVisible(enable);
    }

    public void setCurrentMaster(boolean currentMaster) {
        this.currentMaster = currentMaster;
    }

    public boolean isCurrentMaster() {
        return currentMaster;
    }

    public void setHostName(String hostName) {
        txtFldHostName.setText(hostName);
    }

    public String getHostName() {
        return txtFldHostName.getText();
    }

    public void setPortNum(String portNum) {
        txtFldPortNum.setText(portNum);
    }

    public String getPortNum() {
        return txtFldPortNum.getText();
    }

    public void setSecureConnection(boolean b) {
        chkboxSecureConnect.setSelected(b);
    }

    public boolean isSecureConnection() {
        return chkboxSecureConnect.isSelected();
    }

    public void setKeyStoreFile(String keyStoreFileName) {
        txtFldKeyStoreFile.setText(keyStoreFileName);
    }

    public String getKeyStoreFile() {
        return txtFldKeyStoreFile.getText();
    }

    public void setTrustFileFile(String trustStoreFileName) {
        txtFldTrustStoreFile.setText(trustStoreFileName);
    }

    public String getTrustStoreFile() {
        return txtFldTrustStoreFile.getText();
    }

    public boolean isHostNamePopulated() {
        return (txtFldHostName.getText().length() > 0);
    }

    public boolean isPortNumPopulated() {
        return (txtFldPortNum.getText().length() > 0);
    }

    public boolean isKeyStorePopulated() {
        return (txtFldKeyStoreFile.getText().length() > 0);
    }

    public boolean isTrustStorePopulated() {
        return (txtFldTrustStoreFile.getText().length() > 0);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final HostInfoPanel other = (HostInfoPanel) obj;
        if (txtFldHostName.getText() == null) {
            if (other.txtFldHostName.getText() != null) {
                return false;
            }
        } else if (!txtFldHostName.getText().equalsIgnoreCase(
                other.txtFldHostName.getText())) {
            return false;
        }

        if (txtFldPortNum.getText() == null) {
            if (other.txtFldPortNum.getText() != null) {
                return false;
            }
        } else if (!txtFldPortNum.getText().equalsIgnoreCase(
                other.txtFldPortNum.getText())) {
            return false;
        }

        if (txtFldKeyStoreFile.getText() == null) {
            if (other.txtFldKeyStoreFile.getText() != null) {
                return false;
            }
        } else if (!txtFldKeyStoreFile.getText().equalsIgnoreCase(
                other.txtFldKeyStoreFile.getText())) {
            return false;
        }

        if (txtFldTrustStoreFile.getText() == null) {
            if (other.txtFldTrustStoreFile.getText() != null) {
                return false;
            }
        } else if (!txtFldTrustStoreFile.getText().equalsIgnoreCase(
                other.txtFldTrustStoreFile.getText())) {
            return false;
        }

        if (chkboxSecureConnect == null) {
            if (other.chkboxSecureConnect != null) {
                return false;
            }
        } else if ((chkboxSecureConnect != null)
                && (other.chkboxSecureConnect != null)) {
            boolean case1 =
                    !chkboxSecureConnect.isSelected()
                            && other.isSecureConnection();
            boolean case2 =
                    chkboxSecureConnect.isSelected()
                            && !other.isSecureConnection();
            if (case1 || case2) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;

        try {
            int prime = 13;

            result =
                    prime
                            * result
                            + ((getHostName() == null) ? 0 : getHostName()
                                    .toLowerCase().hashCode());
            result =
                    prime
                            * result
                            + ((getHostName() == null) ? 0 : getHostName()
                                    .hashCode());

            result = prime * result + Integer.valueOf(getPortNum());
            result =
                    prime
                            * result
                            + +((getKeyStoreFile() == null) ? 0
                                    : getKeyStoreFile().hashCode());
            result =
                    prime
                            * result
                            + +((getTrustStoreFile() == null) ? 0
                                    : getTrustStoreFile().hashCode());

        } catch (NumberFormatException e) {
            hostInfoListener.showErrorMessage("Hash Code Error: "
                    + this.getClass().getName());
        }

        return result;
    }

    public boolean isEditValid() {
        return txtFldHostName.isEditValid() && txtFldPortNum.isEditValid()
                && txtFldKeyStoreFile.isEditValid()
                && txtFldTrustStoreFile.isEditValid();
    }

    protected class SecureStorage extends FieldPair<JFormattedTextField> {
        private static final long serialVersionUID = -3358500581251310645L;

        private JButton btnStoreBrowser;

        public SecureStorage(String name, JFormattedTextField textField) {
            super(name, (int) (LABEL_WIDTH * 2.2), textField);
        }

        @Override
        protected void initComponents(String name, int labelWidth,
                final JFormattedTextField textField) {
            super.initComponents(name, labelWidth, textField);

            btnStoreBrowser =
                    ComponentFactory.getImageButton(UIImages.FOLDER_ICON
                            .getImageIcon());
            btnStoreBrowser.setMargin(new Insets(2, 2, 2, 2));
            btnStoreBrowser
                    .setToolTipText(STLConstants.K0642_BROWSE.getValue());
            btnStoreBrowser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newFile = chooseFile(textField.getText());
                    if (newFile != null) {
                        textField.setText(newFile);
                        hostInfoListener.setDirty();
                    }
                }
            });
            add(btnStoreBrowser, BorderLayout.EAST);
        }

        protected String chooseFile(String iniFile) {

            if (!iniFile.isEmpty()) {
                File file = new File(iniFile);
                chooser.setCurrentDirectory(file.getParentFile());
            }
            int returnVal = hostInfoListener.showFileChooser();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile().getAbsolutePath();
            }
            return null;
        }

        public JButton getBtnStoreBrowser() {
            return btnStoreBrowser;
        }

    }
}
