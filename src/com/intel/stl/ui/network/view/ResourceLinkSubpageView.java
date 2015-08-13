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
 *  File Name: ResourceLinkSubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10.2.1  2015/08/12 15:27:06  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/04/03 21:06:29  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/20 21:11:34  jijunwan
 *  Archive Log:    PR 127179 - Information of Links tabs under "More" in Topology tab does not provide nice readability
 *  Archive Log:     - added tool tip text
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/02/05 19:09:21  jijunwan
 *  Archive Log:    fixed a issue reported by klocwork that is actually not a problem
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/24 20:12:06  jijunwan
 *  Archive Log:    Limited number of tabs to 5 and put other items on a "drop down" menu
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/23 16:00:06  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/26 15:15:35  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/23 18:14:50  rjtierne
 *  Archive Log:    Added protection to only select tab if tabs exist
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/23 18:03:13  rjtierne
 *  Archive Log:    Moved tab state change listener to EDT
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/18 13:42:40  rjtierne
 *  Archive Log:    Added a new ResourceLinkTabView to the tabs on the Link/Path
 *  Archive Log:    tabbed pane to house 2 stacked labels for the to/from nodes on
 *  Archive Log:    a link or path
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/10 14:29:14  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: JCardView to display tabbed pages when links are selected on
 *  the topology graph 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.rollover.RolloverProducer;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ButtonPopup;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.IntelTabbedPaneUI;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.network.ResourceLinkPage;

