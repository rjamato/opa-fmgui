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
 *  File Name: LogCommander.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/11/19 12:51:15  rjtierne
 *  Archive Log:    PR 130965 - ESM support on Log Viewer
 *  Archive Log:    - Restore the original command syntax in getNumLines()
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/11/18 23:49:12  rjtierne
 *  Archive Log:    PR 130965 - ESM support on Log Viewer
 *  Archive Log:    - Added new command to check if the user has permission to access the log file
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/09/25 13:53:00  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Fixed incorrect commanding for getPreviousPage()
 *  Archive Log:    - Set the starting/ending line numbers in getEndOfFile()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/18 21:31:34  jijunwan
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    - checked in Rick's code that fixed currentLine calculation issues
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
 *  Overview: The LogCommander creates the command script to be sent to the
 *  remote SSH server
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.api.logs;

public class LogCommander {

    public static final String RESPONSE_EOM = "intel-inside";

    private boolean firstTime = true;

    private ILogPageListener logPageListener;

    private FileInfoBean fileInfo;

    public LogCommander(FileInfoBean fileInfo) {
        this.fileInfo = fileInfo;
    }

    public void setPageMonitorListener(ILogPageListener listener) {
        logPageListener = listener;
    }

    public FileInfoBean getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoBean fileInfo) {
        this.fileInfo = fileInfo;
    }

    public synchronized String getCommand(LogMessageType cmdType,
            long numLinesRequested) {

        String cmdStr = null;

        switch (cmdType) {

            case CHECK_FOR_FILE:
                cmdStr = checkForFile(fileInfo);
                break;

            case CHECK_FILE_ACCESS:
                cmdStr = checkFileAccess(fileInfo);
                break;

            case PREVIOUS_PAGE:
                cmdStr = getPreviousPage(fileInfo, numLinesRequested);
                break;

            case NEXT_PAGE:
                cmdStr = getNextPage(numLinesRequested, fileInfo);
                break;

            case NUM_LINES:
                cmdStr = getNumLines(fileInfo);
                break;

            case LAST_LINES:
                cmdStr = getEndOfFile(fileInfo, numLinesRequested);
                break;

            case FILE_SIZE:
                cmdStr = getFileSize(fileInfo);
                break;

            default:
                break;
        }

        cmdStr += "; echo " + RESPONSE_EOM;
        return cmdStr;
    }

    protected synchronized String checkForFile(FileInfoBean fileInfo) {

        String cmdStr = null;

        cmdStr = "ls " + fileInfo.getFileName();

        return cmdStr;
    }

    protected synchronized String checkFileAccess(FileInfoBean fileInfo) {

        String cmdStr = null;

        cmdStr =
                "tail " + fileInfo.getFileName()
                        + " > /dev/null > 2&>1; echo $?";

        return cmdStr;
    }

    protected synchronized String getPreviousPage(FileInfoBean fileInfo,
            long numLinesRequested) {

        String cmdStr = null;
        String fileName = fileInfo.getFileName();
        long currentLine = fileInfo.getCurrentLine();
        long newCurrentLine =
                (currentLine >= numLinesRequested) ? (currentLine - numLinesRequested)
                        : 1;

        boolean topOfFile = ((currentLine - 1) < numLinesRequested);
        long endLine = topOfFile ? numLinesRequested : (currentLine - 1);
        if (topOfFile) {
            cmdStr = "head -n " + endLine + " " + fileName;
        } else {
            cmdStr =
                    "head -n " + endLine + " " + fileName + " | tail -n "
                            + numLinesRequested;
        }
        fileInfo.setCurrentLine(newCurrentLine);

        if (newCurrentLine > 0) {
            logPageListener.setStartLine(newCurrentLine);
            logPageListener.setEndLine(endLine);
        }

        if (fileInfo.getCurrentLine() == 1) {
            logPageListener.setFirstPage(true);
            logPageListener.setLastPage(false);
        } else {
            logPageListener.setFirstPage(false);
            logPageListener.setLastPage(false);
        }

        return cmdStr;
    }

    protected synchronized String getNextPage(long numLinesRequested,
            FileInfoBean fileInfo) {

        String cmdStr = null;
        String fileName = fileInfo.getFileName();
        long currentLine = fileInfo.getCurrentLine();

        long newCurrentLine = currentLine + numLinesRequested;
        cmdStr =
                "head -n " + (newCurrentLine + numLinesRequested - 1) + " "
                        + fileName + " | tail -n " + numLinesRequested;
        fileInfo.setCurrentLine(newCurrentLine);

        if (newCurrentLine > 0) {
            logPageListener.setStartLine(newCurrentLine);
            logPageListener.setEndLine(newCurrentLine + numLinesRequested - 1);
        }

        if (fileInfo.getCurrentLine() >= fileInfo.getTotalNumLines()
                - numLinesRequested + 1) {
            logPageListener.setFirstPage(false);
            logPageListener.setLastPage(true);
        } else {
            logPageListener.setFirstPage(false);
            logPageListener.setLastPage(false);
        }

        return cmdStr;
    }

    protected synchronized String getNumLines(FileInfoBean fileInfo) {

        String cmdStr = null;
        String fileName = fileInfo.getFileName();
        long oldSize = fileInfo.getFileSize();

        String newSize =
                "newSize=`ls -l " + fileName + " | awk '{print $5}'`; ";
        String diffSize =
                "diffSize=$(echo `expr \"$newSize\" - " + oldSize + "`); ";
        String numLines = "tail -c $diffSize " + fileName + " | wc -l";

        if (firstTime) {
            // String fileSize = "ls -l " + fileName + " | awk '{print $5}'";
            String fileSize = "ls -l " + fileName + " | awk '{print $5}'";
            cmdStr = "tail -c " + fileSize + " " + fileName + " | wc -l";
            firstTime = false;
        } else {
            cmdStr = newSize + diffSize + numLines;
        }

        return cmdStr;
    }

    protected synchronized String getEndOfFile(FileInfoBean fileInfo,
            long numLinesRequested) {

        String cmdStr = null;
        long totalNumLines = fileInfo.getTotalNumLines();
        String fileName = fileInfo.getFileName();

        long newCurrentLine;
        if (totalNumLines < numLinesRequested) {
            newCurrentLine = 1;
        } else {
            newCurrentLine =
                    (fileInfo.getCurrentLine() <= 0) ? totalNumLines
                            : (totalNumLines - numLinesRequested + 1);
        }

        cmdStr = "tail -n " + numLinesRequested + " " + fileName;
        fileInfo.setCurrentLine(newCurrentLine);

        if (totalNumLines > 0) {
            logPageListener.setStartLine(totalNumLines - numLinesRequested + 1);
            logPageListener.setEndLine(totalNumLines);
        }

        if (numLinesRequested >= totalNumLines) {
            logPageListener.setFirstPage(true);
            logPageListener.setLastPage(true);
        } else {
            logPageListener.setFirstPage(false);
            logPageListener.setLastPage(true);
        }

        return cmdStr;
    }

    protected synchronized String getFileSize(FileInfoBean fileInfo) {

        String fileName = fileInfo.getFileName();

        return "ls -l " + fileName + " | awk '{print $5}'";
    }
}
