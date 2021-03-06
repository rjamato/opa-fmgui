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
 *  File Name: AttrPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/09/21 21:40:31  jijunwan
 *  Archive Log:    PR 130229 - The text component of all editable combo boxes should provide validation of the input
 *  Archive Log:    - adapt to the new IntelComboBoxUI
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/17 18:53:52  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/12 17:39:39  rjtierne
 *  Archive Log:    PR 128624 - Klocwork and FindBugs fixes for UI
 *  Archive Log:    In getAttrRenderer(), use equals() to compare strings name and currentRendererName.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/06 11:14:11  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/30 14:25:39  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:43:51  jijunwan
 *  Archive Log:    applied IAttribute on the item editor framework
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:08:15  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.intel.stl.api.management.IAttribute;
import com.intel.stl.ui.admin.impl.IRendererModel;
import com.intel.stl.ui.common.ExComboBoxModel;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.ExComboBox;
import com.intel.stl.ui.common.view.IntelComboBoxUI;

public abstract class AbstractAttrPanel extends JPanel {
    private static final long serialVersionUID = 8015988954981766653L;

    private static final String INIT = "init";

    private static final String STARTED = "started";

    private JPanel namePanel;

    private JPanel editorPanel;

    protected ExComboBox<String> typeList;

    protected JButton removeBtn;

    protected IRendererModel rendererModel;

    protected String currentRendererName;

    protected IAttrRenderer<? extends IAttribute> currentRenderer;

    public AbstractAttrPanel(IRendererModel rendererModel) {
        super();
        this.rendererModel = rendererModel;
        initComponent();
    }

    protected void initComponent() {
        setLayout(new BorderLayout(5, 5));
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createLineBorder(UIConstants.INTEL_BORDER_GRAY, 1, true),
                BorderFactory.createEmptyBorder(2, 5, 2, 2)));

        editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBorder(BorderFactory
                .createLineBorder(UIConstants.INTEL_BORDER_GRAY));
        add(editorPanel, BorderLayout.CENTER);

        ExComboBoxModel<String> comboModel =
                new ExComboBoxModel<String>(rendererModel.getRendererNames(),
                        false);
        final Border rendererBorder =
                BorderFactory.createCompoundBorder(BorderFactory
                        .createMatteBorder(1, 1, 1, 0, UIConstants.INTEL_GRAY),
                        BorderFactory.createEmptyBorder(0, 2, 0, 2));
        typeList = new ExComboBox<String>(comboModel) {
            private static final long serialVersionUID = 826992741561960993L;

            @Override
            protected void decorateDisabledCell(JLabel label,
                    boolean isDisabled, int index) {
                label.setIcon(isDisabled ? UIImages.UNEDITABLE.getImageIcon()
                        : null);
                if (index == -1) {
                    label.setBorder(rendererBorder);
                }
            }

        };
        typeList.setDisabledColor(UIConstants.INTEL_LIGHT_GRAY);
        IntelComboBoxUI ui = new IntelComboBoxUI();
        typeList.setUI(ui);
        typeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String old = currentRendererName;
                if (currentRenderer != null) {
                    remove(currentRenderer.getView());
                } else {
                    remove(editorPanel);
                }
                IAttrRenderer<?> renderer = getAttrRenderer();
                JComponent view = null;
                if (renderer != null) {
                    view = renderer.getView();
                }
                add(view, BorderLayout.CENTER);
                if (!removeBtn.isEnabled()) {
                    removeBtn.setEnabled(true);
                }
                revalidate();
                repaint();
                if (old != null) {
                    onChangeRenderer(old, currentRendererName);
                }
            }
        });

        JLabel hintLabel =
                ComponentFactory.getH4Label(
                        STLConstants.K2117_SEL_ATTR_TYPE.getValue(), Font.BOLD);
        hintLabel.setForeground(UIConstants.INTEL_BLUE);
        hintLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        hintLabel.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent
             * )
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                ExComboBoxModel<String> comboModel =
                        (ExComboBoxModel<String>) typeList.getModel();
                String toSelect = comboModel.getFirstAvailableItem();
                if (toSelect != null) {
                    ((CardLayout) namePanel.getLayout()).show(namePanel,
                            STARTED);
                    typeList.setSelectedItem(toSelect);
                    onAddAttr();
                }
            }

        });
        namePanel = new JPanel(new CardLayout());
        // namePanel.setOpaque(false);
        namePanel.add(hintLabel, INIT);
        namePanel.add(typeList, STARTED);
        add(namePanel, BorderLayout.WEST);

        removeBtn =
                ComponentFactory.getImageButton(UIImages.CLOSE_GRAY
                        .getImageIcon());
        removeBtn.setEnabled(false);
        removeBtn.setRolloverEnabled(true);
        removeBtn.setRolloverIcon(UIImages.CLOSE_RED.getImageIcon());
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRemoveAttr();
            }
        });
        add(removeBtn, BorderLayout.EAST);
    }

    public IAttrRenderer<? extends IAttribute> getAttrRenderer() {
        String name = (String) typeList.getSelectedItem();
        if (!name.equals(currentRendererName)) {
            currentRendererName = name;
            try {
                if (rendererModel != null) {
                    currentRenderer = rendererModel.getRenderer(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
                currentRenderer = null;
            }
        }
        return currentRenderer;
    }

    public String getRendererName() {
        return (String) typeList.getSelectedItem();
    }

    @SuppressWarnings("unchecked")
    public <E extends IAttribute> void setAttr(String type, E attr,
            boolean isEditable) {
        typeList.setSelectedItem(type);
        IAttrRenderer<E> renderer = (IAttrRenderer<E>) getAttrRenderer();
        if (renderer != null) {
            renderer.setAttr(attr);
            renderer.setEditable(isEditable);
        }
        typeList.setEnabled(isEditable);
        ((CardLayout) namePanel.getLayout()).show(namePanel, STARTED);
        removeBtn.setEnabled(isEditable);
    }

    protected abstract void onAddAttr();

    protected abstract void onRemoveAttr();

    protected abstract void onChangeRenderer(String oldRenderer,
            String newRenderer);
}
