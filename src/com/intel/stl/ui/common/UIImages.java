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
 *  File Name: UIImages.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.49.2.1  2015/08/12 15:27:03  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.49  2015/04/18 01:39:59  fisherma
 *  Archive Log:    PR 127653 - FM GUI errors after connection loss.  The code changes address issue #2 reported in the bug.  Adding common dialog to display errors.  Needs further appearance improvements.
 *  Archive Log:
 *  Archive Log:    Revision 1.48  2015/04/16 16:26:09  jijunwan
 *  Archive Log:    changed splash with new product name
 *  Archive Log:
 *  Archive Log:    Revision 1.47  2015/04/09 12:48:56  fisherma
 *  Archive Log:    Update switch, host and HFI hosts icons.
 *  Archive Log:
 *  Archive Log:    Revision 1.46  2015/04/08 13:39:51  jijunwan
 *  Archive Log:    checked in experiment code by accident. changed it back.
 *  Archive Log:
 *  Archive Log:    Revision 1.45  2015/04/07 20:17:44  jijunwan
 *  Archive Log:    second round wizard polishment
 *  Archive Log:
 *  Archive Log:    Revision 1.44  2015/04/06 11:14:13  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.43  2015/03/30 22:36:11  jijunwan
 *  Archive Log:    improved AboutDialog
 *  Archive Log:
 *  Archive Log:    Revision 1.42  2015/03/30 15:10:18  rjtierne
 *  Archive Log:    Added new icon WARNING2_ICON
 *  Archive Log:
 *  Archive Log:    Revision 1.41  2015/03/24 17:44:42  jijunwan
 *  Archive Log:    added UI resources for DeviceGroup editor
 *  Archive Log:
 *  Archive Log:    Revision 1.40  2015/03/18 20:53:23  fisherma
 *  Archive Log:    Adding AboutDialog and new images for the dialog.  Updated build.xml file to copy the html file containing copyright text into the 'help' directory inside the jar file.
 *  Archive Log:
 *  Archive Log:    Revision 1.39  2015/03/11 15:22:59  rjtierne
 *  Archive Log:    Multinet Wizard: Added icons for the wizard welcome window
 *  Archive Log:
 *  Archive Log:    Revision 1.38  2015/03/05 17:35:15  jijunwan
 *  Archive Log:    new icons
 *  Archive Log:
 *  Archive Log:    Revision 1.37  2015/02/26 20:07:41  fisherma
 *  Archive Log:    Changes to display Link Quality data to port's Performance tab and switch/port configuration table.
 *  Archive Log:
 *  Archive Log:    Revision 1.36  2015/02/03 21:12:37  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.35  2015/01/11 21:08:25  jijunwan
 *  Archive Log:    icon update
 *  Archive Log:
 *  Archive Log:    Revision 1.34  2014/12/23 18:25:21  rjtierne
 *  Archive Log:    Added email and display icons for action list on event wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.33  2014/12/19 18:38:13  rjtierne
 *  Archive Log:    Added DEVICE_TYPE_ICON as part of upcoming change to Event Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.32  2014/11/17 17:11:07  jijunwan
 *  Archive Log:    updated resources with text change or text reserved for unimplemented  function
 *  Archive Log:
 *  Archive Log:    Revision 1.31  2014/10/24 20:55:27  jijunwan
 *  Archive Log:    added connect icon to link tab
 *  Archive Log:
 *  Archive Log:    Revision 1.30  2014/10/23 15:48:45  jijunwan
 *  Archive Log:    added new resources for topology information display
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2014/10/21 16:32:25  fernande
 *  Archive Log:    Customization of Properties display (Show Options/Apply Options)
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2014/10/07 14:59:10  rjtierne
 *  Archive Log:    New icons for the admin page
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2014/10/01 19:25:54  rjtierne
 *  Archive Log:    Relocated image directory to src/main/image. Added new images for Topology graph and removed hardcoded static path to images
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2014/09/18 14:55:00  jijunwan
 *  Archive Log:    added UI resources that support UI component linking capability
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2014/09/05 21:56:31  jijunwan
 *  Archive Log:    L&F adjustment on Console Views
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2014/09/04 16:51:55  jijunwan
 *  Archive Log:    resources for property style change
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/08/26 14:06:08  jijunwan
 *  Archive Log:    added refresh icon
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/08/05 18:39:01  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/07/22 18:26:59  jijunwan
 *  Archive Log:    new resources
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/07/09 21:17:34  jijunwan
 *  Archive Log:    renamed CA_ICON to FI_ICON, CA_GROUP_ICON to FI_GROUP_ICON
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/06/26 14:54:56  jijunwan
 *  Archive Log:    added couple images
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/06/24 20:19:37  rjtierne
 *  Archive Log:    Added new image to depict normal speed links
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/06/17 19:20:40  rjtierne
 *  Archive Log:    Added new enum  SLOW_LINK for the slow link indicator on the
 *  Archive Log:    Connectivity table.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/06/05 17:16:38  jijunwan
 *  Archive Log:    new resources
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/05/23 19:47:25  jijunwan
 *  Archive Log:    added resources to support topology page
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/05/13 13:03:53  jypak
 *  Archive Log:    Event Summary Bar panel in pin board implementation.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/04/24 18:11:50  rjtierne
 *  Archive Log:    Added icons for CA_GROUP SW_GROUP, and INACTIVE_PORTS.
 *  Archive Log:    Also renamed SUBNET_ICON to DEVICE_TYPES_ICON
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/04/23 19:57:08  rjtierne
 *  Archive Log:    Added INACTIVE_PORT_ICON
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/04/23 16:24:59  jijunwan
 *  Archive Log:    reuse image icon and images
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/04/22 22:23:46  jypak
 *  Archive Log:    Folder icon created for the Logging Config Panel File Location button.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/04/22 17:55:39  rjtierne
 *  Archive Log:    Added new image for an UP icon
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/04/18 21:16:26  jijunwan
 *  Archive Log:    improvement on pagination buttons
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/04/18 19:21:24  jijunwan
 *  Archive Log:    bigger arrow images
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/04/18 13:43:51  jypak
 *  Archive Log:    Icons file change related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/17 20:10:40  jijunwan
 *  Archive Log:    new main frame layout
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/17 15:09:30  jijunwan
 *  Archive Log:    added Intel Logo, fixed a minor issue on splash screen
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/17 14:36:43  rjtierne
 *  Archive Log:    Added icon for device groups
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/16 17:55:46  jijunwan
 *  Archive Log:    made setup wizard works within our app
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:14  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.awt.Image;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum UIImages {
    LOGO_BK("intel_logo_1024x768.png"),
    LOGO_128("intel_logo_128x128.png"),
    LOGO_64("intel_logo_64x64.png"),
    LOGO_32("intel_logo_32x32.png"),
    LOGO_24("intel_logo_24x24.png"),
    HELP_ICON("help_16x16.png"), // Images used in the Swing plugin
    HFI_ICON("18x18-host.png"),
    HFI_GROUP_ICON("systemImage_16x16.png"),
    SW_ICON("switch_16x16.png"),
    SW_GROUP_ICON("switch_16x16.png"),
    ROUTER_ICON("router_16x16.png"),
    PIN_ICON("pin_16x16.png"),
    DOWN_ICON("down_16x16.png"),
    UP_ICON("up_16x16.png"),
    PORT_ICON("port_16x16.png"),
    INACTIVE_PORT_ICON("port_inactive_9x9.png"),
    SUBNET_ICON("subnet_16x16.png"),
    DEVICE_GROUP_ICON("device_group_16x16.png"),
    DEVICE_TYPE_ICON("device_types_16x16.png"),
    VIRTUAL_FABRIC_ICON("vf_16x16.png"),
    CRITICAL_ICON("critical_16x16.png"),
    ERROR_ICON("error_16x16.png"),
    WARNING_ICON("warning_16x16.png"),
    WARNING2_ICON("warning2_16x16.png"),
    NORMAL_ICON("normal_16x16.png"),
    OPTIONS_NOTSELECTED_ICON("options_notselected_16x16.png"),
    SPLASH_IMAGE("intel_fmgui_splash.png"),
    BAR_ICON("bar_16x16.png"),
    PIE_ICON("pie_16x16.png"),
    SETTING_ICON("setting_16x16.png"),
    FORWARD_WHITE_ICON("right-next_9x9.png"),
    BACK_WHITE_ICON("left-previous_9x9.png"),
    FORWARD_BLUE_ICON("right-next_12x12.png"),
    BACK_BLUE_ICON("left-previous_12x12.png"),
    EMPTY_BOX_ICON("emptyBox_16x16.png"),
    CHECK_BOX_ICON("checkBox_16x16.png"),
    FOLDER_ICON("folder_16x16.png"),
    INFORMATION_ICON("information_16x16.png"),
    ZOOM_IN_ICON("zoom_in_16x16.png"),
    ZOOM_OUT_ICON("zoom_out_16x16.png"),
    FIT_WINDOW("fit_window_16x16.png"),
    EXPAND_ALL("expandAll_16x16.png"),
    COLLAPSE_ALL("collapseAll_16x16.png"),
    UNDO("undo_16x16.png"),
    REDO("redo_16x16.png"),
    RESET("reset_16x16.png"),
    SLOW_LINK("slow_link_16x16.png"),
    NORMAL_LINK("normal_link_16x16.png"),
    INACTIVE_LINK("inactive_link_16x16.png"),
    RUNNING("running_16x16.gif"),
    DATA_TYPE("data_type_16x16.png"),
    REFRESH("refresh_16x16.png"),
    SHOW_BORDER("showBorder_16x16.png"),
    HIDE_BORDER("hideBorder_16x16.png"),
    ALT_ROWS("alternatingRows_16x16.png"),
    UNI_ROWS("uniformRows_16x16.png"),
    CLOSE_GRAY("close_gray_16x16.png"),
    CLOSE_WHITE("close_white_16x16.png"),
    CLOSE_RED("close_red_16x16.png"),
    LINK("link_16x16.png"),
    SWITCH_COLLAPSED_IMG("switch_plus_32x32.png"),
    SWITCH_EXPANDED_IMG("switch_32x32.png"),
    HFI_IMG("server_32x32.png"),
    CONSOLE_ICON("console_40x40.png"),
    APPS_LARGE_ICON("applications_40x40.png"),
    DEVICE_GROUP_LARGE_ICON("device_group_40x40.png"),
    VIRTUAL_FABRIC_LARGE_ICON("vf_40x40.png"),
    LINKS("links_16x16.png"),
    ROUTE("route_16x16.png"),
    DEVICE_SET("deviceSet_16x16.png"),
    CONNECT_GRAY("connect_gray_16x16.png"),
    CONNECT_WHITE("connect_white_16x16.png"),
    LOG_ICON("log_40x40.png"),
    DIENAMIC_BAR("dienamic_bar-640x56.png"),
    EMAIL_ICON("email_16x16.png"),
    DISPLAY_ICON("message_16X16.png"),
    HISTORY_ICON("history_16x16.png"),
    UNEDITABLE("uneditable_16x16.png"),
    LINK_QUALITY_NONE("link_quality_16x16-0.png"),
    LINK_QUALITY_BAD("link_quality_16x16-1.png"),
    LINK_QUALITY_POOR("link_quality_16x16-2.png"),
    LINK_QUALITY_GOOD("link_quality_16x16-3.png"),
    LINK_QUALITY_VERY_GOOD("link_quality_16x16-4.png"),
    LINK_QUALITY_EXCELLENT("link_quality_16x16-5.png"),
    CHECK_MARK("checkmark_16x16.png"),
    X_MARK("x_mark_16x16.png"),
    DASH("dash_16x16.png"),
    PLAY("play_16x16.png"),
    STOP("stop_16x16.png"),
    MOVE("move_16x16.png"),
    SYS_IMG("systemImage_16x16.png"),
    ABOUT_DIALOG_TOP_BANNER_IMG("AboutDlgTopBanner.png"),
    ABOUT_DIALOG_LEFT_BANNER_IMG("AboutDlgVerticalBanner.png"),
    INFO_DLG("info_32x32.png"),
    CONFIRM_DLG("confirm_32x32.png"),
    WARNING_DLG("warning_32x32.png"),
    ERROR_DLG("error_32x32.png");

    private static final String STL_IMAGES_PATH = "/image/";

    private final String filename;

    private WeakReference<ImageIcon> icon;

    private WeakReference<Image> image;

    private UIImages(String filename) {
        this.filename = filename;
    }

    public ImageIcon getImageIcon() {
        if (icon == null || icon.get() == null) {
            URL loc = Util.class.getResource(STL_IMAGES_PATH + filename);
            if (loc == null) {
                return null;
            }
            ImageIcon realIcon = new ImageIcon(loc);
            icon = new WeakReference<ImageIcon>(realIcon);
        }
        return icon.get();
    }

    public Image getImage() {
        Image img = null;
        if (image == null || image.get() == null) {
            URL loc = Util.class.getResource(STL_IMAGES_PATH + filename);
            if (loc == null) {
                return null;
            }
            try {
                Image realImage = ImageIO.read(loc);
                image = new WeakReference<Image>(realImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (image != null) {
            img = image.get();
        }
        return img;
    }

    public String getFileName() {

        return STL_IMAGES_PATH + filename;
    }
}
