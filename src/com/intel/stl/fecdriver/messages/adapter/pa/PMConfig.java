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
 *  File Name: PMConfig.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 18:19:11  jijunwan
 *  Archive Log:    PR 126331 - PortRecvBECN and PortRecvFECN: Recv should be Rcv
 *  Archive Log:    updated to the latest version stl_pa.h v1.33; stl_pa.h v1.26
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/12 20:01:58  fernande
 *  Archive Log:    New command to get PM Configuration
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.pa;

import com.intel.stl.api.performance.PMConfigBean;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * 
 * ref: /ALL_EMB/IbAccess/Common/Inc/stl_pa.h v1.26
 * 
 * <pre>
 * typedef struct _STL_PA_PM_Cfg_Data {
 *     uint32                  sweepInterval;
 *     uint32                  maxClients;
 *     uint32                  sizeHistory;
 *     uint32                  sizeFreeze;
 *     uint32                  lease;
 *     uint32                  pmFlags;
 *     STL_CONGESTION_WEIGHTS_T congestionWeights;
 *     STL_PM_ERR_THRESHOLDS   errorThresholds;
 *     STL_INTEGRITY_WEIGHTS_T integrityWeights;
 *     uint64                  memoryFootprint;
 *     uint32                  maxAttempts;
 *     uint32                  respTimeout;
 *     uint32                  minRespTimeout;
 *     uint32                  maxParallelNodes;
 *     uint32                  pmaBatchSize;
 *     uint8                   errorClear;
 *     uint8                   reserved[3];
 * } PACK_SUFFIX STL_PA_PM_CFG_DATA;
 * 
 * typedef struct _STL_CONGESTION_WEIGHTS {
 *     uint8                   PortXmitWait;
 *     uint8                   SwPortCongestion;
 *     uint8                   PortRcvFECN;
 *     uint8                   PortRcvBECN;
 *     uint8                   PortXmitTimeCong;
 *     uint8                   PortMarkFECN;
 *     uint16                  reserved;
 * } PACK_SUFFIX STL_CONGESTION_WEIGHTS_T;
 * 
 * typedef struct _STL_PM_ERR_THRESHOLDS {
 *     uint32                  integrityErrors;
 *     uint32                  congestionErrors;
 *     uint32                  smaCongestionErrors;
 *     uint32                  bubbleErrors;
 *     uint32                  securityErrors;
 *     uint32                  routingErrors;
 * } PACK_SUFFIX STL_PM_ERR_THRESHOLDS;
 * 
 * typedef struct _STL_INTEGRITY_WEIGHTS {
 *     uint8                   LocalLinkIntegrityErrors;
 *     uint8                   PortRcvErrors;
 *     uint8                   ExcessiveBufferOverruns;
 *     uint8                   LinkErrorRecovery;
 *     uint8                   LinkDowned;
 *     uint8                   UncorrectableErrors;
 *     uint8                   FMConfigErrors;
 *     uint8                   reserved;
 * } PACK_SUFFIX STL_INTEGRITY_WEIGHTS_T;
 * 
 * </pre>
 */

public class PMConfig extends SimpleDatagram<PMConfigBean> {

    public PMConfig() {
        super(96);
    }

    @Override
    public PMConfigBean toObject() {
        buffer.clear();
        PMConfigBean bean = new PMConfigBean();
        bean.setSweepInterval(buffer.getInt());
        bean.setMaxClients(buffer.getInt());
        bean.setSizeHistory(buffer.getInt());
        bean.setSizeFreeze(buffer.getInt());
        bean.setLease(buffer.getInt());
        bean.setPmFlags(buffer.getInt());
        //
        bean.setPortXmitWait(buffer.get());
        bean.setSwPortCongestion(buffer.get());
        bean.setPortRcvFECN(buffer.get());
        bean.setPortRcvBECN(buffer.get());
        bean.setPortXmitTimeCong(buffer.get());
        bean.setPortMarkFECN(buffer.get());
        // Reserved
        buffer.getShort();
        //
        bean.setIntegrityErrors(buffer.getInt());
        bean.setCongestionErrors(buffer.getInt());
        bean.setSmaCongestionErrors(buffer.getInt());
        bean.setBubbleErrors(buffer.getInt());
        bean.setSecurityErrors(buffer.getInt());
        bean.setRoutingErrors(buffer.getInt());
        //
        bean.setLocalLinkIntegrityErrors(buffer.get());
        bean.setPortRcvErrors(buffer.get());
        bean.setExcessiveBufferOverruns(buffer.get());
        bean.setLinkErrorRecovery(buffer.get());
        bean.setLinkDowned(buffer.get());
        bean.setUncorrectableErrors(buffer.get());
        bean.setFmConfigErrors(buffer.get());
        // Reserved
        buffer.get();
        bean.setMemoryFootprint(buffer.getLong());
        bean.setMaxAttempts(buffer.getInt());
        bean.setResponseTimeout(buffer.getInt());
        bean.setMinResponseTimeout(buffer.getInt());
        bean.setMaxParallelNodes(buffer.getInt());
        bean.setPmaBatchSize(buffer.getInt());
        bean.setErrorClear(buffer.get());
        // Reserved bytes
        buffer.get();
        buffer.get();
        buffer.get();
        return bean;
    }

}
