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
 *  File Name: PropertyCategoryPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4.2.1  2015/08/12 15:27:09  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/28 17:35:27  jijunwan
 *  Archive Log:    chassis viewer is using http rather than https
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 21:33:36  jijunwan
 *  Archive Log:    support ipv4 and ipv6 and creating links for ip addresses
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 03:21:17  jijunwan
 *  Archive Log:    only try url link when the address is valid
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:15:33  jijunwan
 *  Archive Log:    1) abstracted property related panels to general panels that can be reused at other places
 *  Archive Log:    2) introduced renderer into property panels to allow customizes property render
 *  Archive Log:    3) generalized property style to be able to apply on any ui component
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/10/13 21:06:47  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/09 13:04:36  fernande
 *  Archive Log:    Adding IContextAware interface to generalize setting up Context
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/09/04 19:56:45  jijunwan
 *  Archive Log:    minor L&F adjustments on property viz
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/09/04 16:54:25  jijunwan
 *  Archive Log:    added code to support changing property viz style through UI
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/08/22 16:52:29  fernande
 *  Archive Log:    Closing the gaps between properties and sa_query
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/14 17:37:51  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/04 21:17:34  fernande
 *  Archive Log:    Changed to adjust to new DeviceProperties model
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/25 20:28:04  fernande
 *  Archive Log:    New property views
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import java.awt.Component;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.configuration.PropertyCategoryController;
import com.intel.stl.ui.framework.AbstractView;
import com.intel.stl.ui.model.DeviceProperty;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.PropertyItem;

public class DevicePropertyCategoryPanel extends
        AbstractView<DevicePropertyCategory, PropertyCategoryController> {

    private static final long serialVersionUID = 1L;

    protected PropertyCategoryPanel propsPanel;

    protected PropertyVizStyle style;

    public DevicePropertyCategoryPanel() {
        this(new PropertyVizStyle());
    }

    /**
     * Description:
     * 
     * @param style
     */
    public DevicePropertyCategoryPanel(PropertyVizStyle style) {
        super();
        this.style = style;
    }

    @Override
    public void modelUpdateFailed(DevicePropertyCategory model, Throwable caught) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.framework.AbstractView#modelChanged(com.intel.stl.ui
     * .framework.IModel)
     */
    @Override
    public void modelChanged(DevicePropertyCategory model) {
        propsPanel.setModel(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.framework.AbstractView#getMainComponent()
     */
    @Override
    public JComponent getMainComponent() {
        if (propsPanel == null) {
            propsPanel = new PropertyCategoryPanel(style);
            propsPanel.setPropertyRenderer(new DefaultPropertyRenderer() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * com.intel.stl.ui.configuration.view.DefaultPropertyRenderer
                 * #getValueComponent(com.intel.stl.ui.model.PropertyItem, int,
                 * int, com.intel.stl.ui.configuration.view.PropertyVizStyle)
                 */
                @Override
                public Component getValueComponent(PropertyItem<?> item,
                        int itemIndex, int row, PropertyVizStyle style) {
                    Object key = item.getKey();
                    if (key instanceof DeviceProperty) {
                        DeviceProperty dp = (DeviceProperty) key;
                        URI uri = null;
                        String address = item.getValue();
                        try {
                            if (dp == DeviceProperty.IP_ADDR_IPV6) {
                                uri = getIPv6URI(address);
                            } else if (dp == DeviceProperty.IP_ADDR_IPV4
                                    || dp == DeviceProperty.IPCHASSIS_NAME) {
                                uri = getIPv4URI(address);
                            }
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        if (uri != null) {
                            AbstractAction action =
                                    HyperlinkAction.createHyperlinkAction(uri);
                            action.putValue(Action.NAME, address);
                            JXHyperlink link = new JXHyperlink(action);
                            link.setUnclickedColor(UIConstants.INTEL_BLUE);
                            link.setToolTipText(uri.toString());
                            link.setBorderPainted(true);
                            link.setBorder(BorderFactory.createEmptyBorder(3,
                                    3, 3, 2));
                            style.decorateValue(link, itemIndex);
                            return link;
                        }
                    }
                    return super.getValueComponent(item, itemIndex, row, style);
                }

            });
        }
        return propsPanel;
    }

    protected URI getIPv6URI(String address) throws URISyntaxException {
        URI uri = null;
        if (address != null && address.length() > 2
                && address.indexOf(':') >= 0) {
            uri = new URI("http://[" + address + "]");
        }
        return uri;
    }

    protected URI getIPv4URI(String address) throws URISyntaxException {
        URI uri = null;
        if (address != null && address.indexOf('.') > 0) {
            uri = new URI("http://" + address);
        }
        return uri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.framework.AbstractView#initComponents()
     */
    @Override
    public void initComponents() {
    }

}
