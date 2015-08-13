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
 *  File Name: SetupWizardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.16.2.1  2015/08/12 15:26:49  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2015/04/21 21:18:23  rjtierne
 *  Archive Log:    Added enableReset() to keep up to date with the latest interface
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2015/03/30 15:12:51  rjtierne
 *  Archive Log:    Updated panel backgrounds to use static variable
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2015/02/20 21:16:10  rjtierne
 *  Archive Log:    Multinet Wizard: New instalment of the multinet wizard targeting display of subnet specific data for all sub-wizards; using a unique model for each wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/13 21:31:54  rjtierne
 *  Archive Log:    Multinet Wizard
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/02/09 22:01:33  jijunwan
 *  Archive Log:    apply button will close the wizard as well
 *  Archive Log:    put close task into background
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/06 15:13:15  fernande
 *  Archive Log:    Changes so that the Setup Wizard depends on the Subnet Manager for all subnet-related operations
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/05 15:06:14  jijunwan
 *  Archive Log:    tried to improve stability on multi-subnet support
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/01/20 19:13:13  rjtierne
 *  Archive Log:    Changed onApply() to return a boolean to indicate success/failure
 *  Archive Log:    Removed requirement to test connection before adding it to the DB
 *  Archive Log:    Apply button is no longer greyed out
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/01/15 19:09:34  rjtierne
 *  Archive Log:    Restored original dialog width
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/01/13 19:00:59  rjtierne
 *  Archive Log:    Increased dialog width to accommodate lengthy event wizard fields
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/01/11 21:48:17  jijunwan
 *  Archive Log:    setup wizard improvements
 *  Archive Log:    1) look and feel adjustment
 *  Archive Log:    2) secure FE support
 *  Archive Log:    3) apply wizard on current subnet
 *  Archive Log:    4) message display based on message type rather than directly specifying UI resources
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/01/08 22:16:33  fisherma
 *  Archive Log:    Fixed the configuration wizard window always on top of non-Java windows.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/12/23 19:34:56  rjtierne
 *  Archive Log:    Now accessing firstRun state from controller. On first run, wizard cannot be
 *  Archive Log:    closed using the "X" in the upper right corner of the dialog.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/23 18:35:14  rjtierne
 *  Archive Log:    Improved L&F of step buttons
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/11 20:01:09  rjtierne
 *  Archive Log:    Removed the disabling of Next/Apply buttons to make Wizard functional again
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/10 21:31:03  rjtierne
 *  Archive Log:    New Setup Wizard based on framework
 *  Archive Log:
 *
 *  Overview: Top level Setup Wizard view
 *
 *  @author: jijunwan, rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.wizards.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.IntelButtonUI;
import com.intel.stl.ui.main.view.IFabricView;
import com.intel.stl.ui.wizards.impl.IWizardListener;
import com.intel.stl.ui.wizards.impl.IWizardTask;

public class SetupWizardView extends JDialog implements IWizardView {

    private static final long serialVersionUID = -4412093414075927051L;

    private final int DIALOG_WIDTH = 600;

    private final int DIALOG_HEIGHT = 400;

    private final List<JButton> stepButtons = new ArrayList<JButton>();

    private IWizardListener wizardListener;

    private final IFabricView mainFrame;

    private JPanel viewPanel;

    private JPanel stepPanel;

    private JPanel controlPanel;

    private JPanel wizardControlPanel;

    private JPanel dialogControlPanel;

    private CardLayout viewLayout;

    private CardLayout controlLayout;

    private JButton btnPrevious;

    private JButton btnNext;

    private JButton btnApply;

    private JButton btnWizardReset;

    private JButton btnDialogReset;

    private boolean isNext = true;

