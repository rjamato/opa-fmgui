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
 *  File Name: AbstrctConfView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:27:17  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/18 01:40:00  fisherma
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss.  The code changes address issue #2 reported in the bug.  Adding common dialog to display errors.  Needs further appearance improvements.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/11 21:16:04  jijunwan
 *  Archive Log:    added remove and deploy features
 *  Archive Log:
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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListModel;

import com.intel.stl.ui.admin.IConfListener;
import com.intel.stl.ui.admin.IItemListListener;
import com.intel.stl.ui.admin.Item;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;

public abstract class AbstractConfView<T, E extends AbstractEditorPanel<T>>
        extends JPanel {
    private static final long serialVersionUID = 8561299073984852795L;

    private final String name;

    protected ItemListPanel<T> selectionPanel;

    protected E editorPanel;

    protected JPanel ctrPanel;

    protected JButton deployBtn;

    private IConfListener listener;

    /**
     * Description:
     * 
     */
    public AbstractConfView(String name) {
        super();
        this.name = name;
        initComponent();
    }

    protected void initComponent() {
        setLayout(new BorderLayout(5, 5));

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        selectionPanel = createItemSelectionPanel();
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pane.setLeftComponent(selectionPanel);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setOpaque(false);
        editorPanel = createrEditorPanel();
        editorPanel.setBorder(BorderFactory.createLineBorder(
                UIConstants.INTEL_TABLE_BORDER_GRAY, 1, true));
        panel.add(editorPanel, BorderLayout.CENTER);

        ctrPanel = new JPanel();
        ctrPanel.setOpaque(false);
        installButtons(ctrPanel);
        panel.add(ctrPanel, BorderLayout.SOUTH);

        pane.setRightComponent(panel);

        add(pane, BorderLayout.CENTER);
    }

    protected ItemListPanel<T> createItemSelectionPanel() {
        return new ItemListPanel<T>(name);
    }

    protected abstract E createrEditorPanel();

    protected void installButtons(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        deployBtn =
                ComponentFactory.getIntelActionButton(STLConstants.K2131_DEPLOY
                        .getValue());
        deployBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onApply(false);
            }
        });
        panel.add(deployBtn);

    }

    /**
     * @return the editorPanel
     */
    public E getEditorPanel() {
        return editorPanel;
    }

    public void addItemListListener(IItemListListener listener) {
        selectionPanel.addItemListListener(listener);
    }

    public void removeItemListListener(IItemListListener listener) {
        selectionPanel.removeItemListListener(listener);
    }

    public void setConfListener(IConfListener listener) {
        this.listener = listener;
    }

    public void setListModel(ListModel<Item<T>> model) {
        selectionPanel.setListModel(model);
        selectionPanel.repaint();
    }

    /**
     * <i>Description:</i>
     * 
     * @param first
     */
    public void selectItem(int index) {
        selectionPanel.selectItem(index);
        revalidate();
        repaint();
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void updateItems() {
        selectionPanel.repaint();
    }

    /**
     * <i>Description:</i>
     * 
     */
    public int confirmDiscard() {
        return Util.showConfirmDialog(this,
                UILabels.STL50081_ABANDON_CHANGES_MESSAGE.getDescription());
    }

}
