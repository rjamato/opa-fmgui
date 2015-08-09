/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
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
 *  File Name: FVBaseController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7  2015/02/24 14:45:47  fernande
 *  Archive Log:    Made the eventBus not final because there is no reset the bus to reuse the controller.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/10/22 02:34:47  jijunwan
 *  Archive Log:    removed debug print out
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/22 01:15:24  jijunwan
 *  Archive Log:    some simplifications on MVC framework
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/09/15 15:24:28  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/14 17:38:11  fernande
 *  Archive Log:    Closing the gap on device properties being displayed.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/22 21:48:13  fernande
 *  Archive Log:    Changes to framework to support model update lifecycle by notifying views of changes
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 13:31:46  fernande
 *  Archive Log:    Moving MVC framework to its own package and renaming for consistency
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/08 20:27:15  fernande
 *  Archive Log:    Basic MVC framework with SwingWorker support
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.framework;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.main.Context;

/**
 *
 */
public abstract class AbstractController<M extends AbstractModel, V extends AbstractView<M, C>, C extends AbstractController<M, V, C>>
        implements IController {

    private final List<IModelListener<M>> listeners;

    protected final M model;

    protected final V view;

    protected MBassador<IAppEvent> eventBus;

    private Context context;

    public AbstractController(M model, V view, MBassador<IAppEvent> eventBus) {
        this.listeners = new ArrayList<IModelListener<M>>();
        this.model = model;
        this.view = view;
        this.eventBus = eventBus;
        view.setController(asController());
        view.initView();
        initModel();
        addModelListener(view);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public abstract void initModel();

    @Override
    public void submitTask(ITask task) {
        task.execute(this);
    }

    /**
     * Description: fires an event through the event bus
     * 
     * @param event
     *            the event being fired
     */
    protected void fireEvent(AbstractEvent event) {
        eventBus.publish(event);
    }

    @Override
    public void onTaskSuccess() {
        notifyModelChanged();
    }

    @Override
    public void onTaskFailure(Throwable caught) {
        notifyModelUpdateFailed(caught);
    }

    @SuppressWarnings("unchecked")
    public C asController() {
        checkController();
        return (C) this;
    }

    public void addModelListener(IModelListener<M> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            notifyModelChanged(listener);
        }
    }

    public boolean removeModelListener(IModelListener<M> listener) {
        return listeners.remove(listener);
    }

    @Override
    public void notifyModelChanged() {
        for (IModelListener<M> listener : listeners) {
            notifyModelChanged(listener);
        }
    }

    @Override
    public void notifyModelUpdateFailed(Throwable caught) {
        for (IModelListener<M> listener : listeners) {
            notifyModelUpdateFailed(listener, caught);
        }
    }

    protected void notifyModelChanged(IModelListener<M> listener) {
        listener.modelChanged(model);
    }

    protected void notifyModelUpdateFailed(IModelListener<M> listener,
            Throwable caught) {
        listener.modelUpdateFailed(model, caught);
    }

    /*
     * This routine traverses the class hierarchy for the controller and makes
     * sure that the controller being used is a proper instance or extends from
     * the declared controller type in the MVC definition (the 'C' in the
     * generics definition)
     * 
     * We need to traverse the hierarchy because you can extend a parameterized
     * controller and the resulting controller should still be valid (although
     * as a controller, the view would be limited to use only the public
     * interface of the original controller.
     * 
     * The IllegalArgumentException should never happen in production settings.
     * It's just to protect the MVC framework from a developer trying to use a
     * controller that does not belong to the MVC definition. At runtime, you
     * would get a CastException if a view ever tries to use the controller
     * (even if it has the same public interface), so technically we can remove
     * this check for efficiency if desired. Just make sure you remove the unit
     * tests that check the conditions above.
     */
    private void checkController() {
        Type type = getClass();
        while (!AbstractController.class.equals(getClass(type))) {
            if (type instanceof Class) {
                type = ((Class<?>) type).getGenericSuperclass();
                if (type == null) {
                    break;
                }
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                if (!rawType.equals(AbstractController.class)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }
        if (type != null && type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (rawType.equals(AbstractController.class)) {
                Class<?> expectedController =
                        (Class<?>) parameterizedType.getActualTypeArguments()[2];
                if (!expectedController.isInstance(this)) {
                    throw new IllegalArgumentException(
                            "This class does not correspond to the declared Controller type: "
                                    + expectedController.getSimpleName());
                }
            }
        }
    }

    private Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else {
            return null;
        }
    }
}
