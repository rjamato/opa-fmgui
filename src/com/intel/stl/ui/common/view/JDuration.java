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
package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;

/**
 * @author jijunwan
 * 
 */
public class JDuration extends JPanel {
    private static final long serialVersionUID = 109270047037650069L;

    private long durationInSeconds;

    private long days, hours, minutes;

    private JLabel daysLabel, hoursLabel, minutesLabel;

    private Font numberFont;
    private Color numberColor;

    private Font labelFont;
    private Color labelColor;

    public JDuration() {
        this(-1, TimeUnit.SECONDS);
    }

    public JDuration(long duration, TimeUnit unit) {
        super();
        durationInSeconds = unit.toSeconds(duration);
        convert();
        initComponent();
    }

    public void setDuration(long duration, TimeUnit unit) {
        durationInSeconds = unit.toSeconds(duration);
        convert();
        updateComponent();
    }

    public void setLAF(Font numberFont, Color numberColor, Font labelFont, Color labelColor) {
        this.numberFont = numberFont;
        this.numberColor = numberColor;
        this.labelFont = labelFont;
        this.labelColor = labelColor;
        removeAll();
        initComponent();
    }

    protected void convert() {
        if (durationInSeconds<0) {
            days = hours = minutes = -1;
        } else {
            days = (int) TimeUnit.SECONDS.toDays(durationInSeconds);
            hours = TimeUnit.SECONDS.toHours(durationInSeconds) - (days * 24);
            minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds)
                    - (TimeUnit.SECONDS.toHours(durationInSeconds) * 60);
            // seconds = TimeUnit.SECONDS.toSeconds(durationInSeconds) -
            // (TimeUnit.SECONDS.toMinutes(durationInSeconds) *60);
        }
    }

    protected void initComponent() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        //FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 3, 2);
        setLayout(layout);
        JLabel label = null;
        if (days > 0) {
            daysLabel = createNumberLabel(days);
            add(daysLabel);

            label = createNameLabel(STLConstants.K0009_DAYS.getValue());
            add(label);
        } else {
            daysLabel = null;
        }

        if (days > 0 || hours > 0) {
            hoursLabel = createNumberLabel(hours);
            add(hoursLabel);

            label = createNameLabel(STLConstants.K0010_HOURS.getValue());
            add(label);
        } else {
            hoursLabel = null;
        }

        minutesLabel = createNumberLabel(minutes);
        add(minutesLabel);

        label = createNameLabel(STLConstants.K0011_MINUTES.getValue());
        add(label);
    }

    protected void updateComponent() {
        if (days > 0 && daysLabel == null) {
            removeAll();
            initComponent();
            return;
        }

        if (hours > 0 && hoursLabel == null) {
            removeAll();
            initComponent();
            return;
        }

        if (daysLabel != null) {
            daysLabel.setText(getValueString(days));
        }

        if (hoursLabel != null) {
            hoursLabel.setText(getValueString(hours));
        }

        minutesLabel.setText(getValueString(minutes));
    }

    protected JLabel createNameLabel(String text) {
        JLabel res = new JLabel(text);
        res.setFont(labelFont);
        res.setForeground(labelColor);
        res.setAlignmentY(JLabel.BOTTOM_ALIGNMENT);
        res.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));
        return res;
    }

    protected JLabel createNumberLabel(long number) {
        String text = getValueString(number);
        JLabel res = new JLabel(text);
        res.setFont(numberFont);
        res.setForeground(numberColor);
        res.setAlignmentY(JLabel.BOTTOM_ALIGNMENT);
        return res;
    }
    
    protected String getValueString(long number) {
        return number>=0 ? Long.toString(number) :
            STLConstants.K0039_NOT_AVAILABLE.getValue();
    }
    
    public void clear() {
        setDuration(-1, TimeUnit.SECONDS);
    }
}
