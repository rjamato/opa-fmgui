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
 *  File Name: StatisticsSubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17  2015/04/08 19:45:16  rjtierne
 *  Archive Log:    PR 126844 - Can make Port counter names in UIs more concise.
 *  Archive Log:    Created new table header to override getToolTipText()
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/08 16:38:04  jijunwan
 *  Archive Log:    PR 127994 - Slow Link icon in Connectivity Table overwritten by Link Quality Icon
 *  Archive Log:    -- create a panel with two labels when necessary to ensure we insert slock link icon
 *  Archive Log:    -- fixed a index bug happens when link quality column is hidden and we intend to find out its column index
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/02 13:32:55  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/05 22:32:14  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/26 20:25:38  fisherma
 *  Archive Log:    Removed comment and system.out.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/26 20:07:38  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/01/11 21:24:08  jijunwan
 *  Archive Log:    generic table view with table model
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/18 21:36:48  jijunwan
 *  Archive Log:    fixed a issue that incorrectly use portNum for rowIndex
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/18 21:03:27  jijunwan
 *  Archive Log:    Added link (jump to) capability to Connectivity tables and PortSummary table
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/26 15:15:26  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/22 19:31:23  jijunwan
 *  Archive Log:    changed table selection background color
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/03 14:08:12  rjtierne
 *  Archive Log:    Moved column-hiding code into separate filterColumns() method so
 *  Archive Log:    this functionality could be overridden
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/26 15:07:15  jijunwan
 *  Archive Log:    added inactive link icon
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/24 20:23:17  rjtierne
 *  Archive Log:    Renamed renderSlowLinks() to renderLinkState() and provided icon rendering
 *  Archive Log:    for normal speed links
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/17 19:24:31  rjtierne
 *  Archive Log:    Added logic to render the Connectivity table entries with an icon when
 *  Archive Log:    links are running slow
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/13 14:35:22  rjtierne
 *  Archive Log:    Connectivity table appearance update
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/12 21:39:38  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:

 *
 *  Overview: View for the Connectivity subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternPredicate;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.view.FVHeaderRenderer;
import com.intel.stl.ui.common.view.FVTableRenderer;
import com.intel.stl.ui.common.view.FVXTableView;
import com.intel.stl.ui.common.view.JumpPopupUtil;
import com.intel.stl.ui.common.view.JumpPopupUtil.IActionCreator;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.model.ConnectivityTableColumns;
import com.intel.stl.ui.model.ConnectivityTableModel;
import com.intel.stl.ui.model.LinkQualityViz;
import com.intel.stl.ui.monitor.ConnectivityTableData;
import com.intel.stl.ui.monitor.IPortSelectionListener;

