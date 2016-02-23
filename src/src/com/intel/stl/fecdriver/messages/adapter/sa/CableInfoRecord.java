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
import com.intel.stl.api.subnet.CertifiedRateType;
import com.intel.stl.api.subnet.OutputModuleType;
import com.intel.stl.api.subnet.PowerClassType;
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
                bean.setId((byte) (data[128 - addr] & 0xFF));
            case 129:
                if ((addr + len) < 129) {
                    break;
                }
                int extIdent = data[129 - addr] & 0xFF;
                bean.setPowerClass(stlCableInfoPowerClassType(extIdent >> 6,
                        extIdent & 0x03));
                bean.setTxCDRSupported((extIdent & 0x08) == 0x08);
                bean.setRxCDRSupported((extIdent & 0x04) == 0x04);
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
                bean.setBitRateLow(data[140 - addr]);
            case 141:
            case 142:
                // SmfLength ignored
            case 143:
                if ((addr + len) < 143) {
                    break;
                }
                // Unit in meter
                bean.setOm3Length(getOM3Length(data[143 - addr]));
            case 144:
                if ((addr + len) < 144) {
                    break;
                }
                // Unit in meter
                bean.setOm2Length(data[144 - addr] & 0xFF);
            case 145:
                // Om1Length ignored
            case 146:
                if ((addr + len) < 146) {
                    break;
                }
                // Unit in meter
                byte length = data[146 - addr];
                boolean isValid =
                        isCableLengthValid(data[147 - addr], data[130 - addr]);
                bean.setOm4Length(getOM4Length(length, isValid));
            case 147:
                if ((addr + len) < 147) {
                    break;
                }
                int codeXmit = data[147 - addr] & 0xFF;
                bean.setCodeXmit(codeXmit >> 4);
            case 148:
                if ((addr + len) < 163) {
                    break;
                }
                // From C byte to C char is 8 bits to 8 bits but from C byte to
                // Java char is 8 bits to 16 bits.
                bean.setVendorName(StringUtils.toString(data, 148 - addr, 16));
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
                // ExtendedModule ignored
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
                bean.setVendorPn(StringUtils.toString(data, 168 - addr, 16));
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
                bean.setVendorRev(StringUtils.toString(data, 184 - addr, 2));
            case 185:
            case 186:
                // OpticalWaveLength ignored
            case 187:
            case 188:
            case 189:
            case 190:
                if ((addr + len) < 190) {
                    break;
                }
                bean.setMaxCaseTemp(data[190 - addr] & 0xFF);
            case 191:
                if ((addr + len) < 191) {
                    break;
                }
                bean.setCcBase(data[191 - addr]);
            case 192:
                if ((addr + len) < 192) {
                    break;
                }
                bean.setLinkCodes(data[192 - addr]);
            case 193:
                if ((addr + len) < 193) {
                    break;
                }
                byte rxtxOptEquemp = data[193 - addr];
                bean.setTxInpEqAutoAdp((rxtxOptEquemp & 0x08) == 0x08);
                bean.setTxInpEqFixProg((rxtxOptEquemp & 0x04) == 0x04);
                bean.setRxOutpEmphFixProg((rxtxOptEquemp & 0x02) == 0x02);
                bean.setRxOutpAmplFixProg((rxtxOptEquemp & 0x01) == 0x01);
            case 194:
                if ((addr + len) < 194) {
                    break;
                }
                byte rxtxOptCdrsquel = data[194 - addr];
                bean.setTxCDROnOffCtrl((rxtxOptCdrsquel & 0x80) == 0x80);
                bean.setRxCDROnOffCtrl((rxtxOptCdrsquel & 0x40) == 0x40);
                bean.setTxSquelchImp((rxtxOptCdrsquel & 0x01) == 0x01);
            case 195:
                if ((addr + len) < 195) {
                    break;
                }
                byte memtxOptPagesquel = data[195 - addr];
                bean.setMemPage02Provided((memtxOptPagesquel & 0x80) == 0x80);
                bean.setMemPage01Provided((memtxOptPagesquel & 0x40) == 0x40);
            case 196:
                if ((addr + len) < 211) {
                    break;
                }
                bean.setVendorSN(StringUtils.toString(data, 196 - addr, 16));
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
                byte[] toDateCode = new byte[6];
                System.arraycopy(data, 212 - addr, toDateCode, 0, 6);
                String year = StringUtils.toString(toDateCode, 0, 2);
                String month = StringUtils.toString(toDateCode, 2, 2);
                String day = StringUtils.toString(toDateCode, 4, 2);
                bean.setDateCode(year, month, day);
            case 213:
            case 214:
            case 215:
            case 216:
            case 217:
            case 218:
                if ((addr + len) < 219) {
                    break;
                }
                bean.setLotCode(StringUtils.toString(data, 218 - addr, 2));
            case 219:
            case 220:
            case 221:
            case 222:
                if ((addr + len) < 222) {
                    break;
                }
                bean.setBitRateHigh(data[222 - addr]);
            case 223:
                if ((addr + len) < 223) {
                    break;
                }
                bean.setCcExt(data[223 - addr]);
            case 250:
                if ((addr + len) < 250) {
                    break;
                }
                bean.setCertCableFlag(isStlCableInfoCableCertified(data[250 - addr]));
            case 251:
                if ((addr + len) < 251) {
                    break;
                }
                bean.setReachClass(data[251 - addr] & 0xFF);
            case 252:
                if ((addr + len) < 252) {
                    break;
                }
                bean.setCertDataRate(CertifiedRateType
                        .getCertifiedRateType(data[252 - addr]));
                break;
            default:
                byte[] unknown = new byte[len];
                System.arraycopy(data, 224 - addr, unknown, 0, len);
                bean.setNotApplicableData(StringUtils.toString(unknown, 0, len));
                break;
        }

        return bean;
    }

    private OutputModuleType[] stlCableInfoOutputModuleType(byte code) {
        OutputModuleType[] type = null;
        type = OutputModuleType.getOuputModuleType(code);

        return type;
    }

    public boolean isCableLengthValid(byte codeXmit, byte connector) {
        if ((codeXmit == 0x08)
                || ((codeXmit <= 0x09) && (connector != SAConstants.CABLEINFO_CONNECTOR_NOSEP))
                || (codeXmit > 0x0F)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isStlCableInfoCableCertified(byte code_cert) {
        if (code_cert == SAConstants.CABLEINFO_OPA_CERTIFIED) {
            return true;
        } else {
            return false;
        }
    }

    private int getOM3Length(byte codeLen) {
        return (codeLen & 0xFF) * 2;
    }

    private int getOM4Length(byte codeLen, boolean codeValid) {
        if (codeValid) {
            return codeLen & 0xFF;
        } else {
            return (codeLen & 0xFF) * 2;
        }

    }

    private PowerClassType stlCableInfoPowerClassType(int codeLow, int codeHigh) {
        PowerClassType type = null;
        type = PowerClassType.getPowerClassType(codeHigh, codeLow);

        return type;
    }
}
