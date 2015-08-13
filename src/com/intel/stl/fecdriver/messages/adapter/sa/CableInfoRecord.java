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
 *  Archive Log:    Revision 1.7.2.2  2015/08/12 15:21:36  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.7.2.1  2015/05/06 19:29:56  jijunwan
 *  Archive Log:    fixed comparison errors found by FindBugs
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
 *  Reference: /All_EMB/IbPrint/stl_sma.c.1.103 for the QSFP interpretation.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.CableInfoBean;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.StringUtils;
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

        // Don't need to make a separate SimpleDatagram like
        // 'CableInfo' because it will depends on the addr, len
        // from this.
        byte[] data = new byte[SAConstants.STL_CIR_DATA_SIZE];
        for (int i = 0; i < SAConstants.STL_CIR_DATA_SIZE; i++) {
            data[i] = buffer.get();
        }

        CableInfoBean cableInfoBean =
                interpretToQSFP(address, (byte) intLength, data);

        CableRecordBean bean =
                new CableRecordBean(lid, port, (byte) intLength, address,
                        portType, cableInfoBean);

        return bean;
    }

    private CableInfoBean interpretToQSFP(short addr, byte len, byte[] data) {

        CableInfoBean bean = new CableInfoBean();
        switch (addr) {
            case 128:
                if ((addr + len) < 128) {
                    break;
                }
                bean.setId(data[128 - addr]);
            case 129:
                if ((addr + len) < 129) {
                    break;
                }
                int intExtID = data[129 - addr] & 0xC0;
                bean.setExtId((byte) intExtID);
            case 130:
                if ((addr + len) < 130) {
                    break;
                }
                bean.setConnector(data[130 - addr]);
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 139:
            case 140:
                if ((addr + len) < 140) {
                    break;
                }
                bean.setNominalBr(data[140 - addr]);
            case 141:
            case 142:
                if ((addr + len) < 142) {
                    break;
                }
                bean.setSmfLength(data[142 - addr]);
            case 143:
                if ((addr + len) < 143) {
                    break;
                }
                bean.setOm3Length(data[143 - addr]);
            case 144:
                if ((addr + len) < 144) {
                    break;
                }
                bean.setOm2Length(data[144 - addr]);
            case 145:
                if ((addr + len) < 145) {
                    break;
                }
                bean.setOm1Length(data[145 - addr]);
            case 146:
                if ((addr + len) < 146) {
                    break;
                }
                bean.setCopperLength(data[146 - addr]);
            case 147:
                if ((addr + len) < 147) {
                    break;
                }
                bean.setDeviceTech(cableInfoDevTechToText(data[147 - addr]));
            case 148:
                if ((addr + len) < 163) {
                    break;
                }
                byte[] toVendorName = new byte[16];
                // From C byte to C char is 8 bits to 8 bits but from C byte to
                // Java char is 8 bits to 16 bits.
                System.arraycopy(data, 148 - addr, toVendorName, 0, 16);
                bean.setVendorName(StringUtils.toString(toVendorName, 0, 16));
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
                if ((addr + len) < 164) {
                    break;
                }
                bean.setExtendedModule(cableInfoOutputModuleCodeToText(data[164 - addr]));
            case 165:
                if ((addr + len) < 167) {
                    break;
                }
                byte[] toVendorOui = new byte[3];
                System.arraycopy(data, 165 - addr, toVendorOui, 0, 3);
                bean.setVendorOui(toVendorOui);
            case 166:
            case 167:
            case 168:
                if ((addr + len) < 183) {
                    break;
                }
                byte[] toVendorPN = new byte[16];
                System.arraycopy(data, 168 - addr, toVendorPN, 0, 16);
                bean.setVendorPn(StringUtils.toString(toVendorPN, 0, 16));
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
                if ((addr + len) < 185) {
                    break;
                }
                byte[] toVendorRev = new byte[2];
                System.arraycopy(data, 184 - addr, toVendorRev, 0, 2);
                bean.setVendorRev(StringUtils.toString(toVendorRev, 0, 2));
            case 185:
            case 186:
                if ((addr + len) < 189) {
                    break;
                }

                // Big Endian
                int opticalWL =
                        ((data[186 - addr] & 0xFF) << 24)
                                | ((data[187 - addr] & 0xFF) << 16)
                                | ((data[188 - addr] & 0xFF) << 8)
                                | (data[189 - addr] & 0xFF);
                bean.setOpticalWaveLength(opticalWL);
            case 187:
            case 188:
            case 189:
            case 190:
                if ((addr + len) < 190) {
                    break;
                }
                bean.setMaxCaseTemp(data[190 - addr]);
            case 191:
                if ((addr + len) < 191) {
                    break;
                }
                bean.setCcBase(data[191 - addr]);
            case 192:
            case 193:
                if ((addr + len) < 193) {
                    break;
                }
                boolean rxOutAmpProg =
                        ((data[193 - addr] & 1) == 1) ? true : false;
                bean.setRxOutAmpProg(rxOutAmpProg);
            case 194:
                if ((addr + len) < 194) {
                    break;
                }
                boolean rxSquelchDisImp =
                        ((data[194 - addr] & (1 << 3)) == 1 << 3) ? true
                                : false;
                bean.setRxSquelchDisImp(rxSquelchDisImp);
                boolean rxOutputDisCap =
                        ((data[194 - addr] & (1 << 2)) == 1 << 2) ? true
                                : false;
                bean.setRxOutputDisCap(rxOutputDisCap);
                boolean txSquelchDisImp =
                        ((data[194 - addr] & (1 << 1)) == 1 << 1) ? true
                                : false;
                bean.setTxSquelchDisImp(txSquelchDisImp);
                boolean txSquelchImp =
                        ((data[194 - addr] & 1) == 1) ? true : false;
                bean.setTxSquelchImp(txSquelchImp);
            case 195:
                if ((addr + len) < 195) {
                    break;
                }
                boolean memPage02Provided =
                        ((data[195 - addr] & (1 << 7)) == 1 << 7) ? true
                                : false;
                bean.setMemPage02Provided(memPage02Provided);
                boolean memPage01Provided =
                        ((data[195 - addr] & (1 << 6)) == 1 << 6) ? true
                                : false;
                bean.setMemPage01Provided(memPage01Provided);
                boolean txDisImp =
                        ((data[195 - addr] & (1 << 4)) == 1 << 4) ? true
                                : false;
                bean.setTxDisImp(txDisImp);
                boolean txFaultRepImp =
                        ((data[195 - addr] & (1 << 3)) == 1 << 3) ? true
                                : false;
                bean.setTxFaultRepImp(txFaultRepImp);
                boolean losReportImp =
                        ((data[195 - addr] & (1 << 1)) == 1 << 1) ? true
                                : false;
                bean.setLosReportImp(losReportImp);
            case 196:
                if ((addr + len) < 211) {
                    break;
                }
                byte[] toVendorSN = new byte[16];
                System.arraycopy(data, 196 - addr, toVendorSN, 0, 16);
                bean.setVendorSN(StringUtils.toString(toVendorSN, 0, 16));
            case 197:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
                if ((addr + len) < 217) {
                    break;
                }
                byte[] toDataCode = new byte[6];
                System.arraycopy(data, 212 - addr, toDataCode, 0, 6);
                bean.setDataCode(StringUtils.toString(toDataCode, 0, 6));
            case 213:
            case 214:
            case 215:
            case 216:
            case 217:
            case 218:
                if ((addr + len) < 219) {
                    break;
                }
                byte[] toLotCode = new byte[2];
                System.arraycopy(data, 218 - addr, toLotCode, 0, 2);
                bean.setLotCode(StringUtils.toString(toLotCode, 0, 2));
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
                if ((addr + len) < 223) {
                    break;
                }
                bean.setCcExt(data[223 - addr]);
                break;
            default:
                byte[] unknown = new byte[len];
                System.arraycopy(data, 224 - addr, unknown, 0, len);
                bean.setNotApplicableData(StringUtils.toString(unknown, 0, len));
                break;
        }

        return bean;
    }

    private String cableInfoDevTechToText(byte code) {
        switch (code) {
            case 0:
                return "850 nm VCSEL";
            case 1:
                return "1300 nm VCSEL";
            case 2:
                return "1550 nm VCSEL";
            case 3:
                return "1310 nm FP";
            case 4:
                return "1310 nm DFP";
            case 5:
                return "1550 nm DFP";
            case 6:
                return "1310 nm EML";
            case 7:
                return "1550 nm EML";
            case 8:
                return "Other";
            case 9:
                return "1490 nm DFB";
            case 10:
                return "copper cable, unequalized";
            case 11:
                return "copper, passive equalized";
            case 12:
                return "copper cable, near and far end limiting active equalizers";
            case 13:
                return "copper cable, far end limiting active equalizers";
            case 14:
                return "copper cable, near end limiting active equalizers";
            case 15:
                return "copper cable, linear active equalizers";
            default:
                return "Unknown Tech";
        }
    }

    private String cableInfoOutputModuleCodeToText(byte code) {
        switch (code) {
            case 0:
                return "SDR";
            case 1:
                return "DDR";
            case 2:
                return "QDR";
            case 3:
                return "FDR";
            case 4:
                return "EDR";
            default:
                return "Unknown";
        }
    }

}
