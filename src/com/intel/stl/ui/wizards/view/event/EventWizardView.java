/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: EventRulesTable.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.11  2015/08/17 18:54:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/07/17 20:48:29  jijunwan
 *  Archive Log:    PR 129594 - Apply new input verification on setup wizard
 *  Archive Log:    - introduced isEditValid to allow us check whether we have valid edit
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/02 13:33:07  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/03/11 15:26:12  rjtierne
 *  Archive Log:    Multinet Wizard: Removed title from titled border
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/20 21:16:06  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/13 21:31:52  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/11 21:48:16  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/23 18:37:37  rjtierne
 *  Archive Log:    Add new cell renderer to action list cells to show display or e-mail selections in the form of icons
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/19 19:41:09  rjtierne
 *  Archive Log:    Added array of Objects to showMessage()
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 20:01:06  rjtierne
 *  Archive Log:    Removed the disabling of Next/Apply buttons to make Wizard functional again
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:10  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: This class replaces the EventRulesPanel, in the Event Wizard, with
 *  a event rules table on a single panel.
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.view.event;

import javax.swing.JComponent;

import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.impl.event.IEventControl;
import com.intel.stl.ui.wizards.model.MultinetWizardModel;
import com.intel.stl.ui.wizards.model.event.EventRulesTableModel;
import com.intel.stl.ui.wizards.view.AbstractTaskView;
import com.intel.stl.ui.wizards.view.IMultinetWizardView;
import com.intel.stl.ui.wizards.view.IWizardView;

public class EventWizardView extends AbstractTaskView {

    private static final long serialVersionUID = 5308404765504020477L;

    private EventTableView tableView;

    private EventRulesTableModel model;

    @SuppressWarnings("unused")
    private IWizardView wizardViewListener = null;

    private IMultinetWizardView multinetWizardViewListener = null;

    private IEventControl eventControlListener;

    public EventWizardView(EventRulesTableModel model,
            IWizardView wizardViewListener) {
        this.model = model;
        this.wizardViewListener = wizardViewListener;
        initComponents("");
        resetPanel();
    }

    public EventWizardView(EventRulesTableModel model,
            IMultinetWizardView wizardViewListener) {
        this.model = model;
        this.multinetWizardViewListener = wizardViewListener;
        initComponents("");
        resetPanel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.AbstractTaskView#getOptionComponent()
     */
    @Override
    protected JComponent getOptionComponent() {
        if (tableView == null) {
            tableView = new EventTableView(model, multinetWizardViewListener);
        }
        return tableView;
    }

    @Override
    public void setWizardListener(IWizardTask listener) {
        super.setWizardListener(listener);
        tableView.setWizardListener(listener);
    }

    public void setEventControlListener(IEventControl listener) {

        eventControlListener = listener;
    }

    /**
     * @return the model
     */
    public EventRulesTableModel getModel() {
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#updatePanel()
     */
    @Override
    public void resetPanel() {
        if (tableView != null) {
            tableView.setDirty(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#isDirty()
     */
    @Override
    public boolean isDirty() {
        return tableView.isDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ITaskView#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {
        tableView.setDirty(dirty);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#setSubnet(com.intel.stl.api.subnet
     * .SubnetDescription)
     */
    @Override
    public void setSubnet(SubnetDescription subnet) {
        resetPanel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ITaskView#update(com.intel.stl.ui.wizards
     * .model.MultinetWizardModel)
     */
    @Override
    public void updateView(MultinetWizardModel mwModel) {

        this.model = mwModel.getEventsModel().getEventsRulesModel();
        tableView.setModel(model);
        eventControlListener.updateTable(model);
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    public boolean isEditValid() {
        return true;
    }
}
