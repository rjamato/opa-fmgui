/**
 * Copyright (c) 2016, Intel Corporation
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
 *  File Name: StartupProgressObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2016/01/26 18:35:32  fernande
 *  Archive Log:    PR 132387 - [Dell]: FMGUI Fails to Open Due to Database Lock. The application was compacting the database on every application exit; changed the scheduling to compact the database after a purge of performance data is done; and to always display a splash screen when shutting down the application so the user gets feedback of the application shutdown process.
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api;

public interface StartupProgressObserver {

    /**
     *
     * <i>Description:</i>sets the progress value. This method is used by the
     * back end code to inform the front end code about the progress
     * initializing back end components.
     *
     * @param progress
     *            the progress as a percentage value from 0 to 100
     */
    void setProgress(int progress);

    /**
     *
     * <i>Description:</i>shows a progress message. Implementors should display
     * the message to inform the user about initialization progress in the back
     * end code.
     *
     * @param message
     *            a progress message for the end user
     */
    void setProgress(String message);

    void setProgress(String message, int progress);

}
