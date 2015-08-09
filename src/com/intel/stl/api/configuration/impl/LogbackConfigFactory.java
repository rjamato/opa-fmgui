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
 *  File Name: LogBackConfigFactory.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/01/20 19:10:26  rjtierne
 *  Archive Log:    Change maxNumOfBackup to String type so blank strings can be detected in wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/08 22:41:18  fernande
 *  Archive Log:    Logback configuration support
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/17 21:40:46  fernande
 *  Archive Log:    Fix for saving settings.xml file
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/17 21:17:43  fernande
 *  Archive Log:    Backend changes to update log config.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import static com.intel.stl.api.configuration.impl.SupportedAppenderType.CONSOLE_APPENDER;
import static com.intel.stl.api.configuration.impl.SupportedAppenderType.ROLLINGFILE_APPENDER;
import static com.intel.stl.common.AppDataUtils.FM_GUI_DIR;
import static com.intel.stl.common.STLMessages.STL50001_ERROR_PARSING_LOGGING_CONFIG;
import static com.intel.stl.common.STLMessages.STL50011_INVALID_XPATH_EXPRESSION;
import static javax.xml.xpath.XPathConstants.NODESET;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.ConfigurationException;
import com.intel.stl.api.configuration.ConsoleAppender;
import com.intel.stl.api.configuration.ILogConfigFactory;
import com.intel.stl.api.configuration.LoggingThreshold;
import com.intel.stl.api.configuration.RollingFileAppender;

public class LogbackConfigFactory implements ILogConfigFactory {

    private static Logger log = LoggerFactory
            .getLogger(LogbackConfigFactory.class);

    protected static DocumentBuilderFactory documentBuilderFactory =
            DocumentBuilderFactory.newInstance();

    protected static XPathFactory xPathFactory = XPathFactory.newInstance();

    protected static TransformerFactory transformerFactory = TransformerFactory
            .newInstance();

    private static final String XPATH_ALL_APPENDERS = "/configuration/appender";

    private static final String XML_APPENDER = "appender";

    private static final String XPATH_APPENDER_PATTERN = "encoder/pattern";

    private static final String XPATH_APPENDER_LEVEL = "filter/level";

    private static final String XPATH_FILEAPPENDER_FILE = "File";

    private static final String XPATH_FILEAPPENDER_MAXFILESIZE =
            "triggeringPolicy/MaxFileSize";

    private static final String XPATH_FILEAPPENDER_MAXINDEX =
            "rollingPolicy/maxIndex";

    private static final String XPATH_FILEAPPENDER_NAMEPATTERN =
            "rollingPolicy/FileNamePattern";

    private static final String LOGBACK_FILTER_CLASS =
            "ch.qos.logback.classic.filter.ThresholdFilter";

    private static final String LOGBACK_ROLLINGPOLICY_CLASS =
            "ch.qos.logback.core.rolling.FixedWindowRollingPolicy";

    private static final String LOGBACK_TRIGGERINGPOLICY_CLASS =
            "ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy";

    private static final String LOGDIR_SYS_PROP = "${" + FM_GUI_DIR + "}";

    private final XPath xPath;

    private final File configFile;

    private Document config;

    public LogbackConfigFactory(File configFile) {
        this.configFile = configFile;
        this.xPath = xPathFactory.newXPath();
    }

    @Override
    public Document getConfig() {
        if (config == null) {
            try {
                DocumentBuilder db =
                        documentBuilderFactory.newDocumentBuilder();
                this.config = db.parse(configFile);
            } catch (Exception e) {
                ConfigurationException ce =
                        new ConfigurationException(
                                STL50001_ERROR_PARSING_LOGGING_CONFIG,
                                StringUtils.getErrorMessage(e));
                log.error(ce.getMessage(), e);
                throw ce;
            }
        }
        return config;
    }

