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
 *  File Name: DefaultCertsAssistant.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/04/06 21:12:01  fernande
 *  Archive Log:    Improving the handling of connection errors
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/03 16:16:04  fernande
 *  Archive Log:    Added getSSLEngine to the interface and moved SSLEngine creation to the BaseCertsAssistant
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/24 15:45:36  fernande
 *  Archive Log:    Fix to properly return the KeyManagerFactory and the TrustManagerFactory
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/24 15:07:49  fernande
 *  Archive Log:    Changes to cache KeyManagerFactories and TrustManagerFactories to avoid requests for password.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/19 21:37:37  fernande
 *  Archive Log:    Fix to change the default action to OK instead of Cancel
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/19 16:52:43  fernande
 *  Archive Log:    Fix for the UI CertsAssistant which needs the SubnetDescription.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/16 17:33:34  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 20:56:32  jijunwan
 *  Archive Log:    support secure FE:
 *  Archive Log:    1) added secured STL Connection to communicate with FE
 *  Archive Log:    2) added cert assistant interface that supports certs conf, persistence and password prompt
 *  Archive Log:    3) added default cert assistant
 *  Archive Log:    4) improved Subnet conf to support secure FE
 *  Archive Log:
 *  Archive Log:    NOTE: the secured connection requires Java 1.7
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.STLMessages;

public class DefaultCertsAssistant extends BaseCertsAssistant {

    public static final int MAX_TRIES = 5;

    public static final long TIME_OUT = 30000; // 30 sec

    private CertsDescription currentCerts;

    private KeyManagerFactory kmf;

    private TrustManagerFactory tmf;

    private JPanel certPanel;

    private JTextField keyFileField;

    private JPasswordField keyPwdField;

    private JTextField trustFileField;

    private JPasswordField trustPwdField;

    public DefaultCertsAssistant() {
    }

    @Override
    public SSLEngine getSSLEngine(SubnetDescription subnet) throws Exception {
        // HostInfo in the SubnetDescription may change during fail over, so it
        // must be retrieved from the description.
        HostInfo hostInfo = subnet.getCurrentFE();
        String host = hostInfo.getHost();
        int port = hostInfo.getPort();
        KeyManagerFactory kmf = getKeyManagerFactory(subnet);
        TrustManagerFactory tmf = getTrustManagerFactory(subnet);
        SSLEngine engine = createSSLEngine(host, port, kmf, tmf);
        return engine;
    }

    @Override
    public KeyManagerFactory getKeyManagerFactory(SubnetDescription subnet) {
        if (subnet == null) {
            return null;
        }
        CertsDescription certs = subnet.getCurrentFE().getCertsDescription();
        if (certs == null) {
            return null;
        }
        checkCerts(certs);
        return kmf;
    }

    @Override
    public TrustManagerFactory getTrustManagerFactory(SubnetDescription subnet) {
        if (subnet == null) {
            return null;
        }
        CertsDescription certs = subnet.getCurrentFE().getCertsDescription();
        if (certs == null) {
            return null;
        }
        checkCerts(certs);
        return tmf;
    }

    @Override
    public void clearSubnetFactories(SubnetDescription subnet) {
        if (subnet != null) {
            CertsDescription certs =
                    subnet.getCurrentFE().getCertsDescription();
            if (certs != null && certs.equals(currentCerts)) {
                kmf = null;
                tmf = null;
            }
        }
    }

    private synchronized void checkCerts(CertsDescription certs) {
        // We need to serialize updates to this class fields (this.kmf, this.tmf
        // and this.currentCerts) so that multiple threads do not step on each
        // other.
        if (!certs.equals(currentCerts)) {
            showPanel(certs);
        }
    }

    private void showPanel(final CertsDescription certs) {
        int tries = MAX_TRIES;
        kmf = null;
        tmf = null;

        final Exchanger<CertsDescription> xchgr =
                new Exchanger<CertsDescription>();
        final List<Throwable> errors = new ArrayList<Throwable>();
        final AtomicReference<CertsDescription> currCerts =
                new AtomicReference<CertsDescription>(certs);
        while (tries > 0) {
            if (!errors.isEmpty() || !currCerts.get().hasPwds()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        CertsDescription newCerts =
                                getNewCerts(currCerts.get());
                        try {
                            xchgr.exchange(newCerts, TIME_OUT,
                                    TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // Here, we wait indefinitely for the CertsAssistant to respond
                try {
                    currCerts.set(xchgr.exchange(null));
                } catch (InterruptedException e) {
                    return;
                }
            }
            if (currCerts.get() == null) {
                System.out.println("Certs is null");
                return;
            }

            errors.clear();
            KeyManagerFactory kmf = null;
            try {
                kmf =
                        getKeyManagerFactory(currCerts.get().getKeyStoreFile(),
                                currCerts.get().getKeyStorePwd());
                this.kmf = kmf;
            } catch (Exception e) {
                errors.add(e);
                showErrorMessage(e);
            } finally {
                currCerts.get().clearKeyPwd();
            }

            TrustManagerFactory tmf = null;
            try {
                tmf =
                        getTrustManagerFactory(currCerts.get()
                                .getTrustStoreFile(), currCerts.get()
                                .getTrustStorePwd());
                this.tmf = tmf;
            } catch (Exception e) {
                errors.add(e);
                showErrorMessage(e);
            } finally {
                currCerts.get().clearTrustPwd();
            }

            if (errors.isEmpty() && kmf != null && tmf != null) {
                currentCerts = currCerts.get();
                break;
            }

            tries -= 1;
        }

    }

    private void showErrorMessage(final Exception e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null,
                        StringUtils.getErrorMessage(e), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private CertsDescription getNewCerts(CertsDescription certs) {
        JPanel panel = getCertPanel(certs);
        String[] options = new String[] { "OK", "Cancel" };
        int option =
                JOptionPane.showOptionDialog(null, panel,
                        STLMessages.STL61001_CERT_CONF.getDescription(),
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        options, options[0]);
        if (option == 0) {
            CertsDescription newCerts =
                    new CertsDescription(keyFileField.getText(),
                            trustFileField.getText());
            newCerts.setKeyStorePwd(keyPwdField.getPassword());
            newCerts.setTrustStorePwd(trustPwdField.getPassword());
            return newCerts;
        } else {
            return null;
        }
    }

    private JPanel getCertPanel(CertsDescription certs) {
        if (certPanel == null) {
            certPanel = new JPanel();
            BoxLayout layout = new BoxLayout(certPanel, BoxLayout.Y_AXIS);
            certPanel.setLayout(layout);

            JLabel label =
                    new JLabel(
                            STLMessages.STL61002_KEY_STORE_LOC.getDescription());
            certPanel.add(label);
            keyFileField = new JTextField();
            certPanel.add(keyFileField);

            label =
                    new JLabel(
                            STLMessages.STL61003_KEY_STORE_PWD.getDescription());
            certPanel.add(label);
            keyPwdField = new JPasswordField();
            certPanel.add(keyPwdField);

            label =
                    new JLabel(
                            STLMessages.STL61004_TRUST_STORE_LOC
                                    .getDescription());
            certPanel.add(label);
            trustFileField = new JTextField();
            certPanel.add(trustFileField);

            label =
                    new JLabel(
                            STLMessages.STL61005_TRUST_STORE_PWD
                                    .getDescription());
            certPanel.add(label);
            trustPwdField = new JPasswordField();
            certPanel.add(trustPwdField);
        }

        if (certs != null) {
            keyFileField.setText(certs.getKeyStoreFile());
            trustFileField.setText(certs.getTrustStoreFile());
        }
        return certPanel;
    }

}
