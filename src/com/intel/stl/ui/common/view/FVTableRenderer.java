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
 *  File Name: FVTableRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:26:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/03/05 22:32:17  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/22 19:31:24  jijunwan
 *  Archive Log:    changed table selection background color
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/05 17:41:13  jijunwan
 *  Archive Log:    added N/A as the default text for null value in a table
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/16 16:20:48  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/08 17:32:57  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/04 19:27:35  rjtierne
 *  Archive Log:    Corrected table row colors.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:09:36  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Enhances the appearance of the table through cell rendering
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

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;

public class FVTableRenderer extends DefaultTableCellRenderer {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7453097750244699779L;

    /**
     * 
     * Description: Constructor for the FVTableRender class
     * 
     */
    public FVTableRenderer() {
        super();
        setOpaque(true);
    }

    /***************************************************************************
     * Get a cell rendered as specified; called when each column has added a
     * table cell renderer to it
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
     **************************************************************************/
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	
    	Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
    	
        if (value==null) {
            ((JLabel)cell).setText(STLConstants.K0383_NA.getValue());
        }
        
        if (isSelected) {
            cell.setFont(UIConstants.H5_FONT.deriveFont(Font.BOLD));
            cell.setForeground(UIConstants.INTEL_WHITE);
            cell.setBackground(UIConstants.INTEL_MEDIUM_BLUE);
        } else {
            cell.setFont(UIConstants.H5_FONT);
            cell.setForeground(UIConstants.INTEL_DARK_GRAY);
            if ((row % 2) == 0) {
                cell.setBackground(UIConstants.INTEL_WHITE);
            } else {
                cell.setBackground(UIConstants.INTEL_TABLE_ROW_GRAY);
            }
        }
        return this;
    }

} // FVTableRenderer
