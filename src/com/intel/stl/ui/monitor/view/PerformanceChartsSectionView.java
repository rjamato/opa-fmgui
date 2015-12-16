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
 *  File Name: PerformanceTableSectionView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.23  2015/08/17 18:54:25  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2015/08/05 04:04:48  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2015/06/25 20:50:01  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2015/05/14 17:43:09  jijunwan
 *  Archive Log:    PR 127700 - Delta data on host performance display is accumulating
 *  Archive Log:    - corrected delta value calculation
 *  Archive Log:    - changed to display data/pkts rate rather than delta on chart and table
 *  Archive Log:    - updated chart unit to show rate
 *  Archive Log:    - renamed the following classes to reflect we are dealing with rate
 *  Archive Log:      DataChartRangeUpdater -> DataRateChartRangeUpdater
 *  Archive Log:      PacketChartRangeUpdater -> PacketRateChartRangeUpdater
 *  Archive Log:      DataChartScaleGroupManager -> DataRateChartScaleGroupManager
 *  Archive Log:      PacketChartScaleGroupManager -> PacketRateChartScaleGroupManager
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2015/04/01 19:54:06  jijunwan
 *  Archive Log:    fixed the following bugs
 *  Archive Log:    1) no link quality clear when we change preview port
 *  Archive Log:    2) loss data when we go from node performance view to port performance view and then go back to node performance view
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/03/27 18:40:04  jijunwan
 *  Archive Log:    moved extended JList and ComboBox to package common.view
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/03/18 16:00:20  jypak
 *  Archive Log:    The layout manager for toolbar that contains the button popups is changed to GridBagLayout. Also, changed initial button texts to be initial enum types.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/03/18 12:05:01  jypak
 *  Archive Log:    Changed from ListSelectionListener to MouseListener so that a mouse click of current selected item from the list at least hide the popup.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/03/17 19:08:22  jypak
 *  Archive Log:    1. Introduced a setIcon method in JSectionView to only set icon. Also, this will fix the following problem:
 *  Archive Log:    Whenever link quality icon is set, the port number portion of a node performance sub-page chart section title was removed. By only setting the icon, the issue was resolved.
 *  Archive Log:    2. The table selection row index for a node's performance sub-page table section should be saved before initialize history query.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/03/17 17:46:19  jypak
 *  Archive Log:    1. Title of history button popup should show "History Type"
 *  Archive Log:    2. Need to set default history type to Current, so that table section of a node performance sub-page work as normal.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/03/17 15:56:20  jypak
 *  Archive Log:    DataType, HistoryType JComboBox have been replaced with button popup to save space.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/10 16:02:16  fisherma
 *  Archive Log:    Update link quality icon in the title of the parent panel.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/05 22:32:14  fisherma
 *  Archive Log:    Added LinkQuality icon to Performance -> Performance tab table.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/26 20:07:38  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/24 14:23:23  jypak
 *  Archive Log:    1. Show Border, Alternating Rows control panel added to the PerformanceErrorsSection.
 *  Archive Log:    2. Undo change of Performance Chart Section title to "Performancefor port Performance subpage.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/18 17:30:45  jypak
 *  Archive Log:    PR 126999 Graph names are changed to include 'Delta' in the middle of the names. Also, added tool tips to the title label, so when a user hover the mouse to the title (for combo box selection of charts, hover on the label values), the explanation about the charts pops up.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/12 19:40:03  jijunwan
 *  Archive Log:    short term PA support
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/10 21:25:58  jypak
 *  Archive Log:    1. Introduced SwingWorker for history query initialization for progress status updates.
 *  Archive Log:    2. Fixed the list of future for history query in TaskScheduler. Now it can have all the Future entries created.
 *  Archive Log:    3. When selecting history type, just cancel the history query not sheduled query.
 *  Archive Log:    4. The refresh rate is now from user settings not from the config api.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/02/06 20:49:37  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/21 17:03:04  jijunwan
 *  Archive Log:    moved ChartsView and ChartsCard to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/28 22:21:57  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/28 19:41:00  rjtierne
 *  Archive Log:    Added Tx/Rx Packet charts to view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/21 14:48:23  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This is the charts section view on the Performance "Node" view. 
 *  It holds the cards that show the Tx/Rx packet graphs. 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.view.ButtonPopup;
