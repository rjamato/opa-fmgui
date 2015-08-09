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
 *  File Name: PerformanceTableView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.16.2.2  2015/05/17 18:30:44  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *  Archive Log:    Revision 1.16.2.1  2015/05/14 18:17:01  jijunwan
 *  Archive Log:    PR 128671 - Wrong data sorting on ports performance table
 *  Archive Log:    - removed the wrong sorter that overrides our value based sorter
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/10 18:20:55  jypak
 *  Archive Log:    Fall back to previous way of displaying received/transmitted data in performance page(chart section, table section, counter (error) section).
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/04/08 19:45:16  rjtierne
 *  Archive Log:    PR 126844 - Can make Port counter names in UIs more concise.
 *  Archive Log:    Created new table header to override getToolTipText()
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/04/07 14:38:29  jypak
 *  Archive Log:    PR 126998 - Received/Transmitted data counters for Device Node and Device ports should show in MB rather than Flits. Fixed by converting units to Byte/KB/MB/GB. Also, tool tips were added to show the units for each value.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/04/01 19:57:33  jijunwan
 *  Archive Log:    format cleanup
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/05 22:32:14  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
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
 *  Archive Log:    Revision 1.8  2014/06/02 19:55:17  rjtierne
 *  Archive Log:    Clean up table columns to hide
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/05/30 20:38:08  rjtierne
 *  Archive Log:    Temporarily hiding all table columns except for
 *  Archive Log:    Tx/Rx Data and Packets
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/29 22:06:41  jijunwan
 *  Archive Log:    support both delta and cumulative portCounters
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 03:57:17  jijunwan
 *  Archive Log:    performance table adjustment: sort by number, hide columns to save space
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/29 03:07:05  jijunwan
 *  Archive Log:    minor adjustment on performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/28 22:21:57  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/28 17:58:40  rjtierne
 *  Archive Log:    Set table to pack all columns
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/28 17:56:18  rjtierne
 *  Archive Log:    New Performance table view for JXTable
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/23 19:20:16  rjtierne
 *  Archive Log:    Added method updatePerformanceTable()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:49:50  rjtierne
 *  Archive Log:    Initial Version
 *
 *  Overview: UI view for the performance table; extends abstract class FVTableView
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.view;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.FVHeaderRenderer;
import com.intel.stl.ui.common.view.FVTableRenderer;
import com.intel.stl.ui.common.view.FVXTableView;
import com.intel.stl.ui.common.view.JumpPopupUtil;
import com.intel.stl.ui.common.view.JumpPopupUtil.IActionCreator;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.model.LinkQualityViz;
import com.intel.stl.ui.model.PerformanceTableColumns;
import com.intel.stl.ui.model.PerformanceTableModel;
import com.intel.stl.ui.monitor.IPortSelectionListener;
import com.intel.stl.ui.monitor.PerformanceTableData;

public class PerformanceXTableView extends FVXTableView<PerformanceTableModel> {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 3269064546124145702L;

    protected IPortSelectionListener listener;

    /**
     * Description: Constructor for the EventTableView class
     * 
     * @param pController
     *            event table card
     */
    public PerformanceXTableView(PerformanceTableModel model) {
        super(model);
        installPopupMenu();
        installListeners();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.tables.FVTableView#formatTable()
     */
    @Override
    public void formatTable() {
        FVTableRenderer tableRenderer = new FVTableRenderer();

        // Format the table headers
        FVHeaderRenderer headerRenderer = new FVHeaderRenderer(mTable);
        mHeader = createTableHeader(mTable.getColumnModel());
        mTable.setTableHeader(mHeader);
        mHeader.setFont(UIConstants.H3_FONT);

        Comparator<Number> numberComparator = new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return Double.compare(o1.doubleValue(), o2.doubleValue());
            }
        };
        for (int i = 0; i < mTable.getColumnCount(); i++) {
            mTable.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);
            TableColumnExt col = mTable.getColumnExt(i);
            col.setComparator(numberComparator);

