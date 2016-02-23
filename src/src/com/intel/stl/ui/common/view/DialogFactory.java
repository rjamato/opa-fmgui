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
 *  File Name: DialogFactory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.13  2015/08/31 15:52:05  jijunwan
 *  Archive Log:    PR 130204 - Shortcut launch results in meaningless error and forces me to reboot my machine
 *  Archive Log:    - added new method to show error messages and wait for user's input
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/08/17 18:53:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/06/17 15:40:28  fisherma
 *  Archive Log:    PR129220 - partial fix for the login changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/05/21 17:02:41  fisherma
 *  Archive Log:    Make error dialogs non-modal by default.  Added code to make the error dialog "sticky" to its parent frame if there is one.  This way the error dialog is always shown over the parent window and will be moved around along with the parent window.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/05/11 12:26:07  rjtierne
 *  Archive Log:    PR 128585 - Fix errors found by Klocwork and FindBugs
 *  Archive Log:    Removed static DialogBuilder errorDialog and returned new local instantiation
 *  Archive Log:    in getDialogBuilder()
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/05/05 18:29:45  jijunwan
 *  Archive Log:    improvement to avoid potential sync issue
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/05/04 21:14:59  jijunwan
 *  Archive Log:    Fixed the very strange case where when we have massive error messages, sometime we get a dialog that can not respond to mouse input and but respond to keyboard. The fixes include
 *  Archive Log:    1) remove  #setAlwaysOnTop
 *  Archive Log:    2) create a new dialog after it's closed either by OK button by the win close button
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/05/01 21:29:12  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/30 18:11:45  jijunwan
 *  Archive Log:    improved to handle the case that an error dialog pops up when we have a opened setup wizard. The current solution is that
 *  Archive Log:    1) show error dialog on top of setup wizard, so a user knows the errors promptly
 *  Archive Log:    2) since the error dialog is on top, set it's modal type to Application, so the user must respond the error dialog rathe then the setup dialog under it
 *  Archive Log:    3) set error dialog's modal type back to Document when there is no opened setup wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/29 22:05:28  jijunwan
 *  Archive Log:    1) show parent's title on error dialog
 *  Archive Log:    2) add Intel log icon on title bar when no parent frame
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/28 22:04:17  jijunwan
 *  Archive Log:    improved to
 *  Archive Log:    1) share error dialog among components have the same root
 *  Archive Log:    2) set ModalityType to DOCUMENT_MODAL for shared dialog with a valid parent
 *  Archive Log:    3) support directly handle showing exceptions and ignore InterruptedException and NullPointerException
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/21 20:07:24  fisherma
 *  Archive Log:    Updated dialog's appearance - centered relatively to parent if there is a parent.  Set the size of the scroll bar to 10 pixels - nicer on windows.   Updated layout.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/18 01:40:01  fisherma
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss.  The code changes address issue #2 reported in the bug.  Adding common dialog to display errors.  Needs further appearance improvements.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fisherma
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIImages;

public class DialogFactory {
    private final static Logger log = LoggerFactory
            .getLogger(DialogFactory.class);

    private static ImageIcon INFO_ICON = UIImages.LOGO_64.getImageIcon();

    private static ImageIcon CONFIRM_ICON = UIImages.CONFIRM_DLG.getImageIcon();

    private static ImageIcon WARNING_ICON = UIImages.WARNING_DLG.getImageIcon();

    private static ImageIcon ERROR_ICON = UIImages.ERROR_DLG.getImageIcon();

    public static int OK_OPTION = 0;

    public static int CANCEL_OPTION = 1;

    private static Map<Component, DialogBuilder> errorDialogs =
            new HashMap<Component, DialogBuilder>();

    //
    // Confirmation dialog has two options: 'OK' and 'Cancel'
    // 'OK' will return '0'
    // 'Cancel' will return '1'
    //
    public static int showConfirmDialog(Component owner, String msg) {
        DialogBuilder infoDialog =
                new DialogBuilder(owner, STLConstants.K0671_CONFIRM.getValue(),
                        true, STLConstants.K0645_OK.getValue(),
                        STLConstants.K0621_CANCEL.getValue());

        infoDialog.appendText(msg);
        infoDialog.setImageIcon(DialogFactory.CONFIRM_ICON);
        infoDialog.getDialog().setVisible(true);
        return infoDialog.getButtonPressed();
    }

