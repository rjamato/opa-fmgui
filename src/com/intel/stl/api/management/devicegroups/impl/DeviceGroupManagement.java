/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: DeviceGroupManagement.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/03/25 18:57:18  jijunwan
 *  Archive Log:    fixed typos
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/24 17:33:16  jijunwan
 *  Archive Log:    introduced IAttribute for attributes defined in xml file
 *  Archive Log:    changed all attributes for Appliation and DG to be an IAttribute
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/16 22:01:06  jijunwan
 *  Archive Log:    changed package name from application to applications, and from devicegroup to devicegroups
 *  Archive Log:    Added #getType to ServiceID, MGID, LongNode and their subclasses,
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/13 20:57:02  jijunwan
 *  Archive Log:    minor  improvement on FM Application
 *  Archive Log:    Added support on FM DeviceGroup
 *  Archive Log:    put all constants used in xml file to XMLConstants
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.devicegroups.impl;

import static com.intel.stl.api.management.XMLConstants.DEVICE_GROUP;
import static com.intel.stl.api.management.XMLConstants.DEVICE_GROUPS;
import static com.intel.stl.api.management.XMLConstants.NAME;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.intel.stl.api.IMessage;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.management.DuplicateNameException;
import com.intel.stl.api.management.FMConfHelper;
import com.intel.stl.api.management.ReferenceConflictException;
import com.intel.stl.api.management.XMLUtils;
import com.intel.stl.api.management.devicegroups.DeviceGroup;
import com.intel.stl.api.management.devicegroups.DeviceGroupException;
import com.intel.stl.api.management.devicegroups.DeviceGroups;
import com.intel.stl.api.management.devicegroups.IDeviceGroupManagement;
import com.intel.stl.common.STLMessages;

public class DeviceGroupManagement implements IDeviceGroupManagement {
    private final static Logger log = LoggerFactory
            .getLogger(DeviceGroupManagement.class);

    private final static Set<String> RESERVED = new HashSet<String>() {
        private static final long serialVersionUID = -8507198541424973196L;

        {
            add("All");
            add("AllFIs");
            add("AllSWs");
            add("AllSWE0s");
            add("AllEndNodes");
            add("AllSMs");
            add("AllMgmtAllowed");
            add("HFIDirectConnect");
            add("Self");
        }
    };

    private final FMConfHelper confHelp;

