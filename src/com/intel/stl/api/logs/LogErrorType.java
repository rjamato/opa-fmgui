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
 *  File Name: LogErrorType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/10/06 15:50:25  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Changed INVALID_LOG_USER to ESM_NOT_SUPPORTED
 *  Archive Log:    - Added UNEXPECTED_LOGIN_FAILURE to display an error if an exception occurs during initializationTask()
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/09/29 15:30:14  rjtierne
 *  Archive Log:    PR 130332 - windows FM GUI - Admin-Logs - when logging in it displays error message about NULL log
 *  Archive Log:    - Removed LOG_FILE_NOT_FOUND_USING_DEFAULT error
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/09/25 13:53:24  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Added new INVALID_LOG_USER error to be displayed when a non-root user tries to log in
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
 *  Overview: Types of log errors
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.api.logs;

import java.util.HashMap;
import java.util.Map;

public enum LogErrorType {

    LOG_OK((byte) 0),
    LOG_FILE_NOT_FOUND((byte) 1),
    RESPONSE_TIMEOUT((byte) 2),
    SSH_HOST_CONNECT_ERROR((byte) 3),
    INVALID_LOG_USER((byte) 4),
    ESM_NOT_SUPPORTED((byte) 5),
    UNEXPECTED_LOGIN_FAILURE((byte) 6);

    private static final Map<Byte, LogErrorType> logErrorMap =
            new HashMap<Byte, LogErrorType>() {
                private static final long serialVersionUID = 1L;

                {
                    for (LogErrorType type : LogErrorType.values()) {
                        put(type.id, type);
                    }
                }
            };

    private final byte id;

    private LogErrorType(byte id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public byte getId() {
        return id;
    }

    public static LogErrorType getLogErrorType(byte id) {
        return logErrorMap.get(id);
    }

}
