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
 *  File Name: CertsAssistant.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17  2015/10/06 20:20:20  fernande
 *  Archive Log:    PR130749 - FM GUI virtual fabric information doesn't match opafm.xml file. Removed System.out.println statement
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/08/17 18:53:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/06/22 13:11:55  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/06/17 15:40:27  fisherma
 *  Archive Log:    PR129220 - partial fix for the login changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/05/29 20:43:46  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/05/26 15:53:23  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/05/11 12:26:35  rjtierne
 *  Archive Log:    PR 128585 - Fix errors found by Klocwork and FindBugs
 *  Archive Log:    For calls to remove key 0 from a map in clearSubnetFactories(), force the argument 0 to be a long.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/08 15:20:39  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/03 16:19:10  fernande
 *  Archive Log:    Implemented getSSLEngine to comply with interface
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/27 22:40:30  fernande
 *  Archive Log:    Fix to not remove TrustManagerFactory from cache, which makes the UI prompt for password over and over.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/27 20:49:36  fernande
 *  Archive Log:    Fix for lists of store filename per subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/25 17:56:18  rjtierne
 *  Archive Log:    Added maps to keep track of secure certificate files for each subnet
 *  Archive Log:    and clear key factories at appropriate times
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/24 15:14:21  fernande
 *  Archive Log:    Changes to cache KeyManagerFactories and TrustManagerFactories to avoid requests for password.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/19 16:53:50  fernande
 *  Archive Log:    Fix for the UI CertsAssistant which needs the SubnetDescription.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/16 17:44:56  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/09 21:53:08  jijunwan
 *  Archive Log:    minor change on CertsAssitant
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 21:41:55  jijunwan
 *  Archive Log:    added certs assistant to support certs conf and password prompt
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.CertsDescription;
import com.intel.stl.api.FMKeyStoreException;
import com.intel.stl.api.FMTrustStoreException;
import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.SSLStoreCredentialsDeniedException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;
import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.DialogFactory;
import com.intel.stl.ui.main.view.CertsPanel;
import com.intel.stl.ui.wizards.view.MultinetWizardView;

public class CertsAssistant implements ICertsAssistant {
    private static Logger log = LoggerFactory.getLogger(CertsAssistant.class);

    public static final int MAX_TRIES = 5;

    public static final long TIME_OUT = 30000; // 30 sec

    private final IConfigurationApi confApi;

    private final ConcurrentHashMap<String, KeyManagerFactory> cachedKeyFactories =
            new ConcurrentHashMap<String, KeyManagerFactory>();

    private final ConcurrentHashMap<String, TrustManagerFactory> cachedTrustFactories =
            new ConcurrentHashMap<String, TrustManagerFactory>();

    private final ConcurrentHashMap<Long, List<String>> subnetKeyMap =
            new ConcurrentHashMap<Long, List<String>>();

    private final ConcurrentHashMap<Long, List<String>> subnetTrustMap =
            new ConcurrentHashMap<Long, List<String>>();

    private final CertsPanel panel;

    public CertsAssistant(CertsPanel panel, IConfigurationApi confApi) {
        this.confApi = confApi;
        this.panel = panel;
    }

    @Override
    public SSLEngine getSSLEngine(SubnetDescription subnet) throws Exception {
        HostInfo hostInfo = subnet.getCurrentFE();
        String host = hostInfo.getHost();
        int port = hostInfo.getPort();
        KeyManagerFactory kmf = getKeyManagerFactory(subnet);
        TrustManagerFactory tmf = getTrustManagerFactory(subnet);
        SSLEngine engine = Utils.createSSLEngine(host, port, kmf, tmf);
        return engine;
    }

    @Override
    public SSLEngine getSSLEngine(HostInfo hostInfo) throws Exception {
        return null;
    }

    @Override
    public KeyManagerFactory getKeyManagerFactory(SubnetDescription subnet) {
        CertsDescription certs = checkParameters(subnet);
        KeyManagerFactory factory =
                cachedKeyFactories.get(certs.getKeyStoreFile());
        if (factory == null) {
            factory = showPanelForKeyFactory(subnet, certs);
        }
        return factory;
    }

    @Override
    public TrustManagerFactory getTrustManagerFactory(SubnetDescription subnet) {
        CertsDescription certs = checkParameters(subnet);
        TrustManagerFactory factory =
                cachedTrustFactories.get(certs.getTrustStoreFile());
        if (factory == null) {
            factory = showPanelForTrustFactory(subnet, certs);
        }
        return factory;
    }

