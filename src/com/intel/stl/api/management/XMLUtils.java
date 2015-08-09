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
 *  File Name: XMLUtils.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/03/13 14:29:17  jijunwan
 *  Archive Log:    improved to have consistent xml format when we add, remove or replace an Application, Device Group, or Virtual Fabric node.
 *  Archive Log:    improved to add comments to indicate the change was made by FM GUI and also the time we change it
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/05 17:30:37  jijunwan
 *  Archive Log:    init version to support Application management
 *  Archive Log:    1) read/write opafm.xml from/to host with backup file support
 *  Archive Log:    2) Application parser
 *  Archive Log:    3) Add/remove and update Application
 *  Archive Log:    4) unique name, reference conflication check
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management;

import java.io.File;
import java.util.Date;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {

    public static Node getNodeByName(Node parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public static void writeDoc(Document doc, File file) throws Exception {
        TransformerFactory transformerFactory =
                SAXTransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // transformer.setOutputProperty(
        // "{http://xml.apache.org/xslt}indent-amount", "2");

        // XPathFactory xpathFactory = XPathFactory.newInstance();
        // // XPath to find empty text nodes.
        // XPathExpression xpathExp =
        // xpathFactory.newXPath().compile(
        // "//text()[normalize-space(.) = '']");
        // NodeList emptyTextNodes =
        // (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
        //
        // // Remove each empty text node from document.
        // for (int i = 0; i < emptyTextNodes.getLength(); i++) {
        // Node emptyTextNode = emptyTextNodes.item(i);
        // emptyTextNode.getParentNode().removeChild(emptyTextNode);
        // }

        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    // -----------------------------------------------------------------------//
    // opafm.xml has text, such as new line, spaces for indent etc.. These
    // texts are treated as text node, and they make it impossible to use java
    // standard api to do pretty print. The following methods try to hacked the
    // format for new or updated Application, Device Group and Virtual Fabric
    // in opafm.xml to have it has the same format as other nodes in opafm.xml.
    // When print a dom into xml file, we do not use pretty print, so we will
    // fully keep the old format (with the new lines, indent spaces etc.) and
    // hacked format for new or updated node.
    // -----------------------------------------------------------------------//

    /**
     * 
     * <i>Description:</i> append a node into specified <code>parent</code> node
     * 
     * @param doc
     * @param parent
     * @param node
     */
    public static void appendNode(Document doc, Node parent, Node node) {
        Node txt = doc.createTextNode("\n    ");
        parent.appendChild(txt);
        Node comment =
                doc.createComment(" Created by FM GUI @ " + (new Date()) + " ");
        parent.appendChild(comment);
        doc.adoptNode(node);
        parent.appendChild(node);
        formatNode(doc, parent, node);
        txt = doc.createTextNode("\n\n  ");
        parent.appendChild(txt);
    }

    public static void removeNode(Document doc, Node parent, Node node,
            String nodeName) {
        Node comment =
                doc.createComment(" '" + nodeName
                        + "' was removed by FM GUI @ " + (new Date()) + " ");
        parent.insertBefore(comment, node);
        parent.removeChild(node);
    }

    public static void replaceNode(Document doc, Node parent, Node oldNode,
            Node newNode) {
        Node comment =
                doc.createComment(" Updated by FM GUI @ " + (new Date()) + " ");
        parent.insertBefore(comment, oldNode);
        parent.replaceChild(newNode, oldNode);
        formatNode(doc, parent, newNode);
    }

    private static void formatNode(Document doc, Node parent, Node node) {
        Node txt = doc.createTextNode("\n    ");
        parent.insertBefore(txt, node);
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            txt = doc.createTextNode("\n      ");
            node.insertBefore(txt, children.item(i));
            i += 1;
        }
        txt = doc.createTextNode("\n    ");
        node.appendChild(txt);
    }
}
