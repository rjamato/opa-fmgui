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
 *  File Name: Session.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/08/18 14:23:27  jijunwan
 *  Archive Log:    PR 130032 - Fix critical issues found by Klocwork or FindBugs
 *  Archive Log:    - fixed null pointer issue and dead code
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:49:23  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/14 21:52:44  fernande
 *  Archive Log:    PR 128703 - Fail over doesn't work on A0 Fabric. Fix for issues in the retry logic of SMFailoverManager
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/07/10 20:47:13  fernande
 *  Archive Log:    PR 129522 - Notice is not written to database due to topology not found. Moved FE Helpers to the session object and changed the order of initialization for the SubnetContext.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/29 20:39:32  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Second wave of changes: the application can be switched between the old adapter and the new; moved out several initialization pieces out of objects constructor to allow subnet initialization with a UI in place; improved generics definitions for FV commands.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/05/26 15:41:45  fernande
 *  Archive Log:    PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. A new FEAdapter is being added to handle requests through SubnetRequestDispatchers, which manage state for each connection to a subnet.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.session;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.intel.stl.api.performance.impl.PAHelper;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.api.subnet.impl.SAHelper;
import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.IStatement;
import com.intel.stl.fecdriver.dispatcher.IRequestDispatcher;

public class Session implements ISession {

    private final List<WeakReference<Statement>> statements =
            new ArrayList<WeakReference<Statement>>();

    private final IRequestDispatcher dispatcher;

    private SAHelper saHelper;

    private PAHelper paHelper;

    public Session(IRequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public synchronized IStatement createStatement() {
        Statement res = new Statement(this);
        addStatement(res);
        return res;
    }

    @Override
    public SAHelper getSAHelper() {
        if (saHelper == null) {
            saHelper = new SAHelper(createStatement());
        }
        return saHelper;
    }

    @Override
    public PAHelper getPAHelper() {
        if (paHelper == null) {
            paHelper = new PAHelper(createStatement());
        }
        return paHelper;
    }

    @Override
    public synchronized void removeStatement(IStatement statement) {
        Iterator<WeakReference<Statement>> it = statements.iterator();
        while (it.hasNext()) {
            WeakReference<Statement> ref = it.next();
            Statement refStatement = ref.get();
            if (refStatement != null && refStatement.equals(statement)) {
                it.remove();
                break;
            }
        }
    }

    @Override
    public void close() {
        // Make a shallow copy of the list of statements
        List<WeakReference<Statement>> copy =
                new ArrayList<WeakReference<Statement>>(statements);
        Iterator<WeakReference<Statement>> it = copy.iterator();
        while (it.hasNext()) {
            WeakReference<Statement> ref = it.next();
            try {
                // this will call removeStatement(IStatement), above
                Statement statement = ref.get();
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
            }
        }
        if (saHelper != null) {
            saHelper.close();
        }
        if (paHelper != null) {
            paHelper.close();
        }
        dispatcher.removeSession(this);
    }

    @Override
    public SubnetDescription getSubnetDescription() {
        return dispatcher.getSubnetDescription();
    }

    protected void addStatement(Statement statement) {
        statements.add(new WeakReference<Statement>(statement));
    }

    protected void fireOnRequestTimeout(TimeoutException toe) {
        dispatcher.onRequestTimeout(toe);
    }

    protected <E extends IResponse<F>, F> void submitCmd(ICommand<E, F> cmd) {
        dispatcher.queueCmd(cmd);
    }

}
