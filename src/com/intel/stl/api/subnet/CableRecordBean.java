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
 *  File Name: CableRecordBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/04/22 04:46:12  jijunwan
 *  Archive Log:    1) workaround code that use LE order for address/type filed in cable info
 *  Archive Log:    2) change command values back to the correct ones
 *  Archive Log:    3) fixed a typo in CableRecordBean#toString
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/04 21:37:55  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: Cable Record from SA populated by the connect manager.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

/*
 * CableInfoRecord
 * 
 * STL Differences:
 *      LID lengthened to 32 bits.
 *      Reserved2 field shortened from 20 bits to 4 to preserve word-alignment.
 * 
 * #define STL_CIR_DATA_SIZE       64
 * typedef struct {
 *   struct {
 *     uint32  LID;
 *     uint8   Port;
 *     IB_BITFIELD2(uint8,
 *                 Length:7,
 *                 Reserved:1);
 *     IB_BITFIELD2(uint16,
 *                 Address:12,
 *                 PortType:4); // Port type for response only 
 *     };
 *     
 *     uint8       Data[STL_CIR_DATA_SIZE];
 * 
 * } PACK_SUFFIX STL_CABLE_INFO_RECORD;
 * 
 * 
 * CableInfo
 * 
 * Attribute Modifier as: 0AAA AAAA AAAA ALLL LLL0 0000 PPPP PPPP
 *                        A: Starting address of cable data
 *                        L: Length (bytes) of cable data - 1
 *                           (L+1 bytes of data read)  
 *                        P: Port number (0 - management port, switches only)
 * 
 * NOTE: Cable Info is mapped onto a linear 4096-byte address space (0-4095).
 * Cable Info can only be read within 128-byte pages; that is, a single
 * read cannot cross a 128-byte (page) boundary.
 * 
 * typedef struct {
 *     uint8   Data[64];           // RO Cable Info data (up to 64 bytes) 
 *         
 * } PACK_SUFFIX STL_CABLE_INFO;
 * 
 */

import java.io.Serializable;

import com.intel.stl.api.Utils;

public class CableRecordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lid;

    private short port; // promote to handle unsigned byte

    private byte length;

    private short address;

    private byte portType;

    private CableInfoBean cableInfo;

    public CableRecordBean() {
        super();
    }

    public CableRecordBean(int lid, byte port, byte length, short address,
            byte portType, CableInfoBean cableInfo) {
        super();
        this.lid = lid;
        this.port = Utils.unsignedByte(port);
        this.length = length;
        this.address = address;
        this.portType = portType;
        this.cableInfo = cableInfo;
    }

    /**
     * @return the lid
     */
    public int getLid() {
        return lid;
    }

    /**
     * @param lid
     *            the lid to set
     */
    public void setLid(int lid) {
        this.lid = lid;
    }

    /**
     * @return the port
     */
    public short getPort() {
        return port;
    }

    /**
     * @return the length
     */
    public byte getLength() {
        return length;
    }

    /**
     * @return the address
     */
    public short getAddress() {
        return address;
    }

    /**
     * @return the portType
     */
    public byte getPortType() {
        return portType;
    }

    /**
     * @return the cableInfo
     */
    public CableInfoBean getCableInfo() {
        return cableInfo;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(short port) {
        this.port = port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(byte port) {
        this.port = Utils.unsignedByte(port);
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(byte length) {
        this.length = length;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(short address) {
        this.address = address;
    }

    /**
     * @param portType
     *            the portType to set
     */
    public void setPortType(byte portType) {
        this.portType = portType;
    }

    /**
     * @param cableInfo
     *            the cableInfo to set
     */
    public void setCableInfo(CableInfoBean cableInfo) {
        this.cableInfo = cableInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CableRecordBean [lid=" + lid + ", port=" + port + ", length="
                + length + ",  address=" + address + ",  portType=" + portType
                + ", cableInfo=" + cableInfo + "]";
    }

}