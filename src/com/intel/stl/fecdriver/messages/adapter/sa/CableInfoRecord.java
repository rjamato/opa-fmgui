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
 *  File Name: CableInfoRecord.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.18  2016/04/01 11:34:32  jypak
 *  Archive Log:    PR 130081 - Adapt FM GUI to use data structure STL_CABLE_INFO_FULL.
 *  Archive Log:    Updated to process each 64 bytes in two cable data from FM through CableInfoStd. Populating CableInfoBean and interpretation to QSFP both are executed by CableInfoStd.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/09/16 14:37:12  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    For code consistency, added case statements for removed offset back.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/09/15 13:31:30  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    Incorporated the FM changes (PR 129390) as of 8/28/15. These changes are mainly from IbPrint/stl_sma.c revision 1.163.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/08/19 22:26:34  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - correction on OM length calculation
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/19 21:02:54  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - adapt to latest FM code
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/08/19 18:08:29  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    Updates for ID and OpticalWaveLength.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/08/17 18:48:48  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/07 16:13:16  jypak
 *  Archive Log:    PR 129397 -gaps in cableinfo output and handling.
 *  Archive Log:    Updates on the formats of the cableinfo output and also new enums were defined for different output values.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/07 14:57:52  jypak
 *  Archive Log:    PR 129397 -gaps in cableinfo output and handling.
 *  Archive Log:    Updates on the formats of the cableinfo output and also new enums were defined for different output values.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/06/29 15:05:41  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    Field name fix has been implemented. Also, introduced a conversion to Date object to add flexibility to display date code.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/05/01 20:52:38  jijunwan
 *  Archive Log:    fixed minor errors
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/04/29 18:21:08  jijunwan
 *  Archive Log:    removed workaround code
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/04/29 14:01:09  jypak
 *  Archive Log:    Updates to display unknown data in the address not interpretable in the QSFP port encoding based on the SFF-8636.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/28 15:47:43  jypak
 *  Archive Log:    Changes related to FM updates for the Cable Info processing.
 *  Archive Log:
 *
 *  Overview:
 *
 *  Reference: /All_EMB/IbPrint/stl_sma.c.1.159 for the QSFP interpretation.
 *             /All_EMB/IbAccess/Common/Inc/stl_helper.h.1.74
 *             /All_EMB/IbAccess/Common/Inc/stl_sm.h.1.149
 *             ftp://ftp.seagate.com/sff/SFF-8436.PDF
 *
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.CableInfoBean;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h.1.90<br>
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sm.h.1.103
 *
 * <pre>
 *
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
 * </pre>
 *
 * @author jypak
 *
 */
public class CableInfoRecord extends SimpleDatagram<CableRecordBean> {

    public CableInfoRecord() {
        super(72);// 4+1+1+2+1*64
    }

    public void setLID(int lid) {
        buffer.putInt(0, lid);
    }

    public void setPort(byte port) {
        buffer.put(4, port);
    }

    public void setDataLength(byte length) {
        int val = (length << 1) & 0xff;
        buffer.put(5, (byte) val);
    }

    public void setAddress(short address) {
        int val = (address << 4) | (buffer.getShort(6) & 0x0f);
        buffer.putShort(6, (short) val);
    }

    public void setPortType(byte portType) {
        int val = (buffer.getShort(6) & 0xfff0) | (portType & 0x0f);
        buffer.putShort(6, (short) val);
    }

    public void setData(byte[] data) {
        if (data.length != SAConstants.STL_CIR_DATA_SIZE) {
            throw new IllegalArgumentException("Invalid data length. Expect "
                    + SAConstants.STL_CIR_DATA_SIZE + ", got " + data.length);
        }

        buffer.position(8);
        for (byte val : data) {
            buffer.put(val);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.hpc.stl.resourceadapter.data.SimpleDatagram#toObject()
     */
    @Override
    public CableRecordBean toObject() {
        buffer.clear();
        int lid = buffer.getInt();
        byte port = buffer.get();
        byte length = buffer.get();

        int intLength = length >>> 1;

        short shortVal = buffer.getShort();
        short address = (short) (shortVal >>> 4);
        byte portType = (byte) (shortVal & 0x0f);

        // NOTE: Cable Info is mapped onto a linear 4096-byte address space
        // (0-4095). Cable Info can only be read within 128-byte pages; that is,
        // a single read cannot cross a 128-byte (page) boundary.
        byte[] data = new byte[SAConstants.STL_CABLE_INFO_PAGESZ];
        for (int i = 0; i < SAConstants.STL_CIR_DATA_SIZE; i++) {
            data[i + address - SAConstants.STL_CIB_STD_START_ADDR] =
                    buffer.get();
        }

        CableInfoBean cableInfoBean = interpretToQSFP(data, address);

        CableRecordBean bean = new CableRecordBean(lid, port, (byte) intLength,
                address, portType, cableInfoBean);

        return bean;
    }

    private CableInfoBean interpretToQSFP(byte[] data, int addr) {

        // CableInfoStd is size of 128 bytes.
        CableInfoStd cableInfoStd = new CableInfoStd();
        cableInfoStd.wrap(data, 0);

        // Only fill the fields by 64 bytes (1st 64 bytes and then 2nd 64
        // bytes). When byte array 'data' is passed,
        // every byte is at least initialized with zero. We cannot tell if the
        // byte is data zero or initialized value zero. When UI process this
        // CableInfoBean, null fields are not processed.
        if (addr == SAConstants.STL_CIB_STD_START_ADDR) {
            return cableInfoStd.toLowerObject();
        } else {
            return cableInfoStd.toUpperObject();
        }
    }
}
