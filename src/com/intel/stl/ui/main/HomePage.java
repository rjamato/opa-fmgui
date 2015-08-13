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
package com.intel.stl.ui.main;

import static com.intel.stl.ui.common.PageWeight.MEDIUM;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SMRecordBean;
import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.ISectionController;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.JSectionView;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.event.TaskStatusEvent;
import com.intel.stl.ui.event.TaskStatusEvent.Status;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.HomeView;
import com.intel.stl.ui.main.view.PerformanceSectionView;
import com.intel.stl.ui.main.view.SummarySectionView;
import com.intel.stl.ui.model.GroupStatistics;
import com.intel.stl.ui.model.StateSummary;
import com.intel.stl.ui.publisher.CallbackAdapter;
import com.intel.stl.ui.publisher.ICallback;
import com.intel.stl.ui.publisher.IStateChangeListener;
import com.intel.stl.ui.publisher.Task;
import com.intel.stl.ui.publisher.TaskScheduler;
import com.intel.stl.ui.publisher.subscriber.ImageInfoSubscriber;
import com.intel.stl.ui.publisher.subscriber.SubscriberType;

/**
 * @author jijunwan
 * 
 */
public class HomePage implements IPageController, IStateChangeListener {
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

    private boolean isNewContext;

    private ImageInfoSubscriber imageInfoSubscriber;

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
        isNewContext = false;
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
        try {
            if (scheduler != null) {
                IPerformanceApi perfApi = scheduler.getPerformanceApi();
                ImageInfoBean imgInfo = perfApi.getLatestImageInfo();
                processImageInfo(imgInfo);

                // State summary is updated through notice api (look into
                // EventCalculator),so, don't need to be updated for a notice.

                performance.onRefresh(null);
            }
        } finally {
            taskEvent =
                    new TaskStatusEvent<NodeUpdateEvent>(this, evt,
                            Status.FINISHED);
            eventBus.publish(taskEvent);
        }
    }

    protected List<ISectionController<?>> getSections() {
        List<ISectionController<?>> sections =
                new ArrayList<ISectionController<?>>();

        summary = new SummarySection(new SummarySectionView(), eventBus);
        sections.add(summary);

        performance =
                new PerformanceSection(new PerformanceSectionView(), eventBus);
        sections.add(performance);

        return sections;
    }

    /**
     * @param context
     *            the context to set
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        IProgressObserver[] subObservers = observer.createSubObservers(2);

        clear();
        this.context = context;
        subnetApi = this.context.getSubnetApi();
        scheduler = this.context.getTaskScheduler();
        imageInfoSubscriber =
                (ImageInfoSubscriber) scheduler
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
                    processImageInfo(result);
                }
            }
        };
        imageInfoTask =
                imageInfoSubscriber.registerImageInfo(imageInfoCallback);
        if (observer.isCancelled()) {
            clear();
            return;
        }

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
        isNewContext = true;
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

            if (context != null) {
                if (context.getEvtCal() != null) {
                    context.getEvtCal().removeListener(this);
                }
            }
        }

        synchronized (this) {
            groupStatistics = null;
        }
    }

    protected void processImageInfo(ImageInfoBean imageInfo) {
        synchronized (this) {
            if (groupStatistics == null) {
                summary.clear();
            }
            groupStatistics =
                    new GroupStatistics(subnetApi.getConnectionDescription(),
                            imageInfo);
            try {
                groupStatistics.setNodeTypesDist(subnetApi
                        .getNodesTypeDist(false));
            } catch (Exception e) {
                Util.showError(getView(), e);
                e.printStackTrace();
            }
            try {
                groupStatistics.setPortTypesDist(subnetApi
                        .getPortsTypeDist(true));
            } catch (Exception e) {
                Util.showError(getView(), e);
                e.printStackTrace();
            }
            int msmLid = imageInfo.getSMInfo()[0].getLid();
            SMRecordBean msm = subnetApi.getSM(msmLid);
            if (msm != null && msm.getSmInfo() != null) {
                groupStatistics.setMsmUptimeInSeconds(msm.getSmInfo()
                        .getElapsedTime());
            }
        }
        summary.updateStatistics(groupStatistics);
        if (isNewContext == true) {
            isNewContext = false;
            StateSummary summary = context.getEvtCal().getSummary();
            onStateChange(summary);
        }
    }

    protected void processStateSummary(StateSummary stateSummary) {
        if (groupStatistics == null) {
            return;
        }

        int totalSWs = 0;
        int totalCAs = 0;
        synchronized (this) {
            EnumMap<NodeType, Integer> typeDist =
                    groupStatistics.getNodeTypesDist();
            totalSWs =
                    typeDist.containsKey(NodeType.SWITCH) ? typeDist
                            .get(NodeType.SWITCH) : 0;
            totalCAs =
                    typeDist.containsKey(NodeType.HFI) ? typeDist
                            .get(NodeType.HFI) : 0;
        }
        summary.updateStates(stateSummary.getSwitchStates(), totalSWs,
                stateSummary.getHfiStates(), totalCAs);
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
        return STLConstants.K0100_HOME.getValue();
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
        return "HomePage";
    }
}
