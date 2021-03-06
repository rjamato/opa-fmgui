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
 *  File Name: UILabels.java
 * 
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.75  2015/12/03 14:56:28  jypak
 *  Archive Log: PR 131817 - FM GUI, the status Column to the right requires a header/title.
 *  Archive Log:
 *  Archive Log: Revision 1.74  2015/11/18 23:56:28  rjtierne
 *  Archive Log: PR 130965 - ESM support on Log Viewer
 *  Archive Log: - Added constant STL50215, and STL50217-STL50219 for supporting ESM Log Viewer
 *  Archive Log:
 *  Archive Log: Revision 1.73  2015/10/19 22:30:33  jijunwan
 *  Archive Log: PR 131091 - On an unsuccessful Failover, the Admin | Applications doesn't show the login window
 *  Archive Log: - added resources
 *  Archive Log:
 *  Archive Log: Revision 1.72  2015/10/09 17:48:13  fernande
 *  Archive Log: PR130753 - XML parse errors when new VF is created from FM GUI. Added check during validation of opaconfig.xml to make sure the "All" application is not added if a PKey is especified.
 *  Archive Log:
 *  Archive Log: Revision 1.71  2015/10/06 15:54:01  rjtierne
 *  Archive Log: PR 130390 - Windows FM GUI - Admin tab->Logs side-tab - unable to login to switch SM for log access
 *  Archive Log: - Added constants STL50215-STL50216 for Log Viewer error messages
 *  Archive Log:
 *  Archive Log: Revision 1.70  2015/09/25 20:50:52  fernande
 *  Archive Log: PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log: Revision 1.69  2015/09/25 13:58:13  rjtierne
 *  Archive Log: PR 130011 - Enhance SM Log Viewer to include Standard and Advanced requirements
 *  Archive Log: - Tweaked STL50206 to match text
 *  Archive Log: - Added new label STL50214 for invalid log users
 *  Archive Log:
 *  Archive Log: Revision 1.68  2015/08/17 18:54:12  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.67  2015/08/17 18:34:36  jijunwan
 *  Archive Log: PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log: - improved to ask confirmation when we intend to leave deploy panel while we are deploying FM confs.
 *  Archive Log:
 *  Archive Log: Revision 1.66  2015/08/17 17:35:51  jijunwan
 *  Archive Log: PR 128973 - Deploy FM conf changes on all SMs
 *  Archive Log: - added resources
 *  Archive Log:
 *  Archive Log: Revision 1.65  2015/08/17 14:22:43  rjtierne
 *  Archive Log: PR 128979 - SM Log display
 *  Archive Log: This is the first version of the Log Viewer which displays select lines of text from the remote SM log file. Updates include searchable raw text from file, user-defined number of lines to display, refreshing end of file, and paging. This PR is now closed and further updates can be found by referencing PR 130011 - "Enhance SM Log Viewer to include Standard and Advanced requirements".
 *  Archive Log:
 *  Archive Log: Revision 1.64  2015/08/10 17:31:01  robertja
 *  Archive Log: PR 128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log: Revision 1.63  2015/07/23 18:12:38  jijunwan
 *  Archive Log: PR 129645 - Tree search enhancement
 *  Archive Log: - display message when search is canceled or return empty result
 *  Archive Log: - fixed an issue that hides result tree by mistake
 *  Archive Log: - changed name search field to SafeTextField, so the name rules, such as cannot start with digit, do not apply here. We are doing text match in search, so any rules are unnecessary except the valid characters.
 *  Archive Log: - fixed value setting issue when we change the search field's formatter
 *  Archive Log:
 *  Archive Log: Revision 1.62  2015/07/16 21:22:52  jijunwan
 *  Archive Log: PR 129528 - input validation improvement
 *  Archive Log: - extended SafeTextField to apply rules in name check
 *  Archive Log: - moved valid chars to UIConstants
 *  Archive Log: - made FieldPair more generic and flexible
 *  Archive Log:
 *  Archive Log: Revision 1.61  2015/07/14 20:32:55  rjtierne
 *  Archive Log: PR 129545 - Klocwork and FindBugs fixes
 *  Archive Log: Added label STL50202 for logging an error message if the port column of the
 *  Archive Log: connectivity table is null
 *  Archive Log:
 *  Archive Log: Revision 1.60  2015/07/14 17:06:58  jijunwan
 *  Archive Log: PR 129541 - Should forbid save or deploy when there is invalid edit on management panel
 *  Archive Log: - display warning message when a user intends to save or deploy while there is invalid edit
 *  Archive Log:
 *  Archive Log: Revision 1.59  2015/07/13 16:22:42  jijunwan
 *  Archive Log: PR 129528 - input validation improvement
 *  Archive Log: - new resources
 *  Archive Log:
 *  Archive Log: Revision 1.58  2015/06/30 22:28:02  jijunwan
 *  Archive Log: PR 129215 - Need short chart name to support pin capability
 *  Archive Log: - added new resources to support short name and full name
 *  Archive Log:
 *  Archive Log: Revision 1.57  2015/06/25 19:08:56  jijunwan
 *  Archive Log: Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log: - new resources to support pin capability
 *  Archive Log:
 *  Archive Log: Revision 1.56  2015/06/10 19:58:58  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 * 
 *  Overview:
 * 
 *  @author: Fernando Fernandez
 * 
 ******************************************************************************/

