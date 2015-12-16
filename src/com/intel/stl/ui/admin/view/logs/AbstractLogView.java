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
 *  File Name: AbstractLogView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/10/06 15:53:18  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Added enableControlPanel() to enable/disable log viewer controls; used for ESM disabling
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/09/29 15:31:36  rjtierne
 *  Archive Log:    PR 130332 - windows FM GUI - Admin-Logs - when logging in it displays error message about NULL log
 *  Archive Log:    - Removed the refresh button from the list of buttons that are disabled
 *  Archive Log:    - Implemented cancelLogin() to call stopLog() in SMLogController to stop the log if the cancel button is clicked
 *  Archive Log:    - Removed the refresh button from the list of buttons that are disabled
 *  Archive Log:    - Added a keylistener to the lines/page cbox editor so #lines can be entered when pressing <ENTER> in addition to refresh button
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/09/25 13:56:23  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Added new status panel to display file name, total #lines, #matches, and line range
 *  Archive Log:    - Added a mouse listener to the text content so double-clicked selections can be searched
 *  Archive Log:    - Added filters panel and reformatted control panel
 *  Archive Log:    - Added context menu to the search field
 *  Archive Log:    - Controller now passes model to update the view in compliance with MVC design pattern
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/09/14 18:30:10  jijunwan
 *  Archive Log:    PR 130229 - The text component of all editable combo boxes should provide validation of the input
 *  Archive Log:    - apply FormattedComboBoxEditor on AbstractLogView
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:07  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/17 14:22:38  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *
 *  Overview: The AbstractLogView is a view wrapper for the page control and
 *  the derived main component that has the log view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.logs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.intel.stl.ui.admin.impl.SMLogModel;
import com.intel.stl.ui.admin.impl.logs.FilterType;
import com.intel.stl.ui.admin.impl.logs.ILogViewListener;
import com.intel.stl.ui.admin.impl.logs.SearchKey;
import com.intel.stl.ui.admin.impl.logs.SearchPositionBean;
import com.intel.stl.ui.admin.impl.logs.SearchState;
import com.intel.stl.ui.admin.impl.logs.TextEventType;
import com.intel.stl.ui.admin.view.ILoginListener;
import com.intel.stl.ui.admin.view.LoginPanel;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.SafeNumberField;
import com.intel.stl.ui.console.LoginBean;

public abstract class AbstractLogView extends JPanel {

    private static final long serialVersionUID = 5538100358303437466L;

    private final long MIN_NUM_LINES = 1;

    private final long MAX_NUM_LINES = 1000;

    private LoginPanel pnlLogin;

    protected JPanel pnlFileControl;

    private JButton btnRefresh;

    private SafeNumberField<Long> txtFldLinesPerPage;

    private JComboBox<Long> cboxLinesPerPageValue;

    private JLabel lblTotalLinesValue;

    private JLabel lblStartLineValue;

    private JLabel lblEndLineValue;

    private JLabel lblRangeDelimiter;

    private JLabel lblFileNameValue;

    private JButton btnPrevious;

    private JButton btnNext;

    private JTextField txtfldSearch;

    protected String lastSearchKey;

    private JButton btnSearch;

    private JButton btnCancelSearch;

    private final CardLayout cardLayout = new CardLayout();

    protected ILogViewListener logViewListener;

    private long numLinesRequested = 100;

    private JCheckBox chkboxSM;

    private JCheckBox chkboxPM;

    private JCheckBox chkboxFE;

    private JCheckBox chkboxWarnings;

    private JCheckBox chkboxErrors;

    private final List<JCheckBox> chkboxFilterList = new ArrayList<JCheckBox>();

    private JLabel lblRunning;

    protected JTextComponent textContent;

    private int numLineIndex;

    private DocumentListener numLineListener;

    private JButton[] buttons;

    private boolean[] lastButtonState;

    private JLabel lblNumMatches;

    private JLabel lblNumMatchesValue;

    protected TextMenuPanel pnlSearchMenu;

