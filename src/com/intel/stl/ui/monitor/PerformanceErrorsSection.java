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

package com.intel.stl.ui.monitor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jfree.util.Log;

import com.intel.stl.api.performance.PortCountersBean;
import com.intel.stl.api.performance.VFPortCountersBean;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.configuration.view.IPropertyListener;
import com.intel.stl.ui.configuration.view.PropertyVizStyle;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.main.PerfErrorsCard;
import com.intel.stl.ui.main.view.PerfErrorsItem;
import com.intel.stl.ui.monitor.view.PerformanceErrorsSectionView;

import net.engio.mbassy.bus.MBassador;

public class PerformanceErrorsSection extends
        BaseSectionController<ISectionListener, PerformanceErrorsSectionView>
        implements IPropertyListener {
    private PerfErrorsCard errorsCard;

    private PerfErrorsCard otherCard;

    private final LinkedHashMap<String, PerfErrorsItem> errorMap =
            new LinkedHashMap<String, PerfErrorsItem>();

    private final LinkedHashMap<String, PerfErrorsItem> otherMap =
            new LinkedHashMap<String, PerfErrorsItem>();

    private final PropertyVizStyle style = new PropertyVizStyle();

    public PerformanceErrorsSection(PerformanceErrorsSectionView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        view.setStyleListener(this);
        initErrorsCard();
        initOtherCard();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.BaseSectionController#getHelpID()
     */
    @Override
    public String getHelpID() {
        return HelpAction.getInstance().getCounters();
    }

    private void initErrorsCard() {
        // Creates items for each error and pass all of them to
        // Card. The Card add strings to the card based on each item key/values.
        // We should be able to update the values but don't need to
        // unnecessarily update each item.
        initErrItemMap();
        errorsCard = new PerfErrorsCard(view.getErrorsCardView(), eventBus,
                errorMap.values());
        errorsCard.setHelpID(HelpAction.getInstance().getReceiveCounters());
    }

    private void initOtherCard() {
        initOtherItemMap();
        otherCard = new PerfErrorsCard(view.getOtherCardView(), eventBus,
                otherMap.values());
        otherCard.setHelpID(HelpAction.getInstance().getOtherCounters());
    }

    private void initErrItemMap() {
        /////////////
        // Receive
        /////////////
        errorMap.put(STLConstants.K0746_RECEIVE.getValue(), new PerfErrorsItem(
                STLConstants.K0746_RECEIVE.getValue(), -1, true));

        errorMap.put(STLConstants.K0730_RX_CUMULATIVE_DATA.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0730_RX_CUMULATIVE_DATA.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0728_RX_CUMULATIVE_PACKETS.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0728_RX_CUMULATIVE_PACKETS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0834_RX_MULTICAST_PACKETS.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0834_RX_MULTICAST_PACKETS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Signal Integrity and Node/Link Stability
        errorMap.put(STLConstants.K0519_RX_ERRORS.getValue(),
                new PerfErrorsItem(STLConstants.K0519_RX_ERRORS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0520_RX_REMOTE_PHY_ERRORS.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0520_RX_REMOTE_PHY_ERRORS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Security
        errorMap.put(STLConstants.K0522_RX_PORT_CONSTRAINT.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0522_RX_PORT_CONSTRAINT.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), true));
        // Routing or Down nodes still being sent to
        errorMap.put(STLConstants.K0717_REC_SW_REL_ERR.getValue(),
                new PerfErrorsItem(STLConstants.K0717_REC_SW_REL_ERR.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Congestion
        errorMap.put(STLConstants.K0837_RX_FECN.getValue(),
                new PerfErrorsItem(STLConstants.K0837_RX_FECN.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), true));
        errorMap.put(STLConstants.K0838_RX_BECN.getValue(),
                new PerfErrorsItem(STLConstants.K0838_RX_BECN.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Bubbles
        errorMap.put(STLConstants.K0842_RX_BUBBLE.getValue(),
                new PerfErrorsItem(STLConstants.K0842_RX_BUBBLE.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), true));

        /////////////
        // Transmit
        /////////////
        errorMap.put(STLConstants.K0745_TRANSMIT.getValue(), new PerfErrorsItem(
                STLConstants.K0745_TRANSMIT.getValue(), -1, true));

        errorMap.put(STLConstants.K0732_TX_CUMULATIVE_DATA.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0732_TX_CUMULATIVE_DATA.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0734_TX_CUMULATIVE_PACKETS.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0734_TX_CUMULATIVE_PACKETS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0833_TX_MULTICAST_PACKETS.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0833_TX_MULTICAST_PACKETS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));

        // Security
        errorMap.put(STLConstants.K0521_TX_PORT_CONSTRAINT.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0521_TX_PORT_CONSTRAINT.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Routing or Down nodes still being sent to
        errorMap.put(STLConstants.K0714_TRAN_DISCARDS.getValue(),
                new PerfErrorsItem(STLConstants.K0714_TRAN_DISCARDS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Congestion
        errorMap.put(STLConstants.K0839_TX_TIME_CONG.getValue(),
                new PerfErrorsItem(STLConstants.K0839_TX_TIME_CONG.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0836_TX_WAIT.getValue(),
                new PerfErrorsItem(STLConstants.K0836_TX_WAIT.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Bubbles
        errorMap.put(STLConstants.K0840_TX_WASTED_BW.getValue(),
                new PerfErrorsItem(STLConstants.K0840_TX_WASTED_BW.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        errorMap.put(STLConstants.K0841_TX_WAIT_DATA.getValue(),
                new PerfErrorsItem(STLConstants.K0841_TX_WAIT_DATA.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
    }

    private void insertIntoErrItemMap(PortCountersBean bean) {
        // We want to keep this order. Shouldn't be known to VIEW.
        setConvertedValStr(errorMap,
                STLConstants.K0730_RX_CUMULATIVE_DATA.getValue(),
                bean.getPortRcvData());
        setValStr(errorMap, STLConstants.K0728_RX_CUMULATIVE_PACKETS.getValue(),
                bean.getPortRcvPkts());

        setValStr(errorMap, STLConstants.K0834_RX_MULTICAST_PACKETS.getValue(),
                bean.getPortMulticastRcvPkts());
        setValStr(errorMap, STLConstants.K0519_RX_ERRORS.getValue(),
                bean.getPortRcvErrors());

        setValStr(errorMap, STLConstants.K0522_RX_PORT_CONSTRAINT.getValue(),
                bean.getPortRcvConstraintErrors());

        setValStr(errorMap, STLConstants.K0717_REC_SW_REL_ERR.getValue(),
                bean.getPortRcvSwitchRelayErrors());
        setValStr(errorMap, STLConstants.K0520_RX_REMOTE_PHY_ERRORS.getValue(),
                bean.getPortRcvRemotePhysicalErrors());

        setValStr(errorMap, STLConstants.K0837_RX_FECN.getValue(),
                bean.getPortRcvFECN());

        setValStr(errorMap, STLConstants.K0838_RX_BECN.getValue(),
                bean.getPortRcvBECN());
        setValStr(errorMap, STLConstants.K0842_RX_BUBBLE.getValue(),
                bean.getPortRcvBubble());

        // Transimitted
        setConvertedValStr(errorMap,
                STLConstants.K0732_TX_CUMULATIVE_DATA.getValue(),
                bean.getPortXmitData());
        setValStr(errorMap, STLConstants.K0734_TX_CUMULATIVE_PACKETS.getValue(),
                bean.getPortXmitPkts());
        setValStr(errorMap, STLConstants.K0833_TX_MULTICAST_PACKETS.getValue(),
                bean.getPortMulticastXmitPkts());
        setValStr(errorMap, STLConstants.K0731_TX_DISCARDS.getValue(),
                bean.getPortXmitDiscards());
        setValStr(errorMap, STLConstants.K0521_TX_PORT_CONSTRAINT.getValue(),
                bean.getPortXmitConstraintErrors());
        setValStr(errorMap, STLConstants.K0836_TX_WAIT.getValue(),
                bean.getPortXmitWait());
        setValStr(errorMap, STLConstants.K0839_TX_TIME_CONG.getValue(),
                bean.getPortXmitTimeCong());
        setValStr(errorMap, STLConstants.K0840_TX_WASTED_BW.getValue(),
                bean.getPortXmitWastedBW());
        setValStr(errorMap, STLConstants.K0841_TX_WAIT_DATA.getValue(),
                bean.getPortXmitWaitData());

    }

    private void insertIntoErrItemMap(VFPortCountersBean bean) {
        // Received
        setConvertedValStr(errorMap,
                STLConstants.K0730_RX_CUMULATIVE_DATA.getValue(),
                bean.getPortVFRcvData());
        setValStr(errorMap, STLConstants.K0728_RX_CUMULATIVE_PACKETS.getValue(),
                bean.getPortVFRcvPkts());

        setValStr(errorMap, STLConstants.K0837_RX_FECN.getValue(),
                bean.getPortVFRcvFECN());

        setValStr(errorMap, STLConstants.K0838_RX_BECN.getValue(),
                bean.getPortVFRcvBECN());
        setValStr(errorMap, STLConstants.K0842_RX_BUBBLE.getValue(),
                bean.getPortVFRcvBubble());

        // Transmitted
        setConvertedValStr(errorMap,
                STLConstants.K0732_TX_CUMULATIVE_DATA.getValue(),
                bean.getPortVFXmitData());
        setValStr(errorMap, STLConstants.K0734_TX_CUMULATIVE_PACKETS.getValue(),
                bean.getPortVFXmitPkts());
        setValStr(errorMap, STLConstants.K0731_TX_DISCARDS.getValue(),
                bean.getPortVFXmitDiscards());
        setValStr(errorMap, STLConstants.K0836_TX_WAIT.getValue(),
                bean.getPortVFXmitWait());
        setValStr(errorMap, STLConstants.K0839_TX_TIME_CONG.getValue(),
                bean.getPortVFXmitTimeCong());
        setValStr(errorMap, STLConstants.K0840_TX_WASTED_BW.getValue(),
                bean.getPortVFXmitWastedBW());
        setValStr(errorMap, STLConstants.K0841_TX_WAIT_DATA.getValue(),
                bean.getPortVFXmitWaitData());
    }

    private void initOtherItemMap() {
        // Others
        otherMap.put(STLConstants.K0715_OTHER_COUNTERS.getValue(),
                new PerfErrorsItem(STLConstants.K0715_OTHER_COUNTERS.getValue(),
                        -1, true));
        // Signal Integrity and Node/Link Stability
        otherMap.put(STLConstants.K2068_LINK_QUALITY.getValue(),
                new PerfErrorsItem(STLConstants.K2068_LINK_QUALITY.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K0716_UNCORR_ERR.getValue(),
                new PerfErrorsItem(STLConstants.K0716_UNCORR_ERR.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K0518_LINK_DOWN.getValue(),
                new PerfErrorsItem(STLConstants.K0518_LINK_DOWN.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K1655_NUM_LANES_DOWN.getValue(),
                new PerfErrorsItem(STLConstants.K1655_NUM_LANES_DOWN.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K0719_EXCESS_BUFF_OVERRUNS.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0719_EXCESS_BUFF_OVERRUNS.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), true));
        otherMap.put(STLConstants.K0720_FM_CONFIG_ERR.getValue(),
                new PerfErrorsItem(STLConstants.K0720_FM_CONFIG_ERR.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K0517_LINK_RECOVERIES.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0517_LINK_RECOVERIES.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K0718_LOCAL_LINK_INTEG_ERR.getValue(),
                new PerfErrorsItem(
                        STLConstants.K0718_LOCAL_LINK_INTEG_ERR.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        // Congestion
        otherMap.put(STLConstants.K0835_SW_PORT_CONG.getValue(),
                new PerfErrorsItem(STLConstants.K0835_SW_PORT_CONG.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), false));
        otherMap.put(STLConstants.K0843_MARK_FECN.getValue(),
                new PerfErrorsItem(STLConstants.K0843_MARK_FECN.getValue(),
                        STLConstants.K0039_NOT_AVAILABLE.getValue(), true));
    }

    private void insertIntoOtherItemMap(PortCountersBean bean) {
        // Others
        setValStr(otherMap, STLConstants.K0718_LOCAL_LINK_INTEG_ERR.getValue(),
                bean.getLocalLinkIntegrityErrors());
        setValStr(otherMap, STLConstants.K0720_FM_CONFIG_ERR.getValue(),
                bean.getFmConfigErrors());
        setValStr(otherMap, STLConstants.K0719_EXCESS_BUFF_OVERRUNS.getValue(),
                bean.getExcessiveBufferOverruns());

        setValStr(otherMap, STLConstants.K0835_SW_PORT_CONG.getValue(),
                bean.getSwPortCongestion());
        setValStr(otherMap, STLConstants.K0843_MARK_FECN.getValue(),
                bean.getPortMarkFECN());
        setValStr(otherMap, STLConstants.K0517_LINK_RECOVERIES.getValue(),
                bean.getLinkErrorRecovery());
        setValStr(otherMap, STLConstants.K0518_LINK_DOWN.getValue(),
                bean.getLinkDowned());
        setValStr(otherMap, STLConstants.K1655_NUM_LANES_DOWN.getValue(),
                bean.getNumLanesDown());
        setValStr(otherMap, STLConstants.K0716_UNCORR_ERR.getValue(),
                bean.getUncorrectableErrors());

        setValStr(otherMap, STLConstants.K2068_LINK_QUALITY.getValue(),
                bean.getLinkQualityIndicator());

    }

    private void insertIntoOtherItemMap(VFPortCountersBean bean) {
        // Others
        setValStr(otherMap, STLConstants.K0835_SW_PORT_CONG.getValue(),
                bean.getSwPortVFCongestion());
        setValStr(otherMap, STLConstants.K0843_MARK_FECN.getValue(),
                bean.getPortVFMarkFECN());
    }

    protected void setValStr(LinkedHashMap<String, PerfErrorsItem> map,
            String key, long value) {
        PerfErrorsItem item = map.get(key);
        if (item != null) {
            item.setValStr(UIConstants.INTEGER.format(value));
        } else {
            Log.warn("Couldn't find item '" + key + "'");
        }
    }

    private void setConvertedValStr(LinkedHashMap<String, PerfErrorsItem> map,
            String key, long value) {
        PerfErrorsItem item = map.get(key);
        if (item != null) {
            item.setValStr(UIConstants.INTEGER.format(value) + " "
                    + STLConstants.K0748_FLITS.getValue());
        } else {
            Log.warn("Couldn't find item '" + key + "'");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.hpc.stl.ui.ISection#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return new ICardController[] { errorsCard, otherCard };
    }

    public void updateErrors(PortCountersBean portCountersBean) {
        if (portCountersBean.isDelta()) {
            // it will be complicate to handle delta style data. To get correct
            // cumulative data, we need the initial data when we clear counters.
            // without a database that keeps tracking data from the very
            // beginning, this is almost impossible. Plus we also need
            // to clear it when a user click clear counter button. So it's
            // better to let FM to handle it and we force ourselves to use
            // no delta style port counter data
            throw new IllegalArgumentException(
                    "We do not support delta style PortCounters");
        }

        insertIntoErrItemMap(portCountersBean);
        errorsCard.updateErrorsItems(errorMap.values());

        insertIntoOtherItemMap(portCountersBean);
        otherCard.updateErrorsItems(otherMap.values());
    }

    /**
     * Description:
     *
     * @param result
     */
    public void updateErrors(VFPortCountersBean result) {
        if (result.isDelta()) {
            // it will be complicate to handle delta style data. To get correct
            // cumulative data, we need the initial data when we clear counters.
            // without a database that keeps tracking data from the very
            // beginning, this is almost impossible. Plus we also need
            // to clear it when a user click clear counter button. So it's
            // better to let FM to handle it and we force ourselves to use
            // no delta style port counter data
            throw new IllegalArgumentException(
                    "We do not support delta style PortCounters");
        }

        insertIntoErrItemMap(result);
        errorsCard.updateErrorsItems(errorMap.values());

        insertIntoOtherItemMap(result);
        otherCard.updateErrorsItems(otherMap.values());

    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.BaseSectionController#getSectionListener()
     */
    @Override
    protected ISectionListener getSectionListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.BaseSectionController#clear()
     */
    @Override
    public void clear() {
        initErrItemMap();
        errorsCard.updateErrorsItems(errorMap.values());
        initOtherItemMap();
        otherCard.updateErrorsItems(otherMap.values());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.configuration.view.IPropertyListener#onShowBorder(
     * boolean )
     */
    @Override
    public void onShowBorder(boolean isSelected) {
        System.out.print(isSelected);
        style.setShowBorder(isSelected);
        setStyle(style);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyListener#onShowAlternation
     * (boolean)
     */
    @Override
    public void onShowAlternation(boolean isSelected) {
        style.setAlternateRows(isSelected);
        setStyle(style);
    }

    private void setStyle(PropertyVizStyle style) {
        errorsCard.setStyle(style);
        otherCard.setStyle(style);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.intel.stl.ui.configuration.view.IPropertyListener#onDisplayChanged
     * (java.util.Map)
     */
    @Override
    public void onDisplayChanged(Map<String, Boolean> newSelections) {
        // TODO Auto-generated method stub

    }
}
