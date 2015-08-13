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
 *  File Name: LoggingInputValidator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:27:33  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/02/02 20:37:56  fernande
 *  Archive Log:    Fixing the SetupWizard so that it can define new subnets. Fixed also StackOverflowError exception when switching subnets.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/20 19:12:05  rjtierne
 *  Archive Log:    Now processing MaxNumFiles as a String instead of integer so blanks field can be detected
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/23 18:27:59  rjtierne
 *  Archive Log:    Added logic to make the input validator a singleton
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/19 18:51:39  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Custom input validator to check the validity of the inputs on the
 *  Logging Wizard view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.impl.logging;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.status.Status;

import com.intel.stl.api.configuration.AppenderConfig;
import com.intel.stl.api.configuration.LoggingThreshold;
import com.intel.stl.api.configuration.RollingFileAppender;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Validator;

public class LoggingInputValidator {

    private final int OK = LoggingValidatorError.OK.getId();

    private final int MAX_BACKUP_FILES = 20;

    private final int kiloBytes = 1024;

    private final String byteStr = "B";

    private static LoggingInputValidator instance;

    ArrayList<String> validCodes = new ArrayList<String>(Arrays.asList(
            STLConstants.K0649_SC.getValue().trim(), STLConstants.K0650_C
                    .getValue().trim(), STLConstants.K0651_D.getValue().trim(),
            STLConstants.K0652_F.getValue().trim(), STLConstants.K0653_SL
                    .getValue().trim(), STLConstants.K0654_L.getValue().trim(),
            STLConstants.K0655_SM.getValue().trim(), STLConstants.K0656_M
                    .getValue().trim(),
            STLConstants.K0657_SN.getValue().trim(), STLConstants.K0658_SP
                    .getValue().trim(),
            STLConstants.K0659_SR.getValue().trim(), STLConstants.K0660_ST
                    .getValue().trim(),
            STLConstants.K0661_SX.getValue().trim(),
            STLConstants.K0662_DOUBLE_PERCENT.getValue().trim()));

    public static LoggingInputValidator getInstance() {

        if (instance == null) {
            instance = new LoggingInputValidator();
        }

        return instance;
    }

    public int validate(AppenderConfig appender) {

        int errorCode = OK;

        // Currently supporting File Appender type only
        if (appender instanceof RollingFileAppender) {

            // Assign the rolling file appender
            RollingFileAppender fileAppender = (RollingFileAppender) appender;

            // Validate information level (threshold)
            errorCode = validateInformationLevel(fileAppender);
            if (errorCode == OK) {
                // Validate output format
                errorCode = validateOutputFormat(fileAppender);
                if (errorCode == OK) {
                    // Validate maximum file size
                    errorCode = validateMaxFileSize(fileAppender);
                    if (errorCode == OK) {
                        // Validate maximum number of files
                        errorCode = validateMaxNumFiles(fileAppender);
                        if (errorCode == OK) {
                            // Validate file location
                            errorCode = validateFileLocation(fileAppender);
                        }
                    }
                }
            }
        } else {

            errorCode = LoggingValidatorError.UNSUPPORTED_APPENDER_TYPE.getId();
        }

        return errorCode;
    }

    protected int validateInformationLevel(RollingFileAppender fileAppender) {

        int errorCode = OK;

        LoggingThreshold threshold = fileAppender.getThreshold();
        if (threshold == null) {
            threshold = LoggingThreshold.ERROR;
            fileAppender.setThreshold(threshold);
        } else {
            int i = 0;
            boolean found = false;
            while ((!found) && (i < LoggingThreshold.values().length)) {
                found = threshold.equals(LoggingThreshold.values()[i]);
                i++;
            }

            if (!found) {
                errorCode =
                        LoggingValidatorError.INVALID_THRESHOLD_TYPE.getId();
            }
        }

        return errorCode;
    }

    protected int validateOutputFormat(RollingFileAppender fileAppender) {

        int errorCode = OK;

        // Check if conversion pattern is blank or null
        String conversionPattern = fileAppender.getConversionPattern();
        if ((conversionPattern.equals("")) || (conversionPattern.equals(null))) {
            errorCode = LoggingValidatorError.FORMAT_STRING_EMPTY.getId();
        } else {
            // I think this validation implementation should go in the back end
            // to hide the logging tool in use
            LoggerContext loggerContext = new LoggerContext();
            loggerContext.reset();
            PatternLayout encoder = new PatternLayout();
            encoder.setContext(loggerContext);
            encoder.setPattern(conversionPattern);
            encoder.start();
            List<Status> errors =
                    loggerContext.getStatusManager().getCopyOfStatusList();
            if (errors.size() == 0) {
                errorCode = OK;
            } else {
                String emsg = errors.get(errors.size() - 1).getMessage();
                // This message should be shown to the user
                System.out.println(emsg);
                errorCode = LoggingValidatorError.FORMAT_STRING_INVALID.getId();
            }

            // Evaluate the validity of the pattern
            /*-
            String[] conversionArray = conversionPattern.split(" ");
            for (int i = 0; i < conversionArray.length; i++) {
                boolean validStartToken =
                        ((conversionArray[i].startsWith("%")) || (conversionArray[i]
                                .startsWith("[%")));

                if (validStartToken) {
                    String token = conversionArray[i].replace("[", "");
                    token = token.replace("]", "");
                    if (!validCodes.contains(token)) {
                        errorCode =
                                LoggingValidatorError.FORMAT_STRING_INVALID
                                        .getId();
                    }
                } else {
                    System.out.println("Token in error: " + conversionArray[i]);
                    errorCode =
                            LoggingValidatorError.FORMAT_STRING_INVALID.getId();
                }
            }
             */

        }

        return errorCode;
    }

