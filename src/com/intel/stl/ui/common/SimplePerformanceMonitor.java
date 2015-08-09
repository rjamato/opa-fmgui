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
 *  File Name: SimplePerformanceMonitor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/08/05 13:48:39  jijunwan
 *  Archive Log:    a utility class helps us do some very simple, quick but not so accurate performance monitor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.io.PrintStream;

/**
 * A simple class used for inspect code performance
 */
public class SimplePerformanceMonitor {
    private String id;
    private long time;
    private PrintStream out = System.out;
    
    public SimplePerformanceMonitor() {
        this(SimplePerformanceMonitor.class.getSimpleName());
    }

    /**
     * Description: 
     *
     * @param id 
     */
    public SimplePerformanceMonitor(String id) {
        super();
        this.id = id;
    }

    /**
     * Specify output
     */
    public void setPrintStream(PrintStream out) {
        this.out = out;
    }
    
    /**
     * mark current time as reference point
     */
    public void mark() {
        time = System.currentTimeMillis();
    }
    
    /**
     * print out delta time since last call to this method or #mark
     * 
     * @param text prefix text for description purpose
     */
    public void print(String text) {
        print(text, true);
    }
    
    /**
     * print out delta time either since last call to {@link #print(String)} or
     * {@link #mark()}
     * 
     * @param text
     *            prefix text for description purpose
     * @param relative
     *            delta time will be based on last call to print if
     *            <code>relative</code> is true. Otherwise the delta will be
     *            absolute delta between current time and last mark time.
     */
    public void print(String text, boolean relative) {
        long delta = 0;
        if (relative) {
            delta = -time + (time=System.currentTimeMillis());
        } else {
            delta = -time + System.currentTimeMillis();
        }
        out.println("["+id+"] "+text+" "+delta+" ms");
    }
}
 