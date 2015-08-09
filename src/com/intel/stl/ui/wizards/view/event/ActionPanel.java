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
 *  File Name: ActionPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/03/30 15:12:50  rjtierne
 *  Archive Log:    Updated panel backgrounds to use static variable
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/13 21:31:52  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/13 19:01:25  rjtierne
 *  Archive Log:    Removed warnings by specifying generic types
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/23 18:36:59  rjtierne
 *  Archive Log:    Set dirty state in view when new selections are made on action list
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:10  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Popup panel for the action column of the Event Wizard table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.wizards.view.event;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;

import org.jdesktop.swingx.JXList;

import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.EventRuleAction;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ButtonPopup;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.wizards.impl.IWizardTask;
import com.intel.stl.ui.wizards.model.event.EventRulesTableModel;
import com.intel.stl.ui.wizards.view.IWizardView;
import com.intel.stl.ui.wizards.view.MultinetWizardView;

public class ActionPanel extends JPanel {

    private static final long serialVersionUID = -2996609094010046773L;

    private JButton btnAction;

    private PopupPanel popupPanel;

    private ButtonPopup popup;

    private final List<JCheckBox> cbActionList = new ArrayList<JCheckBox>();

    private List<EventRule> eventRules;

    private final EventRulesTableModel tableModel;

    private int activeRow;

    private final IWizardView wizardViewListener;

    private IWizardTask eventWizardControlListener;

    public ActionPanel(EventRulesTableModel model,
            IWizardView wizardViewListener) {

        this.tableModel = model;
        this.wizardViewListener = wizardViewListener;
        initComponents();
    }

    protected void initComponents() {

        // Create the panel to hold the popup's toolbar
        JPanel mainPanel = new JPanel();

        // Create the toolbar and put the button on it
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        btnAction =
                ComponentFactory.getIntelActionButton(STLConstants.K3004_SELECT
                        .getValue());

        btnAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popup.isVisible()) {
                    popup.hide();
                } else {
                    wizardViewListener.enableApply(true);
                    popup.show();
                }
            }
        });
        toolBar.add(btnAction);
        mainPanel.add(toolBar);

        popupPanel = new PopupPanel();
        popup = new ButtonPopup(btnAction, popupPanel, false) {

            @Override
            public void onShow() {

                List<EventRuleAction> actionList =
                        tableModel.getEntry(activeRow).getEventActions();

                // Initialize the check boxes to unselected
                for (int i = 0; i < cbActionList.size(); i++) {
                    cbActionList.get(i).setSelected(false);
                }

                // Loop through the actions from the model and select the
                // corresponding check boxes before opening the panel
                Iterator<JCheckBox> it = cbActionList.iterator();
                Iterator<EventRuleAction> actionIterator =
                        actionList.iterator();

                while (actionIterator.hasNext()) {

                    EventRuleAction action = actionIterator.next();

                    boolean found = false;
                    while (it.hasNext() && !found) {
                        JCheckBox chkbox = it.next();
                        chkbox.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                eventWizardControlListener.setDirty(true);
                            }

                        });
                        found =
                                (action.name().equals(chkbox.getText())) ? true
                                        : false;
                        chkbox.setSelected(found);
                    }
                }
            }

            @Override
            public void onHide() {
                updateModel(activeRow, ITableListener.ACTION_EDITOR_COLUMN);
            }
        };
    }

    public void updateModel(int row, int column) {
        popupPanel.updateModel(row, column);
    }

    /**
     * @return the action button
     */
    public JButton getActionButton() {
        return btnAction;
    }

    /**
     * @param activeRow
     *            the activeRow to set
     */
    public void setActiveRow(int activeRow) {
        this.activeRow = activeRow;
    }

    /**
     * @param eventWizardControlListener
     *            the eventWizardControlListener to set
     */
    public void setEventWizardControlListener(
            IWizardTask eventWizardControlListener) {
        this.eventWizardControlListener = eventWizardControlListener;
    }

    /*******************************************************************************
     * I N T E L C O R P O R A T I O N
     * 
     * Functional Group: Fabric Viewer Application
     * 
     * File Name: PopupPanel (Inner Class)
     * 
     * Archive Source: $Source:
     * /cvs/vendor/intel/fmgui/client/src/main/java/com/
     * intel/stl/ui/wizards/view/event/ActionPanel.java,v $
     * 
     * Archive Log: $Log$
     * Archive Log: Revision 1.5  2015/03/30 15:12:50  rjtierne
     * Archive Log: Updated panel backgrounds to use static variable
     * Archive Log:
     * Archive Log: Revision 1.4  2015/02/13 21:31:52  rjtierne
     * Archive Log: Multinet Wizard
     * Archive Log:
     * Archive Log: Revision 1.3  2015/01/13 19:01:25  rjtierne
     * Archive Log: Removed warnings by specifying generic types
     * Archive Log: Archive Log: Revision 1.2
     * 2014/12/23 18:36:59 rjtierne Archive Log: Set dirty state in view when
     * new selections are made on action list Archive Log: Archive Log: Revision
     * 1.1 2014/12/10 21:31:10 rjtierne Archive Log: New Setup Wizard based on
     * framework Archive Log:
     * 
     * Overview: Popup panel class
     * 
     * @author: rjtierne
     * 
     ******************************************************************************/
    class PopupPanel extends JPanel implements ListCellRenderer<JCheckBox> {

        private static final long serialVersionUID = 5009991107568106318L;

        private DefaultListModel<JCheckBox> listModel;

        private JXList list;

        public PopupPanel() {
            super();
            initPopupPanel();
        }

        protected void initPopupPanel() {

            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            listModel = new DefaultListModel<JCheckBox>();
            list = new JXList(listModel);
            list.setVisibleRowCount(10);
            list.setCellRenderer(this);

            // Create the check boxes on a panel
            JPanel pnlActionList = new JPanel();
            pnlActionList.setLayout(new BoxLayout(pnlActionList,
                    BoxLayout.PAGE_AXIS));
            for (EventRuleAction action : EventRuleAction.values()) {
                final JCheckBox chkBox =
                        ComponentFactory.getIntelCheckBox(action.name());
                cbActionList.add(chkBox);
                listModel.addElement(chkBox);
                pnlActionList.add(chkBox);
            }

            JScrollPane scroll = new JScrollPane(pnlActionList);
            scroll.getViewport().getView()
                    .setBackground(MultinetWizardView.WIZARD_COLOR);
            add(scroll, BorderLayout.CENTER);
        } // initPopupPanel

        protected void updateModel(final int row, int column) {

            Runnable updater = new Runnable() {
                @Override
                public void run() {

                    // Get the list of event rules from the model
                    eventRules = tableModel.getEventRules();

                    // Create a list of actions based on selected check boxes
                    List<EventRuleAction> actions =
                            new ArrayList<EventRuleAction>();

                    for (int i = 0; i < cbActionList.size(); i++) {

                        if (cbActionList.get(i).isSelected()) {
                            actions.add(EventRuleAction.values()[i]);
                        }
                    }

                    // Update the rules
                    eventRules.get(row).getEventActions().clear();
                    eventRules.get(row).setEventActions(actions);

                    // Update the model
                    tableModel.updateTable(eventRules);
                }
            };
            Util.runInEDT(updater);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
         * .JList, java.lang.Object, int, boolean, boolean)
         */
        @Override
        public Component getListCellRendererComponent(
                JList<? extends JCheckBox> list, JCheckBox value, int index,
                boolean isSelected, boolean cellHasFocus) {

            return cbActionList.get(index);
        }
    } // class PopupPanel

} // class ActionPanel
