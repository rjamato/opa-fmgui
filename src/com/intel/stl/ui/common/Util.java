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
 *  File Name: Util.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12.2.1  2015/05/06 19:39:19  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.intel.stl.ui.common.view.DialogFactory;

/**
 * @author jijunwan
 * 
 */
public class Util {
    public static ImageIcon getImageIcon(String path) {
        if (path != null) {
            URL loc = Util.class.getResource(path);
            if (loc != null) {
                return new ImageIcon(loc);
            }
        }

        return null;
    }

    public static ImageIcon generateImageIcon(Color color, int size,
            Insets insets) {
        BufferedImage image =
                new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        if (insets != null) {
            g2d.fillRect(insets.left, insets.top, size - insets.left
                    - insets.right, size - insets.top - insets.bottom);
        }
        g2d.dispose();
        return new ImageIcon(image);
    }

    /**
     * 
     * <i>Description:</i> short a string to the format xx...xxxx
     * 
     * @param source
     *            the string to shorten
     * @param prefixLength
     *            number of prefix charts to keep
     * @param totalLength
     *            desired total length
     * @return
     */
    public static String truncateString(String source, int prefixLength,
            int totalLength) {
        if (source.length() <= totalLength) {
            return source;
        }

        if (prefixLength + 3 > totalLength) {
            throw new IllegalArgumentException(
                    "total length meust be larger than prefix length + 3!");
        }

        int tokeep = totalLength - prefixLength - 3;
        return source.substring(0, prefixLength) + "..."
                + source.substring(source.length() - tokeep, source.length());
    }

    public static String addSpaceBetText(String str1, int space, String str2) {
        char[] padded = new char[str1.length() + space];
        Arrays.fill(padded, ' ');
        str1.getChars(0, str1.length(), padded, 0);
        return new String(padded) + str2;
    }

    public static String getFuzzKey(String pattern, Collection<String> keys) {
        if (pattern == null || keys == null || keys.isEmpty()) {
            return null;
        }

        MessageFormat format = new MessageFormat(pattern);
        for (String key : keys) {
            try {
                format.parse(key);
                return key;
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public static boolean matchPattern(String pattern, String test) {
        MessageFormat format = new MessageFormat(pattern);
        try {
            format.parse(test);
            return true;
        } catch (ParseException e) {
        }
        return false;
    }

    public static void runInEDT(final Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    public static int[] toPrimitive(Integer[] IntArray) {
        int[] intArray = new int[IntArray.length];
        for (int i = 0; i < IntArray.length; i++) {
            intArray[i] = IntArray[i].intValue();
        }
        return intArray;
    }

    public static Point adjustPoint(Rectangle desired, Window owner) {
        if (owner != null) {
            Rectangle curScreen = owner.getGraphicsConfiguration().getBounds();
            Point res = adjustPoint(desired, curScreen);
            if (res != null) {
                return res;
            }
        }

        // current point is on another screen
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        for (GraphicsDevice gd : gds) {
            Rectangle bounds = gd.getDefaultConfiguration().getBounds();
            Point res = adjustPoint(desired, bounds);
            if (res != null) {
                return res;
            }
        }
        return new Point(desired.x, desired.y);
    }

    private static Point adjustPoint(Rectangle desired, Rectangle curScreen) {
        Point startPoint = new Point(desired.x, desired.y);
        Point endPoint =
                new Point(desired.x + desired.width, desired.y + desired.height);
        if (curScreen.contains(desired)) {
            return startPoint;
        }
        if (curScreen.contains(startPoint)) {
            if (endPoint.x > curScreen.x + curScreen.width) {
                startPoint.x = curScreen.x + curScreen.width - desired.width;
            }
            if (endPoint.y > curScreen.y + curScreen.height) {
                startPoint.y = curScreen.y + curScreen.height - desired.height;
            }
            return startPoint;
        }
        return null;
    }

    public static void showErrorMessage(final Component parent,
            final String message) {

        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showErrorDialog(parent, message);
            }
        });

    }

    public static void showError(final Component parent, final Throwable e) {

        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showErrorDialog(parent, e);
            }
        });

    }

    public static void showErrors(final Component parent,
            final Collection<? extends Throwable> e) {

        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showErrorDialog(parent, e);
            }
        });

    }

    public static void showWarningMessage(Component parent,
            final String message, final String title) {

        final Component root =
                parent == null ? null : SwingUtilities.getRoot(parent);
        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showWarningDialog(root, message);
            }
        });
    }

    public static int showConfirmDialog(Component parent, final String message) {

        final Component root =
                parent == null ? null : SwingUtilities.getRoot(parent);

        return DialogFactory.showConfirmDialog(root, message);

    }
}