    protected int validateMaxFileSize(RollingFileAppender fileAppender) {

        int errorCode = OK;

        String maxFileSizeStr = fileAppender.getMaxFileSize();

        // Check if conversion pattern is blank or null
        if (Validator.isBlankOrNull(maxFileSizeStr)) {

            errorCode = LoggingValidatorError.MAX_FILE_SIZE_MISSING.getId();
        } else {

            String maxFileSizeNumOnly = null;
            String unit =
                    maxFileSizeStr.substring(maxFileSizeStr.length() - 2,
                            maxFileSizeStr.length());

            // Separate the number from the units
            if (!unit.contains(byteStr)) {
                maxFileSizeNumOnly = maxFileSizeStr;
            } else {
                maxFileSizeNumOnly =
                        maxFileSizeStr
                                .substring(0, maxFileSizeStr.length() - 2);
            }

            // Check the validity of the number
            try {
                Long maxFileSize = new Long(maxFileSizeNumOnly);
                Long max = null;

                if (unit.equals(STLConstants.K0695_KB.getValue())) {
                    max = Long.MAX_VALUE / kiloBytes;
                } else if (unit.equals(STLConstants.K0722_MB.getValue())) {
                    max = Long.MAX_VALUE / (kiloBytes * kiloBytes);
                } else if (unit.equals(STLConstants.K0696_GB.getValue())) {
                    max = Long.MAX_VALUE / (kiloBytes * kiloBytes * kiloBytes);
                } else {
                    max = Long.MAX_VALUE;
                }

                if ((maxFileSize < 0) || (max < maxFileSize)) {

                    LoggingValidatorError.data = max;
                    errorCode =
                            LoggingValidatorError.MAX_FILE_SIZE_OUT_OF_RANGE
                                    .getId();
                }

            } catch (NumberFormatException e) {

                errorCode =
                        LoggingValidatorError.MAX_FILE_SIZE_FORMAT_EXCEPTION
                                .getId();
            }

        }

        return errorCode;
    }

    protected int validateMaxNumFiles(RollingFileAppender fileAppender) {

        int errorCode = OK;
        String maxNumFilesStr = fileAppender.getMaxNumOfBackUp();

        try {

            if (Validator.isBlankOrNull(maxNumFilesStr)) {
                errorCode = LoggingValidatorError.MAX_NUM_FILES_MISSING.getId();
            } else {
                int maxNumFiles = Integer.parseInt(maxNumFilesStr);

                // Check that the number is an integer
                if ((Integer.valueOf(maxNumFiles) instanceof Integer) == false) {
                    errorCode =
                            LoggingValidatorError.MAX_NUM_FILES_INVALID_TYPE
                                    .getId();
                }

                // Range check the max number of files
                if ((maxNumFiles < 0) || (MAX_BACKUP_FILES < maxNumFiles)) {

                    LoggingValidatorError.data = MAX_BACKUP_FILES;
                    errorCode =
                            LoggingValidatorError.MAX_FILE_SIZE_OUT_OF_RANGE
                                    .getId();
                }
            }
        } catch (NumberFormatException e) {

            errorCode =
                    LoggingValidatorError.MAX_NUM_FILES_FORMAT_EXCEPTION
                            .getId();
        }

        return errorCode;
    }

    protected int validateFileLocation(RollingFileAppender fileAppender) {

        int errorCode = OK;
        String fileLocationStr = fileAppender.getFileLocation();
        File file = new File(fileLocationStr);

        // Make sure the file location isn't blank
        if (fileLocationStr.equals("") || fileLocationStr.equals(null)) {
            errorCode = LoggingValidatorError.FILE_LOCATION_MISSING.getId();

        } else {

            // Make sure this file location isn't a directory
            if (file.isDirectory()) {
                errorCode =
                        LoggingValidatorError.FILE_LOCATION_DIRECTORY_ERROR
                                .getId();
            } else {
                // See if the file exists
                if (!file.exists()) {

                    try {
                        if (!file.createNewFile()) {
                            errorCode =
                                    LoggingValidatorError.FILE_LOCATION_CREATION_ERROR
                                            .getId();
                        }
                    } catch (HeadlessException e) {
                        errorCode =
                                LoggingValidatorError.FILE_LOCATION_HEADLESS_ERROR
                                        .getId();

                    } catch (IOException e) {
                        errorCode =
                                LoggingValidatorError.FILE_LOCATION_IO_ERROR
                                        .getId();
                    }
                }
            }
        }

        return errorCode;
    }

}
