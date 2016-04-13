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

package com.intel.stl.ui.logger.config.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.view.ComponentFactory;

class ConversionPatternHelpDialog extends JDialog {

    private static final long serialVersionUID = 3640306308504496390L;

    private final JPanel mainPannel = new JPanel(new BorderLayout());

    private final JPanel buttonPannel = new JPanel(new FlowLayout());

    private final JPanel previewButtonPanel = new JPanel(new FlowLayout());

    private JPanel panelHolderPanel = null;

    private JPanel conversionPatternPanel = null;

    private JPanel previewPanel = null;

    private GridBagConstraints constraints = null;

    private GridBagLayout gridBag = null;

    private GridBagLayout gridBagConversionPattern = null;

    private GridBagLayout gridBagPreview = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JButton previewButton = null;

    @SuppressWarnings("unused")
    private JPanel parentDialog = null;

    private JTextField previewText = null;

    private JLabel formatString = null;

    private JLabel formatStringLabel = null;

    private JLabel sampleString = null;

    private JLabel sampleStringLabel = null;

    private JTable conversionCharacterTable = null;

    private final String heading[] = new String[] {
            STLConstants.K0646_CONVERSION_CHARACTER.getValue(),
            STLConstants.K0647_EFFECT.getValue() };

    private final String data[][] = new String[14][2];

    public ConversionPatternHelpDialog(JPanel mainPanel) {
        parentDialog = mainPanel;
        setTitle(STLConstants.K0648_OUTPUT_FORMAT_HELP.getValue());
        this.setResizable(false);
        createDialog();
        setModal(true);
        this.getContentPane().add(mainPannel);
        this.pack();
        this.setLocationRelativeTo(mainPanel);
    }

