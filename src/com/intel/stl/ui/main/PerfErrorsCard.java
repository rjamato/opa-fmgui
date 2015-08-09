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
 *  File Name: PerErrorsCard.java
 *  
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/25 13:57:43  jypak
 *  Archive Log:    Correct comment header
 *  Archive Log:
 *
 *  Overview: Performance page's performance subpage errors section view.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.util.Collection;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.configuration.view.PropertyVizStyle;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.PerfErrorsCardView;
import com.intel.stl.ui.main.view.PerfErrorsItem;

public class PerfErrorsCard extends
        BaseCardController<ICardListener, PerfErrorsCardView> {

    public PerfErrorsCard(PerfErrorsCardView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
    }

    public PerfErrorsCard(PerfErrorsCardView view,
            MBassador<IAppEvent> eventBus, Collection<PerfErrorsItem> itemList) {
        super(view, eventBus);
        view.initializeErrorsItems(itemList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public ICardListener getCardListener() {
        return this;
    }

    /**
     * Description:
     * 
     * @param values
     */
    public void updateErrorsItems(Collection<PerfErrorsItem> values) {
        view.updateErrorsItems(values);
    }

    public void setStyle(PropertyVizStyle style) {
        view.setStyle(style);
    }

}
