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
package com.intel.stl.fecdriver.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.MadException;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.IConnectionEventListener;
import com.intel.stl.fecdriver.messages.command.FVCommand;
import com.intel.stl.fecdriver.messages.response.FVResponse;

/**
 * @author jijunwan TODO: expose MadException to the higher level rather than
 *         hiding it and returning null
 */
public abstract class FEHelper {
    private static Logger log = LoggerFactory.getLogger(FEHelper.class);

    private static boolean DEBUG = false;

    protected STLConnection conn;

    protected STLStatement statement;

    public FEHelper(IConnection conn) {
        super();
        if (conn == null) {
            throw new IllegalArgumentException("STLConnection cannot be null");
        }
        this.conn = (STLConnection) conn;
        statement = this.conn.createStatement();
    }

    public void setConnectionEventListener(IConnectionEventListener listener) {
        conn.addConnectionEventListener(listener);
    }

    public void removeConnectionEventListener(IConnectionEventListener listener) {
        conn.removeConnectionEventListener(listener);
    }

    public IConnection getConnection() {
        return conn;
    }

    protected <E, F> List<F> getResponses(FVCommand<E, F> cmd) throws Exception {
        List<F> res = null;
        statement.execute(cmd);
        FVResponse<F> response = cmd.getResponse();
        res = response.get();
        Exception e = response.getError();
        if (e != null) {
            if (e instanceof MadException) {
                log.error(e.getMessage());
            } else {
                throw response.getError();
            }
        }

        if (DEBUG && res != null) {
            for (int i = 0; i < res.size(); i++) {
                System.out.println(i + " " + res.get(i));
            }
        }
        return res;
    }

    protected <E, F> F getSingleResponse(FVCommand<E, F> cmd) throws Exception {
        List<F> res = null;
        statement.execute(cmd);
        FVResponse<F> response = cmd.getResponse();
        res = response.get();
        Exception e = response.getError();
        if (e != null) {
            if (e instanceof MadException) {
                log.error(e.getMessage());
            } else {
                throw response.getError();
            }
        }

        if (DEBUG && res != null) {
            for (int i = 0; i < res.size(); i++) {
                System.out.println(i + " " + res.get(i));
            }
        }
        if (res != null && !res.isEmpty()) {
            return res.get(0);
        } else {
            return null;
        }
    }

    public void close() {
        statement.close();
    }
}
