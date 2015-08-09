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
 *  File Name: UserOptionsMarshaller.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/09/29 18:51:55  fernande
 *  Archive Log:    Adding UserOptions XML and  saving it to the database. Includes XML schema validation.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Marshals/Unmarshals UserOptions XML document instances
 */
public class UserOptionsMarshaller {

    private static final String USER_OPTIONS_SCHEMA = "UserOptions.xsd";

    private UserOptionsMarshaller() {
    }

    private static Schema schema = null;

    public static Schema getSchema() {
        if (schema == null) {
            createSchema();
        }
        return schema;
    }

    public static UserOptions unmarshal(String xmlDocument)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(UserOptions.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(getSchema());

        StringReader reader = new StringReader(xmlDocument);
        return (UserOptions) unmarshaller.unmarshal(reader);
    }

    public static String marshal(UserOptions userOptions) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(UserOptions.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(getSchema());
        StringWriter writer = new StringWriter();
        marshaller.marshal(userOptions, writer);
        return writer.toString();
    }

    private static void createSchema() {
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL xsdUrl =
                UserOptionsMarshaller.class.getResource(USER_OPTIONS_SCHEMA);
        if (xsdUrl != null) {
            try {
                schema = schemaFactory.newSchema(xsdUrl);
            } catch (SAXException e) {
                // Our schema definition is wrong, this shouldn't happen in
                // production
                RuntimeException rte =
                        new RuntimeException(
                                "Invalid UserOptions schema definition: "
                                        + e.getMessage(), e);
                throw rte;
            }
        } else {
            // This shouldn't happen
            RuntimeException rte =
                    new RuntimeException(
                            "Could not find UserOptions schema file '"
                                    + USER_OPTIONS_SCHEMA + "'.");
            throw rte;
        }
    }
}
