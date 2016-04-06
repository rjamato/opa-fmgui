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
 *  File Name: Util.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.21  2016/03/02 18:27:30  jijunwan
 *  Archive Log: PR 133067 - Add a popup window that e-mail was sent successfully when "test" button is click
 *  Archive Log:
 *  Archive Log: - changed to disable button after we click test button
 *  Archive Log: - changed to show "sending email..." message when we are sending out a test email
 *  Archive Log: - changed to show "Test message sent out, please check your email account." after email sent out
 *  Archive Log: - change to recover message to normal text when there is a user action
 *  Archive Log: - added undo/redo capability to email address text area
 *  Archive Log:
 *  Archive Log: Revision 1.20  2016/01/28 21:38:05  jijunwan
 *  Archive Log: 132565 - Log Error or Warning message display on screen
 *  Archive Log:
 *  Archive Log: - changed to log more detailed info
 *  Archive Log:
 *  Archive Log: Revision 1.19  2015/11/16 18:44:49  jypak
 *  Archive Log: PR 130970 - Console commands produced by arrow keys can't be detected if user changes prompt in session. JUnit updated not to use Swing.
 *  Archive Log:
 *  Archive Log: Revision 1.18  2015/08/18 14:28:35  jijunwan
 *  Archive Log: PR 130033 - Fix critical issues found by Klocwork or FindBugs
 *  Archive Log: - DateFormat is not thread safe. Changed to create new DateFormat to avoid sharing it among different threads
 *  Archive Log:
 *  Archive Log: Revision 1.17  2015/08/17 18:54:12  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.16  2015/07/28 18:29:10  fisherma
 *  Archive Log: PR 129219 - Admin page login dialog improvement
 *  Archive Log:
 *  Archive Log: Revision 1.15  2015/07/14 17:00:59  jijunwan
 *  Archive Log: PR 129541 - Should forbid save or deploy when there is invalid edit on management panel
 *  Archive Log: - removed unnecessary argument for warning message display
 *  Archive Log:
 *  Archive Log: Revision 1.14  2015/06/10 19:58:58  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
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
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.view.DialogFactory;

/**
 * @author jijunwan
 *
 */
public class Util {
    private final static Logger log = LoggerFactory.getLogger(Util.class);

    private static final String UNDO_ACTION = "UNDO";

    private static final String REDO_ACTION = "REDO";

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
            g2d.fillRect(insets.left, insets.top,
                    size - insets.left - insets.right,
                    size - insets.top - insets.bottom);
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
        Point endPoint = new Point(desired.x + desired.width,
                desired.y + desired.height);
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
        log.error("Show Error Message on Dialog", new Exception(message));
        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showErrorDialog(parent, message);
            }
        });

    }

    public static void showError(final Component parent, final Throwable e) {
        log.error("Show Error Message on Dialog", e);
        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showErrorDialog(parent, e);
            }
        });

    }

    public static void showErrors(final Component parent,
            final Collection<? extends Throwable> e) {
        for (Throwable error : e) {
            log.error("Show Error Message on Dialog", error);
        }
        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showErrorDialog(parent, e);
            }
        });

    }

    public static void showWarningMessage(Component parent,
            final String message) {
        log.warn("Show Warning Message on Dialog", new Exception(message));
        final Component root =
                parent == null ? null : SwingUtilities.getRoot(parent);
        runInEDT(new Runnable() {
            @Override
            public void run() {
                DialogFactory.showWarningDialog(root, message);
            }
        });
    }

    public static int showConfirmDialog(Component parent,
            final String message) {

        final Component root =
                parent == null ? null : SwingUtilities.getRoot(parent);

        return DialogFactory.showConfirmDialog(root, message);

    }

    public static void makeUndoable(JTextComponent comp) {
        final UndoManager undoMgr = new UndoManager();
        comp.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                undoMgr.addEdit(evt.getEdit());
            }
        });

        comp.getActionMap().put(UNDO_ACTION, new AbstractAction(UNDO_ACTION) {
            private static final long serialVersionUID = 6916362113277049438L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undoMgr.canUndo()) {
                        undoMgr.undo();
                    }
                } catch (CannotUndoException e) {
                    e.printStackTrace();
                }
            }
        });
        comp.getActionMap().put(REDO_ACTION, new AbstractAction(REDO_ACTION) {
            private static final long serialVersionUID = 1905302135550403038L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undoMgr.canRedo()) {
                        undoMgr.redo();
                    }
                } catch (CannotRedoException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create keyboard accelerators for undo/redo actions (Ctrl+Z/Ctrl+Y)
        comp.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                InputEvent.CTRL_DOWN_MASK), UNDO_ACTION);
        comp.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                InputEvent.CTRL_DOWN_MASK), REDO_ACTION);
    }

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final DateFormat getYYMMDD() {
        return new SimpleDateFormat("yyyy/MM/dd");
    }

    public static final DateFormat getHHMMSS() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    public static final DateFormat getYYYYMMDDHHMMSS() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a, z");
    }

    public static final String extractCommand(String cmd, String prompt) {
        String[] commands = cmd.split(Pattern.quote(prompt));
        String command = null;
        if (commands.length > 1) {
            String commandLine = commands[1];
            commandLine = commandLine.trim();
            command = commandLine.split(" ")[0];
        }
        return command;
    }
}