    @Override
    public void clearSubnetFactories(SubnetDescription subnet) {

        if (subnet != null) {
            // Remove the key file factories for the subnet provided
            List<String> keyNameList =
                    subnetKeyMap.remove(subnet.getSubnetId());
            if (keyNameList != null) {
                synchronized (keyNameList) {
                    for (String name : keyNameList) {
                        cachedKeyFactories.remove(name);
                    }
                }
                keyNameList.clear();
            }

            // Remove the key file factories for subnet with id 0 (temporary
            // connections)
            keyNameList = subnetKeyMap.remove(0L);
            if (keyNameList != null) {
                synchronized (keyNameList) {
                    for (String name : keyNameList) {
                        cachedKeyFactories.remove(name);
                    }
                }
                keyNameList.clear();
            }

            // Remove the trust file factories for the subnet provided
            List<String> trustNameList =
                    subnetTrustMap.remove(subnet.getSubnetId());
            if (trustNameList != null) {
                synchronized (trustNameList) {
                    for (String name : trustNameList) {
                        cachedTrustFactories.remove(name);
                    }
                }
                trustNameList.clear();
            }

            // Remove the trust file factories for subnet with id 0 (temporary
            // connections)
            trustNameList = subnetTrustMap.remove(0L);
            if (trustNameList != null) {
                synchronized (trustNameList) {
                    for (String name : trustNameList) {
                        cachedTrustFactories.remove(name);
                    }
                }
                trustNameList.clear();
            }
        }
    }

    /*
     * This method is synchronized in order to allow only one thread to complete
     * the creation of factories. Enqueued threads waiting might find the
     * factory already created by a previous thread; that's why we check again
     * here
     */
    private synchronized KeyManagerFactory showPanelForKeyFactory(
            SubnetDescription subnet, CertsDescription certs) {
        KeyManagerFactory factory =
                cachedKeyFactories.get(certs.getKeyStoreFile());
        if (factory == null) {
            showPanel(subnet, certs);
        }
        return cachedKeyFactories.get(certs.getKeyStoreFile());
    }

    /*
     * This method is synchronized in order to allow only one thread to complete
     * the creation of factories. Enqueued threads waiting might find the
     * factory already created by a previous thread
     */
    private synchronized TrustManagerFactory showPanelForTrustFactory(
            SubnetDescription subnet, CertsDescription certs) {
        TrustManagerFactory factory =
                cachedTrustFactories.get(certs.getTrustStoreFile());
        if (factory == null) {
            showPanel(subnet, certs);
        }
        return cachedTrustFactories.get(certs.getTrustStoreFile());
    }

    private CertsDescription checkParameters(SubnetDescription subnet) {
        if (subnet == null) {
            IllegalArgumentException e =
                    new IllegalArgumentException(
                            UILabels.STL50200_SUBNETDESC_CANNOT_BE_NULL
                                    .getDescription());
            throw e;
        }
        CertsDescription certs = subnet.getCurrentFE().getCertsDescription();
        if (certs == null) {
            IllegalArgumentException e =
                    new IllegalArgumentException(
                            UILabels.STL50201_CERTSDESC_CANNOT_BE_NULL
                                    .getDescription(subnet.getName(), subnet
                                            .getCurrentFE().getHost()));
            throw e;
        }
        return certs;
    }

