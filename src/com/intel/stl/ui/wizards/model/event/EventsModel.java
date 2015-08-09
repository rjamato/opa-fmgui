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
 *  File Name: EventsModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/20 21:15:17  rjtierne
 *  Archive Log:    Multinet Wizard: Data models for all wizards for data storage and display
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:29:17  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: Model to store the event wizard settings
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.model.event;

public class EventsModel {

    private EventRulesTableModel eventRulesTableModel;

    /**
     * Description:
     * 
     * @param eventRulesTableModel
     */
    public EventsModel(EventRulesTableModel eventRulesTableModel) {
        this.eventRulesTableModel = eventRulesTableModel;
    }

    public EventRulesTableModel getEventsRulesModel() {
        return eventRulesTableModel;
    }

    public void setEventsRulesModel(EventRulesTableModel eventsRulesModel) {
        this.eventRulesTableModel = eventsRulesModel;
    }

    public void clear() {

        eventRulesTableModel = new EventRulesTableModel();
        eventRulesTableModel.clear();
    }
}
