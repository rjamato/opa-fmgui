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
 *  Archive Log:    Revision 1.4.2.2  2015/08/12 15:21:42  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4.2.1  2015/05/06 19:23:03  jijunwan
 *  Archive Log:    fixed printout issue found by FindBugs
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
import java.util.Arrays;

public class CableInfoBean implements Serializable {
    // we need to handle unsigned values
    private static final long serialVersionUID = 1L;

    private Byte id;

    private Byte extId;

    private Byte connector;

    private Byte nominalBr;

    private Byte smfLength;

    private Byte om3Length;

    private Byte om2Length;

    private Byte om1Length;

    private Byte copperLength;

    private String deviceTech;

    private String vendorName;

    private String extendedModule;

    private byte[] vendorOui;

    private String vendorPn;

    private String vendorRev;

    private Integer opticalWaveLength;

    private Byte maxCaseTemp;

    private Byte ccBase;

    private Boolean rxOutAmpProg;

    private Boolean rxSquelchDisImp;

    private Boolean rxOutputDisCap;

    private Boolean txSquelchDisImp;

    private Boolean txSquelchImp;

    private Boolean memPage02Provided;

    private Boolean memPage01Provided;

    private Boolean txDisImp;

    private Boolean txFaultRepImp;

    private Boolean losReportImp;

    private String vendorSN;

    private String dataCode;

    private String lotCode;

    private Byte ccExt;

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
    public Byte getExtId() {
        return extId;
    }

    /**
     * @return the connector
     */
    public Byte getConnector() {
        return connector;
    }

    /**
     * @return the nominalBr
     */
    public Byte getNominalBr() {
        return nominalBr;
    }

    /**
     * @return the smfLength
     */
    public Byte getSmfLength() {
        return smfLength;
    }

    /**
     * @return the om3Length
     */
    public Byte getOm3Length() {
        return om3Length;
    }

    /**
     * @return the om2Length
     */
    public Byte getOm2Length() {
        return om2Length;
    }

    /**
     * @return the om1Length
     */
    public Byte getOm1Length() {
        return om1Length;
    }

    /**
     * @return the copperLength
     */
    public Byte getCopperLength() {
        return copperLength;
    }

    /**
     * @return the deviceTech
     */
    public String getDeviceTech() {
        return deviceTech;
    }

