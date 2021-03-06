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
 *  File Name: FMGUIPlugin.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/09/08 18:34:13  jijunwan
 *  Archive Log:    PR 130277 - FM GUI Locked up due to [AWT-EventQueue-0] ERROR - Unsupported MTUSize 0x0d java.lang.IllegalArgumentException: Unsupported MTUSize 0x0d
 *  Archive Log:    - moved isDev to FMGuiPlugin so both backend and frontend can access it
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/17 18:48:51  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/22 16:51:16  fernande
 *  Archive Log:    Reorganized the startup sequence so that the UI plugin could initialize its own CertsAssistant. This way, autoconnect subnets would require a password using the UI CertsAssistant instead of the default CertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/16 21:09:29  jijunwan
 *  Archive Log:    added single app instance check
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/16 17:54:02  jijunwan
 *  Archive Log:    made setup wizard works within our app
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/15 20:27:02  fernande
 *  Archive Log:    Changes to defer creation of APIs until a subnet is chosen
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/14 17:10:20  fernande
 *  Archive Log:    Passed flag to indicate first run
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:20:23  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;

public abstract class FMGuiPlugin implements FMGui {

    private final static String DEV_NAME = "dev";

    public static boolean IS_DEV = false;

    private AppContext appContext;

    @Override
    public void init(AppContext appContext) {
        this.appContext = appContext;

        String devValue = System.getProperty(DEV_NAME);
        if (devValue != null) {
            IS_DEV = devValue.isEmpty() || devValue.equalsIgnoreCase("true");
        }
    }

    @Override
    public AppContext getAppContext() {
        return appContext;
    }
}
