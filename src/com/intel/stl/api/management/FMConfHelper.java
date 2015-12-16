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
 *  Archive Log:    Revision 1.22  2015/10/06 20:16:42  fernande
 *  Archive Log:    PR130749 - FM GUI virtual fabric information doesn't match opafm.xml file. A previous fix disabled the update of most fields used in the Admin configuration editor. Restored the updating capability in those fields
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/10/01 21:53:32  fernande
 *  Archive Log:    PR130409 - [Dell]: FMGUI Admin Console login fails when switch is configured without username and password. Added ESM support
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/09/28 13:53:23  fisherma
 *  Archive Log:    PR 130425 - added cancel button to allow user to cancel out of hung or slow ssh logins.  Cancel action terminates sftp connection and closes remote ssh session.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/09/21 20:46:57  jijunwan
 *  Archive Log:    PR 130542 - Confusion error message on fetching conf file
 *  Archive Log:    - improved SftpException to include file path information
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/09/10 22:17:28  jijunwan
 *  Archive Log:    PR 130409 - [Dell]: FMGUI Admin Console login fails when switch is configured without username and password
 *  Archive Log:    - fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/09/10 20:56:50  jijunwan
 *  Archive Log:    PR 130409 - [Dell]: FMGUI Admin Console login fails when switch is configured without username and password
 *  Archive Log:    - improved code to better handle conf file not found
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/08/17 18:49:02  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/08/17 17:30:47  jijunwan
 *  Archive Log:    PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log:    - improved FmConfHelper to get ride of ILoginAssistence and deploy with password
 *  Archive Log:    - added tmp FM conf helper that deal with conf file with temporary connection
 *  Archive Log:    - renamed testConnection to fetchConfigFile
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/17 14:22:50  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/07/28 18:20:25  fisherma
 *  Archive Log:    PR 129219 - Admin page login dialog improvement.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/09 17:55:44  jijunwan
 *  Archive Log:    PR 129509 - Shall refresh UI after failover completed
 *  Archive Log:    - added reset method to ManagermentApi so we can reset after failover completed
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/07/01 21:59:58  jijunwan
 *  Archive Log:    PR 129442 - login failed with FileNotFoundException
 *  Archive Log:    - Changed all JSch creation on backend to use this utility method
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

