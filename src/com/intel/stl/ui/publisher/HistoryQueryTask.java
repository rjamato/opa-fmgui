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
 *  File Name: HistoryQueryTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/02 15:28:07  jypak
 *  Archive Log:    History query has been done with current live image ID '0' which isn't correct. Updates here are:
 *  Archive Log:    1. Get the image ID from current image.
 *  Archive Log:    2. History queries are done with this image ID.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/12 19:40:04  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/10 21:25:59  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.publisher;

import java.util.concurrent.Future;

import com.intel.stl.api.ITimestamped;
import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.model.HistoryType;

public abstract class HistoryQueryTask<E extends ITimestamped> extends
        CancellableCall<Void> {
    private final double step;

    private final int maxDataPoints;

    private final int lengthInSeconds;

    private final ICallback<E[]> callback;

    /**
     * Description:
     * 
     * @param step
     */
    public HistoryQueryTask(double step, int refreshRate, HistoryType type,
            ICallback<E[]> callback) {
        super();
        this.step = step;
        this.maxDataPoints = type.getMaxDataPoints(refreshRate);
        this.lengthInSeconds = type.getLengthInSeconds();
        this.callback = callback;
    }

    public void setFuture(final Future<Void> future) {
        setCancelIndicator(new ICancelIndicator() {
            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.publisher.CancellableCall#call(com.intel.stl.ui.common
     * .ICancelIndicator)
     */
    @Override
    public Void call(ICancelIndicator cancelIndicator) throws Exception {
        long firstTime = -1;

        ImageIdBean[] imageIdBeans = queryImageId();
        int imageIdBeanLength = imageIdBeans.length;
        long[] imageIds = new long[imageIdBeanLength];
        for (int j = 0; j < imageIdBeanLength; j++) {
            imageIds[j] = imageIdBeans[j].getImageNumber();
        }

        for (int i = 0; i < maxDataPoints && !cancelIndicator.isCancelled(); i++) {
            int offset = (int) ((-i - 1) * step);
            E[] datapoints = queryHistory(imageIds, offset);
            if (datapoints != null && datapoints.length > 0) {
                E datapoint = datapoints[0];
                long time = datapoint.getTimestamp();
                if (firstTime == -1) {
                    firstTime = time;
                } else if (firstTime - time > lengthInSeconds) {
                    break;
                }
                callback.onDone(datapoints);
            } else {
                break;
            }
        }
        return null;
    }

    protected abstract ImageIdBean[] queryImageId();

    protected abstract E[] queryHistory(long[] imageIDs, int offset);
}
