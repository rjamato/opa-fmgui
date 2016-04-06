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
 *  File Name: PerformanceTableView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.24  2016/02/16 22:16:08  jijunwan
 *  Archive Log:    PR 132888 - Include Num Lanes Down in port counters display
 *  Archive Log:
 *  Archive Log:    - added Num Lanes Down
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2015/08/17 18:54:25  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/08/05 04:04:48  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/06/01 15:01:20  jypak
 *  Archive Log:    PR 128823 - Improve performance tables to include all portcounters fields.
 *  Archive Log:    All port counters fields added to performance table and connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/05/28 15:29:21  jypak
 *  Archive Log:    PR 128873 - Add "Flits" in performance table for Data related columns.
 *  Archive Log:    Added "(MB)" to RcvData, XmitData column header.
 *  Archive Log:    Added "(MBps)" to data rates.
 *  Archive Log:    Added data in "Flits" or data rate in "(Flits/sec)" to tool tips.
 *  Archive Log:    Used the TableDataDescription to convert and format the data.
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/05/14 17:43:09  jijunwan
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
 *  Archive Log:    Revision 1.18  2015/05/14 14:44:38  rjtierne
 *  Archive Log:    PR 128682 - Set link quality indicator to "Unknown" on port error
 *  Archive Log:    On Performance Port Table, change cell renderer to display the link quality description as the tool tip
 *  Archive Log:    instead of just the value
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/05/13 20:54:52  jijunwan
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
import com.intel.stl.ui.monitor.TableDataDescription;

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
        Comparator<TableDataDescription> tableDataComparator =
                new Comparator<TableDataDescription>() {
                    @Override
                    public int compare(TableDataDescription o1,
                            TableDataDescription o2) {
                        return Double.compare(o1.getData(), o2.getData());
                    }
                };
        for (int i = 0; i < mTable.getColumnCount(); i++) {
            mTable.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);
            TableColumnExt col = mTable.getColumnExt(i);

            if (PerformanceTableColumns
                    .values()[i] == PerformanceTableColumns.RX_DATA
                    || PerformanceTableColumns
                            .values()[i] == PerformanceTableColumns.RX_DATA_RATE
                    || PerformanceTableColumns
                            .values()[i] == PerformanceTableColumns.TX_DATA
                    || PerformanceTableColumns
                            .values()[i] == PerformanceTableColumns.TX_DATA_RATE) {
                col.setComparator(tableDataComparator);
                col.setCellRenderer(new FVTableRenderer() {
                    private static final long serialVersionUID =
                            -3747347169291822762L;

                    @Override
                    public Component getTableCellRendererComponent(JTable table,
                            Object value, boolean isSelected, boolean hasFocus,
                            int row, int column) {
                        JLabel renderer =
                                (JLabel) super.getTableCellRendererComponent(
                                        table, value, isSelected, hasFocus, row,
                                        column);

                        TableDataDescription data =
                                (TableDataDescription) value;
                        if (data != null) {
                            renderer.setText(data.getFormattedData());
                            renderer.setToolTipText(data.getDescription());
                        }
                        setHorizontalAlignment(JLabel.LEFT);
                        return this;
                    }
                });

            } else if (PerformanceTableColumns
                    .values()[i] != PerformanceTableColumns.LINK_QUALITY) {
                col.setComparator(numberComparator);
                // For all but LINK_QUALITY column use tableRenderer
                col.setCellRenderer(tableRenderer);
            } else {
                col.setComparator(numberComparator);
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
                    public Component getTableCellRendererComponent(JTable table,
                            Object value, boolean isSelected, boolean hasFocus,
                            int row, int column) {

                        Component renderer =
                                super.getTableCellRendererComponent(table,
                                        value, isSelected, hasFocus, row,
                                        column);

                        renderer.setForeground(renderer.getBackground());

                        setIcon(LinkQualityViz.getLinkQualityIcon(
                                ((Integer) value).byteValue()));

                        setHorizontalAlignment(JLabel.CENTER);

                        setToolTipText(LinkQualityViz.getLinkQualityDescription(
                                ((Integer) value).byteValue()));

                        return this;
                    }

                });
            }
        } // for

        // mTable.setDefaultRenderer(Object.class, tableRenderer);

        PerformanceTableColumns[] toHide = new PerformanceTableColumns[] {
                PerformanceTableColumns.EXCESSIVE_BUFFER_OVERRUNS,
                PerformanceTableColumns.FM_CONFIG_ERRORS,
                PerformanceTableColumns.RX_PACKETS,
                PerformanceTableColumns.RX_DATA,
                PerformanceTableColumns.RX_REMOTE_PHY_ERRORS,
                PerformanceTableColumns.RX_SWITCH_ERRORS,
                PerformanceTableColumns.TX_DISCARDS,
                PerformanceTableColumns.TX_PACKETS,
                PerformanceTableColumns.TX_DATA,
                PerformanceTableColumns.RX_MC_PACKETS,
                PerformanceTableColumns.RX_ERRORS,
                PerformanceTableColumns.RX_CONSTRAINT,
                PerformanceTableColumns.RX_FECN,
                PerformanceTableColumns.RX_BECN,
                PerformanceTableColumns.RX_BUBBLE,
                PerformanceTableColumns.TX_MC_PACKETS,
                PerformanceTableColumns.TX_CONSTRAINT,
                PerformanceTableColumns.TX_WAIT,
                PerformanceTableColumns.TX_TIME_CONG,
                PerformanceTableColumns.TX_WASTED_BW,
                PerformanceTableColumns.TX_WAIT_DATA,
                PerformanceTableColumns.LOCAL_LINK_INTEGRITY,
                PerformanceTableColumns.MARK_FECN,
                PerformanceTableColumns.LINK_ERROR_RECOVERIES,
                PerformanceTableColumns.LINK_DOWNED,
                PerformanceTableColumns.NUM_LANES_DOWN,
                PerformanceTableColumns.UNCORRECTABLE_ERRORS,
                PerformanceTableColumns.SW_PORT_CONGESTION

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
                                    perfData.getPortNumber(),
                                    destination.getName());
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
                if (listener == null || e.getValueIsAdjusting()) {
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
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.
             * MouseEvent )
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
                                JumpDestination.PERFORMANCE.getName());
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
        mTable.scrollRowToVisible(vId);
    }

}