package com.intel.stl.ui.common;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.IMessage;

/**
 * This is the message repository for the Fabric Manager GUI messages. Please,
 * follow the following convention: STL10001-STL10999: Initialization and
 * general messages STL20001-STL20999: Messages related to the Connect Manager
 * component STL30001-STL30999: Messages related to the Database Manager
 * component STL40001-STL40999: Messages related to the UI component Add ranges
 * as more components are added. Don't forget to add the actual message in the
 * messages.properties file.
 * 
 * @author Fernando Fernandez
 * 
 */
public enum UILabels implements IMessage {

    STL10001_SETTINGS_FILE_FORMAT_INVALID(10001),
    STL10002_IO_EXCEPTION_READING_SETTINGS(10002),
    STL10003_FOLDER_CANNOT_BE_CREATED(10003),
    STL10004_SECURITY_EXCEPTION_IN_FOLDER(10004),
    STL10005_DATABASE_ENGINE_NOTSUPPORTED(10005),
    STL10006_ERROR_STARTING_DATABASE_ENGINE(10006),
    STL10007_APPLICATION_CONFIGURATION_EXCEPTION(10007),
    STL10008_NODES_DISTR_SEVERITY(10008),

    STL10100_ERRORS_INIT_APP(10100),
    STL10101_ONE_ERROR_INIT_APP(10101),
    STL10102_MULTI_INSTANCES(10102),
    STL10103_MORE_SELECTIONS(10103),
    STL10104_INIT_SUBNET(10104),
    STL10105_CREATE_CONTEXT(10105),
    STL10106_PERSIST_USER_SETTINGS(10106),
    STL10107_CLEAR_CONTEXT(10107),
    STL10108_INIT_PAGE(10108),
    STL10109_LOADING_PROPERTY(10109),
    STL10110_REFRESHING_PAGES(10110),
    STL10111_REFRESHING_PAGE(10111),
    STL10112_INIT_PAGE_COMPLETED(10112),
    STL10113_CONNECTION_LOST(10113),
    STL10114_USER_CANCELLED(10114),
    STL10115_MAX_PINS(10115),
    STL10116_NOT_INIT(10116),

    STL10200_TOPN_BANDWIDTH(10200),
    STL10201_TOPN_CONGESTION(10201),
    STL10202_NODE_STATES(10202),
    STL10203_NODE_EVENTS(10203),
    STL10204_NODE_NO_EVENTS(10204),
    STL10205_TOPN_PACKET_RATE(10205),
    STL10206_TOPN_SIGNAL_INTEGRITY(10206),
    STL10207_TOPN_SMA_CONGESTION(10207),
    STL10208_TOPN_SECURITY(10208),
    STL10209_TOPN_ROUTING(10209),
    STL10210_TOPN(10210),
    STL10211_WORST_NODE(10211),
    STL10212_TIRE_N(10212),
    STL10213_TOPN_BUBBLE(10213),