    /**
     * @return the vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * @return the extendedModule
     */
    public String getExtendedModule() {
        return extendedModule;
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
     * @return the opticalWaveLength
     */
    public Integer getOpticalWaveLength() {
        return opticalWaveLength;
    }

    /**
     * @return the maxCaseTemp
     */
    public Byte getMaxCaseTemp() {
        return maxCaseTemp;
    }

    /**
     * @return the ccBase
     */
    public Byte getCcBase() {
        return ccBase;
    }

    /**
     * @return the rxOutApmProg
     */
    public Boolean isRxOutAmpProg() {
        return rxOutAmpProg;
    }

    /**
     * @return the rxSquelchDisImp
     */
    public Boolean isRxSquelchDisImp() {
        return rxSquelchDisImp;
    }

    /**
     * @return the rxOutputDisCap
     */
    public Boolean isRxOutputDisCap() {
        return rxOutputDisCap;
    }

    /**
     * @return the txSquelchDisImp
     */
    public Boolean isTxSquelchDisImp() {
        return txSquelchDisImp;
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
     * @return the txDisImp
     */
    public Boolean isTxDisImp() {
        return txDisImp;
    }

    /**
     * @return the txFaultRepImp
     */
    public Boolean isTxFaultRepImp() {
        return txFaultRepImp;
    }

    /**
     * @return the losReportImp
     */
    public Boolean isLosReportImp() {
        return losReportImp;
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
    public String getDataCode() {
        return dataCode;
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
     * @param extId
     *            the extId to set
     */
    public void setExtId(byte extId) {
        this.extId = extId;
    }

    /**
     * @param connector
     *            the connector to set
     */
    public void setConnector(byte connector) {
        this.connector = connector;
    }

    /**
     * @param nominalBr
     *            the nominalBr to set
     */
    public void setNominalBr(byte nominalBr) {
        this.nominalBr = nominalBr;
    }

    /**
     * @param smfLength
     *            the smfLength to set
     */
    public void setSmfLength(byte smfLength) {
        this.smfLength = smfLength;
    }

    /**
     * @param om3Length
     *            the om3Length to set
     */
    public void setOm3Length(byte om3Length) {
        this.om3Length = om3Length;
    }

    /**
     * @param om2Length
     *            the om2Length to set
     */
    public void setOm2Length(byte om2Length) {
        this.om2Length = om2Length;
    }

    /**
     * @param om1Length
     *            the om1Length to set
     */
    public void setOm1Length(byte om1Length) {
        this.om1Length = om1Length;
    }

    /**
     * @param copperLength
     *            the copperLength to set
     */
    public void setCopperLength(byte copperLength) {
        this.copperLength = copperLength;
    }

    /**
     * @param deviceTech
     *            the deviceTech to set
     */
    public void setDeviceTech(String deviceTech) {
        this.deviceTech = deviceTech;
    }

    /**
     * @param vendorName
     *            the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * @param extendedModule
     *            the extendedModule to set
     */
    public void setExtendedModule(String extendedModule) {
        this.extendedModule = extendedModule;
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
     * @param opticalWaveLength
     *            the opticalWaveLength to set
     */
    public void setOpticalWaveLength(int opticalWaveLength) {
        this.opticalWaveLength = opticalWaveLength;
    }

    /**
     * @param maxCaseTemp
     *            the maxCaseTemp to set
     */
    public void setMaxCaseTemp(byte maxCaseTemp) {
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
     * @param rxOutApmProg
     *            the rxOutApmProg to set
     */
    public void setRxOutAmpProg(boolean rxOutAmpProg) {
        this.rxOutAmpProg = rxOutAmpProg;
    }

    /**
     * @param rxSquelchDisImp
     *            the rxSquelchDisImp to set
     */
    public void setRxSquelchDisImp(boolean rxSquelchDisImp) {
        this.rxSquelchDisImp = rxSquelchDisImp;
    }

    /**
     * @param rxOutputDisCap
     *            the rxOutputDisCap to set
     */
    public void setRxOutputDisCap(boolean rxOutputDisCap) {
        this.rxOutputDisCap = rxOutputDisCap;
    }

    /**
     * @param txSquelchDisImp
     *            the txSquelchDisImp to set
     */
    public void setTxSquelchDisImp(boolean txSquelchDisImp) {
        this.txSquelchDisImp = txSquelchDisImp;
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
     * @param txDisImp
     *            the txDisImp to set
     */
    public void setTxDisImp(boolean txDisImp) {
        this.txDisImp = txDisImp;
    }

    /**
     * @param txFaultRepImp
     *            the txFaultRepImp to set
     */
    public void setTxFaultRepImp(boolean txFaultRepImp) {
        this.txFaultRepImp = txFaultRepImp;
    }

    /**
     * @param losReportImp
     *            the losReportImp to set
     */
    public void setLosReportImp(boolean losReportImp) {
        this.losReportImp = losReportImp;
    }

    /**
     * @param vendorSN
     *            the vendorSN to set
     */
    public void setVendorSN(String vendorSN) {
        this.vendorSN = vendorSN;
    }

    /**
     * @param dataCode
     *            the dataCode to set
     */
    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
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
     * @param notApplicableData
     *            the notApplicableData to set
     */
    public void setNotApplicableData(String notApplicableData) {
        this.notApplicableData = notApplicableData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CableInfoBean [id=" + id + ", extId=" + extId + ", connector="
                + connector + ", nominalBr=" + nominalBr + ", smfLength="
                + smfLength + ", om3Length=" + om3Length + ", om2Length="
                + om2Length + ", om1Length=" + om1Length + ", copperLength="
                + copperLength + ", deviceTech=" + deviceTech + ", vendorName="
                + vendorName + ", extendedModule=" + extendedModule
                + ", vendorOui=" + Arrays.toString(vendorOui) + ", vendorPn="
                + vendorPn + ", vendorRev=" + vendorRev
                + ", opticalWaveLength=" + opticalWaveLength + ", maxCaseTemp="
                + maxCaseTemp + ", ccBase=" + ccBase + ", rxOutApmProg="
                + rxOutAmpProg + ", rxSquelchDisImp=" + rxSquelchDisImp
                + ", rxOutputDisCap=" + rxOutputDisCap + ", txSquelchDisImp="
                + txSquelchDisImp + ", txSquelchImp=" + txSquelchImp
                + ", memPage02Provided=" + memPage02Provided
                + ", memPage01Provided=" + memPage01Provided + ", txDisImp="
                + txDisImp + ", txFaultRepImp=" + txFaultRepImp
                + ", losReportImp=" + losReportImp + ", vendorSN=" + vendorSN
                + ", dataCode=" + dataCode + ", lotCode=" + lotCode
                + ", ccExt=" + ccExt + ", notApplicableData="
                + notApplicableData + "]";
    }
}
