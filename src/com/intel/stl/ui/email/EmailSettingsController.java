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
 *  File Name: EmailSetupController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2016/03/02 18:27:31  jijunwan
 *  Archive Log:    PR 133067 - Add a popup window that e-mail was sent successfully when "test" button is click
 *  Archive Log:
 *  Archive Log:    - changed to disable button after we click test button
 *  Archive Log:    - changed to show "sending email..." message when we are sending out a test email
 *  Archive Log:    - changed to show "Test message sent out, please check your email account." after email sent out
 *  Archive Log:    - change to recover message to normal text when there is a user action
 *  Archive Log:    - added undo/redo capability to email address text area
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/21 04:01:30  fisherma
 *  Archive Log:    Added property to turn email notifications feature on/off.  Added strings to localization file.  Fixed dialog to be sized properly on different operating systems under various look and feel.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/17 18:54:23  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/14 19:51:41  fisherma
 *  Archive Log:    Ensure that email settings dialog always shows up on top of all the FV main windows.  Cleanup and improve validation code on the to/from and smtp server name fields.  Allow empty value for smpt server name field.  Fix re/parenting issue for the error dialog to show up on top of the smpt settings dialog.  Moved title string for the dialog to the resource file.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/10 17:30:26  robertja
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: fisherma
 *
 ******************************************************************************/

package com.intel.stl.ui.email;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.intel.stl.api.configuration.MailProperties;
import com.intel.stl.api.notice.IEmailEventListener;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.email.view.EmailSettingsView;
import com.intel.stl.ui.main.ISubnetManager;
import com.intel.stl.ui.main.view.FVMainFrame;

public class EmailSettingsController
        implements IEmailController, IEmailEventListener<NoticeBean> {

    private static EmailSettingsController instance = null;

    private final EmailSettingsView view;

    private final ISubnetManager subnetMgr;

    private String smtpServerName = "";

    private String smtpPortNumber =
            new Integer(UIConstants.DEFAULT_SMTP_PORT).toString();

    private String fromAddress = "";

    private final String toTestAddress = "";

    private boolean isEmailNotificationsEnabled = false;

    private EmailSettingsController(EmailSettingsView view,
            ISubnetManager subnetMgr) {
        this.view = view;
        this.view.setEmailSettingsListener(this);
        this.subnetMgr = subnetMgr;

        subnetMgr.getConfigurationApi().addEmailEventListener(this);

        // Obtain the current values for SMTP properties from DB
        getSmtpSettingsFromDb();

        // Set SMPT settings in the view
        setSmtpSettingsInView();

    }

    /**
     * <i>Description:</i>
     *
     */
    private void setSmtpSettingsInView() {
        view.setSmtpServerNameStr(smtpServerName);
        view.setSmtpServerPortStr(smtpPortNumber);
        view.setFromAddrStr(fromAddress);
        view.setEnableEmailChkbox(isEmailNotificationsEnabled);
    }

    /**
     * <i>Description:</i>
     *
     */
    private void getSmtpSettingsFromDb() {
        MailProperties mailProperties =
                subnetMgr.getConfigurationApi().getMailProperties();
        smtpServerName = mailProperties.getSmtpServer();
        int port = mailProperties.getSmtpPort();
        if (port < 0) {
            port = UIConstants.DEFAULT_SMTP_PORT;
        }
        smtpPortNumber = new Integer(port).toString();
        fromAddress = mailProperties.getFromAddr();
        isEmailNotificationsEnabled =
                mailProperties.getEmailNotificationsEnabled();
    }

    /**
     * <i>Description:</i> If there is NO instance of EmailSettingsController,
     * create one with the current FVMainFrame as owner.
     *
     * If there is an instance of EmailSettingsController, update its owner to
     * the FVMainFrame passed as the parameter. This is to allow for proper
     * parenting and appearance of the dialog.
     *
     */
    public static EmailSettingsController getInstance(FVMainFrame owner,
            ISubnetManager subMgr) {
        if (instance == null) {
            instance = new EmailSettingsController(new EmailSettingsView(owner),
                    subMgr);
        } else {
            instance.updateOwner(owner);
        }

        return instance;
    }

    /**
     * <i>Description:</i>
     *
     * @param owner
     */
    private void updateOwner(FVMainFrame owner) {
        view.setOwner(owner);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.email.IEmailControl#onReset()
     */
    @Override
    public void onReset() {
        // Set the current SMTP values in the view
        setSmtpSettingsInView();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.email.IEmailControl#onOK()
     */
    @Override
    public void onOK() {
        // Should retrieve the values presently set in the view and
        // save them in the controller (here).
        smtpServerName = view.getSmtpServerNameStr();
        smtpPortNumber = view.getSmtpServerPortStr();
        fromAddress = view.getFromAddrStr();
        isEmailNotificationsEnabled = view.getEnableEmail();

        // Save to the database.
        MailProperties mailProperties = new MailProperties();
        mailProperties.setSmtpServer(smtpServerName);
        mailProperties.setFromAddr(fromAddress);
        mailProperties.setSmtpPort(new Integer(smtpPortNumber));
        mailProperties
                .setEmailNotificationsEnabled(isEmailNotificationsEnabled);
        subnetMgr.getConfigurationApi().updateMailProperties(mailProperties);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.email.IEmailControl#onTest()
     */
    @Override
    public void onTest() {
        view.showTesting(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            /*
             * (non-Javadoc)
             *
             * @see javax.swing.SwingWorker#doInBackground()
             */
            @Override
            protected Void doInBackground() throws Exception {
                // Should retrieve the values presently set in the view and use
                // these values to send the test email
                String testSmtpHostName = view.getSmtpServerNameStr();
                String testSmtpPortNum = view.getSmtpServerPortStr();
                String testToAddr = view.getToAddrStr();
                String testFromAddr = view.getFromAddrStr();

                // Test connection with above values...
                MailProperties mailProperties = new MailProperties();
                mailProperties.setSmtpServer(testSmtpHostName);
                mailProperties.setFromAddr(testFromAddr);
                mailProperties.setSmtpPort(new Integer(testSmtpPortNum));
                String subject =
                        UILabels.STL92001_TEST_EMAIL_SUBJECT.getDescription();
                String body = "";
                subnetMgr.getConfigurationApi().sendTestMail(mailProperties,
                        testToAddr, subject, body);
                return null;
            }

            /*
             * (non-Javadoc)
             *
             * @see javax.swing.SwingWorker#done()
             */
            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    Util.showError(view, e);
                } finally {
                    view.showTesting(false);
                }
            }

        };
        worker.execute();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.email.IEmailControl#showEmailSettingsDlg()
     */
    @Override
    public void showEmailSettingsDlg(FVMainFrame owner) {
        view.setOwner(owner);
        view.setLocationRelativeTo(owner);
        view.setVisible(true);
        view.toFront();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.email.IEmailControl#hideEmailSettingsDlg()
     */
    @Override
    public void hideEmailSettingsDlg() {
        this.view.setVisible(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.intel.stl.api.notice.IEmailEventListener#onNewEvent(java.lang.Object
     * [])
     */
    @Override
    public void onNewEvent(NoticeBean[] noticeList) {
        for (NoticeBean bean : noticeList) {
            Util.showErrorMessage(view, new String(bean.getData()));
        }
    }
}
