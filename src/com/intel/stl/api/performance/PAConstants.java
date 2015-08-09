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
package com.intel.stl.api.performance;

/**
 * @author jijunwan
 *
 */
public interface PAConstants {
	byte STL_PM_CLASS_VERSION					= (byte)0x80; 	/* Performance Management version */

	/* Performance Analysis methods */
	byte STL_PA_CMD_GET                = (byte)0x01;
	byte STL_PA_CMD_GET_RESP           = (byte)0x81;
	byte STL_PA_CMD_GETTABLE           = (byte)0x12;
	byte STL_PA_CMD_GETTABLE_RESP      = (byte)0x92;

	short STL_PM_ATTRIB_ID_PORT_STATUS			= 0x40;
	short STL_PM_ATTRIB_ID_CLEAR_PORT_STATUS	= 0x41;
	short STL_PM_ATTRIB_ID_DATA_PORT_COUNTERS	= 0x42;
	short STL_PM_ATTRIB_ID_ERROR_PORT_COUNTERS	= 0x43;
	short STL_PM_ATTRIB_ID_ERROR_INFO			= 0x44;
	
	/* Performance Analysis attribute IDs */

	short STL_PA_ATTRID_GET_CLASSPORTINFO	 = 0x01;
	short STL_PA_ATTRID_GET_GRP_LIST		 = 0xA0;
	short STL_PA_ATTRID_GET_GRP_INFO		 = 0xA1;
	short STL_PA_ATTRID_GET_GRP_CFG		 	 = 0xA2;
	short STL_PA_ATTRID_GET_PORT_CTRS		 = 0xA3;
	short STL_PA_ATTRID_CLR_PORT_CTRS		 = 0xA4;
	short STL_PA_ATTRID_CLR_ALL_PORT_CTRS	 = 0xA5;
	short STL_PA_ATTRID_GET_PM_CONFIG		 = 0xA6;
	short STL_PA_ATTRID_FREEZE_IMAGE		 = 0xA7;
	short STL_PA_ATTRID_RELEASE_IMAGE		 = 0xA8;
	short STL_PA_ATTRID_RENEW_IMAGE			 = 0xA9;
	short STL_PA_ATTRID_GET_FOCUS_PORTS	 	 = 0xAA;
	short STL_PA_ATTRID_GET_IMAGE_INFO	 	 = 0xAB;
	short STL_PA_ATTRID_MOVE_FREEZE_FRAME	 = 0xAC;
	short STL_PA_ATTRID_GET_VF_LIST      	 = 0xAD;
	short STL_PA_ATTRID_GET_VF_INFO      	 = 0xAE;
	short STL_PA_ATTRID_GET_VF_CONFIG    	 = 0xAF;
	short STL_PA_ATTRID_GET_VF_PORT_CTRS 	 = 0xB0;
	short STL_PA_ATTRID_CLR_VF_PORT_CTRS 	 = 0xB1;
	short STL_PA_ATTRID_GET_VF_FOCUS_PORTS 	 = 0xB2;
	
	int STL_PM_GROUPNAMELEN = 64;
	int STL_PM_VFNAMELEN = 64;
	int STL_PM_UTIL_GRAN_PERCENT = 10;
	int STL_PM_UTIL_BUCKETS = (100 / STL_PM_UTIL_GRAN_PERCENT);
	int PM_ERR_GRAN_PERCENT = 25;
	int PM_ERR_BUCKETS = ((100/PM_ERR_GRAN_PERCENT)+1);
}
