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
 *  File Name: PortUtils.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/12/08 20:54:08  rjtierne
 *  Archive Log:    PR 131945 - "UnknownHostKey" when trying to unlock console
 *  Archive Log:    - In method getKnownHosts(), create the .ssh directory if it doesn't
 *  Archive Log:    exist before attempting to create the known_hosts file.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/10/29 12:11:40  robertja
 *  Archive Log:    PR 131014 MailNotifier is now updated if user changes events or recipients in wizard after start-up.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/17 18:48:51  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/10 17:04:43  robertja
 *  Archive Log:    PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/07/02 14:24:20  jijunwan
 *  Archive Log:    PR 129442 - login failed with FileNotFoundException
 *  Archive Log:    - fixed a typo in the utility method
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/07/01 21:59:24  jijunwan
 *  Archive Log:    PR 129442 - login failed with FileNotFoundException
 *  Archive Log:    - Introduced a Utility method to create JSch that will
 *  Archive Log:    1) check whether known_hosts exist
 *  Archive Log:    2) if not exist, create a new file
 *  Archive Log:    3) if have valid known_hosts file, set it to Jsch. Oterwise, let Jsch maintain it's own memory based temporary file
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/29 20:33:35  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/16 21:56:30  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/05 17:30:40  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/28 23:15:20  jijunwan
 *  Archive Log:    added helpers to support unsigned byte, short and int
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/01/11 23:09:30  jijunwan
 *  Archive Log:    renamed PortUtils to Utils
 *  Archive Log:    moved convertFromUnixTime to Utils
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:47:22  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015.
 *  Archive Log:    adapt to changes introduced on FM side.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/21 22:14:14  jijunwan
 *  Archive Log:    Added utilities that is able to detect port status. We should always reuse these utilities when possible to ensure we have consistent results
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;

import com.intel.stl.api.configuration.LinkSpeedMask;
import com.intel.stl.api.configuration.LinkWidthMask;
import com.intel.stl.api.subnet.PortInfoBean;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

public class Utils {

    private static final String SSL_PROTOCOL = "TLSv1.2";

    private static final String TRUST_MANAGEMENT_ALGORITHM = "SunX509";

    private static final String SECURITY_PROVIDER = "SunJSSE";

    private static final String KEYSTORE_TYPE = "JKS";

    public static boolean isExpectedSpeed(PortInfoBean port,
            LinkSpeedMask expectedSpeed) {
        short activeSpeedBit = port.getLinkSpeedActive();
        return activeSpeedBit == expectedSpeed.getSpeedMask();
    }

    public static boolean isExpectedWidth(PortInfoBean port,
            LinkWidthMask expectedWidth) {
        short activeWidthBit = port.getLinkWidthActive();
        return activeWidthBit == expectedWidth.getWidthMask();
    }

    public static boolean isExpectedWidthDowngrade(PortInfoBean port,
            LinkWidthMask expectedWidth) {
        short txActiveWidthDownBit = port.getLinkWidthDownTxActive();
        short rxActiveWidthDownBit = port.getLinkWidthDownRxActive();
        return txActiveWidthDownBit == expectedWidth.getWidthMask()
                && rxActiveWidthDownBit == expectedWidth.getWidthMask();
    }

    public static boolean isSlowPort(PortInfoBean port) {
        return isDegradedPort(port) || !isExpectedSpeed(port)
                || !isExpectedWidth(port) || !isExpectedWidthDowngrade(port);
    }

    public static boolean isExpectedSpeed(PortInfoBean port) {
        short activeSpeedBit = port.getLinkSpeedActive();
        float activeSpeed =
                LinkSpeedMask.getLinkSpeedMask(activeSpeedBit).getSpeedInGb();
        float expectedSpeed = getEnabledSpeed(port);
        return (activeSpeed >= expectedSpeed);
    }

    private static float getEnabledSpeed(PortInfoBean port) {
        float maxSupportedSpeed = 0;
        List<LinkSpeedMask> supportedSpeeds =
                LinkSpeedMask.getSpeedMasks(port.getLinkSpeedSupported());
        for (LinkSpeedMask speed : supportedSpeeds) {
            if (speed.getSpeedInGb() > maxSupportedSpeed) {
                maxSupportedSpeed = speed.getSpeedInGb();
            }
        }
        return maxSupportedSpeed;
    }

    public static boolean isExpectedWidth(PortInfoBean port) {
        short activeWidthBit = port.getLinkWidthActive();
        int activeWidth =
                LinkWidthMask.getLinkWidthMask(activeWidthBit).getWidth();
        int expectedWidth = getEnabledWidth(port);
        return (activeWidth >= expectedWidth);
    }

    private static int getEnabledWidth(PortInfoBean port) {
        int maxSupportedWidth = 0;
        List<LinkWidthMask> supportedWidths =
                LinkWidthMask.getWidthMasks(port.getLinkWidthSupported());
        for (LinkWidthMask speed : supportedWidths) {
            if (speed.getWidth() > maxSupportedWidth) {
                maxSupportedWidth = speed.getWidth();
            }
        }
        return maxSupportedWidth;
    }

