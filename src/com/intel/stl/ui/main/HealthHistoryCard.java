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
 *  File Name: HealthHistoryCard.java
 *
 *  Archive Source: 
 *
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.Timer;

import net.engio.mbassy.bus.MBassador;

import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.util.Log;

import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.HealthHistoryView;
import com.intel.stl.ui.model.TimedScore;
import com.intel.stl.ui.publisher.IRefreshRateListener;

/**
 * @author jijunwan
 * 
 */
public class HealthHistoryCard extends
        BaseCardController<ICardListener, HealthHistoryView> implements
        IRefreshRateListener {
    private final TimePeriodValues dataset;

    private Date lastTime = null;

    private Timer periodicUpdateTimer = null;

    private int refreshRate = 0;

    private Context context = null;

    private int maxDatasetCount = 0;

    private final double INIT_SCORE = 100.0;

    private double lastScore = INIT_SCORE;

    private final long INIT_DELAY = 250;

    private final int INIT_REPS = 4;

    /**
     * the time difference between GUI client time and FM time
     */
    private long timeAdjustment = 0;

    // 21600 seconds in six hours.
    private final int SIX_HR_WINDOW_SECONDS = 6 * 60 * 60;

    public HealthHistoryCard(HealthHistoryView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        dataset =
                new TimePeriodValues(
                        STLConstants.K0105_HEALTH_HISTORY.getValue());
        TimePeriodValuesCollection timeseriescollection =
                new TimePeriodValuesCollection(dataset);
        timeseriescollection.setXPosition(TimePeriodAnchor.START);
        view.setDataset(timeseriescollection);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getHealthTrend(), helpAction.getHelpSet());
    }

    public void updateContext(Context context) {
        this.context = context;
        setPeriodicUpdate();
        initializeCard();
    }

    /**
     * @return the dataset
     */
    public TimePeriodValues getDataset() {
        return dataset;
    }

    @Override
    public void clear() {
        // Exception e = new Exception();
        // e.printStackTrace();

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                lastTime = null;
                if (dataset.getItemCount() > 0) {
                    dataset.delete(0, dataset.getItemCount() - 1);
                }
                view.setCurrentScore(
                        STLConstants.K0039_NOT_AVAILABLE.getValue(),
                        UIConstants.INTEL_DARK_GRAY);
            }
        });
    }

    /**
     * @param score
     * @param time
     * 
     *            Health score can be refreshed periodically by a timer or
     *            updated asynchronously by an external event.
     */
    public void updateHealthScore(final double score, final Date time) {

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {

                lastScore = score;
                timeAdjustment = System.currentTimeMillis() - time.getTime();
                if (lastTime == null) {
                    lastTime = time;
                    dataset.add(new SimpleTimePeriod(lastTime, lastTime),
                            Double.valueOf(score));
                }
                if (dataset.getItemCount() == maxDatasetCount) {
                    dataset.delete(0, 0);
                }

                if (lastTime.after(time)) {
                    Log.warn("time mismatch happend lastTime=" + lastTime
                            + " current time=" + time + " timeAdjustment="
                            + timeAdjustment);
                    dataset.add(new SimpleTimePeriod(lastTime, lastTime),
                            Double.valueOf(score));
                } else {
                    dataset.add(new SimpleTimePeriod(lastTime, time),
                            Double.valueOf(score));
                    lastTime = time;
                }

                long startTime = dataset.getTimePeriod(0).getStart().getTime();
                long endTime =
                        dataset.getTimePeriod(dataset.getItemCount() - 1)
                                .getEnd().getTime();

                String start =
                        DateFormat.getInstance().format(new Date(startTime));
                String end = DateFormat.getInstance().format(new Date(endTime));

                view.setTimeDuration(start, end);
                TimedScore tScore = new TimedScore(time.getTime(), score);
                view.setCurrentScore(tScore.getScoreString(), tScore.getColor());
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public ICardListener getCardListener() {
        return this;
    }

    protected void startPeriodicTimer(int rate) {
        periodicUpdateTimer = new Timer(rate * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (periodicUpdateTimer != null) {
                    updateHealthScore(lastScore,
                            new Date(System.currentTimeMillis()));
                }
            }
        });
        periodicUpdateTimer.setRepeats(true);
        periodicUpdateTimer.start();
    }

    protected void cleanUpPeriodicUpdateTimer() {
        if (context != null) {
            context.getTaskScheduler().removeListener(this);
        }
        if (periodicUpdateTimer != null) {
            if (periodicUpdateTimer.isRunning()) {
                periodicUpdateTimer.stop();
            }
            periodicUpdateTimer = null;
        }
    }

    @Override
    public void onRefreshRateChange(int newRate) {
        refreshRate = newRate;
        setPeriodicUpdate();
    }

    @Override
    public String toString() {
        return "HealthHistoryCard ";
    }

    protected void setPeriodicUpdate() {
        cleanUpPeriodicUpdateTimer();
        context.getTaskScheduler().addListener(this);
        refreshRate = context.getTaskScheduler().getRefreshRate();

        // dataset is sized according to the latest refresh rate to
        // hold six hours of data.
        if (refreshRate > 0) {
            maxDatasetCount = SIX_HR_WINDOW_SECONDS / refreshRate;

        } else {
            maxDatasetCount = 10;
        }

        // Start periodic timer to refresh health score in case there are
        // no asynchronous events reporting changes to the score.
        startPeriodicTimer(refreshRate);
    }

    // Initialize to score of 100.
    protected void initializeCard() {
        for (int i = 0; i < INIT_REPS; i++) {
            updateHealthScore(INIT_SCORE, new Date(System.currentTimeMillis()));
            try {
                Thread.sleep(INIT_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
