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
 *  File Name: HomePage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.64  2016/02/09 20:23:13  jijunwan
 *  Archive Log: PR 132575 - [PSC] Null pointer message in FM GUI
 *  Archive Log:
 *  Archive Log: - some minor improvements
 *  Archive Log:
 *  Archive Log: Revision 1.63  2015/10/15 21:16:03  jijunwan
 *  Archive Log: PR 131044 - Switch/HFI status can go beyond 100%
 *  Archive Log: - changed to use larger number of nodes
 *  Archive Log:
 *  Archive Log: Revision 1.62  2015/09/26 06:28:35  jijunwan
 *  Archive Log: 130487 - FM GUI: Topology refresh required after enabling Fabric Simulator
 *  Archive Log: - changed to do a delayed refresh if there are changes on ImageInfo data
 *  Archive Log:
 *  Archive Log: Revision 1.61  2015/09/26 03:23:52  jijunwan
 *  Archive Log: PR 130522 - OtherPorts doesn't report a value
 *  Archive Log: - set isRefreshing back to false after we processed imageinfo
 *  Archive Log:
 *  Archive Log: Revision 1.60  2015/09/25 20:51:38  fernande
 *  Archive Log: PR129920 - revisit health score calculation. Changed formula to include several factors (or attributes) within the calculation as well as user-defined weights (for now are hard coded).
 *  Archive Log:
 *  Archive Log: Revision 1.59  2015/09/25 13:48:16  jijunwan
 *  Archive Log: changed to log rather than display error message
 *  Archive Log:
 *  Archive Log: Revision 1.58  2015/09/20 22:32:42  jijunwan
 *  Archive Log: PR 130522 - OtherPorts doesn't report a value
 *  Archive Log: - added back setting port distribution with data from SubnetApi
 *  Archive Log: - refresh port distribution calculation when there is a notice, or user manually clicking refresh button, or there is changes on ImageInfo's statistics numbers
 *  Archive Log:
 *  Archive Log: Revision 1.57  2015/08/31 22:01:42  jijunwan
 *  Archive Log: PR 130197 - Calculated fabric health above 100% when entire fabric is rebooted
 *  Archive Log: - changed to only use information from ImageInfo for calculation
 *  Archive Log:
 *  Archive Log: Revision 1.56  2015/08/17 18:53:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.55  2015/08/11 17:37:23  jijunwan
 *  Archive Log: PR 126645 - Topology Page does not show correct data after port disable/enable event
 *  Archive Log: - improved to get distribution data with argument "refresh". When it's true, calculate distribution rather than get it from cache
 *  Archive Log:
 *  Archive Log: Revision 1.54  2015/08/11 14:15:00  jijunwan
 *  Archive Log: PR 129917 - No update on event statistics
 *  Archive Log: - Apply event subscriber on EventSummaryBar and HomePage to periodically update. Both will update either by event or period updating.
 *  Archive Log:
 *  Archive Log: Revision 1.53  2015/08/05 03:09:28  jijunwan
 *  Archive Log: PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log: - improved HomePage to set origin for jumping event
 *  Archive Log: - improved IDataTypeListener to include both old and new type to better support undo
 *  Archive Log: - improved IPageListener to use page name rather than page index, so we can support undo on link selection on Topology Page where links more than 5 are maintained in a drop down menu
 *  Archive Log:
 *  Archive Log: Revision 1.52  2015/06/10 19:58:49  jijunwan
 *  Archive Log: PR 129120 - Some old files have no proper file header. They cannot record change logs.
 *  Archive Log: - wrote a tool to check and insert file header
 *  Archive Log: - applied on backend files
 *  Archive Log:
 *
 *  Overview:
 *
 *  @author: jijunwan
 *
 ******************************************************************************/
package com.intel.stl.ui.main;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.subnet.FabricInfoBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ISectionController;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.event.PageSelectedEvent;
import com.intel.stl.ui.event.TaskStatusEvent;
import com.intel.stl.ui.event.TaskStatusEvent.Status;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.HomeView;
import com.intel.stl.ui.main.view.PerformanceSectionView;
import com.intel.stl.ui.main.view.SummarySectionView;
import com.intel.stl.ui.model.GroupStatistics;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.EventCalculator;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.IStateChangeListener;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.subscriber.EventSubscriber;
import com.intel.stl.ui.publisher.subscriber.ImageInfoSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

/**
 * @author jijunwan
 *
 */
public class HomePage implements IPageController, IStateChangeListener {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    public final static String NAME = STLConstants.K0100_HOME.getValue();

    private TaskScheduler scheduler;

    private ISubnetApi subnetApi;

    private final HomeView view;

    private final List<ISectionController<?>> sections;

    private Context context;

    private SummarySection summary;

    private PerformanceSection performance;