    STL10214_SHORT_TOPN_BW(10214),
    STL10215_SHORT_TOPN_PKT_RATE(10215),
    STL10216_SHORT_TOPN_INTEG(10216),
    STL10217_SHORT_TOPN_CONGST(10217),
    STL10218_SHORT_TOPN_SMA_CONG(10218),
    STL10219_SHORT_TOPN_BUBBLE(10219),
    STL10220_SHORT_TOPN_SECURE(10220),
    STL10221_SHORT_TOPN_ROUTING(10221),

    STL10300_NUM_SWITCHES(10300),
    STL10301_NUM_HFIS(10301),
    STL10302_NUM_ISLINKS(10302),
    STL10303_NUM_HFILINKS(10303),
    STL10304_NUM_PORTS(10304),
    STL10305_NUM_NONDEGRADISLS(10305),
    STL10306_NUM_NONDEGRADHFILINKS(10306),

    // Database Manager messages
    STL30002_DATABASE_ENGINE_ERROR(30002),
    STL30003_DATABASE_DEFINITION_FILE_NOT_FOUND(30003),
    STL30004_ERROR_READING_DATABASE_DEFINITION(30004),
    STL30005_ERRORS_DURING_DATABASE_DEFINITION(30005),
    STL30006_SQLEXCEPTION(30006),
    STL30007_ERROR_STARTING_DB_ENGINE(30007),

    STL40001_ERROR_No_DATA(40001),
    STL40002_TO_BAR(40002),
    STL40003_TO_PIE(40003),
    STL40004_ERROR_INACTIVE_PORT(40004),
    STL40005_TREE_INFO_MSG(40005),
    STL40006_FOCUSPORTS_TASK(40006),
    STL40007_IMAGEINFO_TASK(40007),
    STL40008_GROUPINFO_TASK(40008),
    STL40009_VFINFO_TASK(40009),
    STL40010_PORTCOUNTERS_TASK(40010),
    STL40011_VFPORTCOUNTERS_TASK(40011),
    STL40012_DEVICE_STATES(40012),
    STL40013_FATAL_FAILURE(40013),

    // RenameEventDialog
    STL50001_EVENT_VALID(50001),
    // ConversionPatternHelpDialog
    STL50002_DATA1(50002),
    STL50003_DATA2(50003),
    STL50004_DATA3(50004),
    STL50005_DATA4(50005),
    STL50006_DATA5(50006),
    STL50007_DATA6(50007),
    STL50008_DATA7(50008),
    STL50009_DATA8(50009),
    STL50010_DATA9(50010),
    STL50011_DATA10(50011),
    STL50012_DATA11(50012),
    STL50013_DATA12(50013),
    STL50014_DATA13(50014),
    STL50015_DATA14(50015),
    // SetupWizard
    STL50016_MIDDLE_WIZARD_CONFIG(50016),
    // SetupWizardController
    STL50017_CONNECTION_EST(50017),
    // EventRulesPanelController
    STL50018_NAME_EVENT(50018),
    STL50019_DIFF_EVENT_NAME(50019),
    // EventRule
    STL50020_SET_COMMAND_ID(50020),
    STL50021_CREATE_MESSAGE_EVENT_TYPE(50021),
    STL50022_PORT_ACTIVE(50022),
    STL50023_PORT_INACTIVE(50023),
    STL50024_CREATE_MESSAGE(50024),
    // FileUtilities
    STL50025_DATA_SUCCESSFUL_WRTTEN(50025),
    STL50026_SERIALIZABLE_EXCEPTION(50026),
    STL50027_DATA_NOT_SAVED_FILE(50027),
    STL50028_DATA_LOADED_SUCCESSFULLY(50028),
    STL50029_FILE_NOT_FOUND(50029),
    STL50030_NOT_SERIALIZABLE(50030),
    // LoggingConfigurationController
    STL50031_ERROR_WHILE(50031),
    STL50032_CHANGES_APPLIED(50032),
    // SubnetInputValidator
    STL50033_VALID_SUBNET_ENTRY(50033),

