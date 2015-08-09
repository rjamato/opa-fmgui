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
 *  File Name: DeviceCategoryType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/11/11 18:03:25  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/13 21:01:48  fernande
 *  Archive Log:    Added support for valueHeader attribute in UserOptions XML.
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

import javax.xml.bind.TypeConstraintException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.intel.stl.api.configuration.ResourceCategory;
import com.intel.stl.api.configuration.ResourceType;

/**
 * <p>
 * Java class for ResourceCategoryType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceCategoryType">
 *   &lt;simpleContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceCategoryType", propOrder = { "value" })
@XmlSeeAlso({ PortCategoryType.class, HfiCategoryType.class,
        SwitchCategoryType.class })
public abstract class ResourceCategoryType {

    @XmlValue
    protected String value;

    @XmlAttribute
    protected Boolean showHeader;

    @XmlAttribute
    protected String valueHeader;

    /**
     * Gets the value of the value property. This value is used as the key
     * header for this resource category
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property. This value is used as the key
     * header for this resource category
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the showHeader property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public boolean isShowHeader() {
        if (showHeader == null) {
            return true;
        } else {
            return showHeader;
        }
    }

    /**
     * Sets the value of the showHeader property.
     * 
     * @param value
     *            allowed object is {@link boolean }
     * 
     */
    public void setShowHeader(Boolean value) {
        this.showHeader = value;
    }

    /**
     * Gets the value of the valueHeader property. This value is used as the
     * value header for this resource category
     * 
     * @return possible object is {@link String }
     * 
     */

    public String getValueHeader() {
        return valueHeader;
    }

    /**
     * Sets the value of the valueHeader property. This value is used as the
     * value header for this resource category
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValueHeader(String value) {
        this.valueHeader = value;
    }

    /**
     * Gets the corresponding resource category for this ResourceCategoryType
     * 
     * @return possible object is {@link ResourceCategory }
     * 
     */
    protected abstract ResourceCategory getResourceCategory();

    protected ResourceCategory getPropertyCategoryFor(
            ResourceType resourceType, String categoryName) {
        ResourceCategory category =
                ResourceCategory.getResourceCategoryFor(categoryName);
        if (category == null) {
            TypeConstraintException tce =
                    new TypeConstraintException("Category name '"
                            + categoryName + "' (" + resourceType.name()
                            + ") has no matching PropertyCategory");
            throw tce;
        }
        if (!category.isApplicableTo(resourceType)) {
            TypeConstraintException tce =
                    new TypeConstraintException("Category name '"
                            + categoryName
                            + "' is not applicable to resource type "
                            + resourceType.name());
            throw tce;
        }
        return category;
    }

}
