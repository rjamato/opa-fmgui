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
 *  File Name: FVTableView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/08 19:43:18  rjtierne
 *  Archive Log:    Corrected warning due to generic usage
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/23 18:26:54  rjtierne
 *  Archive Log:    Remove main panel and added UI components
 *  Archive Log:    to "this" panel
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:47:19  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/09 21:03:54  rjtierne
 *  Archive Log:    This abstract class is now extending from JPanel instead of JCard
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/04 19:29:12  rjtierne
 *  Archive Log:    Class is now abstract and extends JCard instead of JPanel
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:09:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: The FVTableView class implements the view for a table
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.intel.stl.ui.common.FVViewInterface;

public abstract class FVTableView extends JPanel implements
        FVViewInterface<TableModel>, TableCellRenderer {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1078467233059343389L;

    /**
     * The Table
     */
    protected JTable mTable;

    /**
     * A column of the table
     */
    protected TableColumn mTblCol;

    /**
     * A header column of the table
     */
    protected JTableHeader mHeaderCol;

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
    public FVTableView() {

    } // FVTableView

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
    public void setModel(TableModel pModel) {
        mTable.setModel(pModel);
    } // setModel

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

    /**
     * 
     * Description: formatTable is abstract and must be customized by the
     * implementer
     * 
     */
    public abstract void formatTable();

} // class FVTableView
