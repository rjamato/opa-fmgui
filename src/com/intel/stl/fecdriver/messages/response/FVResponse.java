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
package com.intel.stl.fecdriver.messages.response;

// Java imports
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.intel.stl.fecdriver.IResponse;
import com.intel.stl.fecdriver.messages.adapter.IDatagram;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.command.FVMessage;

/**
 * 
 * <br>
 * Copyright (c) 2001, VIEO, Inc. All rights reserved.
 * 
 * @see FVMessage
 * @since JDK 1.3
 * @author Jason Wiseman
 * @version 1.0
 * 
 */
public abstract class FVResponse<E> extends FVMessage implements IResponse<E> {
    private String description;

    private RmppMad rmppMad;

    private Exception error;

    private boolean canceled;

    private List<E> results;

    private long expireTime;

    /**
     */
    public String getDescription() {
        return description;
    }

    /**
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    protected void setResults(List<E> results) {
        this.results = results;
    }

    public synchronized void setError(Exception e) {
        this.error = e;
        this.notify();
    }

    /**
     */
    public synchronized Exception getError() {
        return error;
    }

    public synchronized void processMad(RmppMad mad) {
        try {
            ByteBuffer buffer = mad.getData().getByteBuffers()[0];
            byte[] bytes = buffer.array();
            int offset = buffer.arrayOffset();
            IDatagram<?> datagram = wrap(bytes, offset);
            mad.setData(datagram);
            this.rmppMad = mad;
            if (canceled) {
                error = new RuntimeException("Request cancelled");
            }
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
     * 
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        canceled = true;
        return true;
    }

    /*
     * 
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return canceled;
    }

    /*
     * 
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        return rmppMad != null || error != null || canceled;
    }

    /*
     * 
     * @see java.util.concurrent.Future#get()
     */
    @Override
    public List<E> get() throws ExecutionException {
        if (!isDone()) {
            waitForResponse();
        }
        return results;
    }

    /*
     * 
     * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public List<E> get(long timeout, TimeUnit unit) throws ExecutionException,
            TimeoutException {
        if (!isDone()) {
            waitForResponse(unit.toMillis(timeout));
        }
        return results;
    }

    public void extendWaitTime(long waitExtension) {
        long current = System.currentTimeMillis();
        this.expireTime = current + waitExtension;
    }

    protected synchronized void waitForResponse(long timeoutInMs)
            throws ExecutionException, TimeoutException {
        long current = System.currentTimeMillis();
        this.expireTime = current + timeoutInMs;
        while (!isDone() && current < expireTime) {
            try {
                this.wait(expireTime - current);
                current = System.currentTimeMillis();
            } catch (InterruptedException e) {
                canceled = true;
                break;
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }
        if (current >= expireTime) {
            throw new TimeoutException();
        }
    }

    protected synchronized void waitForResponse() throws ExecutionException {
        while (!isDone()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                canceled = true;
                break;
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }
    }

    protected IDatagram<?> wrap(byte[] data, int offset) {
        throw new UnsupportedOperationException();
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
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