    public static boolean isExpectedWidthDowngrade(PortInfoBean port) {
        short txActiveWidthDownBit = port.getLinkWidthDownTxActive();
        int txActiveWidthDown =
                LinkWidthMask.getLinkWidthMask(txActiveWidthDownBit).getWidth();
        short rxActiveWidthDownBit = port.getLinkWidthDownRxActive();
        int rxActiveWidthDown =
                LinkWidthMask.getLinkWidthMask(rxActiveWidthDownBit).getWidth();
        int expectedWidthDown = getEnabledWidthDowngrade(port);
        return (txActiveWidthDown >= expectedWidthDown)
                && (rxActiveWidthDown >= expectedWidthDown);
    }

    private static int getEnabledWidthDowngrade(PortInfoBean port) {
        int maxSupportedWidthDown = 0;
        List<LinkWidthMask> supportedWidthDowns =
                LinkWidthMask.getWidthMasks(port.getLinkWidthDownSupported());
        for (LinkWidthMask speed : supportedWidthDowns) {
            if (speed.getWidth() > maxSupportedWidthDown) {
                maxSupportedWidthDown = speed.getWidth();
            }
        }
        return maxSupportedWidthDown;
    }

    public static boolean isDegradedPort(PortInfoBean port) {
        short activeWidthBit = port.getLinkWidthActive();
        short txActiveWidthDownBit = port.getLinkWidthDownTxActive();
        short rxActiveWidthDownBit = port.getLinkWidthDownRxActive();
        return activeWidthBit != txActiveWidthDownBit
                || activeWidthBit != rxActiveWidthDownBit;
    }

    public static final Date convertFromUnixTime(long unixTime) {
        return new Date(unixTime * 1000);
    }

    public static short unsignedByte(byte val) {
        return (short) (val & 0xff);
    }

    public static int unsignedShort(short val) {
        return val & 0xffff;
    }

    public static long unsignedInt(int val) {
        return val & 0xffffffffL;
    }

    public static long toLong(String str) {
        if (str.startsWith("0x") || str.startsWith("0X")) {
            str = str.substring(2, str.length());
        }
        return new BigInteger(str, 16).longValue();
    }

    public static File getFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            ClassLoader classLoader = Utils.class.getClassLoader();
            file = new File(classLoader.getResource(fileName).getFile());
        }
        return file;
    }

    public static SSLEngine createSSLEngine(String host, int port,
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

    public static KeyManagerFactory createKeyManagerFactory(String client,
            char[] pwd) throws FMException {

        File cert = new File(client);
        InputStream stream = null;
        KeyManagerFactory kmf;
        try {
            kmf =
                    KeyManagerFactory.getInstance(TRUST_MANAGEMENT_ALGORITHM,
                            SECURITY_PROVIDER);
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
            stream = new FileInputStream(cert);
            ks.load(stream, pwd);
            kmf.init(ks, pwd);
        } catch (Exception e) {
            throw new FMKeyStoreException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kmf;
    }

    public static TrustManagerFactory createTrustManagerFactory(String trustCA,
            char[] pwd) throws FMException {

        File trustcert = new File(trustCA);
        InputStream truststream = null;
        TrustManagerFactory tmf;
        try {
            tmf = TrustManagerFactory.getInstance(TRUST_MANAGEMENT_ALGORITHM);
            KeyStore trustks = KeyStore.getInstance(KEYSTORE_TYPE);
            truststream = new FileInputStream(trustcert);
            trustks.load(truststream, pwd);
            tmf.init(trustks);
        } catch (Exception e) {
            throw new FMTrustStoreException(e);
        } finally {
            if (truststream != null) {
                try {
                    truststream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tmf;
    }

    public static String getKnownHosts() {
        String knownHost = "~/.ssh/known_hosts";
        if (knownHost.startsWith("~")) {
            knownHost = knownHost.replace("~", System.getProperty("user.home"));
        }

        File file = new File(knownHost);
        if (file.exists()) {
            return knownHost;
        }

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (file.createNewFile()) {
                return knownHost;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSch createJSch() throws JSchException {
        JSch jsch = new JSch();
        String knownHosts = Utils.getKnownHosts();
        if (knownHosts != null) {
            jsch.setKnownHosts(knownHosts);
        }
        return jsch;
    }

    public static String listToConcatenatedString(List<String> stringList,
            String delimiter) {
        StringBuffer sb = new StringBuffer();
	    if (stringList != null){
	        for (String str : stringList) {
	        	if(str != null){
		            if (str.contains(delimiter)) {
		                throw new IllegalArgumentException("Source name '" + str
		                        + "' contains DELIMITER '" + delimiter + "'");
		            }
		
		            if (sb.length() == 0) {
		                sb.append(str);
		            } else {
		                sb.append(delimiter + str);
		            }
	        	}
	        }
        }
        return sb.toString();
    }

    public static List<String> concatenatedStringToList(String concatString,
            String delimiter) {
        if ((concatString == null) || concatString.isEmpty()){
            return new ArrayList<String>();
        } else {
            return Arrays.asList(concatString.split(delimiter));
        }
    }
}
