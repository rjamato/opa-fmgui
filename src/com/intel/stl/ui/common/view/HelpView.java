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
 *  File Name: ConsoleHelpView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/12/11 18:44:57  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/04 21:42:44  jijunwan
 *  Archive Log:    replace OHJ with JavaHelp
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/01 19:39:35  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the console help utility
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.help.BadIDException;
import javax.help.JHelpContentViewer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import com.intel.stl.ui.common.IHelp;

/**
 * @author rjtierne
 * 
 */
public class HelpView extends JPanel {
    private static final long serialVersionUID = 6892075486378808599L;

    private static final Logger log = LoggerFactory.getLogger(HelpView.class);

    private final String title;

    private List<String> topicIdList;

    private final JHelpContentViewer topicPanel;

    private JComboBox cboxTopic;

    private final IHelp helpController;

    public HelpView(String title, List<String> topicIdList,
            JHelpContentViewer topicPanel, IHelp helpController) {

        this.title = title;
        this.topicIdList = topicIdList;
        this.topicPanel = topicPanel;
        this.helpController = helpController;
        initComponents();
    }

    protected void initComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(1, 5, 2, 5);
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.weightx = 1;

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0,
                Color.ORANGE));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        add(lblTitle, gc);

        cboxTopic = new JComboBox();
        cboxTopic.setUI(new IntelComboBoxUI());
        cboxTopic.setEditable(true);
        AutoCompleteDecorator.decorate(cboxTopic);
        add(cboxTopic, gc);
        cboxTopic.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (topicIdList.contains(cboxTopic.getSelectedItem())) {
                    helpController.showTopic((String) cboxTopic
                            .getSelectedItem());
                }
            }
        });
        cboxTopic.setModel(new ListComboBoxModel<String>(topicIdList));

        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        add(topicPanel, gc);
    }

    /**
     * @return the topicIdList
     */
    public List<String> getTopicIdList() {
        return topicIdList;
    }

    /**
     * @param topicIdList
     *            the topicIdList to set
     */
    public void setTopicIdList(List<String> topicIdList) {
        this.topicIdList = topicIdList;
    }

    public void displayTopic(String topic) {
        try {
            topicPanel.setCurrentID(topic);
        } catch (BadIDException e) {
            // silent on this since a user may type in any string
            // e.printStackTrace();
        }
    }

    public void selectTopic(String topicId) {
        cboxTopic.setSelectedItem(topicId);
        helpController.showTopic((String) cboxTopic.getSelectedItem());
    }

    public void updateSelection(String value) {
        cboxTopic.setSelectedItem(value);
    }
}
