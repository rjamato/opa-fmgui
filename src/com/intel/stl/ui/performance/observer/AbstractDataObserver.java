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
 *  File Name: UtilStatsObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:27:11  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