    //
    // Information dialog has just one option: 'OK'
    //
    public static void showInfoDialog(Component owner, String msg) {
        DialogBuilder infoDialog =
                new DialogBuilder(owner,
                        STLConstants.K0032_INFORMATIONAL.getValue(), true,
                        STLConstants.K0645_OK.getValue());

        infoDialog.appendText(msg);
        infoDialog.setImageIcon(DialogFactory.INFO_ICON);
        infoDialog.getDialog().setVisible(true);
    }

    // Same as Info dialog, except use warning icon.
    // Warning dialog is modal
    public static void showWarningDialog(Component owner, String msg) {
        DialogBuilder warningDialog =
                new DialogBuilder(owner, STLConstants.K0031_WARNING.getValue(),
                        true, STLConstants.K0645_OK.getValue());

        warningDialog.appendText(msg);
        warningDialog.setImageIcon(DialogFactory.WARNING_ICON);
        warningDialog.getDialog().setVisible(true);
    }

    //
    // The error dialog displays new errors currently in the buffer.
    // This error dialog is modeless by default.
    // If a modal error dialog is required, add another showErrorDialog()
    // method to construct one with boolean modalityType parameter.
    //
    public static void showErrorDialog(Component comp, String errorString) {
        if (errorString != null && !errorString.isEmpty()) {
            DialogBuilder dlg = getDialogBuilder(comp);
            dlg.appendText(errorString);
            dlg.show();
        }
    }

    public static void showErrorDialog(Component comp, Throwable e) {
        showErrorDialog(comp, Collections.singletonList(e));
    }

    public static void showErrorDialog(Component comp,
            Collection<? extends Throwable> errors) {
        DialogBuilder dlg = getDialogBuilder(comp);
        showErrors(dlg, errors);
    }

    /**
     * 
     * <i>Description:</i> show errors on a model dialog. This is used for
     * errors during App initialization where no Frame available. We need to
     * wait until user click OK and then go ahead shutdown the application
     * 
     * @param comp
     * @param errors
     */
    public static void showModalErrorDialog(Component comp,
            Collection<? extends Throwable> errors) {
        Component root = comp == null ? null : SwingUtilities.getRoot(comp);
        DialogBuilder dlg =
                new DialogBuilder(root, STLConstants.K0030_ERROR.getValue(),
                        true, STLConstants.K0645_OK.getValue(), null);
        dlg.setImageIcon(DialogFactory.ERROR_ICON);
        dlg.getDialog().setAlwaysOnTop(true);
        showErrors(dlg, errors);
    }

    private static void showErrors(DialogBuilder dlg,
            Collection<? extends Throwable> errors) {
        boolean toShow = false;
        for (Throwable e : errors) {
            if (e instanceof ExecutionException) {
                e = ((ExecutionException) e).getCause();
            }
            if (e instanceof InterruptedException) {
                // ignore InterruptedException
                continue;
            }
            if (e instanceof NullPointerException) {
                // hide NullPointerException, put it into log file
                log.error("Null Pointer", e);
                continue;
            }
            String errorString = StringUtils.getErrorMessage(e);
            dlg.appendText(errorString);
            if (!toShow) {
                toShow = true;
            }
        }

        if (toShow) {
            dlg.show();
        }
    }

    private static DialogBuilder getDialogBuilder(Component comp) {
        if (comp == null) {
            // if (null == errorDialog) {
            DialogBuilder errorDialog =
                    new DialogBuilder(STLConstants.K0645_OK.getValue());
            errorDialog.setImageIcon(DialogFactory.ERROR_ICON);
            errorDialog.setTitle(STLConstants.K0030_ERROR.getValue());
            // }
            return errorDialog;
        }

        Component root = SwingUtilities.getRoot(comp);
        DialogBuilder dlg = errorDialogs.get(root);
        if (dlg == null || !dlg.getDialog().isShowing()) {
            // Construct error dialog with modal=false by default
            dlg =
                    new DialogBuilder(root,
                            STLConstants.K0030_ERROR.getValue(), false,
                            STLConstants.K0645_OK.getValue(), null);
            dlg.setImageIcon(DialogFactory.ERROR_ICON);
            errorDialogs.put(root, dlg);
        }
        return dlg;
    }

    public static int showPasswordDialog(java.awt.Component owner,
            String title, java.awt.Component contentPanel) {
        DialogBuilder passwordDialog =
                new DialogBuilder(owner, title, true, contentPanel,
                        STLConstants.K0645_OK.getValue(),
                        STLConstants.K0621_CANCEL.getValue());
        passwordDialog.getDialog().setVisible(true);
        return passwordDialog.getButtonPressed();
    }

}