    @Override
    public NodeList getAppenders() {
        Document config = getConfig();
        try {
            return (NodeList) xPath.evaluate(XPATH_ALL_APPENDERS, config,
                    NODESET);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_ALL_APPENDERS);
            log.error(ce.getMessage(), e);
            throw ce;
        }

    }

    @Override
    public void saveConfig(File configFile) {
        Document config = getConfig();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(config);
            StreamResult result = new StreamResult(configFile);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void updateNode(Node node, ConsoleAppender appender) {
        Element element = getElement(node);
        setLoggingThreshold(element, appender.getThreshold());
        setConversionPattern(element, appender.getConversionPattern());
    }

    @Override
    public void updateNode(Node node, RollingFileAppender appender) {
        Element element = getElement(node);
        setLoggingThreshold(element, appender.getThreshold());
        setConversionPattern(element, appender.getConversionPattern());
        setFileLocation(element, appender.getFileLocation());
        setFileNamePattern(element, appender.getFileNamePattern());
        setMaxFileSize(element, appender.getMaxFileSize());
        setMaxNumOfBackup(element, appender.getMaxNumOfBackUp());
    }

    @Override
    public Node createNode(ConsoleAppender appender) {
        Document document = getConfig();
        Element newElement =
                createAppenderNode(document, appender.getName(),
                        CONSOLE_APPENDER);
        Element pattern =
                createPatternNode(document, appender.getConversionPattern());
        Element level = createLevelNode(document, appender.getThreshold());
        newElement.appendChild(pattern);
        newElement.appendChild(level);
        return newElement;
    }

    @Override
    public Node createNode(RollingFileAppender appender) {
        Document document = getConfig();
        Element newElement =
                createAppenderNode(document, appender.getName(),
                        ROLLINGFILE_APPENDER);
        Element file = createFileNode(document, appender.getFileLocation());
        Element pattern =
                createPatternNode(document, appender.getConversionPattern());
        Element level = createLevelNode(document, appender.getThreshold());
        Element maxIndex =
                createMaxIndexNode(document, appender.getMaxNumOfBackUp(),
                        appender.getFileNamePattern());
        Element maxFileSize =
                createMaxFileSizeNode(document, appender.getMaxFileSize());
        newElement.appendChild(file);
        newElement.appendChild(pattern);
        newElement.appendChild(level);
        newElement.appendChild(maxIndex);
        newElement.appendChild(maxFileSize);
        return newElement;
    }

    @Override
    public void populateFromNode(Node node, ConsoleAppender appender) {
        Element element = getElement(node);
        appender.setName(getName(element));
        appender.setThreshold(getLoggingThreshold(element));
        appender.setConversionPattern(getConversionPattern(element));
    }

    @Override
    public void populateFomNode(Node node, RollingFileAppender appender) {
        Element element = getElement(node);
        appender.setName(getName(element));
        appender.setThreshold(getLoggingThreshold(element));
        appender.setConversionPattern(getConversionPattern(element));
        appender.setFileLocation(getFileLocation(element));
        appender.setFileNamePattern(getFileNamePatern(element));
        appender.setMaxFileSize(getMaxFileSize(element));
        appender.setMaxNumOfBackUp(getMaxNumOfBackup(element));
    }

    private Element createAppenderNode(Document document, String name,
            SupportedAppenderType type) {
        Element appenderNode = document.createElement("appender");
        appenderNode.setAttribute("name", name);
        appenderNode.setAttribute("class", type.getAppenderClass());
        return appenderNode;
    }

    private Element createPatternNode(Document document, String pattern) {
        Element encoderNode = document.createElement("encoder");
        Element patternNode = document.createElement("pattern");
        patternNode.setTextContent(pattern);
        encoderNode.appendChild(patternNode);
        return encoderNode;
    }

    private Element createLevelNode(Document document,
            LoggingThreshold threshold) {
        Element filterNode = document.createElement("filter");
        filterNode.setAttribute("class", LOGBACK_FILTER_CLASS);
        Element levelNode = document.createElement("level");
        levelNode.setTextContent(threshold.name());
        filterNode.appendChild(levelNode);
        return filterNode;
    }

    private Element createFileNode(Document document, String fileLocation) {
        Element fileNode = document.createElement("File");
        fileNode.setTextContent(fileLocation);
        return fileNode;
    }

    private Element createMaxIndexNode(Document document, String maxIndex,
            String fileNamePattern) {
        Element policyNode = document.createElement("rollingPolicy");
        policyNode.setAttribute("class", LOGBACK_ROLLINGPOLICY_CLASS);
        Element maxIndexNode = document.createElement("maxIndex");
        maxIndexNode.setTextContent((new Integer(maxIndex)).toString());
        Element namePatternNode = document.createElement("FileNamePattern");
        namePatternNode.setTextContent(fileNamePattern);
        policyNode.appendChild(maxIndexNode);
        policyNode.appendChild(namePatternNode);
        return policyNode;
    }

    private Element createMaxFileSizeNode(Document document, String maxFileSize) {
        Element policyNode = document.createElement("triggeringPolicy");
        policyNode.setAttribute("class", LOGBACK_TRIGGERINGPOLICY_CLASS);
        Element maxFileSizeNode = document.createElement("MaxFileSize");
        maxFileSizeNode.setTextContent(maxFileSize);
        policyNode.appendChild(maxFileSizeNode);
        return policyNode;
    }

    private Element getElement(Node node) {
        Element element = (Element) node;
        if (!XML_APPENDER.equals(element.getTagName())) {
            RuntimeException rte = new RuntimeException("Invalid node");
            throw rte;
        }
        return element;
    }

    private String getName(Element appenderElement) {
        return appenderElement.getAttribute("name");
    }

    private String getConversionPattern(Element appenderElement) {
        Element pattern = getPatternElement(appenderElement);
        return pattern.getTextContent();
    }

    private void setConversionPattern(Element appenderElement,
            String conversionPattern) {
        Element pattern = getPatternElement(appenderElement);
        pattern.setTextContent(conversionPattern);
    }

    private LoggingThreshold getLoggingThreshold(Element appenderElement) {
        Element level = getLevelElement(appenderElement);
        if (level == null) {
            return null;
        }
        String strLevel = level.getTextContent();
        return LoggingThreshold.valueOf(strLevel);
    }

    private void setLoggingThreshold(Element appenderElement,
            LoggingThreshold threshold) {
        Element level = getLevelElement(appenderElement);
        level.setTextContent(threshold.name());
    }

    private String getFileLocation(Element appenderElement) {
        Element file = getFileElement(appenderElement);
        return file.getTextContent();
    }

    private void setFileLocation(Element appenderElement, String fileLocation) {
        String logDir = System.getProperty(FM_GUI_DIR);
        Element file = getFileElement(appenderElement);
        if (fileLocation.startsWith(logDir)) {
            File logFile = new File(fileLocation);
            String fileName = logFile.getName();
            String logFileLocation = LOGDIR_SYS_PROP + "/" + fileName;
            file.setTextContent(logFileLocation);
        } else {
            file.setTextContent(fileLocation);
        }
    }

    private String getFileNamePatern(Element appenderElement) {
        Element namePattern = getFileNamePatternElement(appenderElement);
        return namePattern.getTextContent();
    }

    private void setFileNamePattern(Element appenderElement,
            String fileNamePattern) {
        Element namePattern = getFileNamePatternElement(appenderElement);
        namePattern.setTextContent(fileNamePattern);
    }

    private String getMaxFileSize(Element appenderElement) {
        Element maxFileSize = getMaxFileSizeElement(appenderElement);
        return maxFileSize.getTextContent();
    }

    private void setMaxFileSize(Element appenderElement, String maxFileSize) {
        Element maxFileSizeEle = getMaxFileSizeElement(appenderElement);
        maxFileSizeEle.setTextContent(maxFileSize);
    }

    private String getMaxNumOfBackup(Element appenderElement) {
        Element maxNumBackups = getMaxIndexElement(appenderElement);
        return maxNumBackups.getTextContent();
    }

    private void setMaxNumOfBackup(Element appenderElement,
            String maxNumOfBackup) {
        Element maxIndex = getMaxIndexElement(appenderElement);
        maxIndex.setTextContent(maxNumOfBackup);
    }

    private Element getPatternElement(Element appenderElement) {
        try {
            return (Element) xPath.evaluate(XPATH_APPENDER_PATTERN,
                    appenderElement, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_APPENDER_PATTERN);
            log.error(ce.getMessage(), e);
            throw ce;
        }
    }

    private Element getLevelElement(Element appenderElement) {
        try {
            return (Element) xPath.evaluate(XPATH_APPENDER_LEVEL,
                    appenderElement, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_APPENDER_LEVEL);
            log.error(ce.getMessage(), e);
            throw ce;
        }
    }

    private Element getFileElement(Element appenderElement) {
        try {
            return (Element) xPath.evaluate(XPATH_FILEAPPENDER_FILE,
                    appenderElement, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_FILEAPPENDER_FILE);
            log.error(ce.getMessage(), e);
            throw ce;
        }
    }

    private Element getFileNamePatternElement(Element appenderElement) {
        try {
            return (Element) xPath.evaluate(XPATH_FILEAPPENDER_NAMEPATTERN,
                    appenderElement, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_FILEAPPENDER_NAMEPATTERN);
            log.error(ce.getMessage(), e);
            throw ce;
        }
    }

    private Element getMaxFileSizeElement(Element appenderElement) {
        try {
            return (Element) xPath.evaluate(XPATH_FILEAPPENDER_MAXFILESIZE,
                    appenderElement, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_FILEAPPENDER_MAXFILESIZE);
            log.error(ce.getMessage(), e);
            throw ce;
        }
    }

    private Element getMaxIndexElement(Element appenderElement) {
        try {
            return (Element) xPath.evaluate(XPATH_FILEAPPENDER_MAXINDEX,
                    appenderElement, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ConfigurationException ce =
                    new ConfigurationException(
                            STL50011_INVALID_XPATH_EXPRESSION,
                            XPATH_FILEAPPENDER_MAXINDEX);
            log.error(ce.getMessage(), e);
            throw ce;
        }
    }
}