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
 *  File Name: DevicegroupsEditorPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/06 11:14:10  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/03/30 14:25:38  jijunwan
 *  Archive Log:    1) introduced IRendererModel to create renderer only we nee
 *  Archive Log:    2) removed #getName from IAttrRenderer to provide more flexibilities and let IRendererModel to take care which attribute should use which renderer, how to init it properly
 *  Archive Log:    3) improved to support repeatable and non-repeatable attributes. For non-repeatable attributes, we only can add once
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:46:09  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:08:14  jijunwan
 *  Archive Log:    added device group visualization on UI
 *  Archive Log:    some refactory to make the conf framework to be more general
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.devicegroups;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jdesktop.swingx.VerticalLayout;

import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.NumberNode;
import com.intel.stl.api.management.devicegroups.DGSelect;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.devicegroups.IncludeGroup;
import com.intel.stl.api.management.devicegroups.NodeDesc;
import com.intel.stl.api.management.devicegroups.NodeTypeAttr;
import com.intel.stl.ui.admin.impl.devicegroups.DeviceGroupRendererModel;
import com.intel.stl.ui.admin.impl.devicegroups.IAttributeListener;
import com.intel.stl.ui.admin.view.AbstractEditorPanel;
import com.intel.stl.ui.admin.view.IAttrRenderer;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;

public class DevicegroupsEditorPanel extends AbstractEditorPanel<DeviceGroup> {
    private static final long serialVersionUID = -4137802341150098071L;

    private JSplitPane mainComp;

    private DevicegroupSelectionPanel selectionPanel;

    private JToolBar dividerToolBar;

    private JButton addBtn;

    private JScrollPane scrollPane;

    private JPanel attrsPanel;

    private final List<DevicegroupAttrPanel> dgAttrPanels =
            new ArrayList<DevicegroupAttrPanel>();

    private IAttributeListener attrListener;

    private final DeviceGroupRendererModel rendererModel;