    public AbstractLogView() {
        super();
        createNumLineListener();
        initComponent();
    }

    protected void createNumLineListener() {
        numLineListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                numLineIndex = cboxLinesPerPageValue.getSelectedIndex();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                numLineIndex = cboxLinesPerPageValue.getSelectedIndex();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                numLineIndex = cboxLinesPerPageValue.getSelectedIndex();
            }
        };
    }

    protected void initComponent() {
        setLayout(cardLayout);

        JPanel pnlLogCard = new JPanel();
        pnlLogCard.setLayout(new BorderLayout(5, 5));
        pnlLogCard.setOpaque(false);
        pnlLogCard.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createLineBorder(UIConstants.INTEL_BORDER_GRAY, 1, true),
                BorderFactory.createEmptyBorder(2, 5, 2, 2)));

        // Control panel
        JPanel pnlControl = createControlPanel();

        // Login Panel
        JPanel pnlLoginCard = new JPanel(new FlowLayout());
        pnlLogin = new LoginPanel();
        pnlLoginCard.setBackground(UIConstants.INTEL_WHITE);
        pnlLoginCard.add(pnlLogin);

        // Add the panels for the log view
        pnlLogCard.add(pnlControl, BorderLayout.NORTH);
        pnlLogCard.add(getMainComponent(), BorderLayout.CENTER);
        textContent = getTextContent();
        textContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    String selected = textContent.getSelectedText();
                    if (selected != null && !selected.isEmpty()) {
                        txtfldSearch.setText(selected);
                        logViewListener.onSearch(SearchState.MARKED_SEARCH);
                    }
                }
            }
        });

        // Add the login and log view panels to the card layout
        add(pnlLoginCard, LogViewType.LOGIN.getValue());
        add(pnlLogCard, LogViewType.SM_LOG.getValue());

        // Initialize last button states
        buttons = new JButton[] { btnNext, btnPrevious };
        lastButtonState =
                new boolean[] { btnNext.isEnabled(), btnPrevious.isEnabled() };
    }

    protected JPanel createControlPanel() {

        // Main Panel
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout());

        // Status Panel
        JPanel pnlStatus = createStatusPanel();

        // Control Panel
        JPanel pnlControl = new JPanel();
        pnlControl.setBackground(UIConstants.INTEL_WHITE);
        pnlControl.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(3, 2, 5, 2);
        gc.weighty = 1;
        gc.weightx = 1;
        gc.gridwidth = 1;

        // Filters Panel
        pnlControl.add(Box.createHorizontalStrut(25));
        JPanel pnlFilters = createFiltersPanel();

        // File Control Panel
        pnlFileControl = createRefreshPanel();

        // Search Panel
        JPanel pnlSearch = createSearchPanel();

        // Navigation Panel
        JPanel pnlNav = createNavPanel();

        // Add components to the Main Control Panel
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.EAST;
        pnlControl.add(pnlFilters, gc);

        gc.weightx = 1;
        pnlControl.add(Box.createHorizontalStrut(100));

        gc.weightx = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        pnlControl.add(pnlFileControl, gc);

        gc.weightx = 1;
        pnlControl.add(Box.createHorizontalStrut(25));

        gc.weightx = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.REMAINDER;
        pnlControl.add(pnlSearch, gc);

        gc.weightx = 1;
        pnlControl.add(Box.createHorizontalStrut(25));

        gc.weightx = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.REMAINDER;
        pnlControl.add(pnlNav, gc);
        pnlControl.add(Box.createHorizontalStrut(25));

        pnlMain.add(pnlControl, BorderLayout.NORTH);
        pnlMain.add(pnlStatus, BorderLayout.CENTER);

        return pnlMain;
    }

    protected JPanel createStatusPanel() {

        // Status Panel
        JPanel pnlStatus = new JPanel();
        pnlStatus.setLayout(new GridBagLayout());
        pnlStatus.setBackground(UIConstants.INTEL_WHITE);
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(3, 2, 5, 2);
        gc.weighty = 1;
        gc.weightx = 1;
        gc.gridwidth = 1;

        // File Name
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.EAST;
        JPanel pnlName = new JPanel();
        pnlName.setBackground(UIConstants.INTEL_WHITE);
        pnlName.setLayout(new BoxLayout(pnlName, BoxLayout.X_AXIS));
        JLabel lblFileName =
                ComponentFactory.getFieldLabel(STLConstants.K2154_FILE_NAME
                        .getValue() + ":");
        lblFileName.setAlignmentX(SwingConstants.RIGHT);

        pnlStatus.add(Box.createHorizontalStrut(25), gc);
        pnlName.add(lblFileName);
        lblFileNameValue =
                ComponentFactory
                        .deriveLabel(
                                ComponentFactory.getH6Label("", Font.PLAIN),
                                false, 200);
        lblFileNameValue.setPreferredSize(new Dimension(1000, 25));
        lblFileNameValue.setMaximumSize(lblFileNameValue.getPreferredSize());
        lblFileNameValue.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        pnlName.add(lblFileNameValue);
        pnlName.setPreferredSize(new Dimension(260, 25));
        pnlName.setMinimumSize(pnlName.getPreferredSize());
        pnlName.setMaximumSize(pnlName.getPreferredSize());
        pnlStatus.add(pnlName, gc);

        gc.weightx = 1;
        pnlStatus.add(Box.createHorizontalStrut(100), gc);

        // Total Number of Lines
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        JPanel pnlTotalLines = new JPanel();
        pnlTotalLines.setBackground(UIConstants.INTEL_WHITE);
        pnlTotalLines.setLayout(new BoxLayout(pnlTotalLines, BoxLayout.X_AXIS));
        JLabel lblTotalLines =
                ComponentFactory.getFieldLabel(STLConstants.K2151_TOTAL_LINES
                        .getValue() + ":");
        lblTotalLines.setAlignmentX(SwingConstants.RIGHT);
        pnlTotalLines.add(lblTotalLines);
        lblTotalLinesValue = ComponentFactory.getH6Label("", Font.PLAIN);
        lblTotalLinesValue.setPreferredSize(new Dimension(1000, 25));
        lblTotalLinesValue
                .setMaximumSize(lblTotalLinesValue.getPreferredSize());
        lblTotalLinesValue.setBorder(BorderFactory
                .createEmptyBorder(0, 5, 0, 5));
        pnlTotalLines.add(lblTotalLinesValue);
        pnlTotalLines.setPreferredSize(new Dimension(150, 25));
        pnlTotalLines.setMinimumSize(pnlTotalLines.getPreferredSize());
        pnlTotalLines.setMaximumSize(pnlTotalLines.getPreferredSize());
        pnlStatus.add(pnlTotalLines, gc);

        gc.weightx = 1;
        pnlStatus.add(Box.createHorizontalStrut(100), gc);

        // Number of matched search results
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.REMAINDER;
        JPanel pnlNumMatches = new JPanel();
        pnlNumMatches.setBackground(UIConstants.INTEL_WHITE);
        pnlNumMatches.setLayout(new BoxLayout(pnlNumMatches, BoxLayout.X_AXIS));
        lblNumMatches =
                ComponentFactory.getFieldLabel(STLConstants.K2158_NUM_MATCHES
                        .getValue() + ":");
        lblNumMatches.setAlignmentX(SwingConstants.RIGHT);
        pnlNumMatches.add(lblNumMatches);
        lblNumMatchesValue = ComponentFactory.getH6Label("", Font.PLAIN);
        lblNumMatchesValue.setPreferredSize(new Dimension(1000, 25));
        lblNumMatchesValue
                .setMaximumSize(lblNumMatchesValue.getPreferredSize());
        lblNumMatchesValue.setBorder(BorderFactory
                .createEmptyBorder(0, 5, 0, 5));
        pnlNumMatches.add(lblNumMatchesValue);
        pnlNumMatches.setPreferredSize(new Dimension(200, 25));
        pnlNumMatches.setMinimumSize(pnlNumMatches.getPreferredSize());
        pnlNumMatches.setMaximumSize(pnlNumMatches.getPreferredSize());
        pnlStatus.add(pnlNumMatches, gc);

        gc.weightx = 1;
        pnlStatus.add(Box.createHorizontalStrut(25), gc);

        // Line Range
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.REMAINDER;
        JPanel pnlRange = new JPanel();
        pnlRange.setBackground(UIConstants.INTEL_WHITE);
        pnlRange.setLayout(new BoxLayout(pnlRange, BoxLayout.X_AXIS));
        JLabel lblLineRange =
                ComponentFactory.getFieldLabel(STLConstants.K2155_LINE_RANGE
                        .getValue() + ":");
        lblLineRange.setAlignmentX(SwingConstants.RIGHT);
        pnlRange.add(lblLineRange);
        lblStartLineValue = ComponentFactory.getH6Label("", Font.PLAIN);
        lblStartLineValue.setPreferredSize(new Dimension(1000, 25));
        lblStartLineValue.setMaximumSize(lblStartLineValue.getPreferredSize());
        lblStartLineValue
                .setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        pnlRange.add(lblStartLineValue);
        lblRangeDelimiter =
                ComponentFactory.getH6Label(
                        STLConstants.K2156_RANGE_DELIMITER.getValue(),
                        Font.PLAIN);
        lblRangeDelimiter.setPreferredSize(new Dimension(10, 25));
        lblRangeDelimiter.setMaximumSize(lblRangeDelimiter.getPreferredSize());
        lblRangeDelimiter
                .setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        pnlRange.add(lblRangeDelimiter);
        lblEndLineValue = ComponentFactory.getH6Label("", Font.PLAIN);
        lblEndLineValue.setPreferredSize(new Dimension(1000, 25));
        lblEndLineValue.setMaximumSize(lblEndLineValue.getPreferredSize());
        lblEndLineValue.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        pnlRange.add(lblEndLineValue);
        pnlStatus.add(pnlRange, gc);

        return pnlStatus;
    }

    protected JPanel createFiltersPanel() {
        // Filters Panel
        JPanel pnlFilters = new JPanel();
        pnlFilters.setBackground(UIConstants.INTEL_WHITE);
        pnlFilters.setBorder(BorderFactory
                .createTitledBorder(STLConstants.K2147_FILTERS.getValue()));
        pnlFilters.setLayout(new BoxLayout(pnlFilters, BoxLayout.X_AXIS));
        chkboxSM = ComponentFactory.getIntelCheckBox(FilterType.SM.getName());
        chkboxFilterList.add(chkboxSM);
        chkboxPM = ComponentFactory.getIntelCheckBox(FilterType.PM.getName());
        chkboxFilterList.add(chkboxPM);
        chkboxFE = ComponentFactory.getIntelCheckBox(FilterType.FE.getName());
        chkboxFilterList.add(chkboxFE);
        chkboxWarnings =
                ComponentFactory
                        .getIntelCheckBox(FilterType.WARNINGS.getName());
        chkboxFilterList.add(chkboxWarnings);
        chkboxErrors =
                ComponentFactory.getIntelCheckBox(FilterType.ERRORS.getName());
        chkboxFilterList.add(chkboxErrors);
        for (JCheckBox chkbox : chkboxFilterList) {
            chkbox.setSelected(false);
            chkbox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    logViewListener.onFilter();
                }
            });
            pnlFilters.add(chkbox);
        }

        return pnlFilters;
    }

    protected JPanel createRefreshPanel() {
        // Refresh Panel
        JPanel pnlRefresh = new JPanel();
        pnlRefresh.setBackground(UIConstants.INTEL_WHITE);
        pnlRefresh.setBorder(BorderFactory
                .createTitledBorder(STLConstants.K0107_REFRESH.getValue()));
        pnlRefresh.setLayout(new BoxLayout(pnlRefresh, BoxLayout.X_AXIS));
        JLabel lblLinesPerPage =
                ComponentFactory
                        .getFieldLabel(STLConstants.K2150_LINES_PER_PAGE
                                .getValue() + ":");
        lblLinesPerPage.setAlignmentX(SwingConstants.RIGHT);
        pnlRefresh.add(lblLinesPerPage);

        txtFldLinesPerPage =
                new SafeNumberField<Long>(new DecimalFormat("###"),
                        MIN_NUM_LINES, true, MAX_NUM_LINES, true);
        txtFldLinesPerPage.setValidChars(UIConstants.DIGITS);
        txtFldLinesPerPage.setColumns(10);
        cboxLinesPerPageValue =
                ComponentFactory.createComboBox(new Long[] { 100L, 300L, 500L,
                        1000L }, txtFldLinesPerPage, numLineListener);
        cboxLinesPerPageValue.setEditable(true);
        cboxLinesPerPageValue.getEditor().getEditorComponent()
                .addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                            onEndOfPage();
                        }
                    }
                });

        // Add the refresh button to the refresh panel
        btnRefresh =
                ComponentFactory
                        .getImageButton(UIImages.REFRESH.getImageIcon());
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEndOfPage();
            }
        });

        pnlRefresh.add(Box.createHorizontalStrut(5));
        pnlRefresh.add(cboxLinesPerPageValue);
        pnlRefresh.add(Box.createHorizontalStrut(5));
        pnlRefresh.add(btnRefresh);

        return pnlRefresh;
    }

    protected JPanel createSearchPanel() {
        // SearchPanel
        JPanel pnlSearch = new JPanel();
        pnlSearch.setBackground(UIConstants.INTEL_WHITE);
        pnlSearch.setBorder(BorderFactory
                .createTitledBorder(STLConstants.K2153_SEARCH.getValue()));
        pnlSearch.setLayout(new BoxLayout(pnlSearch, BoxLayout.X_AXIS));

        // Add a text box to the Search panel
        txtfldSearch = new JTextField();
        txtfldSearch.setPreferredSize(new Dimension(200, 10));
        txtfldSearch.setMinimumSize(txtfldSearch.getPreferredSize());
        txtfldSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    logViewListener.onSearch(SearchState.STANDARD_SEARCH);
                }
            }
        });

        List<TextEventType> eventTypes =
                new ArrayList<TextEventType>(Arrays.asList(TextEventType.PASTE));
        pnlSearchMenu = new TextMenuPanel(eventTypes);
        txtfldSearch.addMouseListener(pnlSearchMenu);
        pnlSearch.add(txtfldSearch);

        // Add a button to the Search panel
        btnSearch =
                ComponentFactory.getImageButton(UIImages.SEARCH.getImageIcon());
        btnSearch.setToolTipText(STLConstants.K2153_SEARCH.getValue());
        btnSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logViewListener.onSearch(SearchState.STANDARD_SEARCH);
            }
        });
        pnlSearch.add(btnSearch);

        // Add a cancel button to the Search panel
        btnCancelSearch =
                ComponentFactory.getImageButton(UIImages.CLOSE_GRAY
                        .getImageIcon());
        btnCancelSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the search text field and let the document listener
                // handle the rest
                txtfldSearch.setText("");
            }

        });
        pnlSearch.add(btnCancelSearch);

        return pnlSearch;
    }

    public void enableSearch(boolean b) {
        btnSearch.setSelected(true);
        btnSearch.setEnabled(b);
    }

    public void enableControlPanel(boolean b) {
        btnSearch.setEnabled(b);
        btnSearch.setEnabled(b);
        btnRefresh.setEnabled(b);
        txtfldSearch.setEnabled(b);
        cboxLinesPerPageValue.setEnabled(b);

        for (JCheckBox chkbox : chkboxFilterList) {
            chkbox.setEnabled(b);
        }
    }

    public String getSearchKey() {
        return txtfldSearch.getText();
    }

    public void setSearchField(String value) {
        txtfldSearch.setText(value);
    }

    public String getLastSearchKey() {
        return lastSearchKey;
    }

    public void saveLastSearchKey() {
        lastSearchKey = getSearchKey();
    }

    public void resetSearchField() {
        txtfldSearch.setText("");
    }

    public String getDocument() {
        // The search field is a JTextField which cannot contain carriage
        // returns
        // So in order to match multiple lines in the search field, replace all
        // carriage returns in the textContent with blanks, and trim off the
        // blank
        // space at the beginning/end of the string.
        return textContent.getText().replaceAll("\\n", " ").trim();
    }

    protected JPanel createNavPanel() {
        // Navigation Panel
        JPanel pnlNav = new JPanel();
        pnlNav.setBackground(UIConstants.INTEL_WHITE);
        pnlNav.setPreferredSize(new Dimension(100, 20));
        pnlNav.setMinimumSize(pnlNav.getPreferredSize());
        pnlNav.setLayout(new BoxLayout(pnlNav, BoxLayout.X_AXIS));

        // Add the previous button to the Nav panel
        btnPrevious =
                ComponentFactory.getIntelActionButton(new AbstractAction() {
                    private static final long serialVersionUID =
                            -4779867622071856085L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        logViewListener.onPrevious(numLinesRequested);
                        setRunningVisible(true);
                        disableUserActions();
                    }
                });
        btnPrevious.setIcon(UIImages.BACK_WHITE_ICON.getImageIcon());
        pnlNav.add(btnPrevious);

        // Put a horizontal gap between the buttons
        pnlNav.add(Box.createHorizontalStrut(5));

        // Add the next button to the Nav panel
        btnNext = ComponentFactory.getIntelActionButton(new AbstractAction() {
            private static final long serialVersionUID = 905214147006236116L;

            @Override
            public void actionPerformed(ActionEvent e) {
                logViewListener.onNext(numLinesRequested);
                setRunningVisible(true);
                disableUserActions();
            }
        });
        // Last page is up first, so disable this button
        btnNext.setEnabled(false);
        btnNext.setIcon(UIImages.FORWARD_WHITE_ICON.getImageIcon());
        pnlNav.add(btnNext);

        // Add a Running icon
        lblRunning = new JLabel();
        lblRunning.setIcon(UIImages.RUNNING.getImageIcon());
        pnlNav.add(lblRunning);

        return pnlNav;
    }

    public void onEndOfPage() {
        if (!txtFldLinesPerPage.isEditValid()) {
            txtFldLinesPerPage.requestFocusInWindow();
            return;
        }

        numLinesRequested = (long) cboxLinesPerPageValue.getEditor().getItem();

        boolean ok = checkNumLines(numLinesRequested);
        if (ok) {
            // Refresh the log
            logViewListener.onLastLines(numLinesRequested);
            setRunningVisible(true);
            disableUserActions();
        } else {
            Util.showErrorMessage(pnlFileControl,
                    UILabels.STL50213_LINES_PER_PAGE_ERROR.getDescription(
                            numLinesRequested, MIN_NUM_LINES, MAX_NUM_LINES));
            cboxLinesPerPageValue.setSelectedIndex(numLineIndex);
        }

        // Prevent duplicate entries
        if (ok
                && ((DefaultComboBoxModel<Long>) cboxLinesPerPageValue
                        .getModel()).getIndexOf(numLinesRequested) == -1) {
            cboxLinesPerPageValue.addItem(numLinesRequested);
            cboxLinesPerPageValue.setSelectedItem(numLinesRequested);

        }
    }

    protected boolean checkNumLines(long numLines) {
        return ((MIN_NUM_LINES <= numLines) && (numLines <= MAX_NUM_LINES));
    }

    public void setRunningVisible(boolean b) {
        lblRunning.setVisible(b);
    }

    public List<FilterType> getSelectedFilters() {

        List<FilterType> filters = new ArrayList<FilterType>();
        for (JCheckBox chkbox : chkboxFilterList) {
            if (chkbox.isSelected()) {
                filters.add(FilterType.getFilter(chkbox.getText()));
            }
        }
        return filters;
    }

    public void updateView(final SMLogModel model) {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                showFileName(model.getFileName());
                showLogEntry(model.getFilteredDoc());
                showTotalLines(model);
                showLineRange(model.getStartLine(), model.getEndLine());

                if (model.getLogMsg() != null) {
                    logViewListener.onFilter();
                }
            }
        });
    }

    public void setNextEnabled(boolean b) {
        btnNext.setEnabled(b);
    }

    public void setPreviousEnabled(boolean b) {
        btnPrevious.setEnabled(b);
    }

    public void showTotalLines(final SMLogModel model) {
        if (hasRunningIcon()) {
            setNumLineIcon(false);
        }
        lblTotalLinesValue.setText(String.valueOf(model.getNumLines()));
    }

    public void showFileName(final String fileName) {
        lblFileNameValue.setText(fileName);
        lblFileNameValue.setToolTipText(fileName);
    }

    public void showLineRange(long startLine, long endLine) {
        if ((startLine > 0) && (endLine > 0)) {
            lblStartLineValue.setText(String.valueOf(startLine));
            lblEndLineValue.setText(String.valueOf(endLine));
        }
    }

    public void setNumLinesLabel(String value) {
        lblNumMatches.setText(value);
    }

    public void showNumMatches(long matches) {
        lblNumMatchesValue.setText(String.valueOf(matches));
    }

    public long getNumLinesRequested() {
        return numLinesRequested;
    }

    public void setLoginListener(ILoginListener listener) {
        pnlLogin.setListener(listener);
    }

    public void setLogViewListener(ILogViewListener listener) {
        logViewListener = listener;

        // Add the document listener to the search text field
        txtfldSearch.getDocument().addDocumentListener(
                logViewListener.getDocumentListener());
    }

    public void setView(String name) {
        cardLayout.show(this, name);
    }

    public void showLogView() {
        pnlLogin.showProgress(false);
        setView(LogViewType.SM_LOG.getValue());
    }

    public void showLoginView() {
        pnlLogin.showProgress(false);
        setView(LogViewType.LOGIN.getValue());
    }

    public void showProgress(boolean show) {
        pnlLogin.showProgress(show);
    }

    public void showError(String message) {
        pnlLogin.setMessage(message);
    }

    public void showErrorDialog(String message) {
        Util.showErrorMessage(pnlLogin, message);
    }

    public void clearLoginData() {
        pnlLogin.clearLoginData();
        pnlLogin.setMessage("");
        pnlLogin.repaint();
    }

    public LoginBean getCredentials() {
        return pnlLogin.getCredentials();
    }

    public void setCredentials(LoginBean credentials) {
        pnlLogin.setHostNameField(credentials.getHostName());
        pnlLogin.setPortNumber(credentials.getPortNum());
        pnlLogin.setUserNameField(credentials.getUserName());
    }

    public void setNumLineIcon(boolean b) {
        if (b) {
            lblTotalLinesValue.setIcon(UIImages.RUNNING.getImageIcon());
        } else {
            lblTotalLinesValue.setIcon(null);
        }
    }

    public boolean hasRunningIcon() {
        return (lblTotalLinesValue.getIcon() != null);
    }

    public void disableUserActions() {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                // Save the current state of each button and then disable
                for (int i = 0; i < buttons.length; i++) {
                    lastButtonState[i] = buttons[i].isEnabled();
                    buttons[i].setEnabled(false);
                }
            }
        });
    }

    public void restoreUserActions(final boolean isFirstPage,
            final boolean isLastPage) {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                // Restore all buttons to their original state
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setEnabled(lastButtonState[i]);
                }

                // Now check whether this is the first or last page and set
                // the button enables accordingly
                setPreviousEnabled(!isFirstPage);
                setNextEnabled(!isLastPage);
            }
        });
    }

    abstract Component getMainComponent();

    abstract JTextComponent getTextContent();

    abstract public void showLogEntry(List<String> entries);

    abstract public void highlightText(List<SearchKey> searchKeys,
            List<SearchPositionBean> searchResults, SearchState searchState);
}
