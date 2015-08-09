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
 *  File Name: HfiCategoryType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/11/11 18:03:25  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.intel.stl.api.configuration.ResourceCategory;
import com.intel.stl.api.configuration.ResourceType;

/**
 * <p>
 * Java class for HfiCategoryType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="HfiCategoryType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;>ResourceCategoryType">
 *       &lt;attribute name="name" type="{}HfiCategory" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HfiCategoryType")
@XmlRootElement(name = "Category")
public class HfiCategoryType extends ResourceCategoryType {

    @XmlAttribute
    protected HfiCategory name;

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link HfiCategory }
     * 
     */
    public HfiCategory getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link HfiCategory }
     * 
     */
    public void setName(HfiCategory value) {
        this.name = value;
    }

    public ResourceCategory getResourceCategory() {
        return getPropertyCategoryFor(ResourceType.HFI, name.name());
    }
}
