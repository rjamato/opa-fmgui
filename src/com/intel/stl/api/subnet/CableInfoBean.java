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
 *  File Name: CableInfoBean.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/09/15 13:31:31  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    Incorporated the FM changes (PR 129390) as of 8/28/15. These changes are mainly from IbPrint/stl_sma.c revision 1.163.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/08/19 22:26:35  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - correction on OM length calculation
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/08/19 21:02:55  jijunwan
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    - adapt to latest FM code
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/19 18:08:30  jypak
 *  Archive Log:    PR 129397 - gaps in cableinfo output and handling.
 *  Archive Log:    Updates for ID and OpticalWaveLength.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/17 18:48:38  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/07 14:57:53  jypak
 *  Archive Log:    PR 129397 -gaps in cableinfo output and handling.
 *  Archive Log:    Updates on the formats of the cableinfo output and also new enums were defined for different output values.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/06/29 15:05:42  jypak
 *  Archive Log:    PR 129284 - Incorrect QSFP field name.
 *  Archive Log:    Field name fix has been implemented. Also, introduced a conversion to Date object to add flexibility to display date code.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/01 21:40:03  jijunwan
 *  Archive Log:    fixed minor issue found by FindBug
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 15:46:08  jypak
 *  Archive Log:    Updated to use Byte class rather than primitive type byte to differentiate between valid data and 'no data'. The cable data are spread across two record entries, so, 'no data' for a field in CableInfoBean means that the data for the field is in the other record. Also, since we need to handle unsigned values, we cannot use NaN as -1.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/02/04 21:37:55  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *
 *  Overview: Cable Info Record from SA populated by the connect manager. QSFP interpreted.
 *
 *  @author: jypak
 *
 ******************************************************************************/