import com.intel.stl.ui.common.view.ChartsView;
import com.intel.stl.ui.common.view.ExJXList;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.main.view.IDataTypeListener;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.model.LinkQualityViz;

public class PerformanceChartsSectionView extends
        JSectionView<ISectionListener> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6166893610476283350L;

    private JPanel mainPanel;

    private ChartsView rxCardView;

    private ChartsView txCardView;

    // Followings are history type realted members.
    private ExJXList<HistoryType> historyTypeList;

    private IDataTypeListener<HistoryType> historyTypeListener;

    private MouseListener historyTypeListSelectionListener;

    private JButton historyTypeBtn;

    private ButtonPopup historyPopupOptions;

    private HistoryType historyType;

    private HistoryType prevHistoryType;

    private String title = null;

    // Place holder until LinkQuality data is received.
    private ImageIcon linkQualityIcon = UIImages.LINK_QUALITY_NONE
            .getImageIcon();

    /**
     * Description:
     * 
     * @param title
     */
    public PerformanceChartsSectionView(String title) {
        super(title);
        this.title = title;
        setHistoryTypes(HistoryType.values());
        super.setIcon(linkQualityIcon);
    }

    private void setHistoryTypes(HistoryType... types) {
        if (types != null && types.length > 0) {
            JPanel ctrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            getTitlePanel(title, linkQualityIcon).add(ctrPanel,
                    BorderLayout.CENTER);

            historyTypeList = new ExJXList<HistoryType>(types);
            historyTypeList.setDisabledColor(UIConstants.INTEL_LIGHT_GRAY);
            historyType = types[0];
            prevHistoryType = types[0];
            setHistoryTypeListener(historyTypeListener);
            addHistoryTypeButton(ctrPanel);
        }
    }

    public void setHistoryTypeListener(
            final IDataTypeListener<HistoryType> listener) {
        historyTypeListener = listener;
        if (listener != null) {
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
                            HistoryType oldType = prevHistoryType;
                            prevHistoryType = type;
                            if (historyTypeBtn != null) {
                                historyTypeBtn.setText(historyTypeList
                                        .getSelectedValue().toString());
                            }

                            listener.onDataTypeChange(oldType, type);
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
        if (historyTypeBtn != null) {
            historyTypeBtn.setText(historyTypeList.getSelectedValue()
                    .toString());
        }
    }

    protected void addHistoryTypeButton(JPanel controlPanel) {
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new GridBagLayout());
        toolBar.setFloatable(false);
        historyTypeBtn =
                new JButton(historyType.getName(),
                        UIImages.HISTORY_ICON.getImageIcon());
        historyTypeBtn.setFocusable(false);
        historyTypeBtn.setOpaque(false);
        historyTypeBtn.setToolTipText(STLConstants.K1113_HISTORY_SCOPE
                .getValue());
        historyTypeBtn.setBackground(UIConstants.INTEL_BACKGROUND_GRAY);
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
        controlPanel.add(toolBar);

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

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JSectionView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
        }
        return mainPanel;
    }

    public void installCardViews(ChartsView rxCardView, ChartsView txCardView) {
        this.rxCardView = rxCardView;
        this.txCardView = txCardView;

        if (mainPanel != null) {
            mainPanel.removeAll();
            mainPanel.setLayout(new GridLayout(1, 2, 5, 5));
            mainPanel.add(rxCardView);
            mainPanel.add(txCardView);
            revalidate();
        }
    }

    /**
     * @return the rxPacketsCardView
     */
    public ChartsView getRxCardView() {
        return rxCardView;
    }

    /**
     * @return the txPacketsCardView
     */
    public ChartsView getTxCardView() {
        return txCardView;
    }

    public void setLinkQualityValue(byte linkQuality) {
        linkQualityIcon =
                (ImageIcon) LinkQualityViz.getLinkQualityIcon(linkQuality);
        linkQualityIcon.setDescription(LinkQualityViz
                .getLinkQualityDescription(linkQuality));
        super.setIcon(linkQualityIcon);
    }

    public void clearQualityValue() {
        setIcon(null);
    }
}
