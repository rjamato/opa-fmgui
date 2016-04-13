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

package com.intel.stl.fecdriver;

import java.util.Properties;

import com.intel.stl.common.Constants;
import com.intel.stl.fecdriver.impl.STLAdapter;

public class FEResources {
    private final static FEResourceAdapter _adapter = STLAdapter.instance();

    public static FEResourceAdapter getUnpooledResourceAdapter(String pHost,
            int pPort, Properties info) {
        int protocol = getProtocolVersion(pHost, pPort, info);
        if (protocol == Constants.PROTOCAL_VERSION) {
            return _adapter;
        } else {
            throw new IllegalArgumentException("Unsupported protocol "
                    + protocol);
        }
    }

    public static FEResourceAdapter getUnpooledResourceAdapter(String pHost,
            int pPort, String user, String password) {
        int protocol = getProtocolVersion(pHost, pPort, user, password);
        if (protocol == Constants.PROTOCAL_VERSION) {
            return _adapter;
        } else {
            throw new IllegalArgumentException("Unsupported protocol "
                    + protocol);
        }
    }

    public static FEResourceAdapter getUnpooledResourceAdapter(int protocol) {
        if (protocol == Constants.PROTOCAL_VERSION) {
            return _adapter;
        } else {
            throw new IllegalArgumentException("Unsupported protocol "
                    + protocol);
        }
    }

    public static FEResourceAdapter getPooledResourceAdapter(
            FEResourceAdapter adapter) {
        // TODO: create pooled adapter from a unpooled adapter
        return adapter;
    }

    private static int getProtocolVersion(String pHost, int pPort,
            Properties info) {
        // TODO: right now we can not get supported protocol version from FE.
        // Should do it later when FE is ready.
        return Constants.PROTOCAL_VERSION;
    }

    private static int getProtocolVersion(String pHost, int pPort, String user,
            String password) {
        // TODO: right now we can not get supported protocol version from FE.
        // Should do it later when FE is ready.
        return Constants.PROTOCAL_VERSION;
    }
}
