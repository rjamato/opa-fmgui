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
 *  File Name: FMConfAccessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8.2.1  2015/08/12 15:22:09  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/04/29 22:02:10  jijunwan
 *  Archive Log:    changed DefaulLoginAssistant to be DOCUMENT_MODAL
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/28 21:55:00  jijunwan
 *  Archive Log:    improved LoginAssistant to support setting owner
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/24 17:40:04  jijunwan
 *  Archive Log:    minor change
 *  Archive Log:    1) increased max number of backup files to 16
 *  Archive Log:    2) use current user defined in SubnetDescription for login
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/16 17:34:40  fernande
 *  Archive Log:    STLConnection lifecycle support. STLConnections can now be reused and temporary connections are not cached and their socket is closed after they are logically closed. Changed SubnetDescription in support of failover to have a list of HostInfo objects instead of just info for one host.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/11 21:13:52  jijunwan
 *  Archive Log:    minor improvement on default login interaction
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/10 23:13:22  jijunwan
 *  Archive Log:    try to fix unit test issue
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 22:41:43  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:37  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DefaultLoginAssistant;
import com.intel.stl.api.ILoginAssistant;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.STLMessages;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * This class helps us get and deploy FM conf file. It also can help us restart
 * FM remotely.To avoid confliction on conf change, the only way to get a helper
 * is through the {@link #getInstance()} method that will guarantee only one
 * helper per subnet. Therefore we can safely do sync control within the helper.
 * Note the sync here is at low level. It prevents us from getting and deploying
 * at the same time. At business logic level, we may have multiple thread read
 * in the file in memory and do different modifications, and turns out one
 * application has multiple versions of FM conf. This kind of sync is controlled
 * at higher level.
 * 
 */
public class FMConfHelper {
    private final static Logger log = LoggerFactory
            .getLogger(FMConfHelper.class);

    private static Map<SubnetDescription, FMConfHelper> helperMap =
            new HashMap<SubnetDescription, FMConfHelper>();

    private final static String SM_DIR = "/etc/sysconfig/";

    private final static String CONF = "opafm.xml";

    private final static int NUM_BACKUPS = 16;

    private final SubnetDescription subnet;

    private ILoginAssistant loginAssistant;

    private File tmpConfFile;

    public static FMConfHelper getInstance(SubnetDescription subnet) {
        FMConfHelper helper = helperMap.get(subnet);
        if (helper == null) {
            helper = new FMConfHelper(subnet);
            helperMap.put(subnet, helper);
        }
        return helper;
    }

    /**
     * Description:
     * 
     * @param subnet
     */
    private FMConfHelper(SubnetDescription subnet) {
        super();
        this.subnet = subnet;
    }

    /**
     * @param loginAssistant
     *            the loginAssistant to set
     */
    public void setLoginAssistant(ILoginAssistant loginAssistant) {
        this.loginAssistant = loginAssistant;
    }

    public ILoginAssistant getLoginAssistant() {
        if (loginAssistant == null) {
            loginAssistant =
                    new DefaultLoginAssistant(null, subnet.getCurrentFE()
                            .getHost(), subnet.getCurrentUser());
        }
        return loginAssistant;
    }

    public String getHost() {
        return subnet.getCurrentFE().getHost();
    }

    /**
     * 
     * <i>Description:</i> return the local copy of FM opafm.xml file. If no
     * local file exist or <code>force</code> is set to true, get a copy of
     * opafm.xml from SM node.
     * 
     * @param force
     *            indicate whether directly get a copy from SM node
     * @return the local file of the opafm.xml
     * @throws Exception
     */
    public synchronized File getConfFile(boolean force) throws Exception {
        if (tmpConfFile == null || force) {
            HostInfo hostInfo = subnet.getCurrentFE();
            String host = hostInfo.getHost();
            ILoginAssistant loginAssistant = getLoginAssistant();
            loginAssistant.init(host, subnet.getCurrentUser());
            loginAssistant.getOption(null);

            loginAssistant.startProgress();
            try {
                loginAssistant.reportProgress(STLMessages.STL61015_CONNECTING
                        .getDescription());
                String userName = loginAssistant.getUserName();
                char[] password = loginAssistant.getPassword();
                int port = loginAssistant.getPort();
                JSch jsch = new JSch();
                Session session = jsch.getSession(userName, host, port);
                session.setPassword(new String(password));
                Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();

                loginAssistant.reportProgress(STLMessages.STL61016_FETCHING
                        .getDescription());
                tmpConfFile = File.createTempFile("~FV", null);
                tmpConfFile.deleteOnExit();
                try {
                    // download file
                    Channel channel = session.openChannel("sftp");
                    channel.connect();
                    try {
                        ChannelSftp channelSftp = (ChannelSftp) channel;
                        channelSftp.cd(SM_DIR);
                        channelSftp.get(CONF, tmpConfFile.getAbsolutePath());
                        log.info("Download " + host + ":" + CONF + " to "
                                + tmpConfFile.getAbsolutePath() + " (size="
                                + tmpConfFile.length() + ")");
                        subnet.setCurrentUser(loginAssistant.getUserName());
                    } finally {
                        channel.disconnect();
                    }
                } finally {
                    session.disconnect();
                }
            } finally {
                loginAssistant.stopProgress();
                loginAssistant.close();
            }

        }

        return tmpConfFile;
    }

    /**
     * 
     * <i>Description:</i> apply the local opafm.xml to SM node. This will do
     * the following: 1) create connection with SM node with username/password
     * 2) make a copy of FM opafm.xml 3) replace FM opafm.xml with local
     * opafm.xml
     */
    public synchronized void deployConf() throws Exception {
        if (tmpConfFile == null) {
            return;
        }
        HostInfo hostInfo = subnet.getCurrentFE();
        String host = hostInfo.getHost();
        ILoginAssistant loginAssistant = getLoginAssistant();
        loginAssistant.init(host, subnet.getCurrentUser());
        int option = loginAssistant.getOption(null);
        if (option == JOptionPane.CANCEL_OPTION) {
            loginAssistant.close();
            return;
        }

        loginAssistant.startProgress();
        try {
            loginAssistant.reportProgress(STLMessages.STL61015_CONNECTING
                    .getDescription());
            String userName = loginAssistant.getUserName();
            char[] password = loginAssistant.getPassword();
            JSch jsch = new JSch();
            Session session = jsch.getSession(userName, host, 22);
            session.setPassword(new String(password));
            Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            loginAssistant.reportProgress(STLMessages.STL61017_DEPLOYING
                    .getDescription());
            try {
                // upload file
                Channel channel = session.openChannel("sftp");
                channel.connect();
                try {
                    ChannelSftp channelSftp = (ChannelSftp) channel;
                    channelSftp.cd(SM_DIR);
                    log.info("Upload " + tmpConfFile.getAbsolutePath() + " to "
                            + host + ":" + tmpConfFile.getName());
                    channelSftp.put(tmpConfFile.getAbsolutePath(),
                            tmpConfFile.getName());
                } finally {
                    channel.disconnect();
                }

                // rename file
                String cmd = getShellScript();
                log.info("execute command @ " + host + " \"" + cmd + "\"");
                channel = session.openChannel("exec");
                try {
                    ((ChannelExec) channel).setCommand(cmd);
                    channel.setInputStream(null);
                    ((ChannelExec) channel).setErrStream(System.err);
                    InputStream in = channel.getInputStream();
                    channel.connect();
                    waitForExecution(in, channel);
                    subnet.setCurrentUser(loginAssistant.getUserName());
                } finally {
                    channel.disconnect();
                }

            } finally {
                session.disconnect();
            }
        } finally {
            loginAssistant.stopProgress();
            loginAssistant.close();
        }
    }

    public synchronized void restartFM() {
        // TODO: restart FM. This is should be called after a user choose to
        // restart via FM GUI. Some user may prefer to do it manually. And we
        // also should info our user FM will temporarily down, etc.
    }

    protected String getShellScript() {
        return "cd " + SM_DIR + "; cp " + CONF + " " + CONF
                + ".`date +%Y%m%d%H%M%S-%3N`" + ".fv; mv "
                + tmpConfFile.getName() + " " + CONF + "; chmod 755 " + CONF
                + "; rm -f `ls -t opafm.xml.??????????????-???.fv | sed 1,"
                + NUM_BACKUPS + "d`";
    }

    protected void waitForExecution(InputStream in, Channel channel)
            throws IOException {
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
            }
            if (channel.isClosed()) {
                if (in.available() > 0) {
                    continue;
                }
                log.info("Command exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(200);
            } catch (Exception ee) {
            }
        }
    }
}
