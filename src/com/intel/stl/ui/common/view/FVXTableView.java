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
 *  File Name: FVXTableView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/04/08 19:43:40  rjtierne
 *  Archive Log:    Renamed mHeaderCol to mHeader
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/11 21:24:09  jijunwan
 *  Archive Log:    generic table view with table model
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/10 20:52:22  rjtierne
 *  Archive Log:    Support for new Setup Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/13 14:37:05  rjtierne
 *  Archive Log:    Added TableColumn attribute
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/05 17:41:47  jijunwan
 *  Archive Log:    removed unused import
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/29 03:57:19  jijunwan
 *  Archive Log:    performance table adjustment: sort by number, hide columns to save space
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/28 17:42:31  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: The FVXTableView class implements the view for a table using
 *  JXTable and JXHeader
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;

import com.intel.stl.ui.common.FVViewInterface;

public abstract class FVXTableView<T extends TableModel> extends JPanel
        implements FVViewInterface<T>, TableCellRenderer {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1078467233059343389L;

    protected T model;

    /**
     * The Table
     */
    protected JXTable mTable;

    /**
     * A column of the table
     */
    protected TableColumn mTblCol;

    /**
     * A header of the table
     */
    protected JXTableHeader mHeader;

    /**
     * Scroll pane on which the table resides
     */
    protected JScrollPane mScrollPane;

    /**
     * Height of the main scroll pane
     */
    protected static final int MAIN_SCROLL_PANE_HEIGHT = 250;

    /**
     * Width of the main scroll pane
     */
    protected static final int MAIN_SCROLL_PANE_WIDTH = 500;

    /**
     * Constructor for the Table class.
     * 
     * @param N
     *            /a
     * 
     * @return FVTableView
     * 
     */
    public FVXTableView(T model) {
        this.model = model;
        mTable = createTable(model);
        mScrollPane = new JScrollPane(mTable);
        initComponents();
        formatTable();
    } // FVTableView

    protected void initComponents() {
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
    }

    /**
     * 
     * Description: Returns whether the scrollpane viewport width is less than
     * the parent width
     * 
     * @return true/false
     */
    protected boolean getScrollableTracksViewportWidth() {
        return this.getPreferredSize().width < getParent().getWidth();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.table.FVViewInterface#getSelectedRowNumber()
     */
    @Override
    public int getSelectedRowNumber() {

        return mTable.getSelectedRow();
    } // getSelectedRowNumber

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.table.FVViewInterface#setModel(com.intel.hpc.stl
     * .ui.table.FVViewInterface)
     */
    @Override
    public void setModel(T pModel) {
        this.model = pModel;
        if (mTable != null) {
            mTable.setModel(pModel);
        }
    } // setModel

    public T getModel() {
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
     * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        return null;
    }

    protected abstract JXTable createTable(T model);

    /**
     * @return the mTable
     */
    public JXTable getTable() {
        return mTable;
    }

    /**
     * 
     * Description: formatTable is abstract and must be customized by the
     * implementer
     * 
     */
    public abstract void formatTable();

} // class FVTableView