    // LoggingInputValidator
    STL50034_MAX_FILE_SIZE_MISSING(50034),
    STL50035_MAX_FILE_SIZE_INVALID_TYPE(50035),
    STL50036_MAX_FILE_SIZE_FORMAT_EXCEPTION(50036),
    STL50037_MAX_NUM_FILES_OUT_OF_RANGE(50037),
    STL50038_MAX_NUM_FILES_MISSING(50038),
    STL50039_MAX_NUM_FILES_INVALID_TYPE(50039),
    STL50040_MAX_NUM_FILES_TOO_LARGE(50040),
    STL50041_MAX_NUM_FILES_FORMAT_EXCEPTION(50041),
    STL50042_FILE_LOCATION_MISSING(50042),
    STL50043_FILE_LOCATION_CREATION_ERROR(50043),
    STL50044_FILE_LOCATION_HEADLESS_ERROR(50044),
    STL50045_FILE_LOCATION_IO_ERROR(50045),
    STL50046_FILE_LOCATION_DIRECTORY_ERROR(50046),
    STL50047_FORMAT_STRING_EMPTY(50047),
    STL50048_FORMAT_STRING_INVALID(50048),

    // ConversionPatternHelpDialog
    STL50049_TO_PREVIEW(50049),
    // SubnetPanel
    STL50050_CONNECTION_FAIL(50050),
    STL50051_USE_OLD_SUBNET(50051),
    STL50052_IGNORE_INVALID_SUBNET(50052),
    // SubnetInputValidator, EventRulesPanelController
    STL50053_MAX_ERROR(50053),
    STL50054_EVENT_NAME(50054),
    // EventRulesPanel
    STL50055_EVENT_CHECKBOX_TIP(50055),
    // LoggingConfigurationPanel
    STL50056_APPENDER_SWITCH_WARNING(50056),
    STL50057_LOGGING_CONFIG_SAVE_FAILURE(50057),
    STL50058_SAMPLE_LOG_MESSAGE(50058),
    STL50059_CONNECTION_FAILURE_PROCEED(50059),
    STL50060_UNSUPPORTED_APPENDER_TYPE(50060),
    STL50061_INVALID_THRESHOLD_TYPE(50061),
    STL50062_MAX_FILE_SIZE_OUT_OF_RANGE(50062),
    STL50063_OK(50063),

    // User Preferences Input Validator
    STL50064_REFRESH_RATE_MISSING(50064),
    STL50065_REFRESH_RATE_INVALID_TYPE(50065),
    STL50066_REFRESH_RATE_OUT_OF_RANGE(50066),
    STL50067_REFRESH_RATE_THRESHOLD_ERROR(50067),
    STL50068_REFRESH_RATE_FORMAT_EXCEPTION(50068),
    STL50069_REFRESH_RATE_UNITS_MISSING(50069),
    STL50070_REFRESH_RATE_UNITS_INVALID_TYPE(50070),
    STL50071_REFRESH_RATE_UNITS_OUT_OF_RANGE(50071),
    STL50072_REFRESH_RATE_UNITS_FORMAT_EXCEPTION(50072),
    STL50073_TIMING_WINDOW_MISSING(50073),
    STL50074_TIMING_WINDOW_INVALID_TYPE(50074),
    STL50075_TIMING_WINDOW_OUT_OF_RANGE(50075),
    STL50076_TIMING_WINDOW_FORMAT_EXCEPTION(50076),
    STL50077_NUM_WORST_NODES_MISSING(50077),
    STL50078_NUM_WORST_NODES_INVALID_TYPE(50078),
    STL50079_NUM_WORST_NODES_OUT_OF_RANGE(50079),
    STL50080_NUM_WORST_NODES_FORMAT_EXCEPTION(50080),

