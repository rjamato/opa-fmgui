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

package com.intel.stl.ui.main.view;

import static java.awt.Font.PLAIN;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.configuration.view.PropertyVizStyle;

/**
 * Performance page's performance subpage errors section.
 */
public class PerfErrorsCardView extends JCardView<ICardListener> {

    private static final long serialVersionUID = 2L;

    List<Component> keys = new ArrayList<Component>();

    List<Component> values = new ArrayList<Component>();

    private JPanel totalPanel;

    private JPanel propCardPanel;

    private PropertyVizStyle style = new PropertyVizStyle();

    private GroupLayout groupLayout;

    private Collection<PerfErrorsItem> itemList;

    // Adding comment to update header with PR 128036
    public PerfErrorsCardView() {
        this("");
    }

    public PerfErrorsCardView(String title) {
        super(title);
        getMainComponent();
    }

    public void initializeErrorsItems(Collection<PerfErrorsItem> itemList) {
        this.itemList = itemList;
        initializeErrorsItems();
    }

    public synchronized void setStyle(PropertyVizStyle style) {
        this.style = style;
        initializeErrorsItems();
    }

    @Override
    protected JComponent getMainComponent() {

        if (totalPanel == null) {
            totalPanel = new JPanel();
            totalPanel.setLayout(new BorderLayout());
            totalPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }

        if (propCardPanel == null) {
            propCardPanel = new JPanel();
            groupLayout = new GroupLayout(propCardPanel);
            propCardPanel.setLayout(groupLayout);
            propCardPanel.setBackground(UIConstants.INTEL_WHITE);
        }

        totalPanel.add(propCardPanel, BorderLayout.CENTER);
        return totalPanel;
    }

    private void initializeErrorsItems() {
        int itemSize = itemList.size();
        int numCols = itemSize / 10;

        int itemsPerColumn = (itemSize + (numCols - 1)) / numCols;

        propCardPanel.removeAll();
        keys.clear();
        values.clear();
        SequentialGroup hGroup = groupLayout.createSequentialGroup();
        SequentialGroup vGroup = groupLayout.createSequentialGroup();
        ParallelGroup[] row = new ParallelGroup[itemsPerColumn];

        for (int i = 0; i < itemsPerColumn; i++) {
            row[i] = groupLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE);
            vGroup.addGroup(row[i]);
        }

        Iterator<PerfErrorsItem> itr = itemList.iterator();
        PerfErrorsItem vItem;
        int itemCt = 0;

        for (int i = 0; i < numCols; i++) {
            ParallelGroup keyCol = groupLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING);
            ParallelGroup valCol = groupLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING);
            hGroup.addGap(15);
            hGroup.addGroup(keyCol);
            hGroup.addGroup(valCol);
            hGroup.addGap(15);
            for (int j = 0; j < itemsPerColumn; j++) {
                if (itemCt < itemSize) {
                    vItem = itr.next();
                    JLabel key, value;
                    if (vItem.isTtl()) {
                        key = createHeaderKey(vItem.getKeyStr(), j);
                        value = createHeaderValue("", j);
                    } else {
                        String keyStr = vItem.getKeyStr();
                        if (vItem.isFromNeighbor()) {
                            keyStr += "*";
                        }
                        key = createKey(keyStr, j);
                        if (vItem.isFromNeighbor()) {
                            key.setToolTipText(UILabels.STL40014_BILL_NEIGHBOR
                                    .getDescription());
                        }
                        value = createValue(vItem.getValStr(), j);
                    }
                    keys.add(key);
                    values.add(value);
                    row[j].addComponent(key);
                    row[j].addComponent(value);
                    keyCol.addComponent(key);
                    valCol.addComponent(value);

                }
                itemCt++;
            }
            groupLayout.linkSize(SwingConstants.HORIZONTAL,
                    keys.toArray(new Component[0]));
            groupLayout.linkSize(SwingConstants.HORIZONTAL,
                    values.toArray(new Component[0]));
        }
        groupLayout.setHorizontalGroup(hGroup);
        groupLayout.setVerticalGroup(vGroup);
        propCardPanel.repaint();
        validate();
    }

    protected JLabel createKey(String text, int row) {
        JLabel label = ComponentFactory.getH4Label(text, PLAIN);
        label.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 3));
        style.decorateKey(label, row);
        return label;
    }

    protected JLabel createValue(String text, int row) {
        JLabel label = ComponentFactory.getH4Label(text, PLAIN);
        label.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 2));
        style.decorateValue(label, row);
        return label;
    }

    protected JLabel createHeaderKey(String text, int row) {
        JLabel label = ComponentFactory.getH4Label(text, PLAIN);
        style.decorateHeaderKey(label, row);
        return label;
    }

    protected JLabel createHeaderValue(String text, int row) {
        JLabel label = ComponentFactory.getH4Label(text, PLAIN);
        style.decorateHeaderValue(label, row);
        return label;
    }

    public synchronized void updateErrorsItems(
            Collection<PerfErrorsItem> itemList) {
        this.itemList = itemList;

        PerfErrorsItem vItem;
        Iterator<PerfErrorsItem> itr = itemList.iterator();
        int i = 0;
        while (itr.hasNext()) {
            vItem = itr.next();

            if (!vItem.isTtl()) {
                JLabel value = (JLabel) values.get(i);
                value.setText(vItem.getValStr());
            }
            i++;
        }

        propCardPanel.repaint();
        revalidate();
    }
}
