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
 * I N T E L C O R P O R A T I O N
 * 
 * Functional Group: Fabric Viewer Application
 * 
 * File Name: FVCommand.java
 * 
 * Archive Source: $Source$
 * 
 * Archive Log: $Log$
 * Archive Log: Revision 1.10  2015/08/17 18:49:05  jijunwan
 * Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 * Archive Log: - change backend files' headers
 * Archive Log:
 * Archive Log: Revision 1.9  2015/06/12 16:23:40  fernande
 * Archive Log: PR 129034 Support secure FE. Removing comments refering to legacy code
 * Archive Log:
 * Archive Log: Revision 1.8  2015/06/08 16:07:26  fernande
 * Archive Log: PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code. Adding connectionInProgress flag to avoid timeouts during connections that require a password (SSL) and restore the timeout after the connection is established.
 * Archive Log:
 * Archive Log: Revision 1.7  2015/06/05 19:10:15  jijunwan
 * Archive Log: PR 129096 - Some old files have no copyright text
 * Archive Log: - added Intel copyright text
 * Archive Log:
 * 
 * Overview:
 * 
 * @author: jijunwan
 * 
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.command;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.messages.adapter.OobPacket;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.response.FVResponse;

public abstract class FVCommand<F> extends FVMessage implements
        ICommand<FVResponse<F>, F> {
    private InputArgument input;

    private IStatement submittingStatement;

    private OobPacket packet;

    /**
     * Unique FVResponse for this FVCommand.
     */
    private FVResponse<F> fvResponse;

    @Override
    public void setMessageID(long messageID) {
        super.setMessageID(messageID);
        if (fvResponse != null) {
            fvResponse.setMessageID(messageID);
        }
    }

    /**
     * Sets the FVResponse associated with this FVCommand.
     * 
     * @param fvResponse
     *            the response associated with this command.
     */
    protected void setResponse(FVResponse<F> fvResponse) {
        fvResponse.setMessageID(getMessageID());
        this.fvResponse = fvResponse;
    }

    /**
     * Gets the FVResponse for this FVCommand.
     * 
     * @return the FVResponse associated with this FVCommand.
     */
    @Override
    public FVResponse<F> getResponse() {
        return fvResponse;
    }

    /**
     * @return the input
     */
    @Override
    public InputArgument getInput() {
        return input;
    }

    /**
     * @param input
     *            the input to set
     */
    public void setInput(InputArgument input) {
        this.input = input;
        fvResponse.setDescription(input.toString());
    }

    @Override
    public void setConnectionInProgress(boolean inProgress) {
        if (fvResponse != null) {
            fvResponse.setConnectionInProgress(inProgress);
        }
    }

    @Override
    public void setStatement(IStatement statement) {
        this.submittingStatement = statement;
    }

    @Override
    public void setPacket(OobPacket packet) {
        this.packet = packet;
        long id = packet.getRmppMad().getCommonMad().getTransactionId();
        setMessageID(id);
    }

    @Override
    public OobPacket getPacket() {
        return packet;
    }

    @Override
    public IStatement getStatement() {
        return submittingStatement;
    }

    @Override
    public RmppMad prepareMad() {
        throw new UnsupportedOperationException();
    }

    public F getResult(long timeout, TimeUnit unit) throws Exception {
        F result = null;
        List<F> results = fvResponse.get(timeout, unit);
        if (results != null && !results.isEmpty()) {
            result = results.get(0);
        }
        return result;
    }

    public List<F> getResults(long timeout, TimeUnit unit) throws Exception {
        return fvResponse.get(timeout, unit);
    }

}