    // Multinet Wizard
    STL50081_ABANDON_CHANGES_MESSAGE(50081),
    STL50082_DELETE_SUBNET_MESSAGE(50082),
    STL50083_WELCOME_MESSAGE(50083),
    STL50084_CANT_BE_BLANK(50084),
    STL50085_MUST_BE_NUMERIC(50085),
    STL50086_DUPLICATE_HOSTS(50086),
    STL50087_DUPLICATE_SUBNETS(50087),
    STL50088_HOST_NOT_FOUND(50088),
    STL50089_UNABLE_TO_VALIDATE(50089),
    STL50090_DB_SAVE_FAILURE(50090),
    STL50091_CONNECT_TO_SUBNET(50091),
    STL50092_CONFIGURE_SUBNET(50092),
    STL50093_WELCOME_FM_GUI(50093),
    STL50094_WELCOME_ERROR(50094),

    STL50095_TEXT_FIELD_LIMIT(50095),
    STL50096_TEXT_FIELD_INVALID_CHAR(50096),
    STL50097_TEXT_FIELD_VALID_CHARS(50097),
    STL50098_TEXT_FIELD_INVALID_SPACES(50098),
    STL50099_TEXT_FIELD_INVALID_DIGITS(50099),

    // CertsAssistant
    STL50200_SUBNETDESC_CANNOT_BE_NULL(50200),
    STL50201_CERTSDESC_CANNOT_BE_NULL(50201),
    STL50202_CONNECTIVITY_PORT_IS_NULL(50202),

    STL50203_SEARCH_CANCELLED(50203),
    STL50204_SEARCH_NULL(50204),
    STL50205_SEARCH_EMPTY(50205),
    STL50206_SEARCH_TEXT_NOT_FOUND(50206),
    STL50207_FILE_NOT_FOUND(50207),
    STL50208_LOG_OK(50208),
    STL50209_LOG_FILE_NOT_FOUND(50209),
    STL50210_SSH_UNABLE_TO_CONNECT(50210),
    STL50211_LOG_FILE_NOT_FOUND_USING_DEFAULT(50211),
    STL50212_RESPONSE_TIMEOUT(50212),
    STL50213_LINES_PER_PAGE_ERROR(50213),
    STL50214_INVALID_LOG_USER(50214),
    STL50215_SYSLOG_ACCESS_ERROR(50215),
    STL50216_UNEXPECTED_LOGIN_FAILURE(50216),
    STL50217_FILE_ACCESS_DENIED(50217),
    STL50218_EMPTY_LOG_FILE(50218),
    STL50219_ESM_SYSLOG_NOTE(50219),

    // Event messages
    STL60001_TOPO_CHANGE(60001),
    STL60002_PORT_ACTIVE(60002),
    STL60003_PORT_INACTIVE(60003),
    STL60004_FE_CONN_LOST(60004),
    STL60005_FE_CONN_ESTABLISH(60005),
    STL60006_SM_CONN_LOST(60006),
    STL60007_SM_CONN_ESTABLISH(60007),
    STL60008_CONN_LOST(60008),
    STL60009_PRESS_REFRESH(60009),

    STL60100_PORT_PREVIEW(60100),

    STL70001_COLLAPSABLE_NODE_TOOLTIP(70001),
    STL70002_SLOW_PORTS(70002),
    STL70003_DEG_PORTS(70003),

    // Console Messages
    STL80001_CONSOLE_CONNECTION_ERROR(80001),
    STL80002_INVALID_PORT_NUMBER(80002),
    STL80003_AUTHENTICATION_FAILURE(80003),
    STL80004_MAX_CONSOLES(80004),
    STL80005_INVALID_COMMAND(80005),
    STL80006_COMMAND_HELP_REFERENCE(80006),
    STL80007_PORT_INVALID_FORMAT(80007),
    STL80008_INVALID_SUBNET_ID(80008),
    STL80009_MAX_CHANNELS_IN_SESSION(80009),