    private GroupStatistics groupStatistics;

    private Task<ImageInfoBean> imageInfoTask;

    private ICallback<ImageInfoBean> imageInfoCallback;

    private final MBassador<IAppEvent> eventBus;

    private ImageInfoSubscriber imageInfoSubscriber;

    private EventSubscriber eventSubscriber;

    private ICallback<StateSummary> stateSummaryCallback;

    private Task<StateSummary> stateSummaryTask;

    private boolean isRefreshing;

    private ImageInfoBean lastImageInfo;

    // refresh after 2 of refresh intervals
    private static final int DELAYED_REFRESH = 2;

    private int refreshDelayCount;

    public HomePage(HomeView view, MBassador<IAppEvent> eventBus) {

        this.view = view;
        this.eventBus = eventBus;

        eventBus.subscribe(this);
        sections = getSections();
        List<JSectionView<?>> sectionViews = new ArrayList<JSectionView<?>>();
        for (ISectionController<?> section : sections) {
            sectionViews.add(section.getView());
        }
        view.installSectionViews(sectionViews);
    }

    /**
     *
     * <i>Description:</i>
     *
     * @param evt
     */
    @Handler(priority = 1)
    protected synchronized void onNodeUpdate(NodeUpdateEvent evt) {
        TaskStatusEvent<NodeUpdateEvent> taskEvent =
                new TaskStatusEvent<NodeUpdateEvent>(this, evt, Status.STARTED);
        eventBus.publish(taskEvent);
        isRefreshing = true;
        try {
            if (scheduler != null) {
                IPerformanceApi perfApi = scheduler.getPerformanceApi();
                ImageInfoBean imgInfo = perfApi.getLatestImageInfo();
                if (imgInfo != null) {
                    processImageInfo(imgInfo);
                }

                // State summary is updated through notice api (look into
                // EventCalculator),so, don't need to be updated for a notice.

                performance.onRefresh(null);
            }
        } finally {
            isRefreshing = false;
            taskEvent = new TaskStatusEvent<NodeUpdateEvent>(this, evt,
                    Status.FINISHED);
            eventBus.publish(taskEvent);
            refreshDelayCount = DELAYED_REFRESH;
        }
    }

    protected List<ISectionController<?>> getSections() {
        List<ISectionController<?>> sections =
                new ArrayList<ISectionController<?>>();

        summary = new SummarySection(new SummarySectionView(), eventBus);
        sections.add(summary);

        performance =
                new PerformanceSection(new PerformanceSectionView(), eventBus);
        performance.setOrigin(new PageSelectedEvent(this, NAME));
        sections.add(performance);

        return sections;
    }