            if (PerformanceTableColumns.values()[i] != PerformanceTableColumns.LINK_QUALITY) {
                // For all but LINK_QUALITY column use tableRenderer
                col.setCellRenderer(tableRenderer);
            } else {
                /**
                 * For LINK_QUALITY column use special renderer to display
                 * center-aligned icon corresponding to the link quality,
                 * tooltip for the icon hide text - numeric representation of
                 * link quality.
                 */
                col.setCellRenderer(new FVTableRenderer() {
                    private static final long serialVersionUID =
                            -3747347169291822762L;

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected,
                            boolean hasFocus, int row, int column) {

                        Component renderer =
                                super.getTableCellRendererComponent(table,
                                        value, isSelected, hasFocus, row,
                                        column);

                        renderer.setForeground(renderer.getBackground());

                        setIcon(LinkQualityViz
                                .getLinkQualityIcon(((Integer) value)
                                        .byteValue()));

                        setHorizontalAlignment(JLabel.CENTER);

                        setToolTipText(LinkQualityViz
                                .getLinkQualityDescription(((Integer) value)
                                        .byteValue()));

                        return this;
                    }

                });
            }
        } // for

        // mTable.setDefaultRenderer(Object.class, tableRenderer);

        PerformanceTableColumns[] toHide =
                new PerformanceTableColumns[] {
                        PerformanceTableColumns.BUFFER_OVERRUNS,
                        PerformanceTableColumns.FM_CONFIG_ERRORS,
                        PerformanceTableColumns.RX_PACKETS,
                        PerformanceTableColumns.RX_DATA,
                        PerformanceTableColumns.RX_REMOTE_PHY_ERRORS,
                        PerformanceTableColumns.RX_SWITCH_ERRORS,
                        PerformanceTableColumns.TX_DISCARDS,
                        PerformanceTableColumns.TX_PACKETS,
                        PerformanceTableColumns.TX_DATA

                // PerformanceTableColumns.LINK_QUALITY,
                // PerformanceTableColumns.RX_DELTA_PACKETS,
                // PerformanceTableColumns.RX_DELTA_DATA,
                // PerformanceTableColumns.RX_PACKETS,
                // PerformanceTableColumns.RX_DATA,

                // PerformanceTableColumns.TX_DELTA_PACKETS,
                // PerformanceTableColumns.TX_DELTA_DATA,
                // PerformanceTableColumns.TX_PACKETS,
                // PerformanceTableColumns.TX_DATA
                };
        for (PerformanceTableColumns col : toHide) {
            mTable.getColumnExt(col.getTitle()).setVisible(false);
        }

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
                return PerformanceTableColumns.values()[modelIndex]
                        .getToolTip();
            }
        };

        return header;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.view.FVXTableView#createTable(javax.swing.table
     * .TableModel)
     */
    @Override
    protected JXTable createTable(final PerformanceTableModel model) {
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
                            PerformanceTableData perfData =
                                    model.getEntry(mRow);
                            listener.onJumpToPort(perfData.getNodeLid(),
                                    perfData.getPortNumber(), destination);
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
                        PerformanceTableData perfData = model.getEntry(mRow);
                        // we usually use default destination
                        // JumpDestination.DEFAULT
                        // for double click jumping. Since we are on port
                        // performance summary table, we use
                        // JumpDestination.PERFORMANCE here no matter what
                        // default destination is.
                        listener.onJumpToPort(perfData.getNodeLid(),
                                perfData.getPortNumber(),
                                JumpDestination.PERFORMANCE);
                    }
                }
            }

        };
        mTable.addMouseListener(mouseListener);
    }

    public void setPortSelectionListener(IPortSelectionListener listener) {
        this.listener = listener;
    }

    public void setSelectedPort(final int portIndex) {
        int vId = mTable.convertRowIndexToView(portIndex);
        mTable.setRowSelectionInterval(vId, vId);
    }

}
