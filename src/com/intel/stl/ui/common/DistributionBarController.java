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
 *  File Name: DistributionBarController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/02/05 19:41:29  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/04 21:44:22  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
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
 *  Archive Log:    Revision 1.1  2014/04/01 20:40:18  jijunwan
 *  Archive Log:    introduced card. And refactoried to apply MVC patterns on UI widgets.
 *  Archive Log:
 *
 *  Overview: The controller for JDistributionBar
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.ImageIcon;

import com.intel.stl.ui.common.view.DistributionBarPanel;

public class DistributionBarController {
    private final String[] items;

    private long[] values;

    private long[] cumulativeSum;

    private final Color[] colors;

    private final DistributionBarPanel distributionBar;

    public DistributionBarController(DistributionBarPanel view, String[] items,
            long[] values, Color[] colors) {
        super();
        this.items = items;
        this.colors = colors;

        distributionBar = view;
        updateValues(values);
        check(items, colors);
        initBar();
    }

    /**
     * @return the distributionBar
     */
    public DistributionBarPanel getDistributionBar() {
        return distributionBar;
    }

    protected void check(String[] names, Color[] colors) {
        if (names == null) {
            throw new IllegalArgumentException("No names for segments");
        }

        if (colors != null && colors.length != names.length) {
            throw new IllegalArgumentException("Invalid argument. Expected "
                    + colors.length + " colors, got " + values.length
                    + " colors.");
        }
    }

    protected void check(long[] values, String[] names) {
        if (names == null) {
            throw new IllegalArgumentException("No names for segments");
        }

        if (values != null && values.length != names.length) {
            throw new IllegalArgumentException("Invalid argument. Expected "
                    + names.length + " values, got " + values.length
                    + " values.");
        }
    }

    protected void initBar() {
        ImageIcon[] icons = new ImageIcon[colors.length];
        String[] itemLabels = new String[colors.length];
        for (int i = 0; i < colors.length; i++) {
            icons[i] =
                    Util.generateImageIcon(colors[i], 10,
                            new Insets(1, 1, 1, 1));
            itemLabels[i] = getItemLegend(i);
        }
        distributionBar.init(itemLabels, icons);
    }

    public void setDistribution(long[] values) {
        updateValues(values);
        final String[] itemLabels = new String[colors.length];
        for (int i = 0; i < colors.length; i++) {
            itemLabels[i] = getItemLegend(i);
        }
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                distributionBar.update(itemLabels, cumulativeSum, colors);
            }
        });
    }

    protected String getItemLegend(int i) {
        if (cumulativeSum == null) {
            return STLConstants.K0039_NOT_AVAILABLE.getValue();
        }

        double perentage =
                (double) values[i] / cumulativeSum[cumulativeSum.length - 1];
        return items[i] + " " + values[i] + " ("
                + UIConstants.PERCENTAGE.format(perentage) + ")";
    }

    protected void updateValues(long[] values) {
        check(values, items);
        this.values = values;

        if (values == null) {
            cumulativeSum = null;
        } else {
            int sum = 0;
            cumulativeSum = new long[values.length];
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
                cumulativeSum[i] = sum;
            }
        }
    }
}
