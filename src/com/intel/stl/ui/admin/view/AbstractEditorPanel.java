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
 *  File Name: AbstractEditorPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/16 22:08:15  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.intel.stl.ui.admin.IItemEditorListener;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;

public abstract class AbstractEditorPanel<E> extends JPanel {
    private static final long serialVersionUID = 6210030398358856720L;

    private JPanel namePanel;

    private JTextField nameField;

    private JPanel ctrPanel;

    private JButton saveBtn;

    private JButton resetBtn;

    private DocumentListener nameListener;

    private IItemEditorListener edtListener;

    public AbstractEditorPanel() {
        super();
        initComponent();
    }

    protected void initComponent() {
        setLayout(new BorderLayout(5, 5));

        JPanel panel = getNamePanel();
        add(panel, BorderLayout.NORTH);

        JComponent mainComp = getMainComponent();
        add(mainComp, BorderLayout.CENTER);

        panel = getControlPanel();
        add(panel, BorderLayout.SOUTH);
    }

    protected JPanel getNamePanel() {
        if (namePanel == null) {
            namePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            JLabel nameLabel =
                    ComponentFactory.getH3Label(
                            STLConstants.K2111_NAME.getValue(), Font.BOLD);
            namePanel.add(nameLabel);
            nameField = new JTextField(32);
            namePanel.add(nameField);
        }
        return namePanel;
    }

    protected abstract JComponent getMainComponent();

    protected JPanel getControlPanel() {
        if (ctrPanel == null) {
            ctrPanel = new JPanel();
            ctrPanel.setOpaque(false);
            installButtons(ctrPanel);
        }
        return ctrPanel;
    }

    protected void installButtons(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        saveBtn =
                ComponentFactory.getIntelActionButton(STLConstants.K3010_SAVE
                        .getValue());
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (edtListener != null) {
                    edtListener.onSave();
                }
            }
        });
        panel.add(saveBtn);

        resetBtn =
                ComponentFactory.getIntelActionButton(STLConstants.K1006_RESET
                        .getValue());
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (edtListener != null) {
                    edtListener.onReset();
                }
            }
        });
        panel.add(resetBtn);
    }

    protected DocumentListener getNameListener() {
        if (nameListener == null) {
            nameListener = new DocumentListener() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * javax.swing.event.DocumentListener#insertUpdate(javax.swing
                 * .event.DocumentEvent)
                 */
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try {
                        updateName(doc.getText(0, doc.getLength()));
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * javax.swing.event.DocumentListener#removeUpdate(javax.swing
                 * .event.DocumentEvent)
                 */
                @Override
                public void removeUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try {
                        updateName(doc.getText(0, doc.getLength()));
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * javax.swing.event.DocumentListener#changedUpdate(javax.swing
                 * .event.DocumentEvent)
                 */
                @Override
                public void changedUpdate(DocumentEvent e) {
                }

            };
        }
        return nameListener;
    }

    /**
     * <i>Description:</i>
     * 
     * @param text
     */
    protected void updateName(String text) {
        if (edtListener != null) {
            edtListener.nameChanged(text);
        }
    }

    public void setEditorListener(IItemEditorListener listener) {
        edtListener = listener;
    }

    public void setItem(final Item<E> item, final Item<E>[] items) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                // start name listening after we set the item properly
                nameField.getDocument().removeDocumentListener(
                        getNameListener());
                setItemName(item.getName());
                String[] appNames = new String[items.length];
                for (int i = 0; i < appNames.length; i++) {
                    appNames[i] = items[i].getName();
                }
                showItemObject(item.getObj(), appNames, item.isEditable());
                nameField.getDocument().addDocumentListener(getNameListener());

                saveBtn.setEnabled(item.isEditable());
                resetBtn.setEnabled(item.isEditable());
            }
        });
    }

    protected void setItemName(String name) {
        nameField.setText(name);
    }

    protected abstract void showItemObject(E obj, String[] itemNames,
            boolean isEditable);

    protected String getCurrentName() {
        return nameField.getText();
    }

    /**
     * 
     * <i>Description:</i> update an item with current content
     * 
     * @param item
     */
    public void updateItem(Item<E> item) {
        item.setName(getCurrentName());
        updateItemObject(item.getObj());
    }

    protected abstract void updateItemObject(E obj);
}
