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
 *  File Name: FVHeaderRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3.2.1  2015/08/12 15:26:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/10 22:46:39  jijunwan
 *  Archive Log:    added tooltip
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/09 19:09:45  rjtierne
 *  Archive Log:    Added new constructor to accept JXTable
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 16:20:48  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/04 19:23:20  rjtierne
 *  Archive Log:    Added call to constructor to set opaque to true.  Changed the
 *  Archive Log:    background and foreground of the heading columns.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:09:07  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enhances the appearance of the table headers through cell rendering
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;

import com.intel.stl.ui.common.UIConstants;

public class FVHeaderRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1072776307198747266L;

    /**
     * Table cell renderer for the header portion of the table
     */
    DefaultTableCellRenderer mHeaderRenderer;

    /**
     * 
     * Description: Constructor for the FVHeaderRenderer class
     * 
     * @param table
     *            - table to be rendered
     */
    public FVHeaderRenderer(JTable table) {

        setOpaque(true);

        mHeaderRenderer =
                (DefaultTableCellRenderer) table.getTableHeader()
                        .getDefaultRenderer();

        mHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setWidth(10);

    } // FVHeaderRenderer

    public FVHeaderRenderer(JXTable table) {

        setOpaque(true);

        mHeaderRenderer =
                (DefaultTableCellRenderer) table.getTableHeader()
                        .getDefaultRenderer();

        mHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setWidth(10);

    } // FVHeaderRenderer

    /**
     * Get a cell rendered as specified; called when each column has added a
     * header cell renderer to it
     * 
     * @param table
     *            the JTable
     * 
     * @param value
     *            the value to assign to the cell at [row, column]
     * 
     * @param isSelected
     *            true if selected
     * 
     * @param hasFocus
     *            true if cell has focus
     * 
     * @param row
     *            the row of the cell to render
     * 
     * @param column
     *            the column of the cell to render
     * 
     * @return a rendered table cell
     * 
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell =
                (JLabel) mHeaderRenderer.getTableCellRendererComponent(table,
                        value, isSelected, hasFocus, row, column);

        cell.setFont(UIConstants.H4_FONT.deriveFont(Font.BOLD));
        cell.setForeground(UIConstants.INTEL_WHITE);
        cell.setBackground(UIConstants.INTEL_BLUE);
        cell.setToolTipText(value.toString());

        return cell;
    } // getTableCellRendererComponent

} // FVHeaderRenderer
