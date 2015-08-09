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
 *  File Name: EventSummaryBarPanel
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/04/06 11:14:06  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/04/02 13:32:58  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/12/11 18:44:57  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/21 16:28:36  jijunwan
 *  Archive Log:    integer format adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/28 17:25:41  jijunwan
 *  Archive Log:    color severity on event table, by default sort event table by time, by default show event table on home page, show text for enums
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/19 22:08:52  jijunwan
 *  Archive Log:    moved filter from EventCalculator to StateSummary, so we can have better consistent result
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/15 14:29:29  jijunwan
 *  Archive Log:    minor UI adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/15 12:17:50  jypak
 *  Archive Log:    Gap between count and name narrowed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/14 21:43:23  jypak
 *  Archive Log:    Event Summary Table updates.
 *  Archive Log:    1. Replace EventMsgBean with EventDescription.
 *  Archive Log:    2. Update table contents with real data from Notice API.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/13 13:03:55  jypak
 *  Archive Log:    Event Summary Bar panel in pin board implementation.
 *  Archive Log:
 *
 *  Overview: Severity count summary panel (EventSummaryBarPanel) that goes to pin board. 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.IEventSummaryBarListener;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.model.NoticeSeverityViz;

public class EventSummaryBarPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1457639937923618304L;

    /**
     * Logging instance
     */
    private static Logger log = LoggerFactory
            .getLogger(EventSummaryBarPanel.class);

    private final EnumMap<NoticeSeverity, LabelPanel> severityPanelList =
            new EnumMap<NoticeSeverity, LabelPanel>(NoticeSeverity.class);

    private final GridBagLayout severityPanelGridBagLayout =
            new GridBagLayout();

    private final GridBagConstraints severityPanelConstraints =
            new GridBagConstraints();

    private MouseListener panelMouseListener = null;

    private IEventSummaryBarListener iEventSummaryBarListener = null;

    public EventSummaryBarPanel() {
        super();
        initialize();
    }

    public void setEventSummaryBarListener(
            IEventSummaryBarListener iEventSummary) {
        iEventSummaryBarListener = iEventSummary;
        installActionListener();
        initializeEventSeverity();
    }

    private void installActionListener() {
        panelMouseListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                iEventSummaryBarListener.toggleEventSummaryTable();
            }

        };
    }

    public void initialize() {
        setLayout(severityPanelGridBagLayout);
        setBackground(UIConstants.INTEL_WHITE);
    }

    /**
     * Add event severity count label panels (LabelPanel) to the
     * EventSummaryBarPanel with the counts set with 'N/A'. Also, add mouse
     * click listener to each panel so that click on each panel can open/close
     * the event summary table at the bottom of left pane.
     * 
     */
    public void initializeEventSeverity() {
        NoticeSeverity[] array = NoticeSeverity.values();
        for (int i = array.length - 1; 0 <= i; i--) {
            LabelPanel panel = new LabelPanel(array[i]);
            panel.addMouseListener(panelMouseListener);

            severityPanelList.put(array[i], panel);
            severityPanelConstraints.weighty = 0.0;

            addToEventSummaryBarPane(panel);

        }
        severityPanelConstraints.weighty = 1.0;
        severityPanelConstraints.fill = GridBagConstraints.BOTH;

        add(Box.createGlue(), severityPanelConstraints);
        // repaint, validate didn't work.
        revalidate();

    }

    public void updateEventSeverity(EnumMap<NoticeSeverity, Integer> countMap) {
        // The severityPanelList is fully populated with four possible
        // severities.
        // Find a matching entry and update the count label.
        for (NoticeSeverity severity : countMap.keySet()) {
            LabelPanel panel = severityPanelList.get(severity);
            JLabel countLabel = panel.getCountLabel();
            countLabel.setText(UIConstants.INTEGER.format(countMap
                    .get(severity)));
        }
    }

    public void addToEventSummaryBarPane(JPanel panel) {

        severityPanelConstraints.weightx = 1.0;
        severityPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        severityPanelConstraints.gridwidth = GridBagConstraints.REMAINDER;

        severityPanelGridBagLayout.setConstraints(panel,
                severityPanelConstraints);
        add(panel, severityPanelConstraints);
    }

    private class LabelPanel extends JPanel {
        private static final long serialVersionUID = -2905931691586163645L;

        private JLabel nameLabel;

        private final JLabel countLabel;

        private final JPanel countNamePanel;

        private JLabel iconLabel;

        private final GridBagLayout gridBagLayout = new GridBagLayout();

        private final GridBagConstraints constraints = new GridBagConstraints();

        public LabelPanel(NoticeSeverity severity) {
            super();

            setLayout(new BorderLayout(5, 2));
            setOpaque(false);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory
                    .createLineBorder(UIConstants.INTEL_BORDER_GRAY),
                    BorderFactory.createEmptyBorder(0, 5, 0, 5)));
            // setPreferredSize(new Dimension(200, 30));
            setBackground(UIConstants.INTEL_WHITE);

            countNamePanel = new JPanel(gridBagLayout);
            countNamePanel.setBackground(UIConstants.INTEL_WHITE);
            // countNamePanel.setPreferredSize(new Dimension(120, 30));
            countLabel =
                    ComponentFactory.getH2Label(
                            STLConstants.K0039_NOT_AVAILABLE.getValue(),
                            Font.BOLD);

            NoticeSeverityViz sevViz =
                    NoticeSeverityViz.getNoticeSeverityVizFor(severity);
            if (sevViz != null) {
                iconLabel =
                        new JLabel(sevViz.getIcon().getImageIcon(),
                                JLabel.RIGHT);
                countLabel.setForeground(sevViz.getColor());
                nameLabel =
                        ComponentFactory.getH4Label(sevViz.getName(),
                                Font.PLAIN);
            }
            if (nameLabel != null) {
                nameLabel.setHorizontalAlignment(JLabel.LEFT);
            }

            gridBagLayout.setConstraints(countNamePanel, constraints);

            constraints.insets = new Insets(2, 5, 2, 0);
            constraints.anchor = GridBagConstraints.EAST;
            constraints.fill = GridBagConstraints.NONE;
            countNamePanel.add(countLabel, constraints);

            constraints.weightx = 1;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.anchor = GridBagConstraints.SOUTHWEST;
            countNamePanel.add(nameLabel, constraints);

            add(iconLabel, BorderLayout.WEST);
            add(countNamePanel, BorderLayout.CENTER);
        }

        /**
         * @return the countLabel
         */
        public JLabel getCountLabel() {
            return countLabel;
        }

        /**
         * @return the nameLabel
         */
        public JLabel getNameLabel() {
            return nameLabel;
        }

    } // class LabelPanel

}