    STL81000_DEPLOY_ERROR(81000),
    STL81001_DUP_NAME(81001),
    STL81002_DUP_NAME_SUG(81002),
    STL81003_REF_CONF(81003),
    STL81004_REMOVE_REF(81004),
    STL81005_UPDATE_REF(81005),
    STL81006_INVALID_IDRANGE(81006),
    STL81007_CHANGE_ID(81007),
    STL81008_INVALID_GIDRANGE(81008),
    STL81009_CHANGE_GID(81009),
    STL81010_APPLICATIONS_ALL(81010),
    STL81011_APPLICATIONS_ALL_SUG(81011),

    STL81020_POSITIVE_VALIDATION(81020),
    STL81021_RANGE1_VALIDATION(81021),
    STL81022_RANGE2_VALIDATION(81022),
    STL81023_RANGE3_VALIDATION(81023),
    STL81024_RANGE4_VALIDATION(81024),

    STL81050_DG_DEVICES_DESC(81050),
    STL81051_DG_SELECT_DESC(81051),
    STL81052_DG_INCLUDE_DESC(81052),
    STL81053_INVALID_EDIT(81053),

    STL81100_SAVE_ITEM(81100),
    STL81101_REMOVE_ITEM(81101),
    STL81102_ISSUES_FOUND(81102),
    STL81103_NO_ISSUES_FOUND(81103),
    STL81104_ONE_ISSUE_FOUND(81104),

    STL81110_DEPLOY_MSG(81110),
    STL81111_LOGIN_ERROR(81111),
    STL81112_NO_CHANGES(81112),
    STL81113_APPLY_CREDENTIAL(81113),
    STL81114_ADD_SM(81114),
    STL81115_DEPLOY_CANCELLED(81115),
    STL81116_UNKNOWN_HOST(81116),
    STL81117_ABANDON_DEPLOY(81117),

    // Error messages in the UI
    STL90001_DEVICE_TYPE_NOT_SET(90001),
    STL90002_DEVICE_CATEGORY_NOT_APPLICABLE(90002),
    STL90003_DEVICE_CATEGORY_NOT_SELECTED(90003),
    STL90004_BUILD_TREE_FAILED(90004),
    STL90005_UPDATE_TREE_FAILED(90005),

    STL91000_ABOUT_APP(91000),
    STL91001_BUILD_ID(91001),
    STL91002_BUILD_DATE(91002),

    STL92000_EMAIL_SUBJECT(92000),
    STL92001_TEST_EMAIL_SUBJECT(92001),
    STL99999_HOLDER(99999);

    private static final String STL_MESSAGES_BUNDLE =
            "com.intel.stl.ui.common.labels";

    private static final String STL_MESSAGES_ENCODING = "UTF-8";

    private static final Control STL_CONTROL = new UTFControl(
            STL_MESSAGES_ENCODING);

    private static final ResourceBundle STL_MESSAGES = ResourceBundle
            .getBundle(STL_MESSAGES_BUNDLE, STL_CONTROL);

    private static Logger log = LoggerFactory.getLogger(UILabels.class);

    private final int errorcode;

    private final String key;

    private UILabels(int errorcode) {
        this.errorcode = errorcode;
        this.key = String.format("STL%05d", errorcode);
    }

    @Override
    public int getErrorCode() {
        return errorcode;
    }

    @Override
    public String getMessageKey() {
        return key;
    }

    @Override
    public String getDescription() {
        try {
            return STL_MESSAGES.getString(key);
        } catch (MissingResourceException mre) {
            String message = "Message '" + key + "' not found!";
            log.error(message);
            return message;
        }
    }

    @Override
    public String getDescription(Object... arguments) {
        try {
            return MessageFormat.format(STL_MESSAGES.getString(key), arguments);
        } catch (MissingResourceException mre) {
            String message = "Message '" + key + "' not found!";
            log.error(message);
            return message;
        }
    }

}