public class ConnectivitySubpageView extends
        FVXTableView<ConnectivityTableModel> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2465696391555897980L;

    private static final int TYPE_COLUMN = 3;

    private static final int PORT_COLUMN = 2;

    protected IPortSelectionListener listener;

    public ConnectivitySubpageView(ConnectivityTableModel model) {
        super(model);
        installPopupMenu();
        installListeners();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.FVXTableView#createTable(javax.swing.table
     * .TableModel)
     */
    @Override
    protected JXTable createTable(ConnectivityTableModel model) {
        // Configure the table
        JXTable table = new JXTable(model);
        table.setColumnControlVisible(true);
        table.setHorizontalScrollEnabled(true);
        table.setAutoscrolls(true);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(getMaximumSize());
        table.setAutoCreateColumnsFromModel(true);
        table.setAlignmentX(JTable.LEFT_ALIGNMENT);
        table.setBackground(UIConstants.INTEL_WHITE);
        table.setVisibleRowCount(15);
        table.setHighlighters(HighlighterFactory.createAlternateStriping(
                UIConstants.INTEL_WHITE, UIConstants.INTEL_TABLE_ROW_GRAY));
        table.getTableHeader().setReorderingAllowed(false);

        // Highlight the "Neighbor" ports in blue
        HighlightPredicate predicate =
                new PatternPredicate(STLConstants.K0525_NEIGHBOR.getValue());
        ColorHighlighter highlighter =
                new ColorHighlighter(predicate,
                        UIConstants.INTEL_TABLE_ROW_GRAY,
                        UIConstants.INTEL_BLUE, null, null);
        table.setHighlighters(highlighter);

        // Make the background of the "Inactive" ports gray
        predicate =
                new PatternPredicate(STLConstants.K0524_INACTIVE.getValue());
        highlighter =
                new ColorHighlighter(predicate, UIConstants.INTEL_GRAY, null,
                        null, null);
        table.addHighlighter(highlighter);

        // Turn of the ability to sort columns
        table.setSortable(false);
        return table;
    }

    protected void installPopupMenu() {
        final JPopupMenu popupMenu = new JPopupMenu();
        JumpPopupUtil.appendPopupMenu(popupMenu, false, new IActionCreator() {

            @Override
            public Action createAction(final JumpDestination destination) {
                return new AbstractAction(destination.getName()) {
                    private static final long serialVersionUID =
                            -2783822215801105313L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (listener == null) {
                            return;
                        }

                        int vRow = mTable.getSelectedRow();
                        if (vRow >= 0) {
                            int mRow = mTable.convertRowIndexToModel(vRow);
                            ConnectivityTableData data = model.getEntry(mRow);
                            listener.onJumpToPort(data.getNodeLidValue(),
                                    data.getPortNumValue(), destination);
                        }
                    }

                };
            }

        });
        mTable.setComponentPopupMenu(popupMenu);
    }

    protected void installListeners() {
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listener == null) {
                    return;
                }

                int vRow = mTable.getSelectedRow();
                if (vRow >= 0) {
                    int mRow = mTable.convertRowIndexToModel(vRow);
                    listener.onPortSelection(mRow);
                }
            }
        };
        mTable.getSelectionModel().addListSelectionListener(selectionListener);

        MouseListener mouseListener = new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent
             * )
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener == null) {
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)
                        && e.getClickCount() > 1) {
                    Point p = e.getPoint();
                    int vRow = mTable.rowAtPoint(p);
                    if (vRow >= 0) {
                        int mRow = mTable.convertRowIndexToModel(vRow);
                        ConnectivityTableData data = model.getEntry(mRow);
                        listener.onJumpToPort(data.getNodeLidValue(),
                                data.getPortNumValue(), JumpDestination.DEFAULT);
                    }
                }
            }

        };
        mTable.addMouseListener(mouseListener);
    }

    public void setPortSelectionListener(IPortSelectionListener listener) {
        this.listener = listener;
    }

    public JComponent getMainComponent() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.FVTableView#formatTable()
     */
    @Override
    public void formatTable() {
        FVTableRenderer tableRenderer = new FVTableRenderer() {

            private static final long serialVersionUID = 4771573417795067808L;

            // TODO Need to customize the Neighbor rows in this table
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int col) {

                Component cell =
                        super.getTableCellRendererComponent(table, value,
                                false, hasFocus, row, col);

                return renderCell(table, value, isSelected, hasFocus, row, col,
                        cell);
            }
        };

        // Format the table columns and headers
        FVHeaderRenderer headerRenderer = new FVHeaderRenderer(mTable);
        mHeader = createTableHeader(mTable.getColumnModel());
        mTable.setTableHeader(mHeader);
        mHeader.setFont(UIConstants.H3_FONT);

        for (int i = 0; i < mTable.getColumnCount(); i++) {
            mTable.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);
            mTblCol = mTable.getColumnModel().getColumn(i);
            mTblCol.setCellRenderer(tableRenderer);
        } // for

        // Choose columns to hide
        filterColumns();

        mTable.packTable(2);
    }

    protected JXTableHeader createTableHeader(TableColumnModel columnModel) {

        JXTableHeader header = new JXTableHeader(columnModel) {

            private static final long serialVersionUID = 1552295147223847158L;

            @Override
            public String getToolTipText(MouseEvent e) {

                Point point = e.getPoint();
                int column = columnModel.getColumnIndexAtX(point.x);
                int modelIndex = table.convertColumnIndexToModel(column);
                return ConnectivityTableColumns.values()[modelIndex]
                        .getToolTip();
            }
        };

        return header;
    }

    protected void filterColumns() {
        ConnectivityTableColumns[] toHide =
                new ConnectivityTableColumns[] {

                        // Show these columns
                        // ConnectivityTableColumns.DEVICE_NAME,
                        // ConnectivityTableColumns.NODE_GUID,
                        // ConnectivityTableColumns.PORT_NUMBER,
                        // ConnectivityTableColumns.LINK_STATE,
                        // ConnectivityTableColumns.PHYSICAL_LINK_STATE,
                        // ConnectivityTableColumns.LINK_QUALITY,
                        // ConnectivityTableColumns.ACTIVE_LINK_SPEED,
                        // ConnectivityTableColumns.SUPPORTED_LINK_SPEED,
                        // ConnectivityTableColumns.LINK_DOWN,

                        // Hide these columns
                        ConnectivityTableColumns.LINK_RECOVERIES,
                        ConnectivityTableColumns.ACTIVE_LINK_WIDTH,
                        ConnectivityTableColumns.ENABLED_LINK_WIDTH,
                        ConnectivityTableColumns.SUPPORTED_LINK_WIDTH,
                        ConnectivityTableColumns.ENABLED_LINK_SPEED,
                        ConnectivityTableColumns.TX_PACKETS,
                        ConnectivityTableColumns.RX_PACKETS,
                        ConnectivityTableColumns.RX_ERRORS,
                        ConnectivityTableColumns.RX_REMOTE_PHYSICAL_ERRRORS,
                        ConnectivityTableColumns.TX_DISCARDS,
                        ConnectivityTableColumns.LOCAL_LINK_INTEGRITY_ERRRORS,
                        ConnectivityTableColumns.EXCESSIVE_BUFFER_OVERRUNS,
                        ConnectivityTableColumns.SWITCH_RELAY_ERRRORS,
                        ConnectivityTableColumns.TX_PORT_CONSTRAINT,
                        ConnectivityTableColumns.RX_PORT_CONSTRAINT };

        for (ConnectivityTableColumns col : toHide) {
            mTable.getColumnExt(col.getTitle()).setVisible(false);
        }
    }

    private Component renderLinkState(JTable table, int row, int col,
            Component cell, Object value) {
        JLabel label = (JLabel) cell;
        ConnectivityTableData tableEntry = model.getEntry(row);
        Icon icon = null;
        if (tableEntry.isSlowLinkState()) {
            icon = UIImages.SLOW_LINK.getImageIcon();
        } else {
            String str = (String) table.getModel().getValueAt(row, TYPE_COLUMN);
            if (str.equals(STLConstants.K0524_INACTIVE.getValue())) {
                icon = UIImages.INACTIVE_LINK.getImageIcon();
            } else {
                icon = UIImages.NORMAL_LINK.getImageIcon();
            }
        }

        if (label.getIcon() != null) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(label.getBackground());
            JLabel stateLabel = new JLabel(icon);
            panel.add(stateLabel, BorderLayout.WEST);
            panel.add(label, BorderLayout.CENTER);
            return panel;
        } else {
            label.setIcon(icon);
            return label;
        }
    }

    private Component renderCell(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col,
            Component cell) {

        // Turn off the slow link icon in case it's on
        ((JLabel) cell).setIcon(null);

        // un-set the tooltip text and
        // re-Set alignment to default in case it has been set to CENTER
        // by the LinkQuality column. this is due to the fact we are
        // using same renderer for all the columns.
        ((JLabel) cell).setToolTipText(null);
        ((JLabel) cell).setHorizontalAlignment(JLabel.LEADING);

        Color lastForegroundColor = cell.getForeground();
        Color lastBackgroundColor = cell.getBackground();

        // Handle null values
        if (value == null) {
            ((JLabel) cell).setText(STLConstants.K0383_NA.getValue());
        }

        // Rows with "Inactive" types are bold
        String str = (String) table.getModel().getValueAt(row, TYPE_COLUMN);
        if (str.equals(STLConstants.K0524_INACTIVE.getValue())) {
            cell.setFont(UIConstants.H6_FONT.deriveFont(Font.BOLD));
        }

        // Rows with "Neighbor" ports are italic
        str = (String) table.getModel().getValueAt(row, PORT_COLUMN);
        if (str.contains(STLConstants.K0525_NEIGHBOR.getValue())) {
            cell.setFont(UIConstants.H6_FONT.deriveFont(Font.ITALIC));
        }

        // Alternate rows white and gray
        if ((row % 2) == 0) {
            cell.setBackground(UIConstants.INTEL_WHITE);
            cell.setFont(UIConstants.H6_FONT.deriveFont(Font.BOLD));
        } else {
            cell.setBackground(UIConstants.INTEL_TABLE_ROW_GRAY);
        }

        // Use a blue highlight cursor for selected rows
        if (isSelected) {
            // cell.setFont(UIConstants.H4_FONT.deriveFont(Font.BOLD));
            lastForegroundColor = cell.getForeground();
            lastBackgroundColor = cell.getBackground();
            cell.setForeground(UIConstants.INTEL_WHITE);
            cell.setBackground(UIConstants.INTEL_MEDIUM_BLUE);
        } else {
            cell.setForeground(lastForegroundColor);
            cell.setBackground(lastBackgroundColor);
        }

        // Set icon for LINK_QUALITY column cells
        int qualityIndex = -1;
        try {
            qualityIndex =
                    table.getColumnModel().getColumnIndex(
                            ConnectivityTableColumns.LINK_QUALITY.getTitle());
        } catch (IllegalArgumentException e) {
        }

        if (qualityIndex == col) {
            if (value != null) {
                ((JLabel) cell).setIcon(LinkQualityViz
                        .getLinkQualityIcon(((Integer) value).byteValue()));

                ((JLabel) cell).setToolTipText(LinkQualityViz
                        .getLinkQualityStr(((Integer) value).byteValue()));
            }
            // Don't show link quality number text
            ((JLabel) cell).setText("");
            cell.setForeground(cell.getBackground());
            ((JLabel) cell).setHorizontalAlignment(JLabel.CENTER);
        }

        if (col == 0) {
            // Set the indicator for slow links
            cell = renderLinkState(table, row, col, cell, value);
        }

        return cell;
    }
}