    private void createDialog() {
        // create Button Pannel
        okButton = ComponentFactory.getIntelActionButton(okAction);
        buttonPannel.add(okButton);
        cancelButton = ComponentFactory.getIntelCancelButton(cancelAction);
        buttonPannel.add(cancelButton);
        previewButton = ComponentFactory.getIntelActionButton(previewAction);

        previewButtonPanel.setOpaque(false);
        previewButtonPanel.add(previewButton);

        // conversion pattern layout
        gridBagConversionPattern = new GridBagLayout();
        GridBagConstraints constraintsConversionPattern =
                new GridBagConstraints();

        // holder layout
        gridBag = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.insets.top = 5;
        constraints.insets.bottom = 5;

        // preview layout
        gridBagPreview = new GridBagLayout();
        GridBagConstraints constraintsPreview = new GridBagConstraints();
        constraintsPreview.insets = new Insets(5, 5, 0, 5);

        // conversion pattern table creation
        data[0][0] = STLConstants.K0649_SC.getValue();
        data[0][1] = UILabels.STL50002_DATA1.getDescription();

        data[1][0] = STLConstants.K0650_C.getValue();
        data[1][1] = UILabels.STL50003_DATA2.getDescription();

        data[2][0] = STLConstants.K0651_D.getValue();
        data[2][1] = UILabels.STL50004_DATA3.getDescription();

        data[3][0] = STLConstants.K0652_F.getValue();
        data[3][1] = UILabels.STL50005_DATA4.getDescription();

        data[4][0] = STLConstants.K0653_SL.getValue();
        data[4][1] = UILabels.STL50006_DATA5.getDescription();

        data[5][0] = STLConstants.K0654_L.getValue();
        data[5][1] = UILabels.STL50007_DATA6.getDescription();

        data[6][0] = STLConstants.K0655_SM.getValue();
        data[6][1] = UILabels.STL50008_DATA7.getDescription();

        data[7][0] = STLConstants.K0656_M.getValue();
        data[7][1] = UILabels.STL50009_DATA8.getDescription();

        data[8][0] = STLConstants.K0657_SN.getValue();
        data[8][1] = UILabels.STL50010_DATA9.getDescription();

        data[9][0] = STLConstants.K0658_SP.getValue();
        data[9][1] = UILabels.STL50011_DATA10.getDescription();

        data[10][0] = STLConstants.K0659_SR.getValue();
        data[10][1] = UILabels.STL50012_DATA11.getDescription();

        data[11][0] = STLConstants.K0660_ST.getValue();
        data[11][1] = UILabels.STL50013_DATA12.getDescription();

        data[12][0] = STLConstants.K0661_SX.getValue();
        data[12][1] = UILabels.STL50014_DATA13.getDescription();

        data[13][0] = STLConstants.K0662_DOUBLE_PERCENT.getValue();
        data[13][1] = UILabels.STL50015_DATA14.getDescription();

        conversionCharacterTable =
                ComponentFactory.createIntelNonSortableSimpleTable(data,
                        heading);
        conversionCharacterTable.setBorder(BorderFactory.createEtchedBorder());
        conversionCharacterTable.setEnabled(false);

        packRows(conversionCharacterTable, 2);
        packCols(conversionCharacterTable, 2);

        // conversion pannel creation
        conversionPatternPanel = new JPanel(gridBagConversionPattern);
        conversionPatternPanel.setBackground(UIConstants.INTEL_WHITE);
        conversionPatternPanel.setBorder(new TitledBorder(BorderFactory
                .createEtchedBorder(), STLConstants.K0663_CONVERSION_PATTERN
                .getValue(), TitledBorder.LEADING, TitledBorder.TOP,
                UIConstants.H4_FONT.deriveFont(Font.BOLD), null));

        // conversion panel element generation
        constraintsConversionPattern.gridx = 0;
        constraintsConversionPattern.gridy = 0;
        constraintsConversionPattern.anchor = GridBagConstraints.WEST;
        constraintsConversionPattern.insets = new Insets(5, 5, 0, 5);
        gridBagConversionPattern.setConstraints(
                conversionCharacterTable.getTableHeader(),
                constraintsConversionPattern);
        conversionPatternPanel.add(conversionCharacterTable.getTableHeader());

        constraintsConversionPattern.gridx = 0;
        constraintsConversionPattern.gridy = 1;
        constraintsConversionPattern.anchor = GridBagConstraints.WEST;
        constraintsConversionPattern.insets = new Insets(1, 5, 5, 5);
        gridBagConversionPattern.setConstraints(conversionCharacterTable,
                constraintsConversionPattern);
        conversionPatternPanel.add(conversionCharacterTable);

        // preview panel creation
        previewPanel = new JPanel(gridBagPreview);
        previewPanel.setBackground(UIConstants.INTEL_WHITE);
        previewPanel.setBorder(new TitledBorder(BorderFactory
                .createEtchedBorder(), STLConstants.K0664_PREVIEW.getValue(),
                TitledBorder.LEADING, TitledBorder.TOP, UIConstants.H4_FONT
                        .deriveFont(Font.BOLD), null));

        // preview Panel element generation
        formatStringLabel =
                ComponentFactory.getH5Label(
                        UILabels.STL50049_TO_PREVIEW.getDescription(),
                        Font.BOLD);
        constraintsPreview.gridx = 0;
        constraintsPreview.gridy = 0;
        constraintsPreview.gridwidth = 3;
        constraintsPreview.fill = GridBagConstraints.HORIZONTAL;
        constraintsPreview.anchor = GridBagConstraints.WEST;
        gridBagPreview.setConstraints(formatStringLabel, constraintsPreview);
        previewPanel.add(formatStringLabel);

        formatString =
                ComponentFactory.getH5Label(
                        STLConstants.K0665_ENTER_FORMAT.getValue(), Font.BOLD);
        constraintsPreview.gridx = 0;
        constraintsPreview.gridy = 1;
        constraintsPreview.gridwidth = 1;
        constraintsPreview.weightx = 0.0;
        constraintsPreview.anchor = GridBagConstraints.WEST;
        gridBagPreview.setConstraints(formatString, constraintsPreview);
        previewPanel.add(formatString);

        previewText = new JTextField(40);
        constraintsPreview.gridx = 1;
        constraintsPreview.gridy = 1;
        constraintsPreview.weightx = 1.0;
        constraintsPreview.anchor = GridBagConstraints.WEST;
        gridBagPreview.setConstraints(previewText, constraintsPreview);
        previewPanel.add(previewText);

        // Disable this feature until a non-apache solution can be devised
        previewButton.setEnabled(false);
        previewText.setEnabled(false);

        constraintsPreview.gridx = 2;
        constraintsPreview.gridy = 1;
        constraintsPreview.weightx = 0.0;
        constraintsPreview.anchor = GridBagConstraints.WEST;
        gridBagPreview.setConstraints(previewButtonPanel, constraintsPreview);
        previewPanel.add(previewButtonPanel);

        sampleStringLabel =
                ComponentFactory.getH5Label(
                        STLConstants.K0666_SAMPLE_FORMATTED.getValue(),
                        Font.BOLD);
        constraintsPreview.gridx = 0;
        constraintsPreview.gridy = 2;
        constraintsPreview.gridwidth = 1;
        constraintsPreview.weightx = 0.0;
        constraintsPreview.anchor = GridBagConstraints.WEST;
        constraintsPreview.insets = new Insets(5, 5, 10, 5);
        gridBagPreview.setConstraints(sampleStringLabel, constraintsPreview);
        previewPanel.add(sampleStringLabel);

        sampleString = new JLabel("");
        sampleString.setForeground(Color.RED);
        constraintsPreview.gridx = 1;
        constraintsPreview.gridy = 2;
        constraintsPreview.gridwidth = 2;
        constraintsPreview.weightx = 1.0;
        constraintsPreview.fill = GridBagConstraints.HORIZONTAL;
        constraintsPreview.anchor = GridBagConstraints.WEST;
        gridBagPreview.setConstraints(sampleString, constraintsPreview);
        previewPanel.add(sampleString);

        // holder pannel layout
        panelHolderPanel = new JPanel(gridBag);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 0, 5);
        gridBag.setConstraints(previewPanel, constraints);
        panelHolderPanel.add(previewPanel);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        gridBag.setConstraints(conversionPatternPanel, constraints);
        panelHolderPanel.add(conversionPatternPanel);