package com.intel.stl.api.subnet;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CableInfoBean implements Serializable {
    // we need to handle unsigned values
    private static final long serialVersionUID = 1L;

    protected static Logger log = LoggerFactory.getLogger(CableInfoBean.class);

    private Byte id;

    private PowerClassType powerClass;

    private Boolean txCDRSupported;

    private Boolean rxCDRSupported;

    private Byte connector;

    private Byte bitRateLow;

    private Byte bitRateHigh;

    private Integer om3Length;

    private Integer om2Length;

    private Integer om4Length;

    private Integer codeXmit;

    private String vendorName;

    private byte[] vendorOui;

    private String vendorPn;

    private String vendorRev;

    private Integer maxCaseTemp;

    private Byte ccBase;

    private Byte linkCodes;

    private Boolean txInpEqAutoAdp;

    private Boolean txInpEqFixProg;

    private Boolean rxOutpEmphFixProg;

    private Boolean rxOutpAmplFixProg;

    private Boolean txCDROnOffCtrl;

    private Boolean rxCDROnOffCtrl;

    private Boolean txSquelchImp;

    private Boolean memPage02Provided;

    private Boolean memPage01Provided;

    private String vendorSN;

    private String dateCode;

    private Date date;

    private String lotCode;

    private Byte ccExt;

    private Boolean certCableFlag;

    private Integer reachClass;

    private CertifiedRateType certDataRate;

    private String notApplicableData;

    // How about number of blocks? List<LFTRecordBean> size will be it.

    public CableInfoBean() {
        super();
    }

    /**
     * @return the id
     */
    public Byte getId() {
        return id;
    }

    /**
     * @return the extId
     */
    public PowerClassType getPowerClass() {
        return powerClass;
    }

    /**
     * @return the connector
     */
    public Byte getConnector() {
        return connector;
    }

    /**
     * @return the bitRateLow
     */
    public Byte getBitRateLow() {
        return bitRateLow;
    }

    /**
     * @return the bitRateHigh
     */
    public Byte getBitRateHigh() {
        return bitRateHigh;
    }

    /**
     * @return the om3Length
     */
    public Integer getOm3Length() {
        return om3Length;
    }

    /**
     * @return the om2Length
     */
    public Integer getOm2Length() {
        return om2Length;
    }

    /**
     * @return the copperLength
     */
    public Integer getOm4Length() {
        return om4Length;
    }

    /**
     * @return the codeXmit
     */
    public Integer getCodeXmit() {
        return codeXmit;
    }

    /**
     * @return the vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * @return the vendorOui
     */
    public byte[] getVendorOui() {
        return vendorOui;
    }

    /**
     * @return the vendorPn
     */
    public String getVendorPn() {
        return vendorPn;
    }

    /**
     * @return the vendorRev
     */
    public String getVendorRev() {
        return vendorRev;
    }

    /**
     * @return the maxCaseTemp
     */
    public Integer getMaxCaseTemp() {
        return maxCaseTemp;
    }

    /**
     * @return the ccBase
     */
    public Byte getCcBase() {
        return ccBase;
    }

    /**
     * @return the linkCodes
     */
    public Byte getLinkCodes() {
        return linkCodes;
    }

    /**
     * @return the txInpEqAutoAdp
     */
    public Boolean getTxInpEqAutoAdp() {
        return txInpEqAutoAdp;
    }

    /**
     * @return the txInpEqFixProg
     */
    public Boolean getTxInpEqFixProg() {
        return txInpEqFixProg;
    }

    /**
     * @return the rxOutpEmphFixProg
     */
    public Boolean getRxOutpEmphFixProg() {
        return rxOutpEmphFixProg;
    }

    /**
     * @return the rxOutpAmplFixProg
     */
    public Boolean getRxOutpAmplFixProg() {
        return rxOutpAmplFixProg;
    }

    /**
     * @return the txCDROnOffCtrl
     */
    public Boolean getTxCDROnOffCtrl() {
        return txCDROnOffCtrl;
    }

    /**
     * @return the rxCDROnOffCtrl
     */
    public Boolean getRxCDROnOffCtrl() {
        return rxCDROnOffCtrl;
    }

    /**
     * @return the txSquelchImp
     */
    public Boolean getTxSquelchImp() {
        return txSquelchImp;
    }

    /**
     * @return the txSquelchImp
     */
    public Boolean isTxSquelchImp() {
        return txSquelchImp;
    }

    /**
     * @return the memPage02Provided
     */
    public Boolean isMemPage02Provided() {
        return memPage02Provided;
    }

    /**
     * @return the memPage01Provided
     */
    public Boolean isMemPage01Provided() {
        return memPage01Provided;
    }

    /**
     * @return the vendorSN
     */
    public String getVendorSN() {
        return vendorSN;
    }

    /**
     * @return the dataCode
     */
    public String getDateCode() {
        return dateCode;
    }

    public Date getDateCodeAsDate() {
        return date;
    }

    /**
     * @return the lotCode
     */
    public String getLotCode() {
        return lotCode;
    }

    /**
     * @return the ccExt
     */
    public Byte getCcExt() {
        return ccExt;
    }

    /**
     * @return the certCableFlag
     */
    public Boolean getCertCableFlag() {
        return certCableFlag;
    }

    /**
     * @return the reachClass
     */
    public Integer getReachClass() {
        return reachClass;
    }

    /**
     * @return the certDataRate
     */
    public CertifiedRateType getCertDataRate() {
        return certDataRate;
    }

    /**
     * @return the notApplicableData
     */
    public String getNotApplicableData() {
        return notApplicableData;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(byte id) {
        this.id = id;
    }

    /**
     * @param extID
     *            the extId to set
     */
    public void setPowerClass(PowerClassType extID) {
        this.powerClass = extID;
    }

    /**
     * @return the txCDRSupported
     */
    public Boolean getTxCDRSupported() {
        return txCDRSupported;
    }

    /**
     * @param txCDRSupported
     *            the txCDRSupported to set
     */
    public void setTxCDRSupported(Boolean txCDRSupported) {
        this.txCDRSupported = txCDRSupported;
    }

    /**
     * @return the rxCDRSupported
     */
    public Boolean getRxCDRSupported() {
        return rxCDRSupported;
    }

    /**
     * @param rxCDRSupported
     *            the rxCDRSupported to set
     */
    public void setRxCDRSupported(Boolean rxCDRSupported) {
        this.rxCDRSupported = rxCDRSupported;
    }

    /**
     * @param connector
     *            the connector to set
     */
    public void setConnector(byte connector) {
        this.connector = connector;
    }

    /**
     * @param bitRateLow
     *            the bitRateLow to set
     */
    public void setBitRateLow(byte bitRateLow) {
        this.bitRateLow = bitRateLow;
    }

    /**
     * @param bitRateHigh
     *            the bitRateHigh to set
     */
    public void setBitRateHigh(byte bitRateHigh) {
        this.bitRateHigh = bitRateHigh;
    }

    /**
     * @param om3Length
     *            the om3Length to set
     */
    public void setOm3Length(int om3Length) {
        this.om3Length = om3Length;
    }

    /**
     * @param om2Length
     *            the om2Length to set
     */
    public void setOm2Length(int om2Length) {
        this.om2Length = om2Length;
    }

    /**
     * @param copperLength
     *            the copperLength to set
     */
    public void setOm4Length(int om4Length) {
        this.om4Length = om4Length;
    }

    /**
     * @param codeXmit
     *            the codeXmit to set
     */
    public void setCodeXmit(int codeXmit) {
        this.codeXmit = codeXmit;
    }

    /**
     * @param vendorName
     *            the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * @param vendorOui
     *            the vendorOui to set
     */
    public void setVendorOui(byte[] vendorOui) {
        this.vendorOui = vendorOui;
    }

    /**
     * @param vendorPn
     *            the vendorPn to set
     */
    public void setVendorPn(String vendorPn) {
        this.vendorPn = vendorPn;
    }

    /**
     * @param vendorRev
     *            the vendorRev to set
     */
    public void setVendorRev(String vendorRev) {
        this.vendorRev = vendorRev;
    }


    /**
     * @param maxCaseTemp
     *            the maxCaseTemp to set
     */
    public void setMaxCaseTemp(int maxCaseTemp) {
        this.maxCaseTemp = maxCaseTemp;
    }

    /**
     * @param ccBase
     *            the ccBase to set
     */
    public void setCcBase(byte ccBase) {
        this.ccBase = ccBase;
    }

    /**
     * @param linkCodes
     *            the linkCodes to set
     */
    public void setLinkCodes(Byte linkCodes) {
        this.linkCodes = linkCodes;
    }

    /**
     * @param txInpEqAutoAdp
     *            the txInpEqAutoAdp to set
     */
    public void setTxInpEqAutoAdp(Boolean txInpEqAutoAdp) {
        this.txInpEqAutoAdp = txInpEqAutoAdp;
    }

    /**
     * @param txInpEqFixProg
     *            the txInpEqFixProg to set
     */
    public void setTxInpEqFixProg(Boolean txInpEqFixProg) {
        this.txInpEqFixProg = txInpEqFixProg;
    }

    /**
     * @param rxOutpEmphFixProg
     *            the rxOutpEmphFixProg to set
     */
    public void setRxOutpEmphFixProg(Boolean rxOutpEmphFixProg) {
        this.rxOutpEmphFixProg = rxOutpEmphFixProg;
    }

    /**
     * @param rxOutpAmplFixProg
     *            the rxOutpAmplFixProg to set
     */
    public void setRxOutpAmplFixProg(Boolean rxOutpAmplFixProg) {
        this.rxOutpAmplFixProg = rxOutpAmplFixProg;
    }

    /**
     * @param txCDROnOffCtrl
     *            the txCDROnOffCtrl to set
     */
    public void setTxCDROnOffCtrl(Boolean txCDROnOffCtrl) {
        this.txCDROnOffCtrl = txCDROnOffCtrl;
    }

    /**
     * @param rxCDROnOffCtrl
     *            the rxCDROnOffCtrl to set
     */
    public void setRxCDROnOffCtrl(Boolean rxCDROnOffCtrl) {
        this.rxCDROnOffCtrl = rxCDROnOffCtrl;
    }

    /**
     * @param txSquelchImp
     *            the txSquelchImp to set
     */
    public void setTxSquelchImp(Boolean txSquelchImp) {
        this.txSquelchImp = txSquelchImp;
    }

    /**
     * @param txSquelchImp
     *            the txSquelchImp to set
     */
    public void setTxSquelchImp(boolean txSquelchImp) {
        this.txSquelchImp = txSquelchImp;
    }

    /**
     * @param memPage02Provided
     *            the memPage02Provided to set
     */
    public void setMemPage02Provided(boolean memPage02Provided) {
        this.memPage02Provided = memPage02Provided;
    }

    /**
     * @param memPage01Provided
     *            the memPage01Provided to set
     */
    public void setMemPage01Provided(boolean memPage01Provided) {
        this.memPage01Provided = memPage01Provided;
    }

    /**
     * @param vendorSN
     *            the vendorSN to set
     */
    public void setVendorSN(String vendorSN) {
        this.vendorSN = vendorSN;
    }

    /**
     * @param dateCode
     *            the dateCode to set
     */
    public void setDateCode(String year, String month, String day) {
        dateCode = year + "/" + month + "/" + day;

        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        try {
            date = formatter.parse(dateCode);
        } catch (ParseException e) {
            date = null;
            log.warn("Parsing exception for date code string", e);
        }
    }

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param lotCode
     *            the lotCode to set
     */
    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    /**
     * @param ccExt
     *            the ccExt to set
     */
    public void setCcExt(byte ccExt) {
        this.ccExt = ccExt;
    }

    /**
     * <i>Description:</i>
     * 
     * @param certCableFlag
     */
    public void setCertCableFlag(boolean certCableFlag) {
        this.certCableFlag = certCableFlag;

    }

    /**
     * @param reachClass
     *            the reachClass to set
     */
    public void setReachClass(int reachClass) {
        this.reachClass = reachClass;
    }

    /**
     * @param certDataRate
     *            the certDataRate to set
     */
    public void setCertDataRate(CertifiedRateType certDataRate) {
        this.certDataRate = certDataRate;
    }

    /**
     * @param notApplicableData
     *            the notApplicableData to set
     */
    public void setNotApplicableData(String notApplicableData) {
        this.notApplicableData = notApplicableData;
    }

    /**
     * 
     * <i>Description:</i>Unit in Gbps.
     * 
     * @param codeLow
     * @param codeHigh
     * @return
     */
    public Integer stlCableInfoBitRate(byte codeLow, byte codeHigh) {
        int result;
        if (codeLow == (byte) 0xFF) {
            result = (codeHigh / 4);
        } else {
            result = (codeLow / 10);
        }
        return result;
    }

    public CableType stlCableInfoCableType(int codeXmit, byte codeConnector) {
        CableType cableType = null;
        int connectId = 0;
        if ((codeXmit <= 9) && (codeXmit != 8)) {
            if (codeConnector == SAConstants.CABLEINFO_CONNECTOR_NOSEP) {
                connectId = 1;
            } else {
                connectId = 2;
            }
        }

        cableType = CableType.getCableType(connectId, codeXmit);

        return cableType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CableInfoBean [id=" + id + ", extId=" + powerClass
                + ", txCDRSupported=" + txCDRSupported + ", rxCDRSupported="
                + rxCDRSupported + ", connector=" + connector + ", bitRateLow="
                + bitRateLow + ", bitRateHigh=" + bitRateHigh + ", om3Length="
                + om3Length + ", om2Length=" + om2Length + ", om4Length="
                + om4Length + ", codeXmit=" + codeXmit + ", vendorName="
                + vendorName + ", vendorOui=" + Arrays.toString(vendorOui)
                + ", vendorPn=" + vendorPn + ", vendorRev=" + vendorRev
                + ", maxCaseTemp=" + maxCaseTemp + ", ccBase=" + ccBase
                + ", txInpEqAutoAdp=" + txInpEqAutoAdp + ", txInpEqFixProg="
                + txInpEqFixProg + ", rxOutpEmphFixProg=" + rxOutpEmphFixProg
                + ", rxOutpAmplFixProg=" + rxOutpAmplFixProg
                + ", txCDROnOffCtrl=" + txCDROnOffCtrl + ", rxCDROnOffCtrl="
                + rxCDROnOffCtrl + ", txSquelchImp=" + txSquelchImp
                + ", memPage02Provided=" + memPage02Provided
                + ", memPage01Provided=" + memPage01Provided + ", vendorSN="
                + vendorSN + ", dateCode=" + dateCode + ", date=" + date
                + ", lotCode=" + lotCode + ", ccExt=" + ccExt
                + ", certCableFlag=" + certCableFlag + ", reachClass="
                + reachClass + ", certDataRate=" + certDataRate
                + ", notApplicableData=" + notApplicableData + "]";
    }

}
