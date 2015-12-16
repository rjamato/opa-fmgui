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
 *  File Name: SMLogModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/09/25 13:53:59  rjtierne
 *  Archive Log:    PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log:    - Added new attributes to support:
 *  Archive Log:      * starting/ending line on each page
 *  Archive Log:      * selected search
 *  Archive Log:      * filtered search
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/08/17 18:54:28  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/08/17 14:22:39  rjtierne
 *  Archive Log:    PR 128979 - SM Log display
 *  Archive Log:    This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *
 *  Overview: Model for the SM log
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.api.logs.LogResponse;
import com.intel.stl.ui.admin.impl.logs.SearchPositionBean;

public class SMLogModel {

    private String fileName;

    private long numLines;

    private long currentLine;

    private LogResponse logMsg;

    private String latestEntry;

    private boolean firstPage;

    private boolean lastPage;

    private long startLine;

    private long endLine;

    private String selectedSearchKey;

    private int selectionStart;

    private int selectionEnd;

    private List<String> filteredDoc = new ArrayList<String>();

    private final List<SearchPositionBean> searchResults =
            new ArrayList<SearchPositionBean>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getNumLines() {
        return numLines;
    }

    public void setNumLines(long numLines) {
        this.numLines = numLines;
    }

    public long getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(long currentLine) {
        this.currentLine = currentLine;
    }

    public void setLogMsg(LogResponse msg) {
        this.logMsg = msg;
    }

    public LogResponse getLogMsg() {
        return logMsg;
    }

    public void setLatestEntry(String entry) {
        this.latestEntry = entry;
    }

    public String getLatestEntry() {
        return this.latestEntry;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<SearchPositionBean> getSearchResults() {
        return searchResults;
    }

    public void addSearchResult(SearchPositionBean position) {
        this.searchResults.add(position);
    }

    public void clearSearchResults(String key) {

        if (key == null) {
            return;
        }

        for (int i = searchResults.size() - 1; i >= 0; i--) {
            SearchPositionBean pos = searchResults.get(i);
            if (pos.getKey().equals(key)) {
                searchResults.remove(i);
            }
        }

    }

    public void resetSearchResults() {
        this.searchResults.clear();
    }

    public List<String> getFilteredDoc() {
        return filteredDoc;
    }

    public void setFilteredDoc(List<String> filteredDoc) {
        this.filteredDoc = filteredDoc;
    }

    public long getStartLine() {
        return startLine;
    }

    public void setStartLine(long startLine) {
        this.startLine = startLine;
    }

    public long getEndLine() {
        return endLine;
    }

    public void setEndLine(long endLine) {
        this.endLine = endLine;
    }

    public String getSelectedKey() {
        return selectedSearchKey;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelection(String key, int start, int end) {
        selectedSearchKey = key;
        selectionStart = start;
        selectionEnd = end;
    }
}