    private void showPanel(SubnetDescription subnet, CertsDescription certs) {
        int tries = MAX_TRIES;

        final CertsDescription currCerts =
                new CertsDescription(certs.getKeyStoreFile(),
                        certs.getTrustStoreFile());
        HostInfo hostInfo = subnet.getCurrentFE();
        final String host = hostInfo.getHost();
        final List<Throwable> errors = new ArrayList<Throwable>();
        final Exchanger<CertsDescription> xchgr =
                new Exchanger<CertsDescription>();
        while (tries > 0) {
            if (!errors.isEmpty()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        CertsDescription certs;
                        try {
                            certs = getNewCerts(host, currCerts, errors, null);

                            xchgr.exchange(certs, TIME_OUT,
                                    TimeUnit.MILLISECONDS);
                        } catch (SSLStoreCredentialsDeniedException e1) {
                            // TODO Auto-generated catch block
                            errors.add(e1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CertsDescription certs =
                                    getNewCerts(host, currCerts, null, null);
                            if (xchgr != null) {

                                xchgr.exchange(certs, TIME_OUT,
                                        TimeUnit.MILLISECONDS);
                            }
                        } catch (SSLStoreCredentialsDeniedException e1) {
                            // TODO Auto-generated catch block
                            errors.add(e1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            // Here, we wait indefinitely for the CertsAssistant to respond
            CertsDescription newCerts;
            try {
                newCerts = xchgr.exchange(null);
            } catch (InterruptedException e1) {
                return;
            }
            if (newCerts == null) {
                return;
            }

            errors.clear();
            KeyManagerFactory kmf = null;
            try {
                String keyFile = newCerts.getKeyStoreFile();
                kmf =
                        Utils.createKeyManagerFactory(keyFile,
                                newCerts.getKeyStorePwd());
                cachedKeyFactories.put(keyFile, kmf);
                List<String> subnetKeyList =
                        subnetKeyMap.get(subnet.getSubnetId());
                if (subnetKeyList == null) {
                    List<String> tempKeyList =
                            Collections
                                    .synchronizedList(new ArrayList<String>());
                    subnetKeyList =
                            subnetKeyMap.putIfAbsent(subnet.getSubnetId(),
                                    tempKeyList);
                    if (subnetKeyList == null) {
                        subnetKeyList = tempKeyList;
                    }
                }
                subnetKeyList.add(keyFile);
            } catch (Exception e) {
                System.out
                        .println("KeyManagerFactory error: " + e.getMessage());
                e.printStackTrace();
                errors.add(e);
            } finally {
                newCerts.clearKeyPwd();
            }

            TrustManagerFactory tmf = null;
            try {
                String trustFile = newCerts.getTrustStoreFile();
                tmf =
                        Utils.createTrustManagerFactory(trustFile,
                                newCerts.getTrustStorePwd());
                cachedTrustFactories.put(trustFile, tmf);
                List<String> subnetTrustList =
                        subnetTrustMap.get(subnet.getSubnetId());
                if (subnetTrustList == null) {
                    List<String> tempTrustList =
                            Collections
                                    .synchronizedList(new ArrayList<String>());
                    subnetTrustList =
                            subnetTrustMap.putIfAbsent(subnet.getSubnetId(),
                                    tempTrustList);
                    if (subnetTrustList == null) {
                        subnetTrustList = tempTrustList;
                    }
                }
                subnetTrustList.add(trustFile);
            } catch (Exception e) {
                System.out.println("TrustManagerFactory error: "
                        + e.getMessage());
                e.printStackTrace();
                errors.add(e);
            } finally {
                newCerts.clearTrustPwd();
            }

            if (!errors.isEmpty() || kmf == null || tmf == null) {
                tries -= 1;
                continue;
            }
            commit(subnet);
            return;
        }

    }

    public CertsDescription getNewCerts(String host, CertsDescription curCerts,
            List<Throwable> errors, Component subnetWindow)
            throws SSLStoreCredentialsDeniedException {
        if (errors == null || errors.isEmpty()) {
            panel.reset();
        }
        panel.setKeyStoreLocation(curCerts.getKeyStoreFile());
        panel.setTrustStoreLocation(curCerts.getTrustStoreFile());
        if (errors != null) {
            for (Throwable error : errors) {
                if (error instanceof FMKeyStoreException) {
                    Throwable cause = getPasswordException(error);
                    if (cause != null) {
                        panel.setKeyStorePwdError(StringUtils
                                .getErrorMessage(cause));
                    } else {
                        panel.setKeyStoreLocError(StringUtils
                                .getErrorMessage(error.getCause()));
                    }
                } else if (error instanceof FMTrustStoreException) {
                    Throwable cause = getPasswordException(error);
                    if (cause != null) {
                        panel.setTrustStorePwdError(StringUtils
                                .getErrorMessage(cause));
                    } else {
                        panel.setTrustStoreLocError(StringUtils
                                .getErrorMessage(error.getCause()));
                    }
                } else {
                    log.warn("Unsupported error", error);
                }
            }
        }

        Window multiNetWizardWindow = null;

        Window[] wins = Frame.getWindows();
        List<Window> toChange = new ArrayList<Window>();
        for (Window win : wins) {
            if (win instanceof MultinetWizardView) {
                multiNetWizardWindow = win;
            }

            if (win.isAlwaysOnTop()) {
                win.setAlwaysOnTop(false);
            }
        }
        try {
            Component parentComponent = subnetWindow;
            if (multiNetWizardWindow != null
                    && multiNetWizardWindow.isVisible()) {
                // the password dialog request is from the wizard
                parentComponent = multiNetWizardWindow;
            }
            int option =
                    DialogFactory.showPasswordDialog(parentComponent,
                            STLConstants.K2000_CERT_CONF.getValue() + host,
                            panel);

            if (option == DialogFactory.OK_OPTION) {
                CertsDescription newCerts =
                        new CertsDescription(panel.getKeyStoreLocation(),
                                panel.getTrustStoreLocation());
                newCerts.setKeyStorePwd(panel.getKeyStorePwd());
                newCerts.setTrustStorePwd(panel.getTrustStorePwd());
                return newCerts;
            } else { // (option == DialogFactory.CANCEL_OPTION){
                     // Need to throw exception here
                     // throw new Exception ("Certs cancelled exception");
                throw new SSLStoreCredentialsDeniedException(
                        UILabels.STL10114_USER_CANCELLED);
            }
        } catch (Exception e) {
            if (e instanceof SSLStoreCredentialsDeniedException
                    && errors != null) {
                errors.add(e);
                // re-throw
                throw e;
            } else {
                e.printStackTrace();
            }
        } finally {
            for (Window win : toChange) {
                win.setAlwaysOnTop(true);
            }
        }
        return null;
    }

    private Throwable getPasswordException(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        if ((e instanceof UnrecoverableKeyException)
                && e.getMessage().contains("Password")) {
            return e;
        }
        return null;
    }

    private void commit(final SubnetDescription subnet) {
        panel.reset();
        if (subnet.getSubnetId() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        confApi.updateSubnet(subnet);
                    } catch (SubnetDataNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
