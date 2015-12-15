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
 *  File Name: SMLogController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/10/06 15:51:56  rjtierne
 *  Archive Log:    PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log:    - In method startLog(), when hostType is null or ESM, call onEsmHost()
 *  Archive Log:    - Added onEsmHost() to display a message on the Log Viewer indicating that it isn't supported and grey
 *  Archive Log:    out the log controls
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/09/29 15:31:22  rjtierne
 *  Archive Log:    PR 130332 - windows FM GUI - Admin-Logs - when logging in it displays error message about NULL log
 *  Archive Log:    - Implemented cancelLogin() to call stopLog() in SMLogController to stop the log if the cancel
 *  Archive Log:    button is clicked
 *  Archive Log:    - Clear password on error or when the cancel button is clicked
 *  Archive Log:    - Display error only when no log file can be found
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/09/28 17:54:18  fisherma
 *  Archive Log:    PR 130425 - added cancel button to the Admin tab login page to allow user to cancel out of hung or slow ssh logins.  Cancel action terminates sftp connection and closes remote ssh session. This fix also addresses PR 130386 and 130390.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/09/25 13:56:10  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Implemented ITextMenuListener interface to handle actions brought on by menu selections
 *  Archive Log:    - Facilitate search and filter tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/18 21:37:37  jijunwan
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    - checked in Rick's code that treat search key as literal pattern string to handle special chars
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:36  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/17 14:22:41  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *
 *  Overview: Controller for the SM log view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.logs.ILogApi;
import com.intel.stl.api.logs.ILogStateListener;
import com.intel.stl.api.logs.LogErrorType;
import com.intel.stl.api.logs.LogResponse;
import com.intel.stl.api.subnet.HostInfo;
import com.intel.stl.api.subnet.HostType;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.ui.admin.impl.SMLogModel;
import com.intel.stl.ui.admin.view.ILoginListener;
import com.intel.stl.ui.admin.view.logs.SMLogView;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.console.LoginBean;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.model.LogErrorTypeViz;

