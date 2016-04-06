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
 *  File Name: CableInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.17  2016/04/01 11:34:33  jypak
 *  Archive Log:    PR 130081 - Adapt FM GUI to use data structure STL_CABLE_INFO_FULL.
 *  Archive Log:    Updated to process each 64 bytes in two cable data from FM through CableInfoStd. Populating CableInfoBean and interpretation to QSFP both are executed by CableInfoStd.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/09/15 13:31:34  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    Incorporated the FM changes (PR 129390) as of 8/28/15. These changes are mainly from IbPrint/stl_sma.c revision 1.163.
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/08/19 22:27:13  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - adapt to integer OM length
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/08/19 21:06:35  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - adapt to latest FM code
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/08/19 18:08:31  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    Updates for ID and OpticalWaveLength.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/08/18 14:28:32  jijunwan
 *  Archive Log:    PR 130033 - Fix critical issues found by Klocwork or FindBugs
 *  Archive Log:    - DateFormat is not thread safe. Changed to create new DateFormat to avoid sharing it among different threads
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/17 21:44:01  jijunwan
 *  Archive Log:    PR 129397 -gaps in cableinfo output and handling.
 *  Archive Log:    - fixed a typo
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/17 18:53:50  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/07 14:57:57  jypak
 *  Archive Log:    PR 129397 -gaps in cableinfo output and handling.
 *  Archive Log:    Updates on the formats of the cableinfo output and also new enums were defined for different output values.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/07/20 16:33:53  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    Avoid adding 'N/A' date code and also 'Invalid' by setting the property only when processing the 2nd bean.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/06/30 14:36:53  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    For date code, if invalid data, set the property field value as 'Invalid' and if both data code string and Date object are null, set the field value as 'N/A'.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/06/29 15:05:44  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    Field name fix has been implemented. Also, introduced a conversion to Date object to add flexibility to display date code.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/04/29 14:01:06  jypak
 *  Archive Log:    Updates to display unknown data in the address not interpretable in the QSFP port encoding based on the SFF-8636.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 15:50:10  jypak
 *  Archive Log:    Display byte data in hex form like FFT does. Updated to use Byte class rather than primitive type byte to differentiate between valid data and 'no data'. The cable data are spread across two record entries, so, 'no data' for a field in CableInfoBean means that the data for the field is in the other record. Also, since we need to handle unsigned values, we cannot use NaN as -1.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/21 17:57:30  jypak
 *  Archive Log:    Display 'N/A' for each attributes when not applicable.
 *  Archive Log:    For a switch port 0, display 'N/A' for each attributes.
 *  Archive Log:
 *
 *  Overview:
 *
 *  Reference: /All_EMB/IbPrint/stl_sma.c.1.103 for the QSFP interpretation.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0385_TRUE;
import static com.intel.stl.ui.common.STLConstants.K0386_FALSE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_CC_BASE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_CC_EXT;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_CERT_CABLE_FLAG;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_CERT_DATA_RATE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_CONNECTOR;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_COPPER_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_DATE_CODE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_DEVICE_TECH;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_ID;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_MAXCASE_TEMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_MEM_PAGE01_PROV;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_MEM_PAGE02_PROV;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_NOMINAL_BR;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_OM2_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_OM3_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_POWER_CLASS;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_REACH_CLASS;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_CDR_ON_OFF_CTRL;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_CDR_SUP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_OUTP_AMPL_FIX_PROG;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_OUTP_EMPH_FIX_PROG;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_CDR_ON_OFF_CTRL;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_CDR_SUP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_INP_EQ_AUTO_ADP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_INP_EQ_FIX_PROG;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_SQUELCH_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_NAME;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_OUI;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_PN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_REV;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_SN;

import java.util.Date;
import java.util.List;

