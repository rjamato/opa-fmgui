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

import com.intel.stl.api.performance.impl.PAHelper;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.session.ISession;

public class ConnectionSession implements ISession {

    private int roundRobin = 0;

    private final STLConnection subnetApiConn;

    private final STLConnection perfApiConn;

    private final STLConnection noticeApiConn;

    private SAHelper saHelper;

    private PAHelper paHelper;

    public ConnectionSession(STLConnection subnetApiConn,
            STLConnection perfApiConn, STLConnection noticeApiConn) {
        this.subnetApiConn = subnetApiConn;
        this.perfApiConn = perfApiConn;
        this.noticeApiConn = noticeApiConn;
    }

    @Override
    public IStatement createStatement() {
        IStatement statement = null;
        switch (roundRobin) {
            case 0:
                roundRobin = 1;
                statement = subnetApiConn.createStatement();
                break;
            case 1:
                roundRobin = 2;
                statement = perfApiConn.createStatement();
                break;
            case 2:
                roundRobin = 0;
                statement = noticeApiConn.createStatement();
                break;
        }
        return statement;
    }

    @Override
    public SAHelper getSAHelper() {
        if (saHelper == null) {
            saHelper = new SAHelper(subnetApiConn.createStatement());
        }
        return saHelper;
    }

    @Override
    public PAHelper getPAHelper() {
        if (paHelper == null) {
            paHelper = new PAHelper(perfApiConn.createStatement());
        }
        return paHelper;
    }

    @Override
    public void removeStatement(IStatement statement) {

    }

    @Override
    public void close() {
        subnetApiConn.close();
        perfApiConn.close();
        noticeApiConn.close();
    }

    @Override
    public SubnetDescription getSubnetDescription() {
        return subnetApiConn.getConnectionDescription();
    }

}
