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
 *  File Name: IAssistant.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/10 22:41:41  jijunwan
 *  Archive Log:    improved to show progress while we log into a host
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api;

public interface IAssistant {
    /**
     * 
     * <i>Description:</i> block until get user input
     * 
     * @param err
     *            the err we got before
     * @return user selected option. The value is the options defined in
     *         JOptionPane, such as OK_OPTION, CANCEL_OPTION etc.
     */
    int getOption(Throwable err);

    /**
     * 
     * <i>Description:</i> start to show progress
     * 
     */
    void startProgress();

    /**
     * 
     * <i>Description:</i> stop showing progress
     * 
     */
    void stopProgress();

    /**
     * 
     * <i>Description:</i> show a note about current progress
     * 
     * @param note
     *            the text to show
     */
    void reportProgress(String note);

    /**
     * 
     * <i>Description:</i> close the assistant
     * 
     */
    void close();
}
