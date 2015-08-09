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
 *  File Name: ItemsPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/10 22:45:37  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:38:18  jijunwan
 *  Archive Log:    init version to support Application management
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.intel.stl.ui.admin.IItemListListener;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;

public class ItemListPanel<T> extends JPanel {
    private static final long serialVersionUID = -8018647522341749109L;

    private final String name;

    private JLabel titleLabel;

    private JXList itemList;

    private JPanel ctrPanel;

    private JButton addBtn;

    private JButton removeBtn;

    private final List<IItemListListener> listeners =
            new CopyOnWriteArrayList<IItemListListener>();

    /**
     * Description:
     * 
     * @param name
     */
    public ItemListPanel(String name) {
        super();
        this.name = name;
        initComponent();
    }

    @SuppressWarnings("rawtypes")
    protected void initComponent() {
        setLayout(new BorderLayout(5, 5));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        UIConstants.INTEL_TABLE_BORDER_GRAY, 1, true),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        // panel.setBorder(BorderFactory.createLineBorder(
        // UIConstants.INTEL_TABLE_BORDER_GRAY, 1, true));
        titleLabel = ComponentFactory.getH3Label(name, Font.BOLD);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 2, 0, UIConstants.INTEL_ORANGE),
                BorderFactory.createEmptyBorder(5, 0, 5, 0)));
        panel.add(titleLabel, BorderLayout.NORTH);

        itemList = new JXList();
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Item<?> item = (Item<?>) value;
                JLabel label =
                        ComponentFactory.getH5Label(item.getName(), Font.PLAIN);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
                if (isSelected) {
                    label.setBackground(UIConstants.INTEL_BLUE);
                    label.setForeground(UIConstants.INTEL_WHITE);
                } else {
                    label.setBackground(UIConstants.INTEL_WHITE);
                }
                if (!item.isEditable()) {
                    label.setIcon(UIImages.UNEDITABLE.getImageIcon());
                }
                return label;
            }
        });
        itemList.setRolloverEnabled(true);
        ColorHighlighter rooloverHighlighter =
                new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW,
                        UIConstants.INTEL_LIGHT_BLUE, UIConstants.INTEL_WHITE);
        ColorHighlighter evenHighlighter =
                new ColorHighlighter(HighlightPredicate.EVEN,
                        UIConstants.INTEL_TABLE_ROW_GRAY, null);
        itemList.setHighlighters(evenHighlighter, rooloverHighlighter);
        itemList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Item<?> item = (Item<?>) itemList.getSelectedValue();
                if (item != null) {
                    removeBtn.setEnabled(item.isEditable());
                    fireItemSelected(item);
                }
            }
        });
        JScrollPane pane = new JScrollPane(itemList);
        panel.add(pane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);

        ctrPanel = new JPanel();
        ctrPanel.setOpaque(false);
        installButtons(ctrPanel);
        add(ctrPanel, BorderLayout.SOUTH);
    }

    protected void installButtons(JPanel panel) {
        addBtn = ComponentFactory.getIntelActionButton("+");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireAddItem();
            }
        });
        panel.add(addBtn);

        removeBtn = ComponentFactory.getIntelActionButton("-");
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireRemoveItem(itemList.getSelectedValue());
            }
        });
        panel.add(removeBtn);
    }

    /**
     * <i>Description:</i>
     * 
     * @param model
     */
    public void setListModel(ListModel<Item<T>> model) {
        itemList.setModel(model);
    }

    protected void fireItemSelected(Object selectedValue) {
        if (selectedValue == null) {
            return;
        }

        long id = ((Item<?>) selectedValue).getId();
        for (IItemListListener listener : listeners) {
            listener.onSelect(id);
        }
    }

    protected void fireAddItem() {
        for (IItemListListener listener : listeners) {
            listener.onAdd();
        }
    }

    protected void fireRemoveItem(Object selectedValue) {
        if (selectedValue == null) {
            return;
        }

        long id = ((Item<?>) selectedValue).getId();
        for (IItemListListener listener : listeners) {
            listener.onRemove(id);
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param selectedValue
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        itemList.addListSelectionListener(listener);
    }

    public void removeListSelectionListener(ListSelectionListener listener) {
        itemList.removeListSelectionListener(listener);
    }

    /**
     * <i>Description:</i>
     * 
     * @param index
     */
    public void selectItem(final int index) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                itemList.setSelectedIndex(index);
                Item<?> item = (Item<?>) itemList.getSelectedValue();
                removeBtn.setEnabled(item.isEditable());
                revalidate();
                repaint();
            }
        });
    }

    /**
     * <i>Description:</i>
     * 
     * @param listener
     */
    public void addItemListListener(IItemListListener listener) {
        listeners.add(listener);
    }

    /**
     * <i>Description:</i>
     * 
     * @param listener
     */
    public void removeItemListListener(IItemListListener listener) {
        listeners.remove(listener);
    }

}
