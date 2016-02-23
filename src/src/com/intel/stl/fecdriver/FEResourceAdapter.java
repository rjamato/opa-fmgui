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
 *  File Name: FEResourceAdapter.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.6  2015/08/17 18:49:22  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - change backend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.5  2015/06/10 19:36:44  jijunwan
 *  Archive Log: PR 129153 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: 
 * 
 ******************************************************************************/
package com.intel.stl.fecdriver;

import java.io.IOException;
import java.util.Properties;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.fecdriver.impl.STLConnection;

public interface FEResourceAdapter {
    public static final String HOST = "Host";

    public static final String PORT = "Port";

    public static final String USER = "user";

    public static final String PASSWORD = "password";

    /**
     * 
     * @return Version of the FE protocol header see STL_Vol2 Section
     *         3.2.5.2.3.1
     */
    int getProtocolVersion();

    /**
     * Retrieves the driver's version
     * 
     * @return version string
     */
    String getVersion();

    /**
     * Sets the maximum time in seconds that this resource adapter will wait
     * while attempting to connect to a FE.
     * 
     * @param seconds
     */
    void setLoginTimeout(int seconds);

    /**
     * Gets the maximum time in seconds that this resource adapter will wait
     * while attempting to connect to a FE.
     * 
     * @return
     */
    int getLoginTimeout();

    STLConnection connect(SubnetDescription subnet, Properties info)
            throws IOException;

    STLConnection connect(SubnetDescription subnet) throws IOException;
}