public class SMLogController implements ILogViewListener, ILogStateListener,
        ILoginListener, ITextMenuListener {

    private final static Logger log = LoggerFactory
            .getLogger(SMLogController.class);

    private final SMLogModel model;

    private final SMLogView view;

    private ILogApi logApi;

    public SubnetDescription subnet;

    public boolean endOfFile = true;

    public boolean initInProgress = true;

    private IProgressObserver observer;

    private boolean firstPage;

    private boolean lastPage;

    private SearchTask searchTask;

    private FilterTask filterTask;

    private boolean dirty;

    private DocumentListener setDirtyListener;

    public SMLogController(SMLogModel model, SMLogView view) {
        this.model = model;
        this.view = view;
        this.view.setLoginListener(this);
        this.view.setLogViewListener(this);
        this.view.setTextMenuListener(this);
    }

    public void setContext(Context context, IProgressObserver observer) {
        this.observer = observer;
        logApi = context.getLogApi();
        subnet = context.getSubnetDescription();
        logApi.setLogStateListener(this);

        if (observer != null) {
            observer.onFinish();
        }
    }

    public void onRefresh() {
        view.onEndOfPage();

        if (observer != null) {
            observer.onFinish();
        }
    }

    public HostInfo getHostInfo() {
        return subnet.getCurrentFE();
    }

    public SMLogView getView() {
        return view;
    }

    public void showView(String name) {
        view.setView(name);
    }

    public LoginBean getCredentials() {
        return view.getCredentials();
    }

    public void startLog() {

        HostType hostType = getHostInfo().getHostType();
        if ((hostType != null) && (hostType.equals(HostType.ESM))) {
            onEsmHost(LogErrorType.ESM_NOT_SUPPORTED,
                    UILabels.STL50215_ESM_NOT_SUPPORTED);
        } else {
            logApi.startLog(subnet, false, getCredentials().getPassword());
            view.setRunningVisible(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onPrevious(long)
     */
    @Override
    public void onPrevious(long numLines) {
        logApi.schedulePreviousPage(numLines);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onNext(long)
     */
    @Override
    public void onNext(long numLines) {
        logApi.scheduleNextPage(numLines);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onLastLines(long)
     */
    @Override
    public void onLastLines(long numLines) {
        logApi.scheduleLastLines(numLines);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.logs.ILogStateListener#onResponse(com.intel.stl.api
     * .logs.LogResponse)
     */
    @Override
    public synchronized void onResponse(LogResponse response) {
        switch (response.getMsgType()) {
            case NUM_LINES:
                try {
                    long totalLines =
                            Long.parseLong(response.getEntries().get(0));
                    model.setNumLines(totalLines);
                    view.showTotalLines(model);
                    view.showLineRange(model.getStartLine(), model.getEndLine());
                    if (initInProgress) {
                        logApi.scheduleLastLines(view.getNumLinesRequested());
                        initInProgress = false;
                    }

                } catch (NumberFormatException e) {
                    log.error(e.getCause().getMessage());
                }
                break;

            case NEXT_PAGE:
            case PREVIOUS_PAGE:
                model.setLogMsg(response);
                model.setFilteredDoc(response.getEntries());
                view.updateView(model);
                view.restoreUserActions(firstPage, lastPage);
                break;

            case LAST_LINES:
                model.setLogMsg(response);
                model.setFilteredDoc(response.getEntries());
                view.showProgress(false);
                view.showLogView();
                view.updateView(model);
                view.restoreUserActions(firstPage, lastPage);
                break;

            case CHECK_FOR_FILE:
                String fileName = response.getEntries().get(0);
                model.setFileName(fileName);
                view.showFileName(fileName);
                break;

            case UNKNOWN:
                log.error(STLConstants.K2152_UNKNOWN_RESPONSE.getValue());
                break;

            default:
                break;
        }

        view.setRunningVisible(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.view.ILoginListener#credentialsReady()
     */
    @Override
    public void credentialsReady() {

        if (!logApi.isRunning()) {
            LoginBean credentials = view.getCredentials();
            subnet.getCurrentFE().setSshUserName(credentials.getUserName());
            subnet.getCurrentFE().setSshPortNum(
                    Integer.valueOf(credentials.getPortNum()));
            logApi.startLog(subnet, false, view.getCredentials().getPassword());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.logs.ILogStateListener#onError(com.intel.stl.api.logs
     * .LogErrorType, java.lang.Object[])
     */
    @Override
    public void onError(LogErrorType errorCode, Object... data) {
        int code = errorCode.getId();
        view.showProgress(false);
        view.setRunningVisible(false);
        view.restoreUserActions(firstPage, lastPage);
        view.clearLoginData();

        if (errorCode.equals(LogErrorType.LOG_FILE_NOT_FOUND)) {
            view.showErrorDialog(LogErrorTypeViz.values()[code].getLabel()
                    .getDescription(data));
        } else {
            view.showError(LogErrorTypeViz.values()[code].getLabel()
                    .getDescription(data));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.api.logs.ILogStateListener#onEsmHost(com.intel.stl.api.
     * logs.LogErrorType, java.lang.Object[])
     */
    @Override
    public void onEsmHost(LogErrorType errorCode, Object... data) {
        int code = errorCode.getId();
        List<String> entries = new ArrayList<String>();
        entries.add(LogErrorTypeViz.values()[code].getLabel().getDescription(
                data));
        view.showLogEntry(entries);
        view.enableControlPanel(false);
        view.disableUserActions();
        view.setRunningVisible(false);
        view.setEsmView();
        view.showLogView();
        logApi.stopLog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogStateListener#onReady()
     */
    @Override
    public void onReady() {
        if (initInProgress) {
            logApi.scheduleLastLines(view.getNumLinesRequested());
            view.setNumLineIcon(true);
            view.clearLoginData();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogViewListener#setLogMsg(com.intel
     * .stl.api.logs.LogResponse)
     */
    @Override
    public void setLogMessage(LogResponse msg) {
        model.setLogMsg(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setFirstPage(boolean)
     */
    @Override
    public void setFirstPage(boolean b) {
        firstPage = b;
        view.setPreviousEnabled(!b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setLastPage(boolean)
     */
    @Override
    public void setLastPage(boolean b) {
        lastPage = b;
        view.setNextEnabled(!b);
    }

    @Override
    public synchronized void onSearch(SearchState searchState) {
        String text = view.getDocument();
        List<FilterType> filters = view.getSelectedFilters();
        model.resetSearchResults();

        // Create search keys
        List<SearchKey> searchKeys = new ArrayList<SearchKey>();
        for (FilterType filter : filters) {
            searchKeys.add(new SearchKey(SearchState.FILTERED_SEARCH, filter
                    .getName(), filter.getColor()));
        }

        String searchKey = view.getSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            searchKeys.add(new SearchKey(searchState, searchKey,
                    UIConstants.INTEL_ORANGE));
        }
        view.enableSearch(false);

        // Search for the filter names in the displayed text and highlight them
        boolean showErrors =
                searchState.equals(SearchState.STANDARD_SEARCH) ? true : false;
        doSearch(text, showErrors, searchState, searchKeys);
    }

    public void doSearch(String text, boolean showErrors,
            SearchState searchState, SearchKey searchKey) {
        doSearch(text, showErrors, searchState,
                Collections.singletonList(searchKey));
    }

    public void doSearch(String text, boolean showErrors,
            SearchState searchState, List<SearchKey> searchKeys) {
        checkFilter();
        checkSearch();

        searchTask = new SearchTask(view, model, text, searchState, searchKeys);
        searchTask.execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onCancelSearch(
     * JTextComponent)
     */
    @Override
    public void onCancelSearch(String searchKey) {
        checkSearch();

        // Reset ALL search results and set the number of matches to 0
        model.resetSearchResults();

        // Reset number of matches to 0
        view.showNumMatches(0);

        // Un-highlight the search results
        view.unHighlightText();

        // Re-run the search to highlight the filters
        onSearch(SearchState.FILTERED_SEARCH);
    }

    protected synchronized void checkSearch() {
        try {
            if (searchTask != null && !searchTask.isDone()) {
                searchTask.cancel(true);
            }
        } catch (CancellationException e) {
            log.error(e.getMessage());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogStateListener#onSessionDown()
     */
    @Override
    public void onSessionDown(String errorMessage) {
        view.showError(errorMessage);
        view.showLoginView();
        initInProgress = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#onFilter()
     */
    @Override
    public void onFilter() {
        checkSearch();
        checkFilter();

        filterTask = new FilterTask(this, model, view.getSelectedFilters());
        filterTask.execute();
    }

    protected synchronized void checkFilter() {
        if (filterTask != null && !filterTask.isDone()) {
            filterTask.cancel(true);
        }
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setStartLine(long)
     */
    @Override
    public void setStartLine(long lineNum) {
        model.setStartLine(lineNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.logs.ILogPageListener#setEndLine(long)
     */
    @Override
    public void setEndLine(long lineNum) {
        model.setEndLine(lineNum);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ITextMenuListener#doAction(TextEvent)
     */
    @Override
    public void doAction(TextEvent e) {

        switch (e.getEventType()) {
            case COPY:
            case PASTE:
                // The copy and paste actions uses the DefaultEditorKit
                // CopyAction() and PasteAction() in the TextContentPanel
                break;

            case HIGHLIGHT:
                view.setSearchField(e.getText());
                onSearch(SearchState.MARKED_SEARCH);
                break;

            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ITextListener#resetHighlights()
     */
    @Override
    public void resetSearch() {
        model.resetSearchResults();
        view.resetSearchField();
        view.unHighlightText();
        view.showNumMatches(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogViewListener#getDocumentListener()
     */
    @Override
    public DocumentListener getDocumentListener() {

        if (setDirtyListener == null) {
            setDirtyListener = new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    dirty = true;
                    onDirty();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    dirty = true;
                    onCancelSearch(view.getLastSearchKey());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    dirty = true;
                    onDirty();
                }
            };
        }

        return setDirtyListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#isDirty()
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ILogViewListener#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    protected void onDirty() {
        dirty = false;
        view.saveLastSearchKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ITextMenuListener#unHighlightSelection
     * (String, int, int)
     */
    @Override
    public void unHighlightSelection(String key, int start, int end) {
        view.unHighlightSelection(key, start, end);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ITextMenuListener#getSelectionStart()
     */
    @Override
    public int getSelectionStart() {
        return model.getSelectionStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ITextMenuListener#getSelectionEnd()
     */
    @Override
    public int getSelectionEnd() {
        return model.getSelectionEnd();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.logs.ILogViewListener#setCurrentSelection
     * (int, int)
     */
    @Override
    public void setCurrentSelection(String key, int start, int end) {
        model.setSelection(key, start, end);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.admin.impl.logs.ITextMenuListener#getSelectedKey()
     */
    @Override
    public String getSelectedKey() {
        return model.getSelectedKey();
    }

    @Override
    public void cancelLogin() {

        view.clearLoginData();
        logApi.stopLog();
    }
}