import com.intel.stl.api.subnet.CableInfoBean;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.CertifiedRateType;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.PowerClassType;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.CableTypeViz;
import com.intel.stl.ui.model.CertifiedRateTypeViz;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.PortTypeViz;
import com.intel.stl.ui.model.PowerClassTypeViz;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class CableInfoProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        FVResourceNode node = context.getResourceNode();
        PortRecordBean portBean = context.getPort();
        ISubnetApi subnetApi = context.getContext().getSubnetApi();
        // For node type Switch and port 0, don't process and display
        // N/A screen.
        if (portBean == null || portBean.getPortNum() == 0
                && node.getParent().getType() == TreeNodeType.SWITCH) {
            getCableInfo(category, null);
            return;
        }

        List<CableRecordBean> cableRecordBeans = subnetApi
                .getCable(portBean.getEndPortLID(), portBean.getPortNum());

        // So far, FM only supports port type Standard (1)
        if (cableRecordBeans == null || cableRecordBeans.isEmpty()) {
            getCableInfo(category, null);
            return;
        } else {
            for (CableRecordBean cableBean : cableRecordBeans) {
                if (cableBean.getPortType() != PortTypeViz.STANDARD
                        .getPortType().getValue()) {
                    getCableInfo(category, null);
                    return;
                }
            }
        }

        CableInfoBean cableInfoBean = new CableInfoBean();
        for (CableRecordBean cableBean : cableRecordBeans) {
            combineCableInfo(cableInfoBean, cableBean.getCableInfo());
        }

        getCableInfo(category, cableInfoBean);
    }

    private void combineCableInfo(CableInfoBean destination,
            CableInfoBean source) {
        Byte id = source.getId();
        if (id != null) {
            destination.setId(id);
        }
        PowerClassType extId = source.getPowerClass();
        if (extId != null) {
            destination.setPowerClass(extId);
        }
        Boolean txCDRSupported = source.getTxCDRSupported();
        if (txCDRSupported != null) {
            destination.setTxCDRSupported(txCDRSupported);
        }
        Boolean rxCDRSupported = source.getRxCDRSupported();
        if (rxCDRSupported != null) {
            destination.setRxCDRSupported(rxCDRSupported);
        }
        Byte connector = source.getConnector();
        if (connector != null) {
            destination.setConnector(connector);
        }
        Byte bitRateHigh = source.getBitRateHigh();
        if (bitRateHigh != null) {
            destination.setBitRateHigh(bitRateHigh);
        }
        Byte bitRateLow = source.getBitRateLow();
        if (bitRateLow != null) {
            destination.setBitRateLow(bitRateLow);
        }
        Integer om3Len = source.getOm3Length();
        if (om3Len != null) {
            destination.setOm3Length(om3Len);
        }
        Integer om2Len = source.getOm2Length();
        if (om2Len != null) {
            destination.setOm2Length(om2Len);
        }
        Integer om4Len = source.getOm4Length();
        if (om4Len != null) {
            destination.setOm4Length(om4Len);
        }
        Byte xmitTech = source.getXmitTech();
        if (xmitTech != null) {
            destination.setXmitTech(xmitTech);
        }
        String vendorName = source.getVendorName();
        if (vendorName != null) {
            destination.setVendorName(vendorName);
        }
        Byte[] vendorOui = source.getVendorOui();
        if (vendorOui != null) {
            destination.setVendorOui(vendorOui);
        }
        String vendorPn = source.getVendorPn();
        if (vendorPn != null) {
            destination.setVendorPn(vendorPn);
        }
        String vendorRev = source.getVendorRev();
        if (vendorRev != null) {
            destination.setVendorRev(vendorRev);
        }
        Integer maxCaseTemp = source.getMaxCaseTemp();
        if (maxCaseTemp != null) {
            destination.setMaxCaseTemp(maxCaseTemp);
        }
        Byte ccBase = source.getCcBase();
        if (ccBase != null) {
            destination.setCcBase(ccBase);
        }
        Boolean txInpEqAutoAdp = source.getTxInpEqAutoAdp();
        if (txInpEqAutoAdp != null) {
            destination.setTxInpEqAutoAdp(txInpEqAutoAdp);
        }
        Boolean txInpEqFixProg = source.getTxInpEqFixProg();
        if (txInpEqFixProg != null) {
            destination.setTxInpEqFixProg(txInpEqFixProg);
        }
        Boolean rxOutpEmphFixProg = source.getRxOutpEmphFixProg();
        if (rxOutpEmphFixProg != null) {
            destination.setRxOutpEmphFixProg(rxOutpEmphFixProg);
        }
        Boolean rxOutpAmplFixProg = source.getRxOutpAmplFixProg();
        if (rxOutpAmplFixProg != null) {
            destination.setRxOutpAmplFixProg(rxOutpAmplFixProg);
        }
        Boolean txCDROnOffCtrl = source.getTxCDROnOffCtrl();
        if (txCDROnOffCtrl != null) {
            destination.setTxCDROnOffCtrl(txCDROnOffCtrl);
        }
        Boolean rxCDROnOffCtrl = source.getRxCDROnOffCtrl();
        if (rxCDROnOffCtrl != null) {
            destination.setRxCDROnOffCtrl(rxCDROnOffCtrl);
        }
        Boolean txSquelchImp = source.isTxSquelchImp();
        if (txSquelchImp != null) {
            destination.setTxSquelchImp(txSquelchImp);
        }
        Boolean memPage02Provided = source.isMemPage02Provided();
        if (memPage02Provided != null) {
            destination.setMemPage02Provided(memPage02Provided);
        }
        Boolean memPage01Provided = source.isMemPage01Provided();
        if (memPage01Provided != null) {
            destination.setMemPage01Provided(memPage01Provided);
        }
        String vendorSN = source.getVendorSN();
        if (vendorSN != null) {
            destination.setVendorSN(vendorSN);
        }
        String dateCode = source.getDateCode();
        if (dateCode != null) {
            destination.setDateCode(dateCode);
        }
        Date date = source.getDateCodeAsDate();
        if (date != null) {
            destination.setDate(date);
        }
        Byte ccExt = source.getCcExt();
        if (ccExt != null) {
            destination.setCcExt(ccExt);
        }
        Boolean certCableFlag = source.getCertCableFlag();
        if (certCableFlag != null) {
            destination.setCertCableFlag(certCableFlag);
        }
        Integer reachClass = source.getReachClass();
        if (reachClass != null) {
            destination.setReachClass(reachClass);
        }
        CertifiedRateType certDataRate = source.getCertDataRate();
        if (certDataRate != null) {
            destination.setCertDataRate(certDataRate);
        }

    }

    public void getCableInfo(DevicePropertyCategory category,
            CableInfoBean bean) {

        String na = STLConstants.K0039_NOT_AVAILABLE.getValue();
        if (bean != null) {
            Byte id = bean.getId();
            if (id != null) {
                addProperty(category, CABLE_ID, hex(id));
            } else {
                addProperty(category, CABLE_ID, na);
            }

            PowerClassType powerClass = bean.getPowerClass();
            if (powerClass != null) {
                try {
                    addProperty(category, CABLE_POWER_CLASS, PowerClassTypeViz
                            .getPowerClassTypeVizFor(powerClass).getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                addProperty(category, CABLE_POWER_CLASS, na);
            }

            Boolean txCDRSupported = bean.getTxCDRSupported();
            if (txCDRSupported != null) {
                addProperty(category, CABLE_TX_CDR_SUP, txCDRSupported
                        ? K0385_TRUE.getValue() : K0386_FALSE.getValue());
            } else {
                addProperty(category, CABLE_TX_CDR_SUP, na);
            }

            Boolean rxCDRSupported = bean.getTxCDRSupported();
            if (rxCDRSupported != null) {
                addProperty(category, CABLE_RX_CDR_SUP, rxCDRSupported
                        ? K0385_TRUE.getValue() : K0386_FALSE.getValue());
            } else {
                addProperty(category, CABLE_RX_CDR_SUP, na);
            }
            Byte connector = bean.getConnector();
            if (connector != null) {
                addProperty(category, CABLE_CONNECTOR, hex(connector));
            } else {
                addProperty(category, CABLE_CONNECTOR, na);
            }

            Byte bitRateLow = bean.getBitRateLow();
            Byte bitRateHigh = bean.getBitRateHigh();
            if (bitRateLow != null && bitRateHigh != null) {
                Integer nominalBr =
                        bean.stlCableInfoBitRate(bitRateLow, bitRateHigh);
                if (nominalBr != null) {
                    addProperty(category, CABLE_NOMINAL_BR, nominalBr.toString()
                            + " " + STLConstants.K1152_CABLE_GB.getValue());
                } else {
                    addProperty(category, CABLE_NOMINAL_BR, na);
                }
            } else {
                addProperty(category, CABLE_NOMINAL_BR, na);
            }
            Integer om2Length = bean.getOm2Length();
            if (om2Length != null) {
                addProperty(category, CABLE_OM2_LEN, Integer.toString(om2Length)
                        + " " + STLConstants.K1154_CABLE_M.getValue());
            } else {
                addProperty(category, CABLE_OM2_LEN, na);
            }

            Integer om3Length = bean.getOm3Length();
            if (om3Length != null) {
                addProperty(category, CABLE_OM3_LEN, Integer.toString(om3Length)
                        + " " + STLConstants.K1154_CABLE_M.getValue());
            } else {
                addProperty(category, CABLE_OM3_LEN, na);
            }

            Integer om4Length = bean.getOm4Length();
            if (om4Length != null) {
                addProperty(category, CABLE_COPPER_LEN,
                        Integer.toString(om4Length) + " "
                                + STLConstants.K1154_CABLE_M.getValue());
            } else {
                addProperty(category, CABLE_COPPER_LEN, na);
            }

            Byte xmitTech = bean.getXmitTech();
            Byte codeConnector = bean.getConnector();
            if (xmitTech != null && codeConnector != null) {
                try {
                    {
                        CableTypeViz cableType = CableTypeViz
                                .getCableTypeVizFor(bean.stlCableInfoCableType(
                                        xmitTech.intValue(), codeConnector));
                        if (cableType != null) {
                            addProperty(category, CABLE_DEVICE_TECH,
                                    cableType.getName());
                        } else {
                            addProperty(category, CABLE_DEVICE_TECH, na);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                addProperty(category, CABLE_DEVICE_TECH, na);
            }

            String vendorName = bean.getVendorName();
            if (vendorName != null) {
                addProperty(category, CABLE_VENDOR_NAME, vendorName);
            } else {
                addProperty(category, CABLE_VENDOR_NAME, na);
            }
            Byte[] vendorOui = bean.getVendorOui();
            if (vendorOui != null) {
                StringBuilder sb = new StringBuilder(vendorOui.length * 2);
                sb.append("0x");
                for (byte b : vendorOui) {
                    sb.append(String.format("%02x", b));
                }
                addProperty(category, CABLE_VENDOR_OUI, sb.toString());
            } else {
                addProperty(category, CABLE_VENDOR_OUI, na);
            }

            String vendorPN = bean.getVendorPn();
            if (vendorPN != null) {
                addProperty(category, CABLE_VENDOR_PN, vendorPN);
            } else {
                addProperty(category, CABLE_VENDOR_PN, na);
            }
            String vendorRev = bean.getVendorRev();
            if (vendorRev != null) {
                addProperty(category, CABLE_VENDOR_REV, vendorRev);
            } else {
                addProperty(category, CABLE_VENDOR_REV, na);
            }
            Integer maxCaseTemp = bean.getMaxCaseTemp();
            if (maxCaseTemp != null) {
                addProperty(category, CABLE_MAXCASE_TEMP,
                        Integer.toString(maxCaseTemp) + " "
                                + STLConstants.K1158_CABLE_C.getValue());
            } else {
                addProperty(category, CABLE_MAXCASE_TEMP, na);
            }
            Byte ccBase = bean.getCcBase();
            if (ccBase != null) {
                addProperty(category, CABLE_CC_BASE, hex(bean.getCcBase()));
            } else {
                addProperty(category, CABLE_CC_BASE, na);
            }

            Boolean txInpEqAutoAdp = bean.getTxInpEqAutoAdp();
            if (txInpEqAutoAdp != null) {
                if (txInpEqAutoAdp) {
                    addProperty(category, CABLE_TX_INP_EQ_AUTO_ADP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_INP_EQ_AUTO_ADP,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_TX_INP_EQ_AUTO_ADP, na);
            }

            Boolean txInpEqFixProg = bean.getTxInpEqFixProg();
            if (txInpEqFixProg != null) {
                if (txInpEqFixProg) {
                    addProperty(category, CABLE_TX_INP_EQ_FIX_PROG,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_INP_EQ_FIX_PROG,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_TX_INP_EQ_FIX_PROG, na);
            }

            Boolean rxOutpEmphFixProg = bean.getRxOutpEmphFixProg();
            if (rxOutpEmphFixProg != null) {
                if (rxOutpEmphFixProg) {
                    addProperty(category, CABLE_RX_OUTP_EMPH_FIX_PROG,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_RX_OUTP_EMPH_FIX_PROG,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_RX_OUTP_EMPH_FIX_PROG, na);
            }

            Boolean rxOutpAmplFixProg = bean.getRxOutpAmplFixProg();
            if (rxOutpAmplFixProg != null) {
                if (rxOutpAmplFixProg) {
                    addProperty(category, CABLE_RX_OUTP_AMPL_FIX_PROG,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_RX_OUTP_AMPL_FIX_PROG,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_RX_OUTP_AMPL_FIX_PROG, na);
            }

            Boolean txCDROnOffCtrl = bean.getTxCDROnOffCtrl();
            if (txCDROnOffCtrl != null) {
                if (txCDROnOffCtrl) {
                    addProperty(category, CABLE_TX_CDR_ON_OFF_CTRL,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_CDR_ON_OFF_CTRL,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_TX_CDR_ON_OFF_CTRL, na);
            }

            Boolean rxCDROnOffCtrl = bean.getRxCDROnOffCtrl();
            if (rxCDROnOffCtrl != null) {
                if (rxCDROnOffCtrl) {
                    addProperty(category, CABLE_RX_CDR_ON_OFF_CTRL,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_RX_CDR_ON_OFF_CTRL,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_RX_CDR_ON_OFF_CTRL, na);
            }

            Boolean txSquelchImp = bean.isTxSquelchImp();
            if (txSquelchImp != null) {
                if (txSquelchImp) {
                    addProperty(category, CABLE_TX_SQUELCH_IMP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_SQUELCH_IMP,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_TX_SQUELCH_IMP, na);
            }

            Boolean memPage02Provided = bean.isMemPage02Provided();
            if (memPage02Provided != null) {
                if (memPage02Provided) {
                    addProperty(category, CABLE_MEM_PAGE02_PROV,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_MEM_PAGE02_PROV,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_MEM_PAGE02_PROV, na);
            }

            Boolean memPage01Provided = bean.isMemPage01Provided();
            if (memPage01Provided != null) {
                if (memPage01Provided) {
                    addProperty(category, CABLE_MEM_PAGE01_PROV,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_MEM_PAGE01_PROV,
                            K0386_FALSE.getValue());
                }
            } else {
                addProperty(category, CABLE_MEM_PAGE01_PROV, na);
            }
            String vendorSn = bean.getVendorSN();
            if (vendorSn != null) {
                addProperty(category, CABLE_VENDOR_SN, vendorSn);
            } else {
                addProperty(category, CABLE_VENDOR_SN, na);
            }
            String dateCode = bean.getDateCode();
            if (dateCode != null) {
                addProperty(category, CABLE_DATE_CODE, "20" + dateCode);
            } else {
                String beanDateCode = bean.getDateCode();
                if (beanDateCode != null) {
                    dateCode = STLConstants.K1116_INVALID.getValue();
                } else {
                    dateCode = na;

                }
                addProperty(category, CABLE_DATE_CODE, dateCode);
            }

            Byte ccExt = bean.getCcExt();
            if (ccExt != null) {
                addProperty(category, CABLE_CC_EXT, hex(ccExt));
            } else {
                addProperty(category, CABLE_CC_EXT, na);
            }

            Boolean ccFlag = bean.getCertCableFlag();
            if (ccFlag != null) {
                addProperty(category, CABLE_CERT_CABLE_FLAG,
                        ccFlag ? STLConstants.K1155_CABLE_Y.getValue()
                                : STLConstants.K1156_CABLE_N.getValue());
            } else {
                addProperty(category, CABLE_CERT_CABLE_FLAG, na);
            }

            Integer reachClass = bean.getReachClass();
            if (reachClass != null) {
                addProperty(category, CABLE_REACH_CLASS,
                        Integer.toString(reachClass));
            } else {
                addProperty(category, CABLE_REACH_CLASS, na);
            }

            CertifiedRateType certDataRate = bean.getCertDataRate();
            if (certDataRate != null) {
                try {
                    addProperty(category, CABLE_CERT_DATA_RATE,
                            CertifiedRateTypeViz
                                    .getCertifiedRateTypeVizFor(certDataRate)
                                    .getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                addProperty(category, CABLE_CERT_DATA_RATE, na);
            }

        } else {
            getNullCableInfo(category);
        }
    }

    private void getNullCableInfo(DevicePropertyCategory category) {
        String na = STLConstants.K0039_NOT_AVAILABLE.getValue();
        addProperty(category, CABLE_ID, na);
        addProperty(category, CABLE_POWER_CLASS, na);
        addProperty(category, CABLE_TX_CDR_SUP, na);
        addProperty(category, CABLE_RX_CDR_SUP, na);
        addProperty(category, CABLE_CONNECTOR, na);
        addProperty(category, CABLE_NOMINAL_BR, na);
        addProperty(category, CABLE_OM2_LEN, na);
        addProperty(category, CABLE_OM3_LEN, na);
        addProperty(category, CABLE_COPPER_LEN, na);
        addProperty(category, CABLE_DEVICE_TECH, na);
        addProperty(category, CABLE_VENDOR_NAME, na);
        addProperty(category, CABLE_VENDOR_OUI, na);
        addProperty(category, CABLE_VENDOR_PN, na);
        addProperty(category, CABLE_VENDOR_REV, na);
        addProperty(category, CABLE_MAXCASE_TEMP, na);
        addProperty(category, CABLE_CC_BASE, na);
        addProperty(category, CABLE_TX_INP_EQ_AUTO_ADP, na);
        addProperty(category, CABLE_TX_INP_EQ_FIX_PROG, na);
        addProperty(category, CABLE_RX_OUTP_EMPH_FIX_PROG, na);
        addProperty(category, CABLE_RX_OUTP_AMPL_FIX_PROG, na);
        addProperty(category, CABLE_TX_CDR_ON_OFF_CTRL, na);
        addProperty(category, CABLE_RX_CDR_ON_OFF_CTRL, na);
        addProperty(category, CABLE_TX_SQUELCH_IMP, na);
        addProperty(category, CABLE_MEM_PAGE02_PROV, na);
        addProperty(category, CABLE_MEM_PAGE01_PROV, na);
        addProperty(category, CABLE_VENDOR_SN, na);
        addProperty(category, CABLE_DATE_CODE, na);
        addProperty(category, CABLE_CC_EXT, na);
        addProperty(category, CABLE_CERT_CABLE_FLAG, na);
        addProperty(category, CABLE_REACH_CLASS, na);
        addProperty(category, CABLE_CERT_DATA_RATE, na);
    }

}
