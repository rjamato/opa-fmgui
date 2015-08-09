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
 *  File Name: VirtualFabrics.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/25 19:10:04  jijunwan
 *  Archive Log:    first version of VirtualFabric support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.management.virtualfabrics;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.intel.stl.api.management.XMLConstants;

@XmlRootElement(name = XMLConstants.VIRTUAL_FABRICS)
@XmlAccessorType(XmlAccessType.FIELD)
public class VirtualFabrics {
    @XmlElement(name = XMLConstants.VIRTUAL_FABRIC)
    private List<VirtualFabric> vfs;

    /**
     * @return the vfs
     */
    public List<VirtualFabric> getVFs() {
        return vfs;
    }

    /**
     * @param vfs
     *            the vfs to set
     */
    public void setVFs(List<VirtualFabric> vfs) {
        this.vfs = vfs;
    }

    public VirtualFabric getVF(String name) {
        for (VirtualFabric group : vfs) {
            if (group.getName().equals(name)) {
                return group;
            }
        }

        throw new IllegalArgumentException(
                "Counldn't find Virtual Fabric by name '" + name + "'");
    }
}
