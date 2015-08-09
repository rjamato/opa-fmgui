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
 *  File Name: PerformanceDAO.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/06 15:04:21  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 21:19:16  fernande
 *  Archive Log:    Adding unit test for PerformanceApi and fixes for some issues found.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/12 20:00:07  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine;

import java.util.List;

import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.PerformanceDataNotFoundException;
import com.intel.stl.datamanager.SubnetRecord;

public interface PerformanceDAO {

    void saveImageInfos(SubnetRecord subnet, List<ImageInfoBean> imageInfo);

    List<ImageInfoBean> getImageInfo(SubnetRecord subnet, long imageNumber)
            throws PerformanceDataNotFoundException;

    ImageInfoBean getLastImageInfo(SubnetRecord subnet)
            throws PerformanceDataNotFoundException;
}
