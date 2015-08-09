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
 *  File Name: CableInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
import static com.intel.stl.ui.model.DeviceProperty.CABLE_CONNECTOR;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_COPPER_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_DATA_CODE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_DEVICE_TECH;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_EXT_ID;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_EXT_MODULE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_ID;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_LOS_REP_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_LOT_CODE;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_MAXCASE_TEMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_MEM_PAGE01_PROV;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_MEM_PAGE02_PROV;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_NA;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_NOMINAL_BR;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_OM1_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_OM2_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_OM3_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_OPTICAL_WL;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_OUT_AMP_PROG;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_OUT_DIS_CAP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_RX_SQULECH_DIS_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_SMF_LEN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_DIS_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_FAULT_REP_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_SQUELCH_DIS_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_TX_SQUELCH_IMP;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_NAME;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_OUI;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_PN;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_REV;
import static com.intel.stl.ui.model.DeviceProperty.CABLE_VENDOR_SN;

import java.util.List;

import com.intel.stl.api.subnet.CableInfoBean;
import com.intel.stl.api.subnet.CableRecordBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.PortTypeViz;
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

        List<CableRecordBean> cableRecordBeans =
                subnetApi.getCable(portBean.getEndPortLID(),
                        portBean.getPortNum());

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

        for (CableRecordBean cableBean : cableRecordBeans) {
            getCableInfo(category, cableBean.getCableInfo());
        }
    }

    public void getCableInfo(DevicePropertyCategory category, CableInfoBean bean) {

        if (bean != null) {
            Byte id = bean.getId();
            if (id != null) {
                addProperty(category, CABLE_ID, hex(id));
            }
            Byte extId = bean.getExtId();
            if (extId != null) {
                addProperty(category, CABLE_EXT_ID, hex(extId));
            }
            Byte connector = bean.getConnector();
            if (connector != null) {
                addProperty(category, CABLE_CONNECTOR, hex(connector));
            }
            Byte nominalBr = bean.getNominalBr();
            if (nominalBr != null) {
                addProperty(category, CABLE_NOMINAL_BR, hex(nominalBr));
            }
            Byte smfLength = bean.getSmfLength();
            if (smfLength != null) {
                addProperty(category, CABLE_SMF_LEN, hex(smfLength));
            }
            Byte om3Length = bean.getOm3Length();
            if (om3Length != null) {
                addProperty(category, CABLE_OM3_LEN, hex(om3Length));
            }
            Byte om2Length = bean.getOm2Length();
            if (om2Length != null) {
                addProperty(category, CABLE_OM2_LEN, hex(om2Length));
            }
            Byte om1Length = bean.getOm1Length();
            if (om1Length != null) {
                addProperty(category, CABLE_OM1_LEN, hex(om1Length));
            }
            Byte copperLength = bean.getCopperLength();
            if (copperLength != null) {
                addProperty(category, CABLE_COPPER_LEN, hex(copperLength));
            }
            String deviceTech = bean.getDeviceTech();
            if (deviceTech != null) {
                addProperty(category, CABLE_DEVICE_TECH, deviceTech);
            }
            String vendorName = bean.getVendorName();
            if (vendorName != null) {
                addProperty(category, CABLE_VENDOR_NAME, vendorName);
            }
            String extendedModule = bean.getExtendedModule();
            if (extendedModule != null) {
                addProperty(category, CABLE_EXT_MODULE, extendedModule);
            }
            byte[] vendorOui = bean.getVendorOui();
            if (vendorOui != null) {
                StringBuilder sb = new StringBuilder(vendorOui.length * 2);
                sb.append("0x");
                for (byte b : vendorOui) {
                    sb.append(String.format("%02x", b));
                }
                addProperty(category, CABLE_VENDOR_OUI, sb.toString());
            }

            String vendorPN = bean.getVendorPn();
            if (vendorPN != null) {
                addProperty(category, CABLE_VENDOR_PN, vendorPN);
            }
            String vendorRev = bean.getVendorRev();
            if (vendorRev != null) {
                addProperty(category, CABLE_VENDOR_REV, vendorRev);
            }
            Integer opticalWaveLength = bean.getOpticalWaveLength();
            if (opticalWaveLength != null) {
                addProperty(category, CABLE_OPTICAL_WL,
                        Integer.toString(opticalWaveLength));
            }
            Byte maxCaseTemp = bean.getMaxCaseTemp();
            if (maxCaseTemp != null) {
                addProperty(category, CABLE_MAXCASE_TEMP, hex(maxCaseTemp));
            }
            Byte ccBase = bean.getCcBase();
            if (ccBase != null) {
                addProperty(category, CABLE_CC_BASE, hex(bean.getCcBase()));
            }
            Boolean rxOutAmpProg = bean.isRxOutAmpProg();
            if (rxOutAmpProg != null) {
                if (rxOutAmpProg) {
                    addProperty(category, CABLE_RX_OUT_AMP_PROG,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_RX_OUT_AMP_PROG,
                            K0386_FALSE.getValue());
                }
            }
            Boolean rxSquelchDisImp = bean.isRxSquelchDisImp();
            if (rxSquelchDisImp != null) {
                if (rxSquelchDisImp) {
                    addProperty(category, CABLE_RX_SQULECH_DIS_IMP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_RX_SQULECH_DIS_IMP,
                            K0386_FALSE.getValue());
                }
            }
            Boolean rxOutputDisCap = bean.isRxOutputDisCap();
            if (rxOutputDisCap != null) {
                if (rxOutputDisCap) {
                    addProperty(category, CABLE_RX_OUT_DIS_CAP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_RX_OUT_DIS_CAP,
                            K0386_FALSE.getValue());
                }
            }
            Boolean txSquelchDisImp = bean.isTxSquelchDisImp();
            if (txSquelchDisImp != null) {
                if (txSquelchDisImp) {
                    addProperty(category, CABLE_TX_SQUELCH_DIS_IMP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_SQUELCH_DIS_IMP,
                            K0386_FALSE.getValue());
                }
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
            }
            Boolean txDisImp = bean.isTxDisImp();
            if (txDisImp != null) {
                if (txDisImp) {
                    addProperty(category, CABLE_TX_DIS_IMP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_DIS_IMP,
                            K0386_FALSE.getValue());
                }
            }
            Boolean txFaultRepImp = bean.isTxFaultRepImp();
            if (txFaultRepImp != null) {
                if (txFaultRepImp) {
                    addProperty(category, CABLE_TX_FAULT_REP_IMP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_TX_FAULT_REP_IMP,
                            K0386_FALSE.getValue());
                }
            }
            Boolean losReportImp = bean.isLosReportImp();
            if (losReportImp != null) {
                if (losReportImp) {
                    addProperty(category, CABLE_LOS_REP_IMP,
                            K0385_TRUE.getValue());
                } else {
                    addProperty(category, CABLE_LOS_REP_IMP,
                            K0386_FALSE.getValue());
                }
            }
            String vendorSn = bean.getVendorSN();
            if (vendorSn != null) {
                addProperty(category, CABLE_VENDOR_SN, vendorSn);
            }
            String dataCode = bean.getDataCode();
            if (dataCode != null) {
                addProperty(category, CABLE_DATA_CODE, dataCode);
            }
            String lotCode = bean.getLotCode();
            if (lotCode != null) {
                addProperty(category, CABLE_LOT_CODE, lotCode);
            }
            Byte ccExt = bean.getCcExt();
            if (ccExt != null) {
                addProperty(category, CABLE_CC_EXT, hex(ccExt));
            }
            String notApplicableData = bean.getNotApplicableData();
            if (notApplicableData != null) {
                addProperty(category, CABLE_NA, notApplicableData);
            }
        } else {
            String na = STLConstants.K0039_NOT_AVAILABLE.getValue();

            addProperty(category, CABLE_ID, na);
            addProperty(category, CABLE_EXT_ID, na);
            addProperty(category, CABLE_CONNECTOR, na);
            addProperty(category, CABLE_NOMINAL_BR, na);
            addProperty(category, CABLE_SMF_LEN, na);
            addProperty(category, CABLE_OM3_LEN, na);
            addProperty(category, CABLE_OM2_LEN, na);
            addProperty(category, CABLE_OM1_LEN, na);
            addProperty(category, CABLE_COPPER_LEN, na);
            addProperty(category, CABLE_DEVICE_TECH, na);
            addProperty(category, CABLE_VENDOR_NAME, na);
            addProperty(category, CABLE_EXT_MODULE, na);
            addProperty(category, CABLE_VENDOR_OUI, na);
            addProperty(category, CABLE_VENDOR_PN, na);
            addProperty(category, CABLE_VENDOR_REV, na);
            addProperty(category, CABLE_OPTICAL_WL, na);
            addProperty(category, CABLE_MAXCASE_TEMP, na);
            addProperty(category, CABLE_CC_BASE, na);
            addProperty(category, CABLE_RX_OUT_AMP_PROG, na);
            addProperty(category, CABLE_RX_SQULECH_DIS_IMP, na);
            addProperty(category, CABLE_RX_OUT_DIS_CAP, na);
            addProperty(category, CABLE_TX_SQUELCH_DIS_IMP, na);
            addProperty(category, CABLE_TX_SQUELCH_IMP, na);
            addProperty(category, CABLE_MEM_PAGE02_PROV, na);
            addProperty(category, CABLE_MEM_PAGE01_PROV, na);
            addProperty(category, CABLE_TX_DIS_IMP, na);
            addProperty(category, CABLE_TX_FAULT_REP_IMP, na);
            addProperty(category, CABLE_LOS_REP_IMP, na);
            addProperty(category, CABLE_VENDOR_SN, na);
            addProperty(category, CABLE_DATA_CODE, na);
            addProperty(category, CABLE_LOT_CODE, na);
            addProperty(category, CABLE_CC_EXT, na);
        }
    }
}