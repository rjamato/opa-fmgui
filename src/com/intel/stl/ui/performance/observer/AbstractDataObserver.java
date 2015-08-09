/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
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
 *  File Name: UtilStatsObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/12 19:40:12  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/11 21:15:00  jypak
 *  Archive Log:    1. For 'current' history scope, default max data points need to be set.
 *  Archive Log:    2. History icon fixed.
 *  Archive Log:    3. Home Page performance section trend charts should show history scope selections.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/03 21:13:49  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/21 17:30:42  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/16 15:08:59  jijunwan
 *  Archive Log:    new framework for performance data visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.performance.observer;

import com.intel.stl.api.performance.ErrStatBean;
import com.intel.stl.api.performance.GroupInfoBean;
import com.intel.stl.api.performance.UtilStatsBean;
import com.intel.stl.api.performance.VFInfoBean;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.performance.item.IPerformanceItem;

public abstract class AbstractDataObserver<E, I extends IPerformanceItem>
        implements IDataObserver<E> {
    protected I controller;

    protected volatile DataType type;

    public AbstractDataObserver(I controller) {
        this(controller, DataType.ALL);
    }

    /**
     * Description:
     * 
     * @param controller
     * @param type
     */
    public AbstractDataObserver(I controller, DataType type) {
        super();
        this.controller = controller;
        this.type = type;
    }

    /**
     * @param type
     *            the type to set
     */
    @Override
    public void setType(DataType type) {
        this.type = type;
    }

    protected UtilStatsBean[] getUtilStatsBeans(GroupInfoBean bean,
            DataType type) {
        switch (type) {
            case INTERNAL:
                return new UtilStatsBean[] { bean.getInternalUtilStats() };
            case TRANSMIT:
                return new UtilStatsBean[] { bean.getSendUtilStats() };
            case RECEIVE:
                return new UtilStatsBean[] { bean.getRecvUtilStats() };
            case EXTERNAL:
                return new UtilStatsBean[] { bean.getSendUtilStats(),
                        bean.getRecvUtilStats() };
            case ALL:
                return new UtilStatsBean[] { bean.getInternalUtilStats(),
                        bean.getSendUtilStats(), bean.getRecvUtilStats() };
        }
        throw new UnsupportedOperationException("Unknown Type " + type);
    }

    protected ErrStatBean[] getErrStatBeans(GroupInfoBean bean, DataType type) {
        switch (type) {
            case INTERNAL:
                return new ErrStatBean[] { bean.getInternalErrors() };
            case EXTERNAL:
                return new ErrStatBean[] { bean.getExternalErrors() };
            case ALL:
                return new ErrStatBean[] { bean.getInternalErrors(),
                        bean.getExternalErrors() };
            case TRANSMIT:
            case RECEIVE:
                throw new IllegalArgumentException("Unsupported Type " + type);
        }
        throw new UnsupportedOperationException("Unknown Type " + type);
    }

    protected UtilStatsBean[] getUtilStatsBeans(VFInfoBean bean, DataType type) {
        switch (type) {
            case ALL:
            case INTERNAL:
                return new UtilStatsBean[] { bean.getInternalUtilStats() };
            case EXTERNAL:
            case TRANSMIT:
            case RECEIVE:
                throw new IllegalArgumentException("Unsupported Type " + type);
        }
        throw new UnsupportedOperationException("Unknown Type " + type);
    }

    protected ErrStatBean[] getErrStatBeans(VFInfoBean bean, DataType type) {
        switch (type) {
            case INTERNAL:
            case ALL:
                return new ErrStatBean[] { bean.getInternalErrors() };
            case TRANSMIT:
            case RECEIVE:
            case EXTERNAL:
                throw new IllegalArgumentException("Unsupported Type " + type);
        }
        throw new UnsupportedOperationException("Unknown Type " + type);
    }
}
