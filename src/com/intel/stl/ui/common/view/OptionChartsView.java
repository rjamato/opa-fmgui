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
 *  File Name: OptionChartsView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.8  2015/03/27 18:40:02  jijunwan
 *  Archive Log:    moved extended JList and ComboBox to package common.view
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/03/18 16:00:19  jypak
 *  Archive Log:    The layout manager for toolbar that contains the button popups is changed to GridBagLayout. Also, changed initial button texts to be initial enum types.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/03/18 12:05:00  jypak
 *  Archive Log:    Changed from ListSelectionListener to MouseListener so that a mouse click of current selected item from the list at least hide the popup.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/03/17 15:56:17  jypak
 *  Archive Log:    DataType, HistoryType JComboBox have been replaced with button popup to save space.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/13 23:05:36  jijunwan
 *  Archive Log:    PR 126911 - Even though HFI does not represent "Internal" data under opatop, FV still provides drop down for "Internal"
 *  Archive Log:     -- added a feature to be able to disable unsupported types
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/03 21:12:36  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/05 21:52:31  jijunwan
 *  Archive Log:    improved IntelComboBoxUI to support editable Combo Box
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/22 18:41:41  jijunwan
 *  Archive Log:    added DataType support for chart view
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.main.view.IDataTypeListener;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.HistoryType;

public class OptionChartsView extends ChartsView {
    private static final long serialVersionUID = 3144008795121961486L;

    private ExJXList<DataType> dataTypeList;

    private ExJXList<HistoryType> historyTypeList;

    private IDataTypeListener<DataType> groupInfoTypeListener;

    private IDataTypeListener<HistoryType> historyTypeListener;

    private MouseListener groupInfoTypeListSelectionListener;

    private MouseListener historyTypeListSelectionListener;

    private JButton dataTypeBtn;

    private JButton historyTypeBtn;

    private ButtonPopup dataPopupOptions;

    private ButtonPopup historyPopupOptions;

    private DataType dataType;

    private DataType prevDataType;

    private HistoryType historyType;

    private HistoryType prevHistoryType;

    private JComponent ctrPanel;

    private JToolBar toolBar;

    /**
     * Description:
     * 
     * @param title
     * @param chartCreator
     * @param dataType
     */
    public OptionChartsView(String title, IChartCreator chartCreator) {
        super(title, chartCreator);
        initView();
    }

    private void initView() {
        ctrPanel = getExtraComponent();

        toolBar = new JToolBar();
        toolBar.setLayout(new GridBagLayout());
        toolBar.setFloatable(false);
        toolBar.setBackground(UIConstants.INTEL_WHITE);

        ctrPanel.add(toolBar);
    }

    public void setTypes(DataType... types) {
        if (types != null && types.length > 0) {
            dataTypeList = new ExJXList<DataType>(types);
            dataTypeList.setDisabledColor(UIConstants.INTEL_LIGHT_GRAY);
            dataType = types[0];
            prevDataType = types[0];
            setDataTypeListener(groupInfoTypeListener);
            addDataTypeButton();
        }
    }

    public void setDisbaledDataTypes(DataType... types) {
        if (dataTypeList != null) {
            dataTypeList.setDisabledItem(types);
        }
    }