    /**
     * Description:
     * 
     * @param confHelp
     */
    public DeviceGroupManagement(FMConfHelper confHelp) {
        super();
        this.confHelp = confHelp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroups.IDeviceGroupManagement#
     * getReservedDeviceGroups()
     */
    @Override
    public Set<String> getReservedDeviceGroups() {
        return RESERVED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroup.IDeviceGroupManagement#
     * getDeviceGroups()
     */
    @Override
    public synchronized List<DeviceGroup> getDeviceGroups()
            throws DeviceGroupException {
        try {
            File confFile = confHelp.getConfFile(false);
            DeviceGroups groups = unmarshal(confFile);
            log.info("Fetch " + groups.getGroups().size()
                    + " Device Groups from host '" + confHelp.getHost() + "'");
            return groups.getGroups();
        } catch (Exception e) {
            throw createDeviceGroupException(STLMessages.STL63011_GET_DGS_ERR,
                    e, confHelp.getHost(), StringUtils.getErrorMessage(e));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroup.IDeviceGroupManagement#
     * getDeviceGroup(java.lang.String)
     */
    @Override
    public synchronized DeviceGroup getDeviceGroup(String name)
            throws DeviceGroupException {
        try {
            File confFile = confHelp.getConfFile(false);
            DeviceGroups groups = unmarshal(confFile);
            return groups.getGroup(name);
        } catch (Exception e) {
            throw createDeviceGroupException(STLMessages.STL63016_GET_DG_ERR,
                    e, name, confHelp.getHost(), StringUtils.getErrorMessage(e));
        }
    }

    /**
     * <i>Description:</i>
     * 
     * @param confFile
     * @return
     */
    private DeviceGroups unmarshal(File xmlFile) throws Exception {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        StreamSource xml = new StreamSource(xmlFile);
        final XMLStreamReader xsr = xif.createXMLStreamReader(xml);
        while (xsr.hasNext()) {
            if (xsr.isStartElement()
                    && xsr.getLocalName().equals(DEVICE_GROUPS)) {
                break;
            }
            xsr.next();
        }

        JAXBContext jc = JAXBContext.newInstance(DeviceGroups.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        JAXBElement<DeviceGroups> jb =
                unmarshaller.unmarshal(xsr, DeviceGroups.class);
        xsr.close();

        return jb.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroup.IDeviceGroupManagement#
     * addDeviceGroup(com.intel.stl.api.management.devicegroup.DeviceGroup)
     */
    @Override
    public synchronized void addDeviceGroup(DeviceGroup app)
            throws DeviceGroupException {
        try {
            File confFile = confHelp.getConfFile(false);
            uniqueNameCheck(null, app.getName());
            // TODO loop check
            addDeviceGroup(confFile, confFile, app);
            log.info("Added application " + app);
        } catch (Exception e) {
            throw createDeviceGroupException(STLMessages.STL63012_ADD_DG_ERR,
                    e, app.getName(), confHelp.getHost(),
                    StringUtils.getErrorMessage(e));
        }
    }

    protected void addDeviceGroup(File srcXml, File dstXml, DeviceGroup group)
            throws Exception {
        // transfer app to DOM
        DOMResult res = new DOMResult();
        JAXBContext context = JAXBContext.newInstance(group.getClass());
        context.createMarshaller().marshal(group, res);
        Document groupsDoc = (Document) res.getNode();
        Node newGroup = groupsDoc.getFirstChild();

        // read in old xml
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(srcXml);

        // check app in old xml
        Node groupsNode = doc.getElementsByTagName(DEVICE_GROUPS).item(0);
        Node matchedGroup = getGroupByName(groupsNode, group.getName());
        if (matchedGroup != null) {
            throw new IllegalArgumentException("Device Group '"
                    + group.getName() + "' alreday exist!");
        }

        // append app to Applications node
        XMLUtils.appendNode(doc, groupsNode, newGroup);

        // save back to xml file
        XMLUtils.writeDoc(doc, dstXml);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroup.IDeviceGroupManagement#
     * removeDeviceGroup(java.lang.String)
     */
    @Override
    public synchronized void removeDeviceGroup(String name)
            throws DeviceGroupException {
        try {
            File confFile = confHelp.getConfFile(false);
            referenceCheck(null, name);
            removeDeviceGroup(confFile, confFile, name);
            log.info("Removed application '" + name + "'");
        } catch (Exception e) {
            throw createDeviceGroupException(
                    STLMessages.STL63013_REMOVE_DG_ERR, e, name,
                    confHelp.getHost(), StringUtils.getErrorMessage(e));
        }
    }

    protected void removeDeviceGroup(File srcXml, File dstXml, String name)
            throws Exception {
        // read in old xml
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(srcXml);

        // check app in old xml
        Node groupsNode = doc.getElementsByTagName(DEVICE_GROUPS).item(0);
        Node matchedGroup = getGroupByName(groupsNode, name);
        if (matchedGroup != null) {
            XMLUtils.removeNode(doc, groupsNode, matchedGroup, name);

            // save back to xml file
            XMLUtils.writeDoc(doc, dstXml);
        } else {
            throw new IllegalArgumentException("Couldn't find Device Group '"
                    + name + "'");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroup.IDeviceGroupManagement#
     * updateDeviceGroup(java.lang.String,
     * com.intel.stl.api.management.devicegroup.DeviceGroup)
     */
    @Override
    public synchronized void updateDeviceGroup(String oldName, DeviceGroup app)
            throws DeviceGroupException {
        try {
            File confFile = confHelp.getConfFile(false);
            if (!oldName.equals(app.getName())) {
                DeviceGroups groups = unmarshal(confFile);
                uniqueNameCheck(groups, app.getName());
                referenceCheck(groups, oldName);
            }
            // TODO loop check
            updateDeviceGroup(confFile, confFile, oldName, app, false);
            log.info("Updated Device Group " + app);
        } catch (Exception e) {
            throw createDeviceGroupException(
                    STLMessages.STL63014_UPDATE_DG_ERR, e, app.getName(),
                    confHelp.getHost(), StringUtils.getErrorMessage(e));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.api.management.devicegroup.IDeviceGroupManagement#
     * addOrUpdateDeviceGroup(java.lang.String,
     * com.intel.stl.api.management.devicegroup.DeviceGroup)
     */
    @Override
    public synchronized void addOrUpdateDeviceGroup(String oldName,
            DeviceGroup group) throws DeviceGroupException {
        try {
            File confFile = confHelp.getConfFile(false);
            referenceCheck(null, oldName);
            // TODO loop check
            updateDeviceGroup(confFile, confFile, oldName, group, true);
            log.info("Added or updated Device Group " + group);
        } catch (Exception e) {
            throw createDeviceGroupException(
                    STLMessages.STL63015_ADDUPDATE_DG_ERR, e, group.getName(),
                    confHelp.getHost(), StringUtils.getErrorMessage(e));
        }
    }

    protected void updateDeviceGroup(File srcXml, File dstXml, String oldName,
            DeviceGroup group, boolean allowAdd) throws Exception {
        // transfer app to DOM
        DOMResult res = new DOMResult();
        JAXBContext context = JAXBContext.newInstance(group.getClass());
        context.createMarshaller().marshal(group, res);
        Document groupsDoc = (Document) res.getNode();
        Node newGroup = groupsDoc.getFirstChild();

        // read in old xml
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(srcXml);

        doc.adoptNode(newGroup);
        // check app in old xml
        Node groupsNode = doc.getElementsByTagName(DEVICE_GROUPS).item(0);
        Node matchedGroup = getGroupByName(groupsNode, oldName);
        if (matchedGroup == null) {
            if (allowAdd) {
                XMLUtils.appendNode(doc, groupsNode, newGroup);
            } else {
                throw new IllegalArgumentException(
                        "Couldn't find Device Gruop '" + oldName + "'");
            }
        } else {
            XMLUtils.replaceNode(doc, groupsNode, matchedGroup, newGroup);
        }
        XMLUtils.writeDoc(doc, dstXml);
    }

    protected void referenceCheck(DeviceGroups groups, String name)
            throws Exception {
        if (groups == null) {
            File confFile = confHelp.getConfFile(false);
            groups = unmarshal(confFile);
        }
        List<DeviceGroup> refs = groups.getReferencedGroups(name);
        if (!refs.isEmpty()) {
            String[] refNames = new String[refs.size()];
            for (int i = 0; i < refNames.length; i++) {
                refNames[i] = refs.get(i).getName();
            }
            throw new ReferenceConflictException(name, refNames);
        }
    }

    protected void uniqueNameCheck(DeviceGroups groups, String name)
            throws Exception {
        if (groups == null) {
            File confFile = confHelp.getConfFile(false);
            groups = unmarshal(confFile);
        }
        for (DeviceGroup group : groups.getGroups()) {
            if (group.getName().equals(name)) {
                throw new DuplicateNameException(name);
            }
        }
    }

    private Node getGroupByName(Node appsNode, String name) {
        NodeList children = appsNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals(DEVICE_GROUP)) {
                Node nameNode = XMLUtils.getNodeByName(child, NAME);
                if (nameNode != null) {
                    if (nameNode.getTextContent().equals(name)) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    protected DeviceGroupException createDeviceGroupException(IMessage msg,
            Throwable error, Object... args) {
        return new DeviceGroupException(msg, error, args);
    }

}
