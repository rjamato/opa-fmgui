/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
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

    private static Logger log = LoggerFactory
            .getLogger(ImageInfoSubscriber.class);

    public ImageInfoSubscriber(IRegisterTask taskScheduler,
            IPerformanceApi perfApi) {
        super(taskScheduler, perfApi);
    }

    public synchronized Task<ImageInfoBean> registerImageInfo(
            ICallback<ImageInfoBean> callback) {
        Task<ImageInfoBean> task =
                new Task<ImageInfoBean>(
                        PAConstants.STL_PA_ATTRID_GET_IMAGE_INFO,
                        DefaultDeviceGroup.ALL.name(),
                        UILabels.STL40007_IMAGEINFO_TASK
                                .getDescription(DefaultDeviceGroup.ALL.name()));
        Callable<ImageInfoBean> caller = new Callable<ImageInfoBean>() {
            @Override
            public ImageInfoBean call() throws Exception {
                ImageInfoBean imageInfo = perfApi.getLatestImageInfo();
                return imageInfo;
            }
        };
        try {
            Task<ImageInfoBean> submittedTask =
                    taskScheduler
                            .scheduleTask(taskList, task, callback, caller);
            return submittedTask;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void deregisterImageInfo(Task<ImageInfoBean> task,
            ICallback<ImageInfoBean> callback) {
        try {
            taskScheduler.removeTask(taskList, task, callback);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