        // create main pannel layout
        mainPannel.add(panelHolderPanel, BorderLayout.CENTER);
        mainPannel.add(buttonPannel, BorderLayout.SOUTH);

        this.setAlwaysOnTop(true);

    }

    protected AbstractAction okAction = new AbstractAction(
            STLConstants.K0645_OK.getValue()) {

        private static final long serialVersionUID = -6961800759728659264L;

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();

        }

    };

    protected AbstractAction cancelAction = new AbstractAction(
            STLConstants.K0621_CANCEL.getValue()) {

        private static final long serialVersionUID = 9180757082471160424L;

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();

        }

    };

    protected AbstractAction previewAction = new AbstractAction(
            STLConstants.K0664_PREVIEW.getValue()) {

        private static final long serialVersionUID = 8019247636030333301L;

        @Override
        public void actionPerformed(ActionEvent e) {
            String sampleStr = new String(getOutputFormat());
            sampleString.setText(sampleStr);
        }

    };

    public String getOutputFormat() {

        String s = previewText.getText().trim();
        StringBuilder ss = new StringBuilder(s);

        if (s.contains(STLConstants.K0652_F.getValue().trim())) {
            int index = ss.indexOf(STLConstants.K0652_F.getValue().trim());
            ss.replace(index, index + 2, "ConversionPatternHelpDialog.java");
        }
        if (s.contains(STLConstants.K0654_L.getValue().trim())) {
            int index = ss.indexOf(STLConstants.K0654_L.getValue().trim());
            ss.replace(index, index + 2, "38");
        }
        s = ss.toString();

        return null;
    }

    public int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
        // Get the current default height for all rows
        int height = table.getRowHeight();

        // Determine highest cell in the row
        for (int c = 0; c < table.getColumnCount(); c++) {
            TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
            Component comp = table.prepareRenderer(renderer, rowIndex, c);
            int h = comp.getPreferredSize().height + 2 * margin;
            height = Math.max(height, h);
        }
        return height;
    }

    public int getPreferredColumnWidth(JTable table, int colIndex, int margin) {
        int width = 90;

        for (int r = 0; r < table.getRowCount(); r++) {
            TableCellRenderer renderer = table.getCellRenderer(r, colIndex);
            Component comp = table.prepareRenderer(renderer, r, colIndex);
            int w = comp.getPreferredSize().width + 2 * margin;
            width = Math.max(width, w);
        }
        return width;
    }

    public void packRows(JTable table, int margin) {
        packRows(table, 0, table.getRowCount(), margin);
    }

    public void packRows(JTable table, int start, int end, int margin) {
        for (int r = 0; r < table.getRowCount(); r++) {
            // Get the preferred height
            int h = getPreferredRowHeight(table, r, margin);

            // Now set the row height using the preferred height
            if (table.getRowHeight(r) != h) {
                table.setRowHeight(r, h);
            }
        }
    }

    public void packCols(JTable table, int margin) {
        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            int w = getPreferredColumnWidth(table, i, margin);
            column.setPreferredWidth(w);
        }
    }
}