    /**
     * @param context
     *            the context to set
     */
    @Override
    public void setContext(final Context context, IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(2);

        clear();
        this.context = context;
        subnetApi = this.context.getSubnetApi();
        scheduler = this.context.getTaskScheduler();
        imageInfoSubscriber = (ImageInfoSubscriber) scheduler
                .getSubscriber(SubscriberType.IMAGE_INFO);

        imageInfoCallback = new CallbackAdapter<ImageInfoBean>() {
            /*
             * (non-Javadoc)
             *
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(ImageInfoBean result) {
                if (result != null) {
                    ImageInfoBean oldImageInfo = lastImageInfo;
                    processImageInfo(result);
                    if (oldImageInfo != null
                            && oldImageInfo.hasChange(result)) {
                        // has fabric change, do a delayed refresh
                        refreshDelayCount = DELAYED_REFRESH;
                    } else if (refreshDelayCount > 0) {
                        refreshDelayCount -= 1;
                        if (refreshDelayCount == 0) {
                            Util.runInEDT(new Runnable() {
                                @Override
                                public void run() {
                                    ((FabricController) context.getController())
                                            .onRefresh();
                                }
                            });
                        }
                    }
                }
            }
        };
        imageInfoTask =
                imageInfoSubscriber.registerImageInfo(imageInfoCallback);
        if (observer.isCancelled()) {
            clear();
            return;
        }

        eventSubscriber =
                (EventSubscriber) scheduler.getSubscriber(SubscriberType.EVENT);
        stateSummaryCallback = new CallbackAdapter<StateSummary>() {
            /*
             * (non-Javadoc)
             *
             * @see
             * com.intel.hpc.stl.ui.publisher.CallBackAdapter#onDone(java.lang
             * .Object)
             */
            @Override
            public synchronized void onDone(StateSummary result) {
                if (result != null) {
                    onStateChange(result);
                }
            }
        };
        stateSummaryTask =
                eventSubscriber.registerStateSummary(stateSummaryCallback);

        if (observer.isCancelled()) {
            clear();
            return;
        }
        subObservers[0].onFinish();

        summary.setContext(context);
        performance.setContext(context, subObservers[1]);
        subObservers[1].onFinish();

        // Register for state change events with Event Calculator.
        this.context.getEvtCal().addListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.intel.stl.ui.common.IPageController#onRefresh(com.intel.stl.ui.common
     * .IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        isRefreshing = true;
        try {
            IPerformanceApi perfApi = scheduler.getPerformanceApi();
            ImageInfoBean imgInfo = perfApi.getLatestImageInfo();
            imageInfoCallback.onDone(imgInfo);
            if (observer.isCancelled()) {
                return;
            }

            StateSummary ss = context.getEvtCal().getSummary();
            onStateChange(ss);
            if (observer.isCancelled()) {
                return;
            }

            performance.onRefresh(observer);
            observer.onFinish();
        } finally {
            isRefreshing = false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
        for (ISectionController<?> section : sections) {
            section.clear();
        }

        if (scheduler != null) {
            if (imageInfoTask != null) {
                imageInfoSubscriber.deregisterImageInfo(imageInfoTask,
                        imageInfoCallback);
            }

            if (stateSummaryTask != null) {
                eventSubscriber.deregisterStateSummary(stateSummaryTask,
                        stateSummaryCallback);
            }

            if (context != null && context.getEvtCal() != null) {
                context.getEvtCal().removeListener(this);
            }
        }

        synchronized (this) {
            groupStatistics = null;
        }
    }

    protected void processImageInfo(ImageInfoBean imageInfo) {
        EventCalculator evtCal = context.getEvtCal();
        FabricInfoBean fabricInfo = subnetApi.getFabricInfo();
        synchronized (this) {
            if (groupStatistics == null) {
                summary.clear();
            }

            groupStatistics = new GroupStatistics(
                    subnetApi.getConnectionDescription(), imageInfo);

            isRefreshing = isRefreshing || lastImageInfo == null
                    || lastImageInfo.hasChange(imageInfo);
            try {
                groupStatistics.setPortTypesDist(
                        subnetApi.getPortsTypeDist(true, isRefreshing));
            } catch (Exception e) {
                log.error("Couldn't get PortTypesDist!", e);
                // Util.showError(getView(), e);
                e.printStackTrace();
            }

            int msmLid = imageInfo.getSMInfo()[0].getLid();
            SMRecordBean msm = null;
            try {
                msm = subnetApi.getSM(msmLid);
            } catch (Exception e) {
                log.error("Couldn't get SMRecordBean for lid=" + msmLid + "!",
                        e);
            }
            if (msm != null && msm.getSmInfo() != null) {
                groupStatistics.setMsmUptimeInSeconds(
                        msm.getSmInfo().getElapsedTime());
            }

            lastImageInfo = imageInfo;
            isRefreshing = false;
            evtCal.processHealthScoreStats(fabricInfo, imageInfo);
        }
        // System.out.println(imageInfo);
        // System.out.println(groupStatistics);
        summary.updateStatistics(groupStatistics);
    }

    protected void processStateSummary(StateSummary stateSummary) {
        if (groupStatistics == null) {
            return;
        }

        int totalSWs = stateSummary.getBaseTotalSWs();
        if (lastImageInfo != null
                && lastImageInfo.getNumSwitchNodes() > totalSWs) {
            totalSWs = lastImageInfo.getNumSwitchNodes();
        }
        int totalHFIs = stateSummary.getBaseTotalHFIs();
        if (lastImageInfo != null
                && lastImageInfo.getNumHFIPorts() > totalHFIs) {
            totalHFIs = lastImageInfo.getNumHFIPorts();
        }
        summary.updateStates(stateSummary.getSwitchStates(), totalSWs,
                stateSummary.getHfiStates(), totalHFIs);
        summary.updateHealthScore(stateSummary.getHealthScore());
        summary.updateWorstNodes(stateSummary.getWorstNodes());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.hpc.stl.ui.IPage#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.hpc.stl.ui.IPage#getDescription()
     */
    @Override
    public String getDescription() {
        return STLConstants.K0101_HOME_DESCRIPTION.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.hpc.stl.ui.IPage#getView()
     */
    @Override
    public JPanel getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.hpc.stl.ui.IPage#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.IPage#cleanup()
     */
    @Override
    public void cleanup() {
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
    }

    /*
     * (non-Javadoc)
     *
     * @see com.intel.stl.ui.common.IPageController#canExit()
     */
    @Override
    public boolean canExit() {
        return true;
    }

    @Override
    public PageWeight getContextSwitchWeight() {
        return MEDIUM;
    }

    @Override
    public PageWeight getRefreshWeight() {
        return MEDIUM;
    }

    @Override
    public void onStateChange(StateSummary summary) {
        if (summary != null) {
            processStateSummary(summary);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
