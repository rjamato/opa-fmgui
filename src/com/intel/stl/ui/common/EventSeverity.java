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
 *  File Name: EventSeverity.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:02:25  rjtierne
 *  Archive Log:    Enumeration describing the severity level of events messages; i.e. INFORMATIONAL	, WARNING, ERROR, and CRITICAL
 *  Archive Log:
 *
 *  Overview: Enumeration to describe the severity level of events messages
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

public enum EventSeverity {
	
	ES0001_SEVERITY_INFORMATIONAL(1),
	ES0002_SEVERITY_WARNING(2),
	ES0003_SEVERITY_ERROR(3),
	ES0004_SEVERITY_CRITICAL(4);	
	
    private final int key;  
	   
    private EventSeverity(int value) {
    	this.key = value;
    }
    
    
    public int getValue() {
    	return key;
    }
    
    
    public String getString() {
    	return name().split("_")[2];
    }
} //enum EventSeverity
