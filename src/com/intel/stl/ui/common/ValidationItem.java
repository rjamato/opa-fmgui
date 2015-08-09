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
 *  File Name: ValidationItem.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/10 22:45:35  jijunwan
 *  Archive Log:    improved to do and show validation before we save an application
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.util.concurrent.Callable;

public class ValidationItem<E> {
    public final static String[] NAMES = new String[] {
            STLConstants.K2120_VALIDATION_TYPE.getValue(),
            STLConstants.K2121_ISSUES.getValue(),
            STLConstants.K2122_SUGGESTION.getValue(),
    // STLConstants.K2123_QUICK_FIX.getValue()
            };

    private final String type;

    private final String issues;

    private final String suggestions;

    private Callable<E[]> quickFix;

    /**
     * Description:
     * 
     * @param type
     * @param issues
     * @param suggestions
     * @param quickFix
     */
    public ValidationItem(String type, String issues, String suggestions) {
        super();
        this.type = type;
        this.issues = issues;
        this.suggestions = suggestions;
    }

    /**
     * @param quickFix
     *            the quickFix to set
     */
    public void setQuickFix(Callable<E[]> quickFix) {
        this.quickFix = quickFix;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the issues
     */
    public String getIssues() {
        return issues;
    }

    /**
     * @return the suggestions
     */
    public String getSuggestions() {
        return suggestions;
    }

    /**
     * @return the quickFix
     */
    public Callable<E[]> getQuickFix() {
        return quickFix;
    }

}
