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
 *  File Name: StaDetailsController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14.2.1  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/04 21:44:24  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/10/23 16:33:19  jijunwan
 *  Archive Log:    minor change on timers - intend to improve timer behavior so the action will be cancelled event if it's already in EDT queue
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/15 22:00:24  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/08/05 18:39:00  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/21 16:28:39  jijunwan
 *  Archive Log:    integer format adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/08 14:24:06  jijunwan
 *  Archive Log:    minor change - rename caXXX to fiXXX
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/08 14:07:04  jijunwan
 *  Archive Log:    removed route from state chart per feedback we got
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/26 20:29:33  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/05 18:32:47  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/29 14:25:05  jijunwan
 *  Archive Log:    jfreechart dataset is not thread safe, put all dataset related operation into EDT, so they will synchronize
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 19:25:36  jijunwan
 *  Archive Log:    MVC refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/30 17:30:04  jijunwan
 *  Archive Log:    use Util.runInEDT
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 16:20:43  jijunwan
 *  Archive Log:    minor refactory
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:50:38  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/08 17:32:56  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.Timer;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.view.StaDetailsPanel;
import com.intel.stl.ui.model.NodeTypeViz;
import com.intel.stl.ui.model.StateShortTypeViz;

/**
 * per feedback we got, we do not show Router info here. we intentionally
 * change it on UI side rather than backend because we need to support it
 * again in the future
 */
public class StaDetailsController {
    private final NodeTypeViz[] nodeTypes;

    private final String name;

    private final DefaultCategoryDataset failedDataset;

    private final DefaultCategoryDataset skippedDataset;

    private final DefaultPieDataset typeDataset;

    private final StaDetailsPanel view;

    private Timer viewClearTimer;

    public StaDetailsController(String name, StaDetailsPanel view) {
        this.view = view;

        this.name = name;
        view.setName(name);

        failedDataset = new DefaultCategoryDataset();
        failedDataset.addValue(0, name, StateShortTypeViz.FAILED.name());
        view.setFailedDataset(failedDataset);

        skippedDataset = new DefaultCategoryDataset();
        skippedDataset.addValue(0, name, StateShortTypeViz.SKIPPED.name());
        view.setSkipedDataset(skippedDataset);

        typeDataset = new DefaultPieDataset();
        nodeTypes = view.getTypes();
        Color[] colors = new Color[nodeTypes.length];
        for (int i = 0; i < colors.length; i++) {
            typeDataset.setValue(nodeTypes[i].getName(), 0);
            colors[i] = nodeTypes[i].getColor();
        }
        view.setTypeDataset(typeDataset, colors);
    }

    /**
     * @return the view
     */
    public StaDetailsPanel getView() {
        return view;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the failedDataset
     */
    public DefaultCategoryDataset getFailedDataset() {
        return failedDataset;
    }

    /**
     * @return the skippedDataset
     */
    public DefaultCategoryDataset getSkippedDataset() {
        return skippedDataset;
    }

    /**
     * @return the typeDataset
     */
    public DefaultPieDataset getTypeDataset() {
        return typeDataset;
    }

    public void setStates(final long total, final long failed,
            final long skipped) {
        clearTimer();
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                failedDataset.setValue((double) failed / total, name,
                        StateShortTypeViz.FAILED.name());
                skippedDataset.setValue((double) skipped / total, name,
                        StateShortTypeViz.SKIPPED.name());
                String failedStr = UIConstants.INTEGER.format(failed);
                String skippedStr = UIConstants.INTEGER.format(skipped);

                view.setFailed(failedStr);
                view.setSkipped(skippedStr);
            }
        });
    }

    public void setTypes(int total, EnumMap<NodeType, Integer> types) {
        EnumMap<NodeType, Long> wrapper =
                new EnumMap<NodeType, Long>(NodeType.class);
        for (NodeType nodeType : types.keySet()) {
            wrapper.put(nodeType, (long) types.get(nodeType));
        }
        setTypes(total, wrapper);
    }

    public void setTypes(final long total, EnumMap<NodeType, Long> types) {
        clearTimer();
        final long[] counts = NodeTypeViz.getDistributionValues(types);

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                String totalNumber = UIConstants.INTEGER.format(total);
                view.setTotalNumber(totalNumber);
                for (NodeTypeViz nodeType : nodeTypes) {
                    long count = counts[nodeType.ordinal()];
                    typeDataset.setValue(nodeType.getName(), count);

                    String number = UIConstants.INTEGER.format(count);
                    String label =
                            count == 1 ? nodeType.getName() : nodeType
                                    .getPluralName();
                    view.setTypeInfo(nodeType, number, label);
                }
            }
        });
    }

    protected void clearTimer() {
        if (viewClearTimer != null) {
            if (viewClearTimer.isRunning()) {
                viewClearTimer.stop();
            }
            viewClearTimer = null;
        }
    }

    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                failedDataset.clear();
                skippedDataset.clear();
                typeDataset.clear();
            }
        });
        if (viewClearTimer == null) {
            viewClearTimer =
                    new Timer(UIConstants.UPDATE_TIME, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (viewClearTimer != null) {
                                view.clear();
                            }
                        }
                    });
            viewClearTimer.setRepeats(false);
        }
        viewClearTimer.restart();
    }
}
