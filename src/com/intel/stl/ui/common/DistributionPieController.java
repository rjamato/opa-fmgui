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
 *  File Name: DistributionPieController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
