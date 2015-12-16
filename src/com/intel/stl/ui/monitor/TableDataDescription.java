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
 *  File Name: TableDataDescription.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:53:40  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/05/28 15:29:22  jypak
 *  Archive Log:    PR 128873 - Add "Flits" in performance table for Data related columns.
 *  Archive Log:    Added "(MB)" to RcvData, XmitData column header.
 *  Archive Log:    Added "(MBps)" to data rates.
 *  Archive Log:    Added data in "Flits" or data rate in "(Flits/sec)" to tool tips.
 *  Archive Log:    Used the TableDataDescription to convert and format the data.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/04/07 14:38:27  jypak
 *  Archive Log:    PR 126998 - Received/Transmitted data counters for Device Node and Device ports should show in MB rather than Flits. Fixed by converting units to Byte/KB/MB/GB. Also, tool tips were added to show the units for each value.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import java.io.Serializable;
import java.text.DecimalFormat;

public class TableDataDescription implements Serializable {

    private static final long serialVersionUID = -929415085901813310L;

    private double data;

    private String description;

    private final long unit = 1000000;

    public TableDataDescription(double data, String description) {
        this.data = data;
        this.description = description;
    }

    /**
     * @return the data
     */
    public double getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(double data) {
        this.data = data;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * <i>Description:</i>This formatting is related to the unit, so, unit
     * conversion (MB) is also done in this class.
     * 
     * @return
     */
    public String getFormattedData() {
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(6);
        double dataConverted = data / unit;

        String dataStr = null;
        if (dataConverted < (1d / unit) && dataConverted != 0) {
            dataStr = "<0.000001";
        } else {
            dataStr = df.format(dataConverted);
        }

        return dataStr;
    }

}
