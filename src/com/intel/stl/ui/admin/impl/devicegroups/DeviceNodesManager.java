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
 *  File Name: DeviceNodeNavigator.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.management.IAttribute;
import com.intel.stl.api.management.devicegroups.NodeDesc;
import com.intel.stl.api.management.devicegroups.NodeGUID;
import com.intel.stl.api.management.devicegroups.NodeTypeAttr;
import com.intel.stl.api.management.devicegroups.PortGUID;
import com.intel.stl.api.management.devicegroups.SystemImageGUID;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class DeviceNodesManager {
    private final static Logger log = LoggerFactory
            .getLogger(DeviceNodesManager.class);

    private final DeviceNode root;

    /**
     * Description:
     * 
     * @param root
     */
    public DeviceNodesManager(DeviceNode root) {
        super();
        this.root = root;
    }

    public DeviceNode[] getNodes(IAttribute attr) {
        if (attr instanceof NodeTypeAttr) {
            try {
                DeviceNode node = getNodeType((NodeTypeAttr) attr);
                return new DeviceNode[] { node };
            } catch (Exception e) {
                log.warn("Couldn't find DeviceNode", e);
            }
        } else if (attr instanceof SystemImageGUID) {
            try {
                DeviceNode node =
                        getSystemImage(((SystemImageGUID) attr).getGuid());
                return new DeviceNode[] { node };
            } catch (Exception e) {
                log.warn("Couldn't find DeviceNode", e);
            }
        } else if (attr instanceof NodeGUID) {
            try {
                DeviceNode node = getNode(((NodeGUID) attr).getGuid());
                return new DeviceNode[] { node };
            } catch (Exception e) {
                log.warn("Couldn't find DeviceNode", e);
            }
        } else if (attr instanceof PortGUID) {
            try {
                DeviceNode node = getPort(((PortGUID) attr).getGuid());
                return new DeviceNode[] { node };
            } catch (Exception e) {
                log.warn("Couldn't find DeviceNode", e);
            }
        } else if (attr instanceof NodeDesc) {
            String desc = ((NodeDesc) attr).getValue();
            int pos = desc.indexOf(":");
            if (pos == -1) {
                try {
                    List<DeviceNode> nodes = getNodes(desc);
                    return nodes.toArray(new DeviceNode[0]);
                } catch (Exception e) {
                    log.warn("Couldn't find DeviceNode", e);
                }
            } else {
                try {
                    List<DeviceNode> nodes = getPorts(desc);
                    return nodes.toArray(new DeviceNode[0]);
                } catch (Exception e) {
                    log.warn("Couldn't find DeviceNode", e);
                }
            }
        }
        return null;
    }

    protected DeviceNode getNodeType(NodeTypeAttr type) {
        for (FVResourceNode child : root.getChildren()) {
            if (child.getTitle().equals(type.getName())) {
                return (DeviceNode) child;
            }
        }
        throw new IllegalArgumentException("Couldn't find DeviceNode " + type);
    }

    protected DeviceNode getSystemImage(long guid) {
        DeviceNode res = getSystemImage(root, guid);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find SystemImage with SystemImageGUID=" + guid);
        }
    }

    private DeviceNode getSystemImage(DeviceNode node, long guid) {
        if (node.getType() == TreeNodeType.SYSTEM_IMAGE) {
            if (node.getGuid() == guid) {
                return node;
            } else {
                return null;
            }
        } else {
            for (FVResourceNode child : node.getChildren()) {
                DeviceNode si = getSystemImage((DeviceNode) child, guid);
                if (si != null) {
                    return si;
                }
            }
        }
        return null;
    }

    protected DeviceNode getNode(long guid) {
        DeviceNode res = getNode(root, guid);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find Node with NodeGUID=" + guid);
        }
    }

    private DeviceNode getNode(DeviceNode node, long guid) {
        if (node.isNode()) {
            if (node.getGuid() == guid) {
                return node;
            } else {
                return null;
            }
        } else {
            for (FVResourceNode child : node.getChildren()) {
                DeviceNode res = getNode((DeviceNode) child, guid);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    // exact match
    protected List<DeviceNode> getNodes(String desc) {
        DescMatcher matcher = new DescMatcher(desc);
        List<DeviceNode> res = new ArrayList<DeviceNode>();
        getNodes(root, matcher, res);
        if (!res.isEmpty()) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find Nodes with description '" + desc + "'");
        }
    }

    private void getNodes(DeviceNode node, DescMatcher matcher,
            List<DeviceNode> res) {
        if (node.isNode()) {
            if (matcher.match(node.getTitle())) {
                res.add(node);
            }
        } else {
            for (FVResourceNode child : node.getChildren()) {
                getNodes((DeviceNode) child, matcher, res);
            }
        }
    }

    protected DeviceNode getPort(long guid) {
        DeviceNode res = getPort(root, guid);
        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find Port with PortGUID=" + guid);
        }
    }

    private DeviceNode getPort(DeviceNode node, long guid) {
        if (node.getType() == TreeNodeType.ACTIVE_PORT) {
            if (node.getGuid() == guid) {
                return node;
            } else {
                return null;
            }
        } else {
            for (FVResourceNode child : node.getChildren()) {
                DeviceNode res = getPort((DeviceNode) child, guid);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    protected List<DeviceNode> getPorts(String desc) {
        String[] segs = desc.split(":");
        if (segs.length != 2) {
            throw new IllegalArgumentException("Unsupported string format '"
                    + desc + "'");
        }
        List<DeviceNode> res = new ArrayList<DeviceNode>();
        PortMatcher matcher = new PortMatcher(segs[1]);
        List<DeviceNode> nodes = getNodes(segs[0]);
        for (DeviceNode node : nodes) {
            for (FVResourceNode port : node.getChildren()) {
                int portNum = port.getId();
                if (matcher.match(portNum)) {
                    res.add((DeviceNode) port);
                }
            }
        }

        if (!res.isEmpty()) {
            return res;
        } else {
            throw new IllegalArgumentException(
                    "Couldn't find Ports with description '" + desc + "'");
        }
    }

    public IAttribute getAttribute(TreePath path, boolean useGUID) {
        IAttribute res = null;
        DeviceNode dn = (DeviceNode) path.getLastPathComponent();
        if (dn.getType() == TreeNodeType.HCA_GROUP) {
            res = NodeTypeAttr.FI;
        } else if (dn.getType() == TreeNodeType.SWITCH_GROUP) {
            res = NodeTypeAttr.SW;
        } else if (dn.getType() == TreeNodeType.SYSTEM_IMAGE) {
            res = new SystemImageGUID(dn.getGuid());
        } else if (dn.getType() == TreeNodeType.HFI
                || dn.getType() == TreeNodeType.SWITCH) {
            if (useGUID) {
                res = new NodeGUID(dn.getGuid());
            } else {
                res = new NodeDesc(dn.getTitle());
            }
        } else if (dn.getType() == TreeNodeType.ACTIVE_PORT) {
            if (useGUID && dn.getGuid() > 0) {
                res = new PortGUID(dn.getGuid());
            } else {
                String nodeDesc = dn.getParent().getTitle();
                int portNum = dn.getId();
                res =
                        new NodeDesc(nodeDesc + ":[" + portNum + "-" + portNum
                                + "]");
            }
        } else {
            throw new IllegalArgumentException("Unsupported TreeNodeType "
                    + dn.getType());
        }
        return res;
    }

    public List<IAttribute> getAttributes(TreePath[] paths, boolean useGUID) {
        List<IAttribute> res = new ArrayList<IAttribute>();
        for (TreePath path : paths) {
            res.add(getAttribute(path, useGUID));
        }

        // merge range
        Map<String, List<Point>> nodeRanges =
                new HashMap<String, List<Point>>();
        for (int i = res.size() - 1; i >= 0; i--) {
            IAttribute attr = res.get(i);
            if (attr instanceof NodeDesc) {
                String desc = ((NodeDesc) attr).getValue();
                String[] segs = desc.split(":");
                if (segs.length == 2) {
                    res.remove(i);
                    String nodeName = segs[0];
                    List<Point> ranges = nodeRanges.get(nodeName);
                    if (ranges == null) {
                        ranges = new ArrayList<Point>();
                        nodeRanges.put(nodeName, ranges);
                    }

                    PortMatcher pm = new PortMatcher(segs[1]);
                    List<Point> newRanges = pm.getRanges();
                    for (Point newRange : newRanges) {
                        addRange(ranges, newRange);
                    }
                }
            }
        }

        // add new ranges back
        for (String nodeName : nodeRanges.keySet()) {
            List<Point> ranges = nodeRanges.get(nodeName);
            Collections.sort(ranges, new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    if (o1.x > o2.x) {
                        return 1;
                    } else if (o1.x < o2.x) {
                        return -1;
                    }
                    return o1.y > o2.y ? 1 : (o1.y < o2.y ? -1 : 0);
                }
            });
            for (Point range : ranges) {
                NodeDesc nd =
                        new NodeDesc(nodeName + ":[" + range.x + "-" + range.y
                                + "]");
                res.add(nd);
            }
        }
        return res;
    }

    protected void addRange(List<Point> src, Point toAdd) {
        boolean merged = false;
        for (Point point : src) {
            if (isConnect(point, toAdd.x) || isConnect(point, toAdd.y)) {
                point.x = Math.min(point.x, toAdd.x);
                point.y = Math.max(point.y, toAdd.y);
                merged = true;
                toAdd = point;
            }
        }
        if (!merged) {
            src.add(toAdd);
        }
    }

    protected boolean isConnect(Point range, int val) {
        return val >= range.x - 1 && val <= range.y + 1;
    }
}
