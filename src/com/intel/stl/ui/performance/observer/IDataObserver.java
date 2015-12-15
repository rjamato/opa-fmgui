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
 *  File Name: IDataObserver.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/08/17 18:54:20  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/06/25 20:42:16  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - improved PerformanceItem to support port counters
 *  Archive Log:    - improved PerformanceItem to use generic ISource to describe data source
 *  Archive Log:    - improved PerformanceItem to use enum DataProviderName to describe data provider name
 *  Archive Log:    - improved PerformanceItem to support creating a copy of PerformanceItem
 *  Archive Log:    - improved TrendItem to share scale with other charts
 *  Archive Log:    - improved SimpleDataProvider to support hsitory data
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

import com.intel.stl.ui.model.DataType;

public interface IDataObserver<E> {
    void processData(E data);

    void setType(DataType type);

    DataType getType();

    /**
     * 
     * <i>Description:</i> reset the observer. useful when data process involves
     * history data. Should call this method before we change history context,
     * such as change history type
     * 
     */
    void reset();
}
