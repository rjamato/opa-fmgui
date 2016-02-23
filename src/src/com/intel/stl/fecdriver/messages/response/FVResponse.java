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
 * File Name: FVResponse.java
 * 
 * Archive Source: $Source$
 * 
 * Archive Log: $Log$
 * Archive Log: Revision 1.16  2015/08/18 14:23:28  jijunwan
 * Archive Log: PR 130032 - Fix critical issues found by Klocwork or FindBugs
 * Archive Log: - fixed null pointer issue and dead code
 * Archive Log:
 * Archive Log: Revision 1.15  2015/08/17 18:49:57  jijunwan
 * Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 * Archive Log: - change backend files' headers
 * Archive Log:
 * Archive Log: Revision 1.14  2015/07/31 21:05:19  fernande
 * Archive Log: PR 129631 - Ports table sometimes not getting performance related data. Changed FVResponse to throw a RequestCancelledException so that the UI gets notified when a request was cancelled by a user action.
 * Archive Log:
 * Archive Log: Revision 1.13  2015/07/30 19:32:51  fernande
 * Archive Log: PR 129592 - removing a subnet a user is monitoring cause internal DB exception. New exception used to recognize when a FE request has been cancelled by user
 * Archive Log:
 * Archive Log: Revision 1.12  2015/06/16 15:57:06  fernande
 * Archive Log: PR 129034 Support secure FE. Changes to the IResponse interface to throw only checked IOExceptions
 * Archive Log:
 * Archive Log: Revision 1.11  2015/06/08 16:07:49  fernande
 * Archive Log: PR 128897 - STLAdapter worker thread is in a continuous loop, even when there are no requests to service. Stabilizing the new FEAdapter code. Adding connectionInProgress flag to avoid timeouts during connections that require a password (SSL) and restore the timeout after the connection is established.
 * Archive Log:
 * Archive Log: Revision 1.10  2015/06/05 19:10:17  jijunwan
 * Archive Log: PR 129096 - Some old files have no copyright text
 * Archive Log: - added Intel copyright text
 * Archive Log:
 * 
 * Overview:
 * 
 * @author: jijunwan
 * 
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.response;

// Java imports
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.intel.stl.api.MadException;
import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.messages.adapter.IDatagram;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.command.FVMessage;
import com.intel.stl.fecdriver.session.RequestCancelledByUserException;

public abstract class FVResponse<E> extends FVMessage implements IResponse<E> {
    private String description;

    private RmppMad rmppMad;

    private Exception error;

    private boolean canceled;

    private List<E> results;

    private long expireTime;

    private boolean connectionInProgress = false;

    private boolean oneMoreTime = false;

    /**
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    protected void setResults(List<E> results) {
        this.results = results;
    }

    @Override
    public synchronized void setError(Exception e) {
        this.error = e;
        this.notify();
    }

    /**
     * @return the error
     */
    @Override
    public synchronized Exception getError() {
        return error;
    }

    @Override
    public synchronized void processMad(RmppMad mad) {
        try {
            ByteBuffer buffer = mad.getData().getByteBuffers()[0];
            byte[] bytes = buffer.array();
            int offset = buffer.arrayOffset();
            IDatagram<?> datagram =
                    wrap(bytes, offset, offset + buffer.limit());
            mad.setData(datagram);
            this.rmppMad = mad;
        } catch (Exception e) {
            e.printStackTrace();
            error = e;
        } finally {
            this.notify();
        }
    }

    public RmppMad getMad() throws IOException {
        if (error != null) {
            throw new IOException(error);
        }

        return rmppMad;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        setCancel();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return canceled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        return rmppMad != null || error != null || canceled;
    }

    @Override
    public List<E> get() throws IOException {
        if (!isDone()) {
            waitForResponse();
        }
        return results;
    }

    @Override
    public List<E> get(long timeout, TimeUnit unit) throws IOException,
            TimeoutException {
        if (!isDone()) {
            waitForResponse(unit.toMillis(timeout));
        }
        return results;
    }

    @Override
    public void extendWaitTime(long waitExtension) {
        long current = System.currentTimeMillis();
        this.expireTime = current + waitExtension;
    }

    public synchronized void setConnectionInProgress(boolean inProgress) {
        this.connectionInProgress = inProgress;
        if (inProgress) {
            // If this response is ever assigned to a connection not yet
            // connected, the waitForResponse with timeout should start counting
            // its timeout since the time the connection is completed.
            this.oneMoreTime = true;
        }
        this.notify();
    }

    protected synchronized void waitForResponse(long timeoutInMs)
            throws IOException, TimeoutException {
        long current = System.currentTimeMillis();
        this.expireTime = current + timeoutInMs;
        while (!isDone() && current < expireTime) {
            try {
                this.wait(expireTime - current);
                current = System.currentTimeMillis();
                if (!isDone()) {
                    if (connectionInProgress) {
                        // extend the wait if connection is in progress
                        expireTime = current + timeoutInMs;
                    } else {
                        if (oneMoreTime) {
                            oneMoreTime = false;
                            expireTime = current + timeoutInMs;
                        }
                    }
                }
            } catch (InterruptedException e) {
                setCancel();
                break;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        checkError();
        if (current >= expireTime) {
            throw new TimeoutException();
        }
    }

    protected synchronized void waitForResponse() throws IOException {
        while (!isDone()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                setCancel();
                break;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        checkError();
    }

    private void checkError() throws IOException {
        if (error != null) {
            if (error instanceof MadException) {
                // Error logged in lower levels; here we just ignore it
            } else {
                if (error instanceof IOException) {
                    IOException ioe = (IOException) error;
                    throw ioe;
                } else if (error instanceof RuntimeException) {
                    RuntimeException rte = (RuntimeException) error;
                    throw rte;
                } else {
                    throw new IOException(error);
                }
            }
        }
    }

    protected IDatagram<?> wrap(byte[] data, int offset, int totalLength) {
        throw new UnsupportedOperationException();
    }

    private void setCancel() {
        error = new RequestCancelledByUserException();
        canceled = true;
    }

    public void dump(PrintStream out) {
        try {
            getMad().dump("", out);
            dumpResults(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dumpResults(PrintStream out) {
        List<E> results;
        try {
            results = get();
            if (results == null) {
                out.println("null");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    out.println(i + " " + results.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
