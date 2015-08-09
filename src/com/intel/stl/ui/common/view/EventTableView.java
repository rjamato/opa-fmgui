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
 *  File Name: EventTableView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/04/02 13:32:58  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/28 17:25:41  jijunwan
 *  Archive Log:    color severity on event table, by default sort event table by time, by default show event table on home page, show text for enums
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 19:25:43  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/23 18:29:16  rjtierne
 *  Archive Log:    Removed main panel and added UI components
 *  Archive Log:    to "this" panel
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:20:48  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:47:19  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/09 21:01:52  rjtierne
 *  Archive Log:    Super class FVTableView is no longer extended from
 *  Archive Log:    JCard so renamed getMainComponent() to initComponents() and constructor set the model and
 *  Archive Log:    formats the table
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/08 17:32:57  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/04 19:20:28  rjtierne
 *  Archive Log:    Concrete table view class previously named FVTableView.  The FVTableView is now an abstract class that this extends from.
 *  Archive Log:
 *
 *  Overview: UI view for the event table; extends abstract class FVTableView
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.intel.stl.api.configuration.EventType;
import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.EventTableModel;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.model.EventTypeViz;
import com.intel.stl.ui.model.NoticeSeverityViz;

public class EventTableView extends FVTableView {

    /**
     * Description: Constructor for the EventTableView class
     * 
     * @param pController
     *            - event table card
     */
    public EventTableView(EventTableModel pModel) {
        super();
        initComponents();
        setModel(pModel);
        formatTable();
    }

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3269064546124145702L;

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.tables.FVTableView#formatTable()
     */
    @Override
    public void formatTable() {

        FVTableRenderer tableRenderer = new FVTableRenderer() {
            private static final long serialVersionUID = -5458310374933530865L;

            /*
             * (non-Javadoc)
             * 
             * @see com.intel.stl.ui.common.view.FVTableRenderer#
             * getTableCellRendererComponent(javax.swing.JTable,
             * java.lang.Object, boolean, boolean, int, int)
             */
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel cell =
                        (JLabel) super.getTableCellRendererComponent(table,
                                value, isSelected, hasFocus, row, column);
                if (column == EventTableModel.SEVERITY_IDX && value != null) {
                    NoticeSeverityViz nsv =
                            NoticeSeverityViz
                                    .getNoticeSeverityVizFor((NoticeSeverity) value);
                    if (nsv != null) {
                        cell.setText(nsv.getName());
                        cell.setForeground(nsv.getColor());
                    }
                    cell.setFont(UIConstants.H5_FONT.deriveFont(Font.BOLD));
                    // cell.setIcon(nsv.getIcon().getImageIcon());
                } else {
                    if (column == EventTableModel.DESCRIPTION_IDX
                            && value != null) {
                        EventTypeViz ntv =
                                EventTypeViz
                                        .getEventTypeVizFor((EventType) value);
                        if (ntv != null) {
                            cell.setText(ntv.getName());
                        }
                    }
                    cell.setForeground(UIConstants.INTEL_DARK_GRAY);
                    cell.setIcon(null);
                }
                return cell;
            }

        };

        // Format the table headers
        FVHeaderRenderer headerRenderer = new FVHeaderRenderer(mTable);
        mHeaderCol = mTable.getTableHeader();
        mHeaderCol.setFont(UIConstants.H3_FONT.deriveFont(Font.BOLD));

        for (int i = 0; i < mTable.getColumnCount(); i++) {
            mTable.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);
            mTblCol = mTable.getColumnModel().getColumn(i);
            mTblCol.setCellRenderer(tableRenderer);
        } // for

        TableRowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(mTable.getModel());
        mTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys =
                Collections.singletonList(new RowSorter.SortKey(
                        EventTableModel.TIME_IDX, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
    }

    /**
     * 
     * Description: Initializes the components for this view
     * 
     */
    public void initComponents() {

        // Configure the table
        mTable = new JTable();
        mTable.setFillsViewportHeight(true);
        mTable.setPreferredScrollableViewportSize(getMaximumSize());
        mTable.setAutoCreateColumnsFromModel(true);
        mTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        mTable.setAlignmentX(JTable.LEFT_ALIGNMENT);
        mTable.setBackground(UIConstants.INTEL_WHITE);
        mTable.setIntercellSpacing(new Dimension(2, 3));
        mTable.getTableHeader().setReorderingAllowed(false);

        // Add the table to the scroll pane and configure
        ScrollPaneLayout spLayout = new ScrollPaneLayout();
        spLayout.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        spLayout.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        mScrollPane = new JScrollPane(mTable);
        mScrollPane.setPreferredSize(new Dimension(MAIN_SCROLL_PANE_WIDTH,
                MAIN_SCROLL_PANE_HEIGHT));
        mScrollPane.createHorizontalScrollBar();
        mScrollPane.createVerticalScrollBar();
        mScrollPane.setLayout(spLayout);

        // Configure the scroll pane layout and constraints
        GridBagLayout gbLayout = new GridBagLayout();
        gbLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        setLayout(new GridBagLayout());

        GridBagConstraints gbcMainPanel = new GridBagConstraints();
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        gbcMainPanel.weightx = 1;
        gbcMainPanel.weighty = 1;
        gbcMainPanel.gridwidth = GridBagConstraints.REMAINDER;

        // Add the scroll pane to this panel
        add(mScrollPane, gbcMainPanel);
    } // initComponents

}
