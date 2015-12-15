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
 *  File Name: LogCommandProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/10/06 15:50:12  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    Set processingThreadRunning to false if an exception occurs in getCommandProcessingTask() so the thread
 *  Archive Log:    is killed on error.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/09/25 13:53:11  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    Code clean up
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:48:54  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/17 14:22:52  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *
 *  Overview: Single thread task to send commands to the remote SSH server, 
 *  wait for the responses, and send them to the listeners   
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.api.logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.common.STLMessages;
import com.intel.stl.fecdriver.network.ssh.impl.JSchSession;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

public class LogCommandProcessor {
    private static Logger log = LoggerFactory
            .getLogger(LogCommandProcessor.class);

    private final boolean DEBUG = false;

    private final boolean DEBUG_COMMANDS = false;

    private static int QUEUE_SIZE = 100;

    private final ExecutorService service;

    private final BlockingQueue<LogCommand> cmdQueue;

    private final Future<?> future;

    private boolean processingThreadRunning = false;

    private final JSchSession jschSession;

    private boolean initialized;

    private ChannelExec execChannel;

    private BufferedReader inputBuffer = null;

    private LogResponse response;

    private IResponseListener responseListener;

    private ILogErrorListener errorListener;

    private LogMessageType msgType;

    private long timeoutInMs;

    private final String identifier;

    private final LogCommandProcessor cmdProc = this;

    public LogCommandProcessor(JSchSession jschSession, int timeoutInMs,
            String id) {
        super();
        identifier = id + ":commandProcessingTask";
        service = Executors.newSingleThreadExecutor(new LogThreadFactory(id));
        cmdQueue = new LinkedBlockingQueue<LogCommand>(QUEUE_SIZE);
        this.jschSession = jschSession;
        this.timeoutInMs = timeoutInMs;

        Runnable task = getCommandProcessingTask();
        future = service.submit(task);
    }

    public void setResponseListener(IResponseListener listener) {
        responseListener = listener;
    }

    public void setErrorListener(ILogErrorListener listener) {
        errorListener = listener;
    }

    public void cancelTask() {
        future.cancel(true);
    }

    public void setTimeoutInMs(long timeoutMs) {
        this.timeoutInMs = timeoutMs;
    }

    protected boolean initializeChannel() throws JSchException, IOException {

        boolean initialized = false;

        execChannel = jschSession.getExecChannel();
        inputBuffer =
                new BufferedReader(new InputStreamReader(
                        execChannel.getInputStream()));
        execChannel.setErrStream(System.err);

        // Wait for the channel to be initialized
        long currentTime = System.currentTimeMillis();
        long expiredTime = currentTime + timeoutInMs;
        while (execChannel.isClosed() && !execChannel.isConnected()
                && (currentTime < expiredTime)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            currentTime = System.currentTimeMillis();
        }
        initialized = !execChannel.isClosed();

        return initialized;
    }

    public LogResponse getResponse() {
        return response;
    }

    protected String getLine() {

        String inputLine = null;

        try {
            inputLine = inputBuffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputLine;
    }

    protected boolean inputReady() {

        boolean ready = false;

        try {
            long currentTime = System.currentTimeMillis();
            long expireTime = currentTime + timeoutInMs;

            // Wait for either the end of the data, data to be available on the
            // input stream, or the timeout to expire
            while (!(ready = inputBuffer.ready()) && (currentTime < expireTime)) {
                Thread.sleep(200);
                currentTime = System.currentTimeMillis();
            }
        } catch (InterruptedException e) {
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        if (DEBUG) {
            if (!ready) {
                debug("!Received:", "Response Timeout!");
            }
        }

        return ready;
    }

    public void submitCommand(LogMessageType msgType, String cmd) {
        try {
            cmdQueue.put(new LogCommand(msgType, cmd));
        } catch (InterruptedException e) {
            log.error(e.getCause().getMessage());
        }
    }

    protected void waitForResponse() {

        boolean done = false;
        String entry = new String("");
        response = new LogResponse(msgType);

        // Process lines until the EOF is encountered or timeout
        while (!done && inputReady()) {
            entry = getLine();
            if (entry != null) {
                done = entry.equals(LogCommander.RESPONSE_EOM);
            }

            if (!done) {
                response.addEntry(entry);
            }

            if (DEBUG) {
                debug("Receive:", entry);
            }
        }

        // DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        if (response.getEntries().size() > 0) {
            responseListener.onResponseReceived(response);
            if (DEBUG_COMMANDS) {
                debug("Receive:", response.getEntries().get((0)));
            }
        } else {
            if (DEBUG_COMMANDS) {
                debug("Receive:", "Response Timeout!");
            }
            responseListener.onResponseError(LogErrorType.RESPONSE_TIMEOUT,
                    response.getMsgType());
        }
    }

    public void shutdown() {
        if (future != null) {
            future.cancel(true);
        }

        processingThreadRunning = false;

        service.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
                service.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.warn("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            service.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        try {
            execChannel.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void debug(String... msgs) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        System.out.println(formatter.format(new Date()) + ": " + identifier
                + ": " + msgs[0] + " " + msgs[1]);
    }

    protected Runnable getCommandProcessingTask() {
        Runnable task = new Runnable() {

            @Override
            public void run() {
                while (processingThreadRunning) {
                    try {
                        // Get a command from the queue
                        LogCommand logMsg = cmdQueue.take();
                        cmdProc.msgType = logMsg.getMsgType();

                        if (DEBUG) {
                            debug("Sending:", logMsg.getMsgType().toString());
                        }

                        // Set up the channel if needed
                        if ((execChannel == null) || (execChannel.isClosed())) {
                            initialized = initializeChannel();
                        }

                        // Process command or exit
                        if (!logMsg.getMsgType().equals(LogMessageType.EXIT)) {

                            // Send the command
                            if (initialized) {
                                execChannel.setCommand(logMsg.getCommand());
                                execChannel.connect();
                                execChannel.getOutputStream().flush();

                                if (DEBUG_COMMANDS) {
                                    debug("Sending:", logMsg.getCommand());
                                }
                            }

                            // Get the response
                            waitForResponse();
                        } else {
                            // Exit the thread
                            processingThreadRunning = false;
                        }
                    } catch (InterruptedException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                        errorListener
                                .onSessionDown(STLMessages.STL61019_SSH_CONNECTION_FAILURE
                                        .getDescription());
                        errorListener.stopLog();
                        processingThreadRunning = false;
                    }
                }
            }
        };

        processingThreadRunning = true;
        return task;
    }
}