    /**
     * 
     * Description: Constructor for the SetWizardView
     * 
     * @param wizardListener
     *            interface to the controller of this view
     * 
     * @param owner
     *            top level window of the application to center this dialog
     *            around
     */
    public SetupWizardView(IFabricView owner) {

        super((JFrame) null, STLConstants.K0667_CONFIG_SETUP_WIZARD.getValue(),
                true);
        this.mainFrame = owner;

        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (wizardListener != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            wizardListener.onClose();
                        }
                    }).start();
                }
            }
        });
    }

    /**
     * 
     * <i>Description: Initializes the GUI components for this view</i>
     * 
     */
    protected void initComponents() {

        // Configure the layout of the content pane
        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new BorderLayout(5, 0));

        contentPanel
                .setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        contentPanel.setBackground(MultinetWizardView.WIZARD_COLOR);

        // Add a panel for the step buttons on the left side
        stepPanel = new JPanel();
        stepPanel.setLayout(new GridBagLayout());
        stepPanel.setPreferredSize(new Dimension(100, DIALOG_HEIGHT));
        stepPanel.setBackground(MultinetWizardView.WIZARD_COLOR);
        contentPanel.add(stepPanel, BorderLayout.WEST);

        // Create a main panel on the right side to hold the view panel and
        // control panel and place it on a scroll pane
        JPanel mainPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Add a panel to the top of the main panel to hold the views
        viewPanel = new JPanel();
        viewLayout = new CardLayout();
        viewPanel.setLayout(viewLayout);
        mainPanel.add(viewPanel, BorderLayout.CENTER);

        // Add a control panel to the bottom of the main panel
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);

        setResizable(false);
        setBackground(MultinetWizardView.WIZARD_COLOR);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pack();
    }

    /**
     * 
     * <i>Description: Center the wizard dialog relative to the main frame</i>
     * 
     */
    private void centerDialog() {
        pack();
        Point dialogLocation = new Point(0, 0);

        dialogLocation.x =
                mainFrame.getScreenPosition().x
                        + mainFrame.getScreenSize().width / 2 - getWidth() / 2;

        dialogLocation.y =
                mainFrame.getScreenPosition().y
                        + mainFrame.getScreenSize().height / 2 - getHeight()
                        / 2;

        setLocation(dialogLocation);
    } // centerDialog

    /**
     * 
     * <i>Description: Creates the control panel to hold either the
     * wizardControlPanel (PREV/NEXT), used for the first run of the
     * application, or the dialogControlPanel (OK/CANCEL) used for subsequent
     * runs of this configuration</i>
     * 
     * @return control panel
     */
    protected JPanel createControlPanel() {

        // Add two control panels; one for initial setup and one for
        // subsequent configurations
        controlPanel = new JPanel();

        controlLayout = new CardLayout();
        controlPanel.setLayout(controlLayout);

        // Create the wizard control panel which will only be used the first
        // time the application is run
        wizardControlPanel = new JPanel();
        wizardControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        wizardControlPanel.setBackground(MultinetWizardView.WIZARD_COLOR);
        btnPrevious =
                ComponentFactory.getIntelActionButton(STLConstants.K0624_BACK
                        .getValue());
        btnPrevious.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Close status panels
                wizardListener.closeStatusPanels();
                wizardListener.onPrevious();
            }
        });
        wizardControlPanel.add(btnPrevious);

        btnNext =
                ComponentFactory.getIntelActionButton(STLConstants.K0622_NEXT
                        .getValue());
        btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Close status panels
                wizardListener.closeStatusPanels();
                wizardListener.onNext();
            }
        });
        wizardControlPanel.add(btnNext);

        btnWizardReset =
                ComponentFactory.getIntelActionButton(STLConstants.K1006_RESET
                        .getValue());
        btnWizardReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close status panels
                wizardListener.closeStatusPanels();
                wizardListener.onReset();
            }
        });
        wizardControlPanel.add(btnWizardReset);

        // Create the dialog control panel which will be used on subsequent
        // configurations
        dialogControlPanel = new JPanel();
        dialogControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialogControlPanel.setBackground(MultinetWizardView.WIZARD_COLOR);

        btnApply =
                ComponentFactory.getIntelActionButton(STLConstants.K0672_APPLY
                        .getValue());
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close status panels
                wizardListener.closeStatusPanels();
                closeWizard();
                wizardListener.onApply();
            }
        });
        dialogControlPanel.add(btnApply);

        btnDialogReset =
                ComponentFactory.getIntelActionButton(STLConstants.K1006_RESET
                        .getValue());
        btnDialogReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close status panels
                wizardListener.closeStatusPanels();
                wizardListener.onReset();
            }
        });
        dialogControlPanel.add(btnDialogReset);

        JButton btnClose =
                ComponentFactory.getIntelCancelButton(STLConstants.K0740_CLOSE
                        .getValue());
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close status panels
                wizardListener.closeStatusPanels();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wizardListener.onClose();
                    }
                }).start();
            }
        });
        dialogControlPanel.add(btnClose);

        // Add the control panels to the card layout
        controlPanel.add(wizardControlPanel,
                STLConstants.K0619_WIZARD.getValue());
        controlPanel.add(dialogControlPanel,
                STLConstants.K0626_DIALOG.getValue());

        return controlPanel;
    } // createControlPanel

    /**
     * @return the mainFrame
     */
    @Override
    public Window getOwner() {
        return (Window) mainFrame;
    }

    /**
     * 
     * <i>Description: Add the tasks view components including the step button
     * and the view</i>
     * 
     * @param task
     */
    @Override
    public void setTasks(List<IWizardTask> tasks) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(25, 10, 5, 10);
        gc.weightx = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridwidth = GridBagConstraints.REMAINDER;

        for (int i = 0; i < tasks.size(); i++) {
            final IWizardTask task = tasks.get(i);
            // On first run, step buttons look more like labels and need to be
            // disabled. The IntelWizardStepButton overrides setEnable() to
            // prevent
            // graying out of the buttons.
            JButton btn =
                    ComponentFactory.getIntelWizardStepButton(task.getName(),
                            new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // Close status panels
                                    wizardListener.closeStatusPanels();
                                    wizardListener.selectStep(task.getName());
                                }
                            });
            Dimension d = btn.getPreferredSize();
            d.height += 2;
            btn.setPreferredSize(d);
            btn.setFont(UIConstants.H4_FONT);
            stepButtons.add(btn);
            gc.insets.top = i == 0 ? 25 : 5;
            stepPanel.add(btn, gc);

            // Add the task view to the panel
            viewPanel.add(task.getView(), task.getName());
        }
        gc.weighty = 1;
        stepPanel.add(Box.createGlue(), gc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ISetupWizardView#showTaskView(java.lang
     * .String)
     */
    @Override
    public void showTaskView(String name) {

        // On first run, highlight the step we are on
        if (wizardListener.isFirstRun()) {
            Iterator<JButton> it = stepButtons.iterator();
            JButton btnCurrentStep = null;

            while (it.hasNext()) {
                btnCurrentStep = it.next();
                if (btnCurrentStep.getText().equals(name)) {
                    btnCurrentStep.setFont(UIConstants.H5_FONT
                            .deriveFont(Font.BOLD));
                } else {
                    this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                }
            }
        }

        viewLayout.show(viewPanel, name);

        centerDialog();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.ISetupWizardView#setWizardListener(com.
     * intel.stl.ui.wizards.impl.IWizardListener)
     */
    @Override
    public void setWizardListener(IWizardListener listener) {

        this.wizardListener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.ISetupWizardView#showWizard(boolean)
     */
    @Override
    public void showWizard(boolean isFirstRun) {

        // Format the step buttons
        for (JButton button : stepButtons) {

            if (isFirstRun) {
                button.setBackground(null);
                button.setFont(UIConstants.H5_FONT);
                button.setForeground(UIConstants.INTEL_DARK_GRAY);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setOpaque(false);
                button.setEnabled(false);

            } else {
                button.setUI(new IntelButtonUI(UIConstants.INTEL_MEDIUM_BLUE,
                        UIConstants.INTEL_MEDIUM_DARK_BLUE));
                button.setBackground(UIConstants.INTEL_BLUE);
                button.setForeground(UIConstants.INTEL_WHITE);
                button.setFont(UIConstants.H5_FONT);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(true);
                button.setMargin(new Insets(2, 14, 2, 14));
                button.setContentAreaFilled(true);
                button.setBorderPainted(true);
                button.setEnabled(true);
            }

            button.revalidate();
        }

        String activePanelName =
                isFirstRun ? STLConstants.K0619_WIZARD.getValue()
                        : STLConstants.K0626_DIALOG.getValue();
        controlLayout.show(controlPanel, activePanelName);

        if (isFirstRun) {
            // First time up, the Subnet step button must be set to BOLD
            stepButtons.get(0).setFont(
                    UIConstants.H5_FONT.deriveFont(Font.BOLD));
        }

        this.setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enablePrevious(boolean)
     */
    @Override
    public void enablePrevious(boolean enable) {

        btnPrevious.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableApply(boolean)
     */
    @Override
    public void enableApply(boolean enable) {

        btnApply.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableNext(boolean)
     */
    @Override
    public void enableNext(boolean enable) {

        btnNext.setEnabled(enable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#closeWizard()
     */
    @Override
    public void closeWizard() {

        this.setVisible(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.wizards.view.IWizardView#updateNextButton(java.lang.
     * String)
     */
    @Override
    public void updateNextButton(String name) {

        btnNext.setText(name);
        isNext = name.equals(STLConstants.K0622_NEXT.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#isNextButton()
     */
    @Override
    public boolean isNextButton() {

        return isNext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableRun(boolean)
     */
    @Override
    public void enableRun(boolean enable) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.wizards.view.IWizardView#enableReset(boolean)
     */
    @Override
    public void enableReset(boolean enable) {
        // TODO Auto-generated method stub

    }

}
