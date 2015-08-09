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
 *  File Name: EventDescription.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/05/08 18:59:21  jijunwan
 *  Archive Log:    added Notice Simulator to simulate notices from FM
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/06 20:24:02  jijunwan
 *  Archive Log:    fixed typo
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/06 15:14:03  jijunwan
 *  Archive Log:    notice and trap implementation
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.notice;

import java.io.Serializable;
import java.util.Date;

import com.intel.stl.api.configuration.EventType;

public class EventDescription implements Serializable {
    private static final long serialVersionUID = -7742471691304907204L;
    private long id;
    private Date date;
    private NoticeSeverity severity;
    private IEventSource source;
    private EventType type;
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the serverity
     */
    public NoticeSeverity getSeverity() {
        return severity;
    }

    /**
     * @param serverity the serverity to set
     */
    public void setSeverity(NoticeSeverity serverity) {
        this.severity = serverity;
    }

    /**
     * @return the source
     */
    public IEventSource getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(IEventSource source) {
        this.source = source;
    }

    /**
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EventDescription [id=" + id + ", date=" + date + ", severity="
                + severity + ", source=" + source + ", type=" + type + "]";
    }

}
