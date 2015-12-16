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
 *  File Name: DistributionPieController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:12  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/09 14:17:20  jijunwan
 *  Archive Log:    moved JFreeChart to view side, controller side only take care dataset
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/08 19:25:40  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/30 17:30:06  jijunwan
 *  Archive Log:    use Util.runInEDT
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/01 20:40:17  jijunwan
 *  Archive Log:    introduced card. And refactoried to apply MVC patterns on UI widgets.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.ImageIcon;

import org.jfree.data.general.DefaultPieDataset;

import com.intel.stl.ui.common.view.DistributionPiePanel;

public class DistributionPieController {
    private DefaultPieDataset dataset;

    private String[] items;

    private int[] values;

    private int sum;

    private DistributionPiePanel distributionPie;

    public DistributionPieController(DistributionPiePanel distributionPie, 
            String[] items, int[] values, Color[] colors, int lableColumns) {
        this.distributionPie = distributionPie;
        this.items = items;
        updateValues(values);

        String[] itemLabels = new String[items.length];
        ImageIcon[] icons = new ImageIcon[items.length];
        for (int i = 0; i < items.length; i++) {
            itemLabels[i] = getItemLegend(i);
            icons[i] = Util.generateImageIcon(colors[i], 10, new Insets(1, 1,
                    1, 1));
        }
        distributionPie.setDataset(dataset, colors);
        distributionPie.setLabels(itemLabels, icons, lableColumns);
    }

    /**
     * @return the distributionPie
     */
    public DistributionPiePanel getDistributionPie() {
        return distributionPie;
    }

    protected void check(String[] names, Color[] colors) {
        if (names == null)
            throw new IllegalArgumentException("No names for segments");

        if (colors != null && colors.length != names.length)
            throw new IllegalArgumentException("Invalid argument. Expected "
                    + colors.length + " colors, got " + values.length
                    + " colors.");
    }

    protected void check(int[] values, String[] names) {
        if (names == null)
            throw new IllegalArgumentException("No names for segments");

        if (values != null && values.length != names.length)
            throw new IllegalArgumentException("Invalid argument. Expected "
                    + names.length + " values, got " + values.length
                    + " values.");
    }

    protected String getItemLegend(int i) {
        if (values == null)
            return " ";

        double perentage = (double) values[i] / sum;
        return UIConstants.PERCENTAGE.format(perentage) + " " + items[i];
    }

    public void setDistribution(int[] values) {
        updateValues(values);
        final String[] itemLabels = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            itemLabels[i] = getItemLegend(i);
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                distributionPie.update(itemLabels);
            }
        });
    }

    protected void updateValues(int[] values) {
        check(values, items);
        this.values = values;

        sum = 0;
        if (dataset == null) {
            dataset = new DefaultPieDataset();
        }
        if (values == null) {
            for (int i = 0; i < items.length; i++) {
                dataset.setValue(items[i], 0);
            }
        } else {
            for (int i = 0; i < values.length; i++) {
                dataset.setValue(items[i], values[i]);
                sum += values[i];
            }
        }
    }
}
