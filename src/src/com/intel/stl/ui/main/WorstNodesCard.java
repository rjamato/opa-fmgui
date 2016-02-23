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
 *  File Name: WorstNodesCard.java
 *
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.15  2015/08/17 18:53:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.14  2015/08/05 02:47:02  jijunwan
 *  Archive Log: PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log: - introduced UndoHandler to manage undo/redo
 *  Archive Log: - added undo/redo to main frame
 *  Archive Log: - improved FabricController to support undoHandler and undo action on page selection
 *  Archive Log: - improved FabricController to support the new page name based IPageListener
 *  Archive Log:
 *  Archive Log: Revision 1.13  2015/06/25 20:24:56  jijunwan
 *  Archive Log: Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log: - applied pin framework on fabric viewer and simple 'static' cards
 *  Archive Log:
 *  Archive Log: Revision 1.12  2015/06/09 18:37:27  jijunwan
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

import java.util.Properties;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.IPinProvider;
import com.intel.stl.ui.common.PinDescription.PinID;
import com.intel.stl.ui.common.PinnableCardController;
import com.intel.stl.ui.common.UndoableJumpEvent;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.NodesSelectedEvent;
import com.intel.stl.ui.event.PageSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.IWorstNodesListener;
import com.intel.stl.ui.main.view.WorstNodesView;
import com.intel.stl.ui.model.NodeScore;

/**
 * @author jijunwan
 * 
 */
public class WorstNodesCard extends
        PinnableCardController<IWorstNodesListener, WorstNodesView> implements
        IWorstNodesListener, IPinProvider {
    private UndoHandler undoHandler;

    private final String origin = HomePage.NAME;

    private NodeScore[] lastNodes;

    public WorstNodesCard(WorstNodesView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ICardController#getHelpID()
     */
    @Override
    public String getHelpID() {
        return HelpAction.getInstance().getWorstNodes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.PinnableCardController#setContext(com.intel.stl
     * .ui.main.Context)
     */
    @Override
    public void setContext(Context context) {
        super.setContext(context);

        if (context != null && context.getController() != null) {
            undoHandler = context.getController().getUndoHandler();
        }
    }

    /**
     * @param nodes
     */
    public void updateWorstNodes(final NodeScore[] nodes) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                lastNodes = nodes;
                view.updateNodes(nodes);
                if (pinView != null) {
                    pinView.updateNodes(nodes);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onNodeSelected(int,
     * com.intel.stl.api.subnet.NodeType)
     */
    @Override
    public void jumpTo(int lid, NodeType type, JumpDestination descination) {
        NodesSelectedEvent event =
                new NodesSelectedEvent(lid, type, this, descination.getName());
        eventBus.publish(event);

        if (undoHandler != null && !undoHandler.isInProgress()) {
            UndoableJumpEvent undoSel =
                    new UndoableJumpEvent(eventBus, new PageSelectedEvent(this,
                            origin), event);
            undoHandler.addUndoAction(undoSel);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public IWorstNodesListener getCardListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#clear()
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                lastNodes = null;
                view.clear();
                if (pinView != null) {
                    pinView.clear();
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onMore()
     */
    @Override
    public void onMore() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onSizeChange(int)
     */
    @Override
    public void onSizeChanged(int size) {
        view.setSize(size);
        if (pinView != null) {
            pinView.setSize(size);
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
        // no argument
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#createPinView()
     */
    @Override
    protected WorstNodesView createPinView() {
        WorstNodesView pinView = new WorstNodesView();
        pinView.setCardListener(getCardListener());
        return pinView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#initPinView()
     */
    @Override
    protected void initPinView() {
        if (lastNodes != null) {
            pinView.updateNodes(lastNodes);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.PinnableCardController#getPinID()
     */
    @Override
    public PinID getPinID() {
        return PinID.WORST;
    }

}