    /**
     * Description:
     * 
     * @param rendererModel
     */
    public DevicegroupsEditorPanel(DeviceGroupRendererModel rendererModel) {
        super();
        this.rendererModel = rendererModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.AbstractEditorPanel#getMainPanel()
     */
    @Override
    protected JComponent getMainComponent() {
        if (mainComp == null) {
            mainComp = new JSplitPane();
            // mainComp.setResizeWeight(0.5);
            final JToolBar toolbar = getDividerToolBar();
            mainComp.setUI(new BasicSplitPaneUI() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * javax.swing.plaf.basic.BasicSplitPaneUI#createDefaultDivider
                 * ()
                 */
                @Override
                public BasicSplitPaneDivider createDefaultDivider() {
                    BasicSplitPaneDivider divider =
                            new BasicSplitPaneDivider(this) {
                                private static final long serialVersionUID =
                                        -4518403689269742327L;

                                @Override
                                public int getDividerSize() {
                                    if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                                        return toolbar.getPreferredSize().width + 5;
                                    }
                                    return toolbar.getPreferredSize().height + 5;
                                }
                            };

                    divider.setLayout(new GridBagLayout());
                    divider.add(toolbar, new GridBagConstraints());

                    return divider;
                }

            });
            JComponent left = getSelctionPanel();
            mainComp.setLeftComponent(left);
            JComponent right = getAttributesPanel();
            mainComp.setRightComponent(right);
        }
        return mainComp;
    }

    protected JToolBar getDividerToolBar() {
        if (dividerToolBar == null) {
            dividerToolBar = new JToolBar(JToolBar.VERTICAL);
            dividerToolBar.setFloatable(false);
            dividerToolBar.addMouseMotionListener(new MouseAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent
                 * )
                 */
                @Override
                public void mouseMoved(MouseEvent e) {
                    dividerToolBar.setCursor(Cursor
                            .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

            });

            addBtn = new JButton(UIImages.MOVE.getImageIcon());
            addBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (attrListener != null) {
                        attrListener.onAdd(selectionPanel.getSelectorName());
                    }
                }
            });
            dividerToolBar.add(addBtn);
        }
        return dividerToolBar;
    }

    public DevicegroupSelectionPanel getSelctionPanel() {
        if (selectionPanel == null) {
            selectionPanel = new DevicegroupSelectionPanel();
        }
        return selectionPanel;
    }

    protected JComponent getAttributesPanel() {
        if (scrollPane == null) {
            attrsPanel = new JPanel(new VerticalLayout(10));
            attrsPanel.setBackground(UIConstants.INTEL_WHITE);

            scrollPane = new JScrollPane(attrsPanel);
            scrollPane.setBackground(UIConstants.INTEL_WHITE);
            scrollPane.setBorder(BorderFactory
                    .createTitledBorder(STLConstants.K2112_ATTRIBUTES
                            .getValue()));

        }
        return scrollPane;
    }

    public void setAttributeListener(IAttributeListener listener) {
        this.attrListener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.AbstractEditorPanel#showItemObject(java.lang
     * .Object, java.lang.String[], boolean)
     */
    @Override
    protected void showItemObject(DeviceGroup dg, String[] itemNames,
            boolean isEditable) {
        rendererModel.setDgNames(itemNames);

        attrsPanel.removeAll();
        dgAttrPanels.clear();
        List<NumberNode> ids = dg.getIDs();
        if (ids != null) {
            for (NumberNode id : ids) {
                addAttr(id.getType(), id, isEditable);
            }
        }
        List<NodeTypeAttr> nodeTypes = dg.getNodeTypes();
        if (nodeTypes != null) {
            for (NodeTypeAttr nodeType : nodeTypes) {
                addAttr(nodeType.getType(), nodeType, isEditable);
            }
        }
        List<NodeDesc> descs = dg.getNodeDesc();
        if (descs != null) {
            for (NodeDesc desc : descs) {
                addAttr(desc.getType(), desc, isEditable);
            }
        }
        List<DGSelect> selects = dg.getSelects();
        if (selects != null) {
            for (DGSelect sel : selects) {
                addAttr(sel.getType(), sel, isEditable);
            }
        }
        List<IncludeGroup> incGroups = dg.getIncludeGroups();
        if (incGroups != null) {
            for (IncludeGroup incGroup : incGroups) {
                addAttr(incGroup.getType(), incGroup, isEditable);
            }
        }

        addBtn.setEnabled(isEditable);
        revalidate();
        repaint();
    }

    private <E extends IAttribute> void addAttr(String type, E attr,
            boolean isEditable) {
        DevicegroupAttrPanel attrPanel =
                new DevicegroupAttrPanel(this, rendererModel);
        attrPanel.setAttr(type, attr, isEditable);
        attrsPanel.add(attrPanel);
        dgAttrPanels.add(attrPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.view.AbstractEditorPanel#updateItemObject(java
     * .lang.Object)
     */
    @Override
    protected void updateItemObject(DeviceGroup obj) {
        obj.setName(getCurrentName());
        obj.clear();
        for (DevicegroupAttrPanel attrPanel : dgAttrPanels) {
            IAttrRenderer<? extends IAttribute> renderer =
                    attrPanel.getAttrRenderer();
            if (renderer != null) {
                IAttribute attr = renderer.getAttr();
                if (attr != null) {
                    attr.installDevieGroup(obj);
                }
            }
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param appAttrPanel
     */
    public void removeEditor(DevicegroupAttrPanel appAttrPanel) {
        attrsPanel.remove(appAttrPanel);
        dgAttrPanels.remove(appAttrPanel);
        revalidate();
        repaint();
        if (attrListener != null && appAttrPanel != null) {
            IAttrRenderer<? extends IAttribute> renderer =
                    appAttrPanel.getAttrRenderer();
            if (renderer != null) {
                attrListener.onRemove(renderer.getAttr());
            }
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param attr
     */
    public void addAttr(IAttribute attr) {
        addAttr(attr.getType(), attr, true);
        revalidate();
        repaint();
    }
}
