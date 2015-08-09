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
package com.intel.stl.fecdriver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import com.intel.stl.api.notice.IEventListener;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.subnet.SubnetDescription;

/**
 * Similar to java.sql.Connection
 * 
 * @see java.sql.Connection
 * @author jijunwan
 * 
 */
public interface IConnection {

    void setClientInfo(Properties properties);

    void setClientInfo(String name, String value);

    Properties getClientInfo();

    InetAddress getInetAddress();

    String getClientInfo(String name);

    SubnetDescription getConnectionDescription();

    boolean waitForConnect() throws IOException;

    IStatement<?> createStatement();

    boolean isClosed();

    void close();

    void addConnectionEventListener(IConnectionEventListener listener);

    void removeConnectionEventListener(IConnectionEventListener listener);

    void addNoticeListener(IEventListener<NoticeBean> listener);

    void removeNoticeListener(IEventListener<NoticeBean> listener);

    void addApplicationEventListener(IApplicationEventListener listener);

    void removeApplicationEventListener(IApplicationEventListener listener);

    void addFailoverEventListener(IFailoverEventListener subnetContextImpl);

    void removeFailoverEventListener(IFailoverEventListener listener);

}
