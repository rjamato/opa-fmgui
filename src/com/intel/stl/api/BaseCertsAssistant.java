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
 *  File Name: BaseCertsAssistant.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:21:59  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
