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
 *  File Name: FVCmdGetPMConfig.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:49:11  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/29 20:38:14  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/12 20:02:36  fernande
 *  Archive Log:    New command to get PM Configuration
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.command.pa;

import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.fecdriver.SingleResponseCommand;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.pa.PMConfig;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.response.pa.FVRspGetPMConfig;

public class FVCmdGetPMConfig extends PACommand<PMConfig, PMConfigBean>
        implements SingleResponseCommand<PMConfigBean, FVRspGetPMConfig> {

    public FVCmdGetPMConfig() {
        setResponse(new FVRspGetPMConfig());
    }

    @Override
    protected void fillCommonMad(CommonMad comm) {
        super.fillCommonMad(comm);
        comm.setMethod(PAConstants.STL_PA_CMD_GET);
        comm.setAttributeID(PAConstants.STL_PA_ATTRID_GET_PM_CONFIG);
    }

    @Override
    protected PMConfig buildRecord() {
        PMConfig pmConfig = new PMConfig();
        pmConfig.build(true);
        return pmConfig;
    }

    @Override
    protected void fillInput(SAHeader header, PMConfig record) {
    }

}
