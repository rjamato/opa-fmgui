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
 *  File Name: PortDownReason.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:48:48  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/06 00:26:59  jijunwan
 *  Archive Log:    added neighbor link down reason to match FM 325
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:29  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 18:36:45  jijunwan
 *  Archive Log:    updated PortInfoRecord, SMInfo to the latest data structure
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.PortDownReasonBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.92
 * 
 * <pre>
 * typedef struct {
 *     uint8 Reserved[6];
 *     uint8 NeighborLinkDownReason;
 *     uint8 LinkDownReason;
 *     uint64 Timestamp;
 * } PACK_SUFFIX STL_LINKDOWN_REASON;
 * </pre>
 */
public class PortDownReason extends SimpleDatagram<PortDownReasonBean> {

    /**
     * Description:
     * 
     * @param length
     */
    public PortDownReason() {
        super(16);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public PortDownReasonBean toObject() {
        buffer.clear();
        buffer.position(6);
        PortDownReasonBean bean =
                new PortDownReasonBean(buffer.get(), buffer.get(),
                        buffer.getLong());
        return bean;
    }

}
