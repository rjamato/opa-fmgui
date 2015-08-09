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
 *  File Name: FMGuiApp.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.21  2015/04/27 17:49:08  jijunwan
 *  Archive Log:    added os, build id and build date info in log file
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/03/17 02:14:41  jijunwan
 *  Archive Log:    improved stability
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/03/16 22:35:05  jijunwan
 *  Archive Log:    ensure application start date will be print in log file. This will help us diagnose issues
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/04 21:29:34  jijunwan
 *  Archive Log:    added Mail Manager
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.FMGui;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.AppDataUtils;
import com.intel.stl.common.STLMessages;
import com.intel.stl.configuration.AppComponentRegistry;
import com.intel.stl.configuration.AppConfig;
import com.intel.stl.configuration.AppConfigurationException;
import com.intel.stl.configuration.AppSettings;
import com.intel.stl.datamanager.DatabaseManager;

/**
 * @author jijunwan
 * 
 */
public class FMGuiApp {
    private static Logger log;

    private final static String PARM_FIRSTRUN = "-firstrun";

    private static boolean firstrunSet = false;

    private static boolean firstRun;

    private Thread mainThread = null;

    private AppComponentRegistry registry;

    private FMGui ui;

    private final List<Throwable> errors = new ArrayList<Throwable>();

    public FMGuiApp() {
        mainThread = Thread.currentThread();
    }

    public boolean isFirstRun() {
        List<SubnetDescription> subnets = null;
        try {
            DatabaseManager dbMgr = registry.getDatabaseManager();
            if (dbMgr != null) {
                subnets = dbMgr.getSubnets();
            } else {
                errors.add(new RuntimeException("No Database Manager"));
            }
        } catch (Exception e) {
            errors.add(e);
        }
        return subnets == null || subnets.isEmpty();
    }

    protected void initApp() {
        AppDataUtils.initializeLogging();
        log = LoggerFactory.getLogger(FMGuiApp.class);
        log.error("===============================");
        log.error("Start Application " + getClass().getName() + " at "
                + (new Date()) + " on " + System.getProperty("os.name"));
        registry = AppConfig.getAppComponentRegistry();
        try {
            registry.initialize();
            AppSettings as = registry.getAppSettings();
            log.error("Application build id: " + as.getAppBuildId()
                    + " build date: " + as.getAppBuildDate());
        } catch (AppConfigurationException e) {
            log.error("Application configuration exception", e);
            errors.add(e);
        } catch (Throwable e) {
            log.error("Application configuration exception", e);
            errors.add(e);
        }
        ui = registry.getUIComponent();
    }

    /**
     * @return the errors
     */
    public List<Throwable> getErrors() {
        return errors;
    }

    public void addShutdownHook(final Callable<Void> hook) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.debug("Starts shutdown hook " + hook);
                try {
                    hook.call();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        mainThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void run() {
        addShutdownHook(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    if (ui != null) {
                        ui.shutdown();
                    }
                } finally {
                    try {
                        registry.shutdown();
                    } finally {
                        System.out.println("Any shutdown cleanup goes here!");
                        Set<Thread> threadSet =
                                Thread.getAllStackTraces().keySet();
                        for (Thread thread : threadSet) {
                            if (thread != mainThread
                                    && thread != Thread.currentThread()) {
                                // System.out.println("interrupt "+thread);
                                thread.interrupt();
                            }
                        }
                    }
                }
                return null;
            }
        });

        initApp();

        if (errors.isEmpty()) {
            try {
                if (!firstrunSet) {
                    firstRun = isFirstRun();
                }
                ui.invokeMain(firstRun);
            } catch (Exception e) {
                errors.add(e);
            }
        }
        if (!errors.isEmpty()) {
            if (ui == null) {
                ui = registry.getUIComponent();
            }

            if (ui != null) {
                ui.showProgress(
                        STLMessages.STL10010_ERROR_DURING_INIT.getDescription(),
                        100);
                ui.showErrors(errors);
                ui.shutdown();
            } else {
                System.err.println(STLMessages.STL10010_ERROR_DURING_INIT
                        .getDescription());
                for (Throwable error : errors) {
                    System.err.println("  " + error.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        for (String param : args) {
            if (PARM_FIRSTRUN.equalsIgnoreCase(param)) {
                firstRun = true;
                firstrunSet = true;
            }
        }
        FMGuiApp app = new FMGuiApp();
        app.run();
    }

}
