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
package com.intel.stl.ui.common;

import java.awt.Component;

import javax.swing.ImageIcon;

/**
 * @author jijunwan
 * 
 */
public interface IPageController extends IContextAware {

    String getDescription();

    Component getView();

    ImageIcon getIcon();

    /**
     * 
     * Description: final cleanup before we destroy this page
     * 
     */
    void cleanup();

    /**
     * 
     * Description: enter this page
     * 
     */
    void onEnter();

    /**
     * 
     * <i>Description:</i> indicate whether we can exit from this page. Useful
     * when there are unsaved data, such as when there are unsaved user input, a
     * user will decide whether to stay and go to the next page.
     * 
     * @return
     */
    boolean canExit();

    /**
     * 
     * Description: leave this page
     * 
     */
    void onExit();

    /**
     * 
     * <i>Description:</i> refresh this page
     * 
     */
    void onRefresh(IProgressObserver observer);

    /**
     * 
     * Description: Clear subpage sections and deregister tasks
     * 
     */
    void clear();
}
