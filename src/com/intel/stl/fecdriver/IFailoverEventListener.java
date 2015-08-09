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
 *  File Name: IFailoverListner.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/31 16:15:27  fernande
 *  Archive Log:    Failover support. Adding interfaces and implementations to display in the UI the failover progress.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/27 20:44:43  fernande
 *  Archive Log:    Added failover support interfaces
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/23 15:19:22  robertja
 *  Archive Log:    Correct file header for CVS.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: robertja
 *
 ******************************************************************************/

package com.intel.stl.fecdriver;

/**
 * @author robertja
 * 
 */
public interface IFailoverEventListener {
    void onFailoverStart(ApplicationEvent event);

    void onFailoverProgress(ApplicationEvent event);

    void onFailoverEnd(ApplicationEvent event);
}
