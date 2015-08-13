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
 *  File Name: SMFailoverManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1.2.1  2015/08/12 15:22:10  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/09 22:49:21  jijunwan
 *  Archive Log:    a new FailoverManager based on the old one
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.STLMessages;
import com.intel.stl.fecdriver.ApplicationEvent;
import com.intel.stl.fecdriver.IFailoverEventListener;
import com.intel.stl.fecdriver.IFailoverManager;

public class SMFailoverManager implements IFailoverManager {
    private final static Logger log = LoggerFactory
            .getLogger(SMFailoverManager.class);

    private final long SM_FAILOVER_TIMEOUT = 5000; // 5 sec * 1000 ms/sec

    private final int SM_FAILOVER_RETRIES = 10;

    private final HostSelector selector;

    /**
     * Description:
     * 
     * @param adapter
     */
    public SMFailoverManager(STLAdapter adapter) {
        super();
        selector = new HostSelector(adapter, SM_FAILOVER_TIMEOUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.fecdriver.IFailoverManager#getFailoverTimeout()
     */
    @Override
    public long getFailoverTimeout() {
        return (SM_FAILOVER_TIMEOUT * SM_FAILOVER_RETRIES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.fecdriver.IFailoverManager#connectionLost(com.intel.stl
     * .fecdriver.impl.STLConnection)
     */
    @Override
    public SubnetDescription connectionLost(final STLConnection conn) {
        System.out.println("FailoverManager - connectionLost called.");
        SubnetDescription subnet = conn.getConnectionDescription();
        List<HostInfo> hosts = subnet.getFEList();

        int count = 0;
        HostInfo next = null;
        SubnetDescription tmp = subnet.copy();
        IFailoverEventListener listener = new IFailoverEventListener() {
            @Override
            public void onFailoverStart(ApplicationEvent event) {
                conn.fireFailoverStart();
            }

            @Override
            public void onFailoverProgress(ApplicationEvent event) {
                conn.fireFailoverProgress(event);
            }

            @Override
            public void onFailoverEnd(ApplicationEvent event) {
                conn.fireFailoverEnd(event.getReason());
            }
        };
        while (count < SM_FAILOVER_RETRIES && next == null) {
            next = selector.getHost(hosts, tmp, listener);
            count += 1;
            listener.onFailoverProgress(new ApplicationEvent("Retry " + count));
        }
        if (next != null) {
            subnet.setCurrentFEIndex(hosts.indexOf(next));
            return subnet;
        } else {
            System.out
                    .println("FailoverManager - Failover unsuccessful. Throw exception.");
            SMFailoverException fe =
                    new SMFailoverException(
                            STLMessages.STL64000_SM_FAILOVER_UNSUCCESSFUL);
            log.error(StringUtils.getErrorMessage(fe), fe);
            throw fe;
        }
    }

}
