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
 *  File Name: ImageInfoSubscriber.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2016/02/09 20:23:09  jijunwan
 *  Archive Log:    PR 132575 - [PSC] Null pointer message in FM GUI
 *  Archive Log:
 *  Archive Log:    - some minor improvements
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/17 18:53:39  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/16 14:41:26  jijunwan
 *  Archive Log:    renamed DevieGroup to DefaultDeviceGroup because it's an enum of default DGs, plus we need to use DeviceGroup for the DG definition used in opafm.xml
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/12 19:40:07  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/06 20:49:35  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/03 21:12:32  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/02 15:36:15  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Subscriber class to schedule tasks for collecting image info beans
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.publisher.subscriber;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.PAConstants;
import com.intel.stl.api.subnet.DefaultDeviceGroup;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.Task;

public class ImageInfoSubscriber extends Subscriber<ImageInfoBean> {

    private static Logger log =
            LoggerFactory.getLogger(ImageInfoSubscriber.class);

    public ImageInfoSubscriber(IRegisterTask taskScheduler,
            IPerformanceApi perfApi) {
        super(taskScheduler, perfApi);
    }

    public synchronized Task<ImageInfoBean> registerImageInfo(
            ICallback<ImageInfoBean> callback) {
        Task<ImageInfoBean> task = new Task<ImageInfoBean>(
                PAConstants.STL_PA_ATTRID_GET_IMAGE_INFO,
                DefaultDeviceGroup.ALL.name(), UILabels.STL40007_IMAGEINFO_TASK
                        .getDescription(DefaultDeviceGroup.ALL.name()));
        Callable<ImageInfoBean> caller = new Callable<ImageInfoBean>() {
            @Override
            public ImageInfoBean call() throws Exception {
                ImageInfoBean imageInfo = perfApi.getLatestImageInfo();
                return imageInfo;
            }
        };
        try {
            Task<ImageInfoBean> submittedTask = taskScheduler
                    .scheduleTask(taskList, task, callback, caller);
            return submittedTask;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public synchronized void deregisterImageInfo(Task<ImageInfoBean> task,
            ICallback<ImageInfoBean> callback) {
        try {
            taskScheduler.removeTask(taskList, task, callback);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
