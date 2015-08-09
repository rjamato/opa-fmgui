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
 *  File Name: MultinetWizardModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/02 13:32:56  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/25 17:59:52  rjtierne
 *  Archive Log:    - Implemented subnet deletion
 *  Archive Log:    - Using new WizardType enumeration to specify model to update to improve efficiency
 *  Archive Log:    - Simplified method onClose()
 *  Archive Log:    - Standardized warning dialogue into one showWarningDialog()
 *  Archive Log:    - Added null pointer protection
 *  Archive Log:    - Removed button redundancy in maps
 *  Archive Log:    - Using current subnet to select the correct subnet sub-wizards to display
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/20 21:15:15  rjtierne
 *  Archive Log:    Multinet Wizard: Data models for all wizards for data storage and display
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/13 21:29:15  rjtierne
 *  Archive Log:    Multinet Wizard: Initial Version
 *  Archive Log:
 *
 *  Overview: Top model use to store the sub-wizard models and update their views
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.intel.stl.ui.wizards.impl.WizardType;
import com.intel.stl.ui.wizards.model.event.EventsModel;
import com.intel.stl.ui.wizards.model.preferences.PreferencesModel;
import com.intel.stl.ui.wizards.model.subnet.SubnetModel;

public class MultinetWizardModel implements IWizardModel {

    private SubnetModel subnetModel;

    private EventsModel eventsModel;

    private PreferencesModel preferencesModel;

    private final List<IModelChangeListener<IWizardModel>> listeners =
            new ArrayList<IModelChangeListener<IWizardModel>>();

    private final Map<WizardType, IModelChangeListener<IWizardModel>> listenerMap =
            new LinkedHashMap<WizardType, IModelChangeListener<IWizardModel>>();

    public SubnetModel getSubnetModel() {
        return subnetModel;
    }

    public void setSubnetModel(SubnetModel subnetModel) {
        this.subnetModel = subnetModel;
    }

    public EventsModel getEventsModel() {
        return eventsModel;
    }

    public void setEventsModel(EventsModel eventsModel) {
        this.eventsModel = eventsModel;
    }

    public PreferencesModel getPreferencesModel() {
        return preferencesModel;
    }

    public void setPreferencesModel(PreferencesModel preferencesModel) {
        this.preferencesModel = preferencesModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.model.IWizardModel#addModelListener(com.intel
     * .stl.ui.wizards.model.IModelChangeListener)
     */
    @Override
    public void addModelListener(IModelChangeListener<IWizardModel> listener) {

        WizardType key = null;

        listeners.add(listener);

        if (listener instanceof MultinetWizardModel) {
            key = WizardType.MULTINET;
        } else if (listener instanceof SubnetModel) {
            key = WizardType.SUBNET;
        } else if (listener instanceof EventsModel) {
            key = WizardType.EVENT;
        } else if (listener instanceof PreferencesModel) {
            key = WizardType.PREFERENCES;
        }

        if (key != null) {
            listenerMap.put(key, listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.model.IWizardModel#notifyModelChange()
     */
    @Override
    public void notifyModelChange() {

        // Notify all wizards of changes to the top model
        if (listeners.size() > 0) {
            for (IModelChangeListener<IWizardModel> listener : listeners) {
                listener.onModelChange(this);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.model.IWizardModel#notifyModelChange(com.intel
     * .stl.ui.wizards.impl.WizardType)
     */
    @Override
    public void notifyModelChange(WizardType wizardType) {

        if (listenerMap.size() > 0) {
            IModelChangeListener<IWizardModel> listener =
                    listenerMap.get(wizardType);
            if (listener != null) {
                listener.onModelChange(this);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.model.IWizardModel#clearModel()
     */
    @Override
    public void clearModel() {
        // TODO Auto-generated method stub

    }
}
