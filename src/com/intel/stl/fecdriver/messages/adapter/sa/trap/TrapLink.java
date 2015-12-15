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
 *  File Name: TrapLink.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:48:49  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/01/11 20:04:28  jijunwan
 *  Archive Log:    updated to the latest FM as of 01/05/2015
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:04  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa.trap;

import com.intel.stl.api.notice.TrapLinkBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * <pre>
 * ref:/ALL_EMB/IbAcess/Common/Inc/stl_sm.h v1.115
 * 
 * typedef struct {
 *     uint32      Lid;
 *     uint8       Port;
 * } PACK_SUFFIX STL_TRAP_LINK;
 * #define STL_TRAP_LINK_INTEGRITY_DATA STL_TRAP_LINK
 * #define STL_TRAP_BUFFER_OVERRUN_DATA STL_TRAP_LINK
 * #define STL_TRAP_FLOW_WATCHDOG_DATA STL_TRAP_LINK
 * </pre>
 */
public class TrapLink extends SimpleDatagram<TrapLinkBean> {
    public TrapLink() {
        super(5);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.messages.adapter.SimpleDatagram#toObject()
     */
    @Override
    public TrapLinkBean toObject() {
        buffer.clear();
        TrapLinkBean bean = new TrapLinkBean(buffer.getInt(), buffer.get());
        return bean;
    }

}