    /**
     * @param groupInfoTypeActionListener
     *            the dataTypeListener to set
     */
    public void setDataTypeListener(final IDataTypeListener<DataType> listener) {

        groupInfoTypeListener = listener;
        if (dataTypeList != null && listener != null) {

            if (groupInfoTypeListSelectionListener != null) {
                dataTypeList
                        .removeMouseListener(groupInfoTypeListSelectionListener);
            }
            dataTypeList.addMouseListener(

            groupInfoTypeListSelectionListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)
                            && e.getClickCount() == 1) {
                        DataType type =
                                (DataType) dataTypeList.getSelectedValue();
                        if (prevDataType != type) {
                            prevDataType = type;
                            if (dataTypeBtn != null) {
                                dataTypeBtn.setText(dataTypeList
                                        .getSelectedValue().toString());
                            }

                            listener.onDataTypeChange(type);
                        }
                        if (dataPopupOptions != null
                                && dataPopupOptions.isVisible()) {
                            dataPopupOptions.hide();
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            });

            setType(dataType);
        }
    }

    public void setType(DataType type) {
        dataTypeList.setSelectedValue(type, true);
    }

    protected void addDataTypeButton() {
        dataTypeBtn =
                new JButton(dataType.getName(),
                        UIImages.DATA_TYPE.getImageIcon());
        dataTypeBtn.setFocusable(false);
        dataTypeBtn.setToolTipText(STLConstants.K0747_DATA_TYPE.getValue());
        dataTypeBtn.setBackground(UIConstants.INTEL_WHITE);
        ActionListener usrOptionsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!dataPopupOptions.isVisible()) {
                    dataPopupOptions.show();
                } else {
                    dataPopupOptions.hide();
                }
            }
        };

        dataTypeBtn.addActionListener(usrOptionsListener);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.weightx = 1;

        toolBar.add(dataTypeBtn, gc);

        dataTypeList.setLayout(new GridBagLayout());
        dataTypeList.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        dataPopupOptions = new ButtonPopup(dataTypeBtn, dataTypeList) {

            @Override
            public void onShow() {
            }

            @Override
            public void onHide() {

            }

        };

    }

    public void setHistoryTypes(HistoryType... types) {
        if (types != null && types.length > 0) {
            historyTypeList = new ExJXList<HistoryType>(types);
            historyTypeList.setDisabledColor(UIConstants.INTEL_LIGHT_GRAY);
            historyType = types[0];
            prevHistoryType = types[0];
            setHistoryTypeListener(historyTypeListener);
            addHistoryTypeButton();
        }
    }

    public void setDisabledHistoryTypes(HistoryType... types) {
        if (historyTypeList != null) {
            historyTypeList.setDisabledItem(types);
        }
    }

    /**
     * @param groupInfoTypeActionListener
     *            the dataTypeListener to set
     */
    public void setHistoryTypeListener(
            final IDataTypeListener<HistoryType> listener) {
        historyTypeListener = listener;
        if (historyTypeList != null && listener != null) {
            if (historyTypeListSelectionListener != null) {
                historyTypeList
                        .removeMouseListener(historyTypeListSelectionListener);
            }
            historyTypeList.addMouseListener(

            historyTypeListSelectionListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)
                            && e.getClickCount() == 1) {
                        HistoryType type =
                                (HistoryType) historyTypeList
                                        .getSelectedValue();
                        if (prevHistoryType != type) {
                            prevHistoryType = type;
                            if (historyTypeBtn != null) {
                                historyTypeBtn.setText(historyTypeList
                                        .getSelectedValue().toString());
                            }

                            listener.onDataTypeChange(type);
                        }
                        if (historyPopupOptions != null
                                && historyPopupOptions.isVisible()) {
                            historyPopupOptions.hide();
                        }
                    }

                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            });

            setHistoryType(historyType);
        }
    }

    public void setHistoryType(HistoryType type) {
        historyTypeList.setSelectedValue(type, true);
    }

    protected void addHistoryTypeButton() {
        historyTypeBtn =
                new JButton(historyType.getName(),
                        UIImages.HISTORY_ICON.getImageIcon());
        historyTypeBtn.setFocusable(false);
        historyTypeBtn.setToolTipText(STLConstants.K1113_HISTORY_SCOPE
                .getValue());
        historyTypeBtn.setBackground(UIConstants.INTEL_WHITE);
        ActionListener usrOptionsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!historyPopupOptions.isVisible()) {
                    historyPopupOptions.show();
                } else {
                    historyPopupOptions.hide();
                }
            }
        };

        historyTypeBtn.addActionListener(usrOptionsListener);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 1;
        gc.weightx = 1;

        toolBar.add(historyTypeBtn, gc);

        historyTypeList.setLayout(new GridBagLayout());
        historyTypeList.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        historyPopupOptions = new ButtonPopup(historyTypeBtn, historyTypeList) {

            @Override
            public void onShow() {
            }

            @Override
            public void onHide() {

            }

        };

    }
}
