/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: DeviceSelector.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/05/06 19:39:15  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/05/01 21:29:08  jijunwan
 *  Archive Log:    changed to directly show exception(s)
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 22:08:57  jijunwan
 *  Archive Log:    removed title argument from #showErrorMessage
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/06 11:14:07  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. Open issues fixed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/03 21:06:31  jijunwan
 *  Archive Log:    Introduced canExit to IPageController, and canPageChange to IPageListener to allow us do some checking before we switch to another page. Fixed the following bugs
 *  Archive Log:    1) when we refresh, do not show login dialog if Admin is not the current page
 *  Archive Log:    2) confirm abandon if we switch from admin page to other pages and there is changes on the Admin page
 *  Archive Log:    3) confirm abandon in Admin page if we switch between Application, DeviceGroup and VirtualFabric
 *  Archive Log:    4) added null check to handle special cases
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/03/24 17:46:10  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.impl.devicegroups;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.devicegroups.NodeTypeAttr;
import com.intel.stl.api.subnet.DefaultDeviceGroup;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.ui.admin.view.devicegroups.DevicesPanel;
import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.common.NameSorter;
import com.intel.stl.ui.common.PageWeight;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class DevicesSelector implements IResourceSelector {
    private final DevicesPanel view;

    private ISubnetApi subnetApi;

    private boolean useGUID;

    private DefaultTreeModel treeModel;

    private TreeSelectionModel seletionModel;

    protected Comparator<FVResourceNode> guidComparator =
            new Comparator<FVResourceNode>() {

                @Override
                public int compare(FVResourceNode o1, FVResourceNode o2) {
                    long guid1 = ((DeviceNode) o1).getGuid();
                    long guid2 = ((DeviceNode) o2).getGuid();
                    return Long.compare(guid1, guid2);
                }

            };

    protected Comparator<FVResourceNode> descComparator =
            new Comparator<FVResourceNode>() {

                @Override
                public int compare(FVResourceNode o1, FVResourceNode o2) {
                    String desc1 = o1.getTitle();
                    String desc2 = o2.getTitle();
                    return NameSorter.instance().compare(desc1, desc2);
                }

            };

    /**
     * Description:
     * 
     * @param view
     */
    public DevicesSelector(final DevicesPanel view) {
        super();
        this.view = view;
        view.addOptionsListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                useGUID = ((JCheckBox) e.getSource()).isSelected();
            }

        });
        view.setSelectionModel(getSelectionModel());
    }

    /**
     * <i>Description:</i>
     * 
     * @return
     */
    private TreeSelectionModel getSelectionModel() {
        if (seletionModel == null) {
            seletionModel = new DefaultTreeSelectionModel() {
                private static final long serialVersionUID =
                        -1143512673362584183L;

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * javax.swing.tree.DefaultTreeSelectionModel#setSelectionPaths
                 * (javax.swing.tree.TreePath[])
                 */
                @Override
                public void setSelectionPaths(TreePath[] paths) {
                    TreePath[] validPaths = getValidPaths(paths);
                    super.setSelectionPaths(validPaths);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * javax.swing.tree.DefaultTreeSelectionModel#addSelectionPaths
                 * (javax.swing.tree.TreePath[])
                 */
                @Override
                public void addSelectionPaths(TreePath[] paths) {
                    TreePath[] validPaths = getValidPaths(paths);
                    super.addSelectionPaths(validPaths);
                }

                protected TreePath[] getValidPaths(TreePath[] paths) {
                    List<TreePath> pathList = new ArrayList<TreePath>();
                    for (TreePath path : paths) {
                        DeviceNode dn =
                                (DeviceNode) path.getLastPathComponent();
                        if (!dn.isSelected()) {
                            pathList.add(path);
                        }
                    }
                    return pathList.toArray(new TreePath[0]);
                }

            };
        }
        return seletionModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getDescription()
     */
    @Override
    public String getDescription() {
        return UILabels.STL81050_DG_DEVICES_DESC.getDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getView()
     */
    @Override
    public Component getView() {
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#getIcon()
     */
    @Override
    public ImageIcon getIcon() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#cleanup()
     */
    @Override
    public void cleanup() {
        // not support
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onEnter()
     */
    @Override
    public void onEnter() {
        // not support
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#onExit()
     */
    @Override
    public void onExit() {
        // not support
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IPageController#onRefresh(com.intel.stl.ui.common
     * .IProgressObserver)
     */
    @Override
    public void onRefresh(IProgressObserver observer) {
        // not support
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IPageController#clear()
     */
    @Override
    public void clear() {
        if (treeModel != null) {
            DeviceNode root = (DeviceNode) treeModel.getRoot();
            root.clearSelection();
            view.repaint();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getName()
     */
    @Override
    public String getName() {
        return STLConstants.K2134_DEVICES.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.common.IContextAware#setContext(com.intel.stl.ui.main
     * .Context, com.intel.stl.ui.common.IProgressObserver)
     */
    @Override
    public void setContext(Context context, IProgressObserver observer) {
        subnetApi = context.getSubnetApi();
        SwingWorker<DefaultTreeModel, Void> worker = getInitWorker();
        worker.execute();
    }

    protected SwingWorker<DefaultTreeModel, Void> getInitWorker() {
        SwingWorker<DefaultTreeModel, Void> worker =
                new SwingWorker<DefaultTreeModel, Void>() {

                    @Override
                    protected DefaultTreeModel doInBackground()
                            throws Exception {
                        DeviceNode root = createTree();
                        DefaultTreeModel model = new DefaultTreeModel(root);
                        return model;
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see javax.swing.SwingWorker#done()
                     */
                    @Override
                    protected void done() {
                        try {
                            treeModel = get();
                            view.setTreeModel(treeModel);
                        } catch (InterruptedException e) {
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            Util.showError(view, e);
                        }
                    }

                };
        return worker;
    }

    protected DeviceNode createTree() throws SubnetDataNotFoundException {
        DeviceNode root =
                new DeviceNode(DefaultDeviceGroup.ALL.getName(),
                        TreeNodeType.ALL, 0, 0);
        DeviceNode fi =
                new DeviceNode(NodeTypeAttr.FI.getName(),
                        TreeNodeType.HCA_GROUP, 0, 0);
        DeviceNode sw =
                new DeviceNode(NodeTypeAttr.SW.getName(),
                        TreeNodeType.SWITCH_GROUP, 0, 0);
        fillNodes(fi, sw);
        if (fi.getChildCount() > 0) {
            root.addChild(fi);
        }
        if (sw.getChildCount() > 0) {
            root.addChild(sw);
        }
        return root;
    }

    protected void fillNodes(DeviceNode fi, DeviceNode sw)
            throws SubnetDataNotFoundException {
        Map<Integer, List<DeviceNode>> portNodes = getPorts();
        List<NodeRecordBean> nodes = subnetApi.getNodes(false);
        Map<Long, DeviceNode> swSIMap = new HashMap<Long, DeviceNode>();
        Map<Long, DeviceNode> fiSIMap = new HashMap<Long, DeviceNode>();
        Map<Long, DeviceNode> fiNodeMap = new HashMap<Long, DeviceNode>();
        for (NodeRecordBean node : nodes) {
            if (node.getNodeType() == NodeType.HFI) {
                NodeInfoBean ni = node.getNodeInfo();
                long siGuid = ni.getSysImageGUID();
                DeviceNode siNode = fiSIMap.get(siGuid);
                if (siNode == null) {
                    siNode =
                            new DeviceNode(StringUtils.longHexString(siGuid),
                                    TreeNodeType.SYSTEM_IMAGE, node.getLid(),
                                    siGuid);
                    fi.addChild(siNode);
                    fiSIMap.put(siGuid, siNode);
                }

                long nodeGuid = ni.getNodeGUID();
                DeviceNode fiNode = fiNodeMap.get(nodeGuid);
                if (fiNode == null) {
                    fiNode =
                            new DeviceNode(node.getNodeDesc(),
                                    TreeNodeType.HFI, node.getLid(), nodeGuid);
                    siNode.addChild(fiNode);
                    fiNodeMap.put(nodeGuid, fiNode);
                }
                DeviceNode fiPort =
                        new DeviceNode(Integer.toString(ni.getLocalPortNum()),
                                TreeNodeType.ACTIVE_PORT, ni.getLocalPortNum(),
                                ni.getPortGUID());
                fiNode.addChild(fiPort);
            } else if (node.getNodeType() == NodeType.SWITCH) {
                NodeInfoBean ni = node.getNodeInfo();
                long siGuid = ni.getSysImageGUID();
                DeviceNode siNode = swSIMap.get(siGuid);
                if (siNode == null) {
                    siNode =
                            new DeviceNode(StringUtils.longHexString(siGuid),
                                    TreeNodeType.SYSTEM_IMAGE, node.getLid(),
                                    siGuid);
                    sw.addChild(siNode);
                    swSIMap.put(siGuid, siNode);
                }
                DeviceNode swNode =
                        new DeviceNode(node.getNodeDesc(), TreeNodeType.SWITCH,
                                node.getLid(), ni.getNodeGUID());
                DeviceNode zeroPort =
                        new DeviceNode("0", TreeNodeType.ACTIVE_PORT, 0,
                                ni.getPortGUID());
                swNode.addChild(zeroPort);
                List<DeviceNode> ports = portNodes.get(node.getLid());
                if (ports != null) {
                    for (DeviceNode port : ports) {
                        if (port.getId() != 0) {
                            swNode.addChild(port);
                        }
                    }
                }
                siNode.addChild(swNode);
            }
        }

        sortTree(fi, guidComparator);
        sortTree(sw, guidComparator);
    }

    protected Map<Integer, List<DeviceNode>> getPorts()
            throws SubnetDataNotFoundException {
        Map<Integer, List<DeviceNode>> res =
                new HashMap<Integer, List<DeviceNode>>();
        List<PortRecordBean> ports = subnetApi.getPorts();
        for (PortRecordBean port : ports) {
            int lid = port.getEndPortLID();
            List<DeviceNode> portNodes = res.get(lid);
            if (portNodes == null) {
                portNodes = new ArrayList<DeviceNode>();
                res.put(lid, portNodes);
            }
            DeviceNode node =
                    new DeviceNode(Integer.toString(port.getPortNum()),
                            TreeNodeType.ACTIVE_PORT, port.getPortNum(), 0);
            portNodes.add(node);
        }
        return res;
    }

    protected void sortTree(DeviceNode node,
            Comparator<FVResourceNode> comparator) {
        if (node.getChildCount() > 1) {
            Vector<FVResourceNode> children = node.getChildren();
            Collections.sort(children, comparator);
            for (FVResourceNode child : children) {
                sortTree((DeviceNode) child, comparator);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getContextSwitchWeight()
     */
    @Override
    public PageWeight getContextSwitchWeight() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.IContextAware#getRefreshWeight()
     */
    @Override
    public PageWeight getRefreshWeight() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.IResourceSelector#getSelections
     * ()
     */
    @Override
    public List<IAttribute> getViewSelections() {
        TreePath[] paths = seletionModel.getSelectionPaths();
        DeviceNodesManager dnm =
                new DeviceNodesManager((DeviceNode) treeModel.getRoot());
        List<IAttribute> res = dnm.getAttributes(paths, useGUID);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.IResourceSelector#addSelections
     * (java .lang.Object)
     */
    @Override
    public void setModelSelections(List<? extends IAttribute> attrs) {
        if (treeModel == null) {
            // shouldn't happen
            throw new RuntimeException("TreeModel is null");
        }

        DeviceNodesManager snMgr =
                new DeviceNodesManager((DeviceNode) treeModel.getRoot());
        for (IAttribute attr : attrs) {
            DeviceNode[] nodes = snMgr.getNodes(attr);
            if (nodes != null) {
                for (DeviceNode node : nodes) {
                    node.setSelected(true);
                }
            }
        }
        view.repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.admin.impl.devicegroups.IResourceSelector#itemReleased
     * (java.lang.Object)
     */
    @Override
    public void removeModelSelection(IAttribute attr) {
        if (treeModel == null) {
            // shouldn't happen
            throw new RuntimeException("TreeModel is null");
        }

        DeviceNodesManager snMgr =
                new DeviceNodesManager((DeviceNode) treeModel.getRoot());
        DeviceNode[] nodes = snMgr.getNodes(attr);
        if (nodes != null) {
            for (DeviceNode node : nodes) {
                node.setSelected(false);
            }
        }
        view.repaint();
    }

    @Override
    public void clearViewSelections() {
        seletionModel.clearSelection();
    }
}
