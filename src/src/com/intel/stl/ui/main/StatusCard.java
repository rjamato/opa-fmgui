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
 *  Functional Group: FabricViewer
 *
 *  File Name: NodeStatesCard.java
 *
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.10  2015/08/17 18:53:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.9  2015/07/17 20:25:04  jijunwan
 *  Archive Log: PR 129593 - empty health pin card
 *  Archive Log: - set card size based on original view size
 *  Archive Log:
 *  Archive Log: Revision 1.8  2015/06/26 22:36:17  jijunwan
 *  Archive Log: PR 126755 - Pin Board functionality is not working in FV
 *  Archive Log: - set new created status view to proper size, so the pin card view has desired size as well
 *  Archive Log:
 *  Archive Log: Revision 1.7  2015/06/25 20:24:56  jijunwan
 *  Archive Log: Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log: - applied pin framework on fabric viewer and simple 'static' cards
 *  Archive Log:
 *  Archive Log: Revision 1.6  2015/06/09 18:37:27  jijunwan
 *  Archive Log: PR 129069 - Incorrect Help action
 *  Archive Log: - moved help action from view to controller
 *  Archive Log: - only enable help button when we have HelpID
 *  Archive Log: - fixed incorrect HelpIDs
 *  Archive Log:
 * 
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.util.EnumMap;
import java.util.Properties;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.IPinProvider;
import com.intel.stl.ui.common.PinDescription.PinID;
import com.intel.stl.ui.common.PinnableCardController;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.IChartStyleListener;
import com.intel.stl.ui.main.view.StatusView;
import com.intel.stl.ui.model.ChartStyle;

/**
 * @author jijunwan
 * 
 */
public class StatusCard extends
        PinnableCardController<IChartStyleListener, StatusView> implements
        IChartStyleListener, IPinProvider {
    private final NodeStatusController swStatusCtl;

    private final NodeStatusController fiStatusCtl;

    private NodeStatusController pinSwStatusCtl;

    private NodeStatusController pinFiStatusCtl;

    public StatusCard(StatusView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        swStatusCtl = new NodeStatusController(view.getSwPanel());
        fiStatusCtl = new NodeStatusController(view.getFiPanel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ICardController#getHelpID()
     */
    @Override
    public String getHelpID() {
        return HelpAction.getInstance().getStatus();
    }

    /**
     * @param swStates
     * @param totalSWs
     */
    public void updateSwStates(EnumMap<NoticeSeverity, Integer> states,
            final int total) {
        swStatusCtl.updateStates(states, total);
        if (pinSwStatusCtl != null) {
            pinSwStatusCtl.updateStates(states, total);
        }
    }

    public void updateFiStates(EnumMap<NoticeSeverity, Integer> states,
            final int total) {
        fiStatusCtl.updateStates(states, total);
        if (pinFiStatusCtl != null) {
            pinFiStatusCtl.updateStates(states, total);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.main.view.INodeStatesListener#onStyleChange(com.intel
     * .stl.ui.main.view.NodeStatesView.Style)
     */
    @Override
    public void onStyleChange(ChartStyle style) {
        ChartStyle newStyle = null;
        if (style == ChartStyle.BAR) {
            newStyle = ChartStyle.PIE;
        } else if (style == ChartStyle.PIE) {
            newStyle = ChartStyle.BAR;
        } else {
            throw new IllegalArgumentException("Unknown chart style: " + style);
        }
        view.setStyle(newStyle);
        swStatusCtl.setStyle(newStyle);
        fiStatusCtl.setStyle(newStyle);
        if (pinView != null) {
            pinView.setStyle(newStyle);
            pinSwStatusCtl.setStyle(newStyle);
            pinFiStatusCtl.setStyle(newStyle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public IChartStyleListener getCardListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#clear()
     */
    @Override
    public void clear() {
        swStatusCtl.clear();
        fiStatusCtl.clear();
        if (pinSwStatusCtl != null) {
            pinSwStatusCtl.clear();
        }
        if (pinFiStatusCtl != null) {
            pinFiStatusCtl.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.PinnableCardController#generateArgument(java.
     * util.Properties)
     */
    @Override
    protected void generateArgument(Properties arg) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#createPinView()
     */
    @Override
    protected StatusView createPinView() {
        StatusView pinView = new StatusView() {
            private static final long serialVersionUID = 461783273933437225L;

            @Override
            protected boolean isConcise() {
                return true;
            }

        };
        pinView.setCardListener(getCardListener());
        pinSwStatusCtl = new NodeStatusController(pinView.getSwPanel());
        pinFiStatusCtl = new NodeStatusController(pinView.getFiPanel());
        return pinView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#initPinView()
     */
    @Override
    protected void initPinView() {
        pinSwStatusCtl.updateStates(swStatusCtl.getLastStates(),
                swStatusCtl.getLastTotal());
        pinFiStatusCtl.updateStates(fiStatusCtl.getLastStates(),
                fiStatusCtl.getLastTotal());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#clearPinView()
     */
    @Override
    protected void clearPinView() {
        super.clearPinView();
        pinSwStatusCtl.clear();
        pinSwStatusCtl = null;
        pinFiStatusCtl.clear();
        pinFiStatusCtl = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#getPinID()
     */
    @Override
    public PinID getPinID() {
        return PinID.STATUS;
    }

}
