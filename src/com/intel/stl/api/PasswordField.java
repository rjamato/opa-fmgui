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
 *  File Name: PasswordField.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class PasswordField {

    private static String CLEAR_THREAD_NAME = "pf-thread";

    private static int DEFAULT_LIFE_TIME = 3000; // 3 sec

    private final int lifetime;

    protected final AtomicLong expiration = new AtomicLong();

    private final AtomicReference<char[]> pwd = new AtomicReference<char[]>(
            null);

    protected Thread clearThread;

    public PasswordField() {
        this(DEFAULT_LIFE_TIME);
    }

    public PasswordField(int lifetime) {
        this.lifetime = lifetime;
    }

    public void setPassword(char[] password) {
        char[] currpwd = pwd.get();
        // Set the value atomically; if another thread is updating the
        // AtomicReference, it might take two or three iterations to update the
        // value, no locking.
        while (!pwd.compareAndSet(currpwd, password)) {
            currpwd = pwd.get();
        }
        if (currpwd != null) {
            for (int i = 0; i < currpwd.length; i++) {
                currpwd[i] = '\0';
            }
        }
        if (pwd.get() == null) {
            stopClearThread();
        } else {
            startClearThread();
            resetExpiration();
        }
    }

    public char[] getPassword() {
        resetExpiration();
        char[] work = pwd.get();
        return (work == null ? null : Arrays.copyOf(work, work.length));
    }

    private void startClearThread() {
        if (clearThread == null) {
            clearThread = createThread(new Runnable() {

                @Override
                public void run() {
                    do {
                        try {
                            waitForExpiration();
                        } catch (InterruptedException e) {
                            break;
                        }
                    } while (clearThread != null
                            && !clearThread.isInterrupted()
                            && System.currentTimeMillis() < expiration.get());
                    if (clearThread != null && !clearThread.isInterrupted()) {
                        setPassword(null);
                    }
                }

            });
            clearThread.start();
        }
    }

    protected void stopClearThread() {
        if (clearThread != null) {
            synchronized (clearThread) {
                clearThread.interrupt();
            }
            clearThread = null;
        }
    }

    private void resetExpiration() {
        if (clearThread != null) {
            synchronized (clearThread) {
                expiration.set(System.currentTimeMillis() + lifetime);
                clearThread.notify();
            }
        }
    }

    protected void waitForExpiration() throws InterruptedException {
        synchronized (clearThread) {
            clearThread.wait(expiration.get() - System.currentTimeMillis());
        }
    }

    protected Thread createThread(Runnable runnable) {
        Thread newThread = new Thread(runnable);
        newThread.setName(CLEAR_THREAD_NAME);
        return newThread;
    }
}
