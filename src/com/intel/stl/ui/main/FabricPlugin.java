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
 *  File Name: FabricPlugin.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.33.2.2  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.33.2.1  2015/05/06 19:39:13  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2015/05/01 21:29:06  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2015/04/28 22:08:56  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2015/04/22 22:31:54  fisherma
 *  Archive Log:    Removing html tags from error messages.
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2015/04/22 17:06:15  fernande
 *  Archive Log:    Changed the initialization sequence. Now the FMGui plugin is given the opportunity to setup its own CertsAssistant before initializing components in the backend.
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2015/04/08 20:32:21  fernande
 *  Archive Log:    Adding support for Timeout exception going to failover processing
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/04/07 20:31:37  jijunwan
 *  Archive Log:    removed unused code
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/03/24 17:46:48  jijunwan
 *  Archive Log:    set SplitPaneDivider.draggingColor
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/03/12 15:58:29  jijunwan
 *  Archive Log:    introduce dev environment variable to tell FM GUI whether we want to run under development mode
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/03/12 04:24:16  jijunwan
 *  Archive Log:    turn off popup dialog for uncaught exception
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/02/13 21:27:50  rjtierne
 *  Archive Log:    Multinet Wizard: Disabled TMP_SOLUTION flag to use subnetMgr instead of subnetMgrTmp
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/02/13 05:59:57  jijunwan
 *  Archive Log:    added support to switch  between different SubnetManagers. By default TMP_SOLUTION is true, and it will use temporary solution. Setting TMP_SOLUTION to false will switch to current solution under development
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/01/30 20:27:16  fernande
 *  Archive Log:    Initial changes to support multiple fabric viewers
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/01/30 17:49:44  jijunwan
 *  Archive Log:    put stack trace in log.error
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/12/11 18:52:55  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/11/13 17:38:08  fernande
 *  Archive Log:    Fixing issue where errors during plugin init would not show in the UI
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/10/24 18:56:20  fernande
 *  Archive Log:    Initial batch of fixes for notice processing. Notice updates not working yet but need a stable environment for Bob.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/10/21 16:36:45  fernande
 *  Archive Log:    Fixes for the shutdown sequence. Now there is a onWindowClose() method that does shutdown stuff on the EDT and cleanup gets invoked from the FabricPlugin in a non-EDT thread. Added NoticeEventListener to the shutdown process.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/10/09 12:59:15  fernande
 *  Archive Log:    Changed the FabricController to use the UI framework and converted Swing workers into AbstractTasks to optimize the switching of contexts and the refreshing of pages. These processes still run under Swing workers, but now each setContext is run on its own Swing worker to improve performance. Also, changed the ProgressObserver mechanism to provide a more accurate progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/08/12 21:02:44  jijunwan
 *  Archive Log:    moved show dialogs to Util class, so in the future if we want to change L&F, we only need to change one place
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/05/30 21:59:08  jijunwan
 *  Archive Log:    moved all random generation to API side, and added a menu item to allow a user turn on/off randomization
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/04/20 03:18:46  jijunwan
 *  Archive Log:    minor change
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/04/18 21:16:55  jijunwan
 *  Archive Log:    minor changes
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/04/18 15:59:23  jijunwan
 *  Archive Log:    better exception handling
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/04/17 20:10:38  jijunwan
 *  Archive Log:    new main frame layout
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/04/17 16:59:48  jijunwan
 *  Archive Log:    integrate SetupWizard into main frame
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/04/17 16:45:52  fernande
 *  Archive Log:    Changed AppContext to provide access to the ConfigurationApi, since it already resides there.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/04/16 21:10:09  jijunwan
 *  Archive Log:    added showing error messages
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/04/16 17:55:45  jijunwan
 *  Archive Log:    made setup wizard works within our app
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/04/16 16:20:43  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/16 15:36:02  jijunwan
 *  Archive Log:    center SetupWizard, made it to be Modal
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 15:17:43  jijunwan
 *  Archive Log:    keep old style "Home Page", apply ApiBroker to update data
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/15 20:31:40  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/14 17:08:01  fernande
 *  Archive Log:    Added missing init call
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:50:38  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.AppContext;
import com.intel.stl.api.FMGuiPlugin;
import com.intel.stl.api.ICertsAssistant;
import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.view.CertsPanel;
import com.intel.stl.ui.main.view.SplashScreen;

public class FabricPlugin extends FMGuiPlugin {
    private final static Logger log = LoggerFactory
            .getLogger(FMGuiPlugin.class);

    public static String DEV_NAME = "dev";

    public static boolean IS_DEV = false;

    SplashScreen splashScreen;

    private ISubnetManager subnetMgr;

    private final List<Throwable> errors = new ArrayList<Throwable>();

    private boolean splashShowing = false;

    @Override
    public void init(AppContext appContext) {
        CertsPanel certsPanel = new CertsPanel();
        ICertsAssistant certsAssistant =
                new CertsAssistant(certsPanel, appContext.getConfigurationApi());
        appContext.registerCertsAssistant(certsAssistant);
        super.init(appContext);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (System.getProperty("os.name").equals("Linux")) {
                UIManager.setLookAndFeel(UIManager
                        .getCrossPlatformLookAndFeelClassName());
            }
            UIManager.put("SplitPaneDivider.draggingColor",
                    UIConstants.INTEL_LIGHT_GRAY);

            String devValue = System.getProperty(DEV_NAME);
            if (devValue != null) {
                IS_DEV =
                        devValue.isEmpty() || devValue.equalsIgnoreCase("true");
            }
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
            System.setProperty("sun.awt.exception.handler",
                    ExceptionHandler.class.getName());

        } catch (Exception e) {
            errors.add(e);
            e.printStackTrace();
        }
        splashScreen = new SplashScreen();
        splashShowing = true;
        splashScreen.showScreen();
    }

    @Override
    public void invokeMain(final boolean firstRun) {
        splashScreen.setProgress("Initializing UI", 99);
        try {
            subnetMgr = createSubnetManager();
            subnetMgr.init(firstRun);
        } catch (Throwable e) {
            errors.add(e);
            e.printStackTrace();
        }
        splashShowing = false;
        // Up to this point, the plugin handles errors. Now the SubnetManager
        // should handle errors and route them to the proper frame
        if (errors.isEmpty()) {
            subnetMgr.startSubnets(splashScreen);
        } else {
            StringBuffer msg = new StringBuffer();
            for (Throwable e : errors) {
                msg.append(StringUtils.getErrorMessage(e));
            }
            throw new RuntimeException(msg.toString());
        }
    }

    protected ISubnetManager createSubnetManager() {
        AppContext appContext = getAppContext();

        return new SubnetManager(appContext);
    }

    @Override
    public void showProgress(String message, int progress) {
        splashScreen.setProgress(message, progress);
    }

    @Override
    public void showErrors(List<Throwable> errors) {
        for (Throwable e : errors) {
            e.printStackTrace();
        }
        Util.showErrors(splashScreen, errors);
    }

    @Override
    public void shutdown() {
        if (splashShowing) {
            splashScreen.close();
            splashShowing = false;
        }
        if (subnetMgr != null) {
            try {
                subnetMgr.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error(e.getMessage() + " @ " + t, e);
            if (IS_DEV) {
                showErrors(Arrays.asList(e));
            }
        }
    }
}
