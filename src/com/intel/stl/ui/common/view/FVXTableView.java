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
 *  File Name: FVXTableView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:26:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
