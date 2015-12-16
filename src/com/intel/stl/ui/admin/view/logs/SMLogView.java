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
 *  File Name: LogView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/10/06 15:53:29  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - Added setEsmView() to grey out the main Log Viewer text area and increase the font for the error message
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/10/01 21:56:08  fernande
 *  Archive Log:    PR130409 - [Dell]: FMGUI Admin Console login fails when switch is configured without username and password. Fix for Klocwork issue
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/09/25 13:56:40  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Added context menu to the main text area
 *  Archive Log:    - Registered text menu listener
 *  Archive Log:    - Revised method highlightText() to display error dialog on failed searches but not filtered searches
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
 *  Overview: The main component of the log page that displays the log text
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.logs;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import com.intel.stl.ui.admin.impl.logs.ITextMenuListener;
import com.intel.stl.ui.admin.impl.logs.SearchKey;
import com.intel.stl.ui.admin.impl.logs.SearchPositionBean;
import com.intel.stl.ui.admin.impl.logs.SearchState;
import com.intel.stl.ui.admin.impl.logs.TextEventType;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;

public class SMLogView extends AbstractLogView {

    private static final long serialVersionUID = -2959984172557178645L;

    private JTextArea txtAreaMain;

    private TextMenuPanel pnlMainMenu;

    /**
     * Description:
     * 
     * @param name
     */
    public SMLogView() {
        super();
    }

    public void setTextMenuListener(ITextMenuListener listener) {
        if (pnlMainMenu != null) {
            pnlMainMenu.setTextMenuListener(listener);
        }
        pnlSearchMenu.setTextMenuListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.logs.AbstractLogView#getMainComponent()
     */
    @Override
    protected Component getMainComponent() {

        txtAreaMain = new JTextArea();
        txtAreaMain.setBackground(UIConstants.INTEL_WHITE);
        txtAreaMain.setFont(UIConstants.H4_FONT);
        txtAreaMain.setLineWrap(true);
        txtAreaMain.setWrapStyleWord(true);
        txtAreaMain.setEditable(false);
        txtAreaMain.getDocument().putProperty(
                DefaultEditorKit.EndOfLineStringProperty, "\n");
        Highlighter h = txtAreaMain.getHighlighter();
        try {
            h.addHighlight(0, 0, new DefaultHighlightPainter(
                    UIConstants.INTEL_BLUE));

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Add a context menu to the text area
        List<TextEventType> eventTypes =
                new ArrayList<TextEventType>(Arrays.asList(TextEventType.COPY,
                        TextEventType.HIGHLIGHT));
        pnlMainMenu = new TextMenuPanel(eventTypes);
        txtAreaMain.addMouseListener(pnlMainMenu);

        JScrollPane scrpnMain = new JScrollPane(txtAreaMain);
        scrpnMain
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrpnMain
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return scrpnMain;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.logs.AbstractLogView#getTextContent()
     */
    @Override
    JTextComponent getTextContent() {
        return txtAreaMain;
    }

    @Override
    public void showLogEntry(final List<String> entries) {

        Util.runInEDT(new Runnable() {

            @Override
            public void run() {
                txtAreaMain.setText(null);
                if (entries == null) {
                    return;
                }

                int numEntries = entries.size();
                for (int i = 0; i < numEntries; i++) {
                    txtAreaMain.append(entries.get(i));

                    // Add a carriage return for all but the last line
                    if (i < (numEntries - 1)) {
                        txtAreaMain.append("\n");
                    }
                }

                // Move cursor to the end
                int endPosition = txtAreaMain.getDocument().getLength();
                txtAreaMain.setCaretPosition(endPosition);
            }
        });
    }

    public void setEsmView() {
        txtAreaMain.setForeground(UIConstants.INTEL_DARK_GRAY);
        txtAreaMain.setBackground(UIConstants.INTEL_LIGHT_GRAY);
        txtAreaMain.setFont(UIConstants.H2_FONT.deriveFont(Font.BOLD));
    }

    public void unHighlightText() {
        txtAreaMain.getHighlighter().removeAllHighlights();
    }

    public void unHighlightSelection(String key, int start, int end) {
        // Preserved for future development
        // Highlight the initially selected text with a slightly different shade
        SearchPositionBean selectedBean =
                new SearchPositionBean(key, start, end,
                        new DefaultHighlightPainter(UIConstants.INTEL_ORANGE));

        try {
            txtAreaMain.getHighlighter().addHighlight(
                    selectedBean.getStartOffset(), selectedBean.getEndOffset(),
                    selectedBean.getPainter());
        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            logViewListener.setCurrentSelection("", 0, 0);
        }
    }

    @Override
    public void highlightText(List<SearchKey> searchKeys,
            List<SearchPositionBean> searchResults, SearchState searchState) {

        boolean showErrors =
                (searchState.equals(SearchState.MARKED_SEARCH) || searchState
                        .equals(SearchState.STANDARD_SEARCH));

        if (showErrors) {
            // Find the search key produced in STANDARD or MARKED search
            boolean found = false;
            String searchToken = null;
            Iterator<SearchKey> it = searchKeys.iterator();
            while (!found && it.hasNext()) {
                SearchKey key = it.next();
                if (key.getState().equals(SearchState.MARKED_SEARCH)
                        || key.getState().equals(SearchState.STANDARD_SEARCH)) {
                    searchToken = key.getText();
                    found = true;
                }
            }

            // Check if the search token is in the search results
            if (found) {
                found = false;
                Iterator<SearchPositionBean> itr = searchResults.iterator();
                while (!found && itr.hasNext()) {
                    SearchPositionBean result = itr.next();
                    found = searchToken.equals(result.getKey());
                }

                // Display an error if no STANDARD or MARKED key was found
                if (!found) {
                    Util.showErrorMessage(this,
                            UILabels.STL50206_SEARCH_TEXT_NOT_FOUND
                                    .getDescription(searchToken));
                    return;
                }
            }
        }

        unHighlightText();
        Highlighter highlighter = txtAreaMain.getHighlighter();

        for (SearchPositionBean position : searchResults) {
            try {
                highlighter.addHighlight(position.getStartOffset(),
                        position.getEndOffset(), position.getPainter());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        // Preserved for future development
        // Highlight the initially selected text with a slightly different shade
        /*-
         *          int selectionStart = textContent.getSelectionStart();
         int selectionEnd = textContent.getSelectionEnd();
         logViewListener.setCurrentSelection(searchKey, selectionStart,
         selectionEnd);
         
            SearchPositionBean selectedBean =
                    new SearchPositionBean(searchKey, selectionStart, selectionEnd,
                            new DefaultHighlightPainter(
                                    UIConstants.INTEL_DARK_ORANGE));
            try {
                highlighter.addHighlight(selectedBean.getStartOffset(),
                        selectedBean.getEndOffset(), selectedBean.getPainter());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
         * 
         */
    }

    public void moveToText(int start, int end) {
        txtAreaMain.setCaretPosition(start);
        txtAreaMain.moveCaretPosition(start);
    }
}