import static com.intel.stl.api.subnet.HostType.ESM;
import static com.intel.stl.api.subnet.HostType.HSM;
import static com.intel.stl.api.subnet.HostType.UNKNOWN;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.Utils;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.HostType;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.STLMessages;
import com.intel.stl.fecdriver.network.ssh.JSchChannelType;
import com.intel.stl.fecdriver.network.ssh.impl.JSchSession;
import com.intel.stl.fecdriver.network.ssh.impl.JSchSessionFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

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

    protected final static String CONF = "opafm.xml";

    protected final static int NUM_BACKUPS = 16;

    protected final SubnetDescription subnet;

    protected File tmpConfFile;

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
    protected FMConfHelper(SubnetDescription subnet) {
        super();
        this.subnet = subnet;
    }

    public String getHost() {
        return subnet.getCurrentFE().getHost();
    }

    public synchronized void reset() {
        tmpConfFile = null;
    }

    /**
     * 
     * <i>Description:</i> return the local copy of FM opafm.xml file.
     * 
     * @return the local file of the opafm.xml
     */
    public synchronized File getConfFile() {
        return tmpConfFile;
    }

    /**
     * 
     * <i>Description:</i> apply the local opafm.xml to SM node. This will do
     * the following: 1) create connection with SM node with username/password
     * 2) make a copy of FM opafm.xml 3) replace FM opafm.xml with local
     * opafm.xml
     */
    public synchronized void deployConf(char[] password) throws Exception {
        if (tmpConfFile == null) {
            return;
        }
        HostInfo hostInfo = subnet.getCurrentFE();
        HostType hostType = hostInfo.getHostType();
        Session session = createSession(hostInfo, password);
        String host = hostInfo.getHost();
        String userName = hostInfo.getSshUserName();
        session.connect();
        try {
            switch (hostType) {
                case HSM:
                    deployHSM(host, userName, session);
                    break;
                case ESM:
                    deployESM(session);
                    break;
                case UNKNOWN:
                    break;
            }
        } finally {
            session.disconnect();
        }

    }

    private void deployHSM(String host, String userName, Session session)
            throws JSchException, SftpException, IOException {
        // upload file
        uploadFile(tmpConfFile.getAbsolutePath(), HSM.getConfigLocation(),
                tmpConfFile.getName(), session);
        // rename file
        String cmd = getShellScript();
        log.info("execute command @ " + host + " \"" + cmd + "\"");
        Channel channel = session.openChannel("exec");
        try {
            ((ChannelExec) channel).setCommand(cmd);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();
            waitForExecution(in, channel);
            subnet.setCurrentUser(userName);
        } finally {
            channel.disconnect();
        }
    }

    private void deployESM(Session session) throws SftpException, JSchException {
        // upload file (there is no way to make a copy of the original
        // configuration file in ESM, so we override it)
        uploadFile(tmpConfFile.getAbsolutePath(), ESM.getConfigLocation(),
                CONF, session);
    }

    private Session createSession(HostInfo hostInfo, char[] password)
            throws JSchException {
        String host = hostInfo.getHost();
        String userName = hostInfo.getSshUserName();
        int port = hostInfo.getSshPortNum();

        JSch jsch = Utils.createJSch();
        Session session = jsch.getSession(userName, host, port);
        session.setPassword(new String(password));
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        return session;
    }

    private void uploadFile(String localFile, String remoteFolder,
            String remoteFile, Session session) throws SftpException,
            JSchException {
        // upload file
        Channel channel =
                session.openChannel(JSchChannelType.SFTP_CHANNEL.getValue());
        String host = session.getHost();
        channel.connect();
        try {
            ChannelSftp channelSftp = (ChannelSftp) channel;
            // channelSftp.cd(hostType.getConfigLocation());
            log.info("Upload {} to {}:{}{}", localFile, host, remoteFolder,
                    remoteFile);
            channelSftp.cd(remoteFolder);
            channelSftp.put(localFile, remoteFile);
        } finally {
            channel.disconnect();
        }

    }

    public synchronized void restartFM() {
        // TODO: restart FM. This is should be called after a user choose to
        // restart via FM GUI. Some user may prefer to do it manually. And we
        // also should info our user FM will temporarily down, etc.
    }

    protected String getShellScript() {
        return "cd " + HSM.getConfigLocation() + "; cp " + CONF + " " + CONF
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

    public boolean checkConfigFilePresense() {
        if (tmpConfFile != null && tmpConfFile.exists()) {
            return true;
        }
        return false;
    }

    public void fetchConfigFile(char[] password) throws Exception {
        HostInfo hostInfo = subnet.getCurrentFE();
        String hostName = hostInfo.getHost();

        String userName = hostInfo.getSshUserName();

        JSchSession jschSession =
                JSchSessionFactory.getSession(subnet, false, password);

        File tmpFile = File.createTempFile("~FV", null);
        tmpFile.deleteOnExit();
        ChannelSftp sftpChannel = jschSession.getSFtpChannel();
        sftpChannel.connect();
        HostType hostType = determineHostType(sftpChannel);
        hostInfo.setHostType(hostType);
        try {
            sftpChannel.get(CONF, tmpFile.getAbsolutePath());
            log.info("Download {}:{}{} to {} (size={})", hostName,
                    hostType.getConfigLocation(), CONF,
                    tmpFile.getAbsolutePath(), tmpFile.length());
            subnet.setCurrentUser(userName);
            tmpConfFile = tmpFile;
        } catch (SftpException e) {
            throw new SftpException(e.id,
                    STLMessages.STL61020_SFTP_FAILURE.getDescription(
                            hostType.getConfigLocation() + CONF,
                            StringUtils.getErrorMessage(e)), e.getCause());
        } finally {
            sftpChannel.disconnect();
        }
    }

    /*
     * This method is called by the ManagementApi to terminate any open
     * connections.
     */
    public void cancelFetchConfigFile(SubnetDescription subnet) {
        // See if there are any in-progress or open connections and close them
        JSchSession subnetSession =
                JSchSessionFactory.getSessionFromMap(subnet);
        if (subnetSession != null) {

            Channel sftp = null;
            try {
                sftp = subnetSession.getSFtpChannel();
                if (sftp != null) {
                    sftp.disconnect();
                }
            } catch (JSchException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                // Close session if user canceled the login
                subnetSession.disconnect();
            }

        } else {
            log.info("cancelFetchConfigFile(): subnetSession is not in the map yet");
        }

    }

    protected HostType determineHostType(ChannelSftp sftpChannel) {
        HostType hostType = UNKNOWN;
        HostType[] hostTypes = HostType.values();
        for (int i = 0; i < hostTypes.length; i++) {
            try {
                sftpChannel.cd(hostTypes[i].getConfigLocation());
                hostType = hostTypes[i];
                break;
            } catch (SftpException e) {
            }
        }
        return hostType;
    }

}
