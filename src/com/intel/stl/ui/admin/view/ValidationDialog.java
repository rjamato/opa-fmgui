/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: ValidationDialog.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/02 16:53:23  jypak
 *  Archive Log:    Spelling error correct
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/02 13:33:05  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/11 21:16:04  jijunwan
 *  Archive Log:    added remove and deploy features
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:37  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.ValidationModel;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.FVHeaderRenderer;
import com.intel.stl.ui.common.view.OptionDialog;

public class ValidationDialog extends OptionDialog {
    private static final long serialVersionUID = -6303832281521441888L;

    private JPanel mainPanel;

    private JLabel msgLabel;

    private JXTable validateTable;

    /**
     * Description:
     * 
     * @param owner
     * @param title
     */
    public ValidationDialog(Component owner, String title) {
        super(owner, title, JOptionPane.OK_CANCEL_OPTION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.OptionDialog#getMainPanel()
     */
    @Override
    protected JComponent getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout(5, 5));
            msgLabel = ComponentFactory.getH3Label(null, Font.PLAIN);
            // msgLabel.setForeground(UIConstants.INTEL_BLUE);
            msgLabel.setHorizontalAlignment(JLabel.LEADING);
            mainPanel.add(msgLabel, BorderLayout.NORTH);

            validateTable = createTable();
            JScrollPane pane = new JScrollPane(validateTable);
            validateTable.setPreferredScrollableViewportSize(new Dimension(100,
                    100));
            mainPanel.add(pane, BorderLayout.CENTER);
        }
        return mainPanel;
    }

    protected JXTable createTable() {
        final JXTable table = new JXTable();
        table.setVisibleRowCount(5);
        table.setHighlighters(HighlighterFactory.createAlternateStriping(
                UIConstants.INTEL_WHITE, UIConstants.INTEL_TABLE_ROW_GRAY));
        table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = -5487920883938816871L;

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.table.DefaultTableCellRenderer#
             * getTableCellRendererComponent(javax.swing.JTable,
             * java.lang.Object, boolean, boolean, int, int)
             */
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel comp =
                        (JLabel) super.getTableCellRendererComponent(table,
                                value, isSelected, hasFocus, row, column);
                comp.setToolTipText(value.toString());
                return comp;
            }

        });
        table.setDefaultRenderer(Boolean.class, new TableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                final Callable<?> quickFix = (Callable<?>) value;
                if (quickFix != null) {
                    JButton button = new JButton("Quick Fix");
                    return button;
                } else {
                    return new JLabel("N/A");
                }
            }

        });
        table.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent
             * )
             */
            @SuppressWarnings("rawtypes")
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                int col = table.columnAtPoint(point);
                if (row == -1 || col == -10) {
                    return;
                }

                Object val = table.getValueAt(row, col);
                if (val != null) {
                    Callable quickFix = (Callable) val;
                    try {
                        quickFix.call();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        showMessage(StringUtils.getErrorMessage(e1));
                    }
                }
            }

        });
        return table;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.OptionDialog#setSize()
     */
    @Override
    protected void setSize() {
        setSize(600, 270);
        // pack();
    }

    public void setValidationTableModel(ValidationModel<?> model) {
        if (validateTable != null) {
            validateTable.setModel(model);

            JTableHeader header = validateTable.getTableHeader();
            header.setDefaultRenderer(new FVHeaderRenderer(validateTable));
        }
        // validateTable.getColumn(0).setMaxWidth(120);
        // validateTable.getColumn(3).setMaxWidth(60);
    }

    public void showMessage(String text) {
        if (msgLabel != null) {
            msgLabel.setText(text);
            revalidate();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.OptionDialog#onClose()
     */
    @Override
    protected void onClose() {
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void updateIssues() {
        validateTable.packAll();
    }

}