public class ResourceLinkSubpageView extends JSectionView<ISectionListener> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8162278424319448619L;

    private static final int MAX_TABS = 5;

    private JTabbedPane tabbedPane;

    private IntelTabbedPaneUI tabUI;

    private JButton moreBtn;

    private PopupPanel popupPanel;

    private ButtonPopup popup;

    public ResourceLinkSubpageView(String title, Icon icon) {
        super(title, icon);
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JCardView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {

        if (tabbedPane != null) {
            return tabbedPane;
        }

        // Create the tabbed pane which will be populated when getMainComponent
        // is called from subpages
        tabbedPane = new JTabbedPane();
        tabUI = new IntelTabbedPaneUI();
        tabbedPane.setUI(tabUI);
        tabUI.setFont(UIConstants.H4_FONT);
        tabUI.setTabAreaInsets(new Insets(2, 5, 4, 5));

        JPanel ctrPanel = tabUI.getControlPanel();
        installMoreButton(ctrPanel);

        // Add change listener to highlight the tabs
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                // Highlight the tabs depending on selection
                Runnable highlightTabs = new Runnable() {
                    @Override
                    public void run() {
                        if (tabbedPane.getSelectedIndex() >= 0) {
                            highlightTabs();
                        }
                    }
                };
                Util.runInEDT(highlightTabs);
            }
        });

        return tabbedPane;
    }

    protected void installMoreButton(JPanel ctrPanel) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        moreBtn =
                new JButton(STLConstants.K0036_MORE.getValue(),
                        UIImages.DOWN_ICON.getImageIcon()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void setEnabled(boolean b) {
                        super.setEnabled(b);
                        setForeground(b ? UIConstants.INTEL_BLUE
                                : UIConstants.INTEL_GRAY);
                    }
                };
        moreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popup.isVisible()) {
                    popup.hide();
                } else {
                    popup.show();
                }
            }
        });
        toolBar.add(moreBtn);
        ctrPanel.add(toolBar);

        popupPanel = new PopupPanel();
        popup = new ButtonPopup(moreBtn, popupPanel) {

            @Override
            public void onShow() {
            }

            @Override
            public void onHide() {
            }

        };
    }

    public String getCurrentSubpage() {
        int currentTab = tabbedPane.getSelectedIndex();
        if (currentTab < 0) {
            return null;
        } else {
            return tabbedPane.getTitleAt(currentTab);
        }
    }

    public synchronized void setTabs(ResourceLinkPage[] subpages, int selection) {
        popupPanel.setItems(subpages);

        // remove all old tabs
        tabbedPane.removeAll();
        moreBtn.setEnabled(subpages.length > MAX_TABS);

        for (int i = 0; i < subpages.length; i++) {
            ResourceLinkPage page = subpages[i];
            if (i < MAX_TABS) {
                addTab(page);
            }
        }

        // Set the selected tab
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }

        // Highlight the tabs
        highlightTabs();
    }

    protected void addTab(ResourceLinkPage page) {
        String title = page.getName();
        String[] nodeNames = title.split(",");
        tabbedPane.addTab(title, page.getIcon(), page.getView(),
                page.getDescription());
        popupPanel.addSelection(title);
        if (tabbedPane.getTabCount() > MAX_TABS) {
            popupPanel.removeSelection(tabbedPane.getTitleAt(0));
            tabbedPane.remove(0);
        }
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        updateTab(title, nodeNames);
    }

    protected boolean selectTab(ResourceLinkPage page) {
        String name = page.getName();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            if (title.equals(name)) {
                tabbedPane.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    public void updateTab(String title, String[] nodeNames) {
        ResourceLinkTabView tab = new ResourceLinkTabView(nodeNames);

        boolean highlight = (getCurrentSubpage() == title);
        tab.setLabelProperties(highlight);

        tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(title), tab);
    }

    public void highlightTabs() {
        // Loop through all the tabs and highlight or unhighlight depending
        // on whether it is the selected tab
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            boolean highlight = (i == tabbedPane.getSelectedIndex());
            ResourceLinkTabView tabView =
                    (ResourceLinkTabView) tabbedPane.getTabComponentAt(i);
            if (tabView != null) {
                tabView.setLabelProperties(highlight);
            }
        }
    }

    class PopupPanel extends JPanel implements ListCellRenderer {
        private static final long serialVersionUID = 2314564428422180815L;

        private DefaultListModel model;

        private JXList list;

        private int highlightedRow = -1;

        private final Set<String> selections = new HashSet<String>();

        public PopupPanel() {
            super();
            initComponents();
        }

        protected void initComponents() {
            setLayout(new BorderLayout());

            model = new DefaultListModel();
            list = new JXList(model);
            list.setVisibleRowCount(10);
            list.setCellRenderer(this);
            list.setRolloverEnabled(true);
            // list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY,
                    new PropertyChangeListener() {

                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            Point location = (Point) evt.getNewValue();
                            if (location != null) {
                                highlightedRow = location.y;
                            } else {
                                highlightedRow = -1;
                            }
                        }

                    });
            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    popup.hide();
                    ResourceLinkPage page =
                            (ResourceLinkPage) list.getSelectedValue();
                    if (page != null) {
                        if (!selectTab(page)) {
                            addTab(page);
                        }
                    }
                }
            });

            JScrollPane scroll = new JScrollPane(list);
            scroll.getViewport().getView()
                    .setBackground(UIConstants.INTEL_WHITE);
            add(scroll, BorderLayout.CENTER);
        }

        public void setItems(ResourceLinkPage[] pages) {
            clear();
            for (ResourceLinkPage page : pages) {
                model.addElement(page);
            }
            list.setVisibleRowCount(Math.min(10, pages.length));
        }

        public void addSelection(String name) {
            selections.add(name);
        }

        public void removeSelection(String name) {
            selections.remove(name);
        }

        public void clear() {
            model.clear();
            selections.clear();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
         * .JList, java.lang.Object, int, boolean, boolean)
         */
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            ResourceLinkPage page = (ResourceLinkPage) value;
            String[] nodeNames = page.getName().split(",");
            final ResourceLinkTabView view = new ResourceLinkTabView(nodeNames);
            view.setOpaque(true);
            view.setBorder(BorderFactory.createEmptyBorder(4, 4, 2, 2));
            if (index == highlightedRow) {
                view.setBackground(UIConstants.INTEL_DARK_GREEN);
                view.setLabelProperties(false);
            } else if (selections.contains(page.getName())) {
                view.setBackground(UIConstants.INTEL_BLUE);
                view.setLabelProperties(false);
            } else if (index % 2 == 0) {
                // view.setBackground(UIConstants.INTEL_PALE_BLUE);
                // view.setLabelProperties(true);
                // } else {
                view.setBackground(UIConstants.INTEL_WHITE);
                view.setLabelProperties(true);
            }
            view.setToolTipText(page.getDescription());
            return view;
        }
    }

}
