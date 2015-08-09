/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: BaseCertsAssistant.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/08 15:16:19  fernande
 *  Archive Log:    Changes to allow for failover to work when the current (initial) FE is not available.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/03 16:16:04  fernande
 *  Archive Log:    Added getSSLEngine to the interface and moved SSLEngine creation to the BaseCertsAssistant
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/27 20:30:11  fernande
 *  Archive Log:    Adding support for failover. The UI can now setup objects to listen to SubnetEvents the the ISubnetEventListener. Listener register in the SubnetContext
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 15:07:49  fernande
 *  Archive Log:    Changes to cache KeyManagerFactories and TrustManagerFactories to avoid requests for password.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;

public abstract class BaseCertsAssistant implements ICertsAssistant {

    private static final String SSL_PROTOCOL = "TLSv1.2";

    private static final String TRUST_MANAGEMENT_ALGORITHM = "SunX509";

    private static final String SECURITY_PROVIDER = "SunJSSE";

    private static final String KEYSTORE_TYPE = "JKS";

    protected SSLEngine createSSLEngine(String host, int port,
            KeyManagerFactory kmf, TrustManagerFactory tmf) throws Exception {
        if (kmf == null || tmf == null) {
            Exception e =
                    new SSLHandshakeException("Couldn't create SSLEngine");
            throw e;
        }

        SSLContext context = SSLContext.getInstance(SSL_PROTOCOL);
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
                new SecureRandom());
        SSLEngine engine = context.createSSLEngine(host, port);
        engine.setUseClientMode(true);
        return engine;
    }

    protected KeyManagerFactory getKeyManagerFactory(String client, char[] pwd)
            throws Exception {
        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance(TRUST_MANAGEMENT_ALGORITHM,
                        SECURITY_PROVIDER);
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);

        File cert = new File(client);
        InputStream stream = null;
        try {
            stream = new FileInputStream(cert);
            ks.load(stream, pwd);
            kmf.init(ks, pwd);
        } catch (Exception e) {
            throw new FMKeyStoreException(e);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return kmf;
    }

    protected TrustManagerFactory getTrustManagerFactory(String trustCA,
            char[] pwd) throws Exception {
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TRUST_MANAGEMENT_ALGORITHM);
        KeyStore trustks = KeyStore.getInstance(KEYSTORE_TYPE);

        File trustcert = new File(trustCA);
        InputStream truststream = null;
        try {
            truststream = new FileInputStream(trustcert);
            trustks.load(truststream, pwd);
            tmf.init(trustks);
        } catch (Exception e) {
            throw new FMTrustStoreException(e);
        } finally {
            if (truststream != null) {
                truststream.close();
            }
        }
        return tmf;
    }

}
