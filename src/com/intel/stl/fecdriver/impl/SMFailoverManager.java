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
 *  File Name: SMFailoverManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
