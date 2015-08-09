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
package com.intel.stl.common;

/**
 * @author jijunwan
 * 
 */
public interface Constants {
    int PROTOCAL_VERSION = 0x80;

    byte STL_BASE_VERSION = (byte) 0x80;

    byte IB_BASE_VERSION = 1;

    byte MCLASS_SUBN_ADM = 0x03; /* Subnet Administration class */

    byte MCLASS_PERF = 0x04; /* Performance Management class */

    byte MCLASS_VFI_PM = 0x32; /* PM VFI mclass value */

    int STL_MAD_BLOCK_SIZE = 2048;

    short MAD_STATUS_SUCCESS = 0x0000;

    short MAD_STATUS_BUSY = 0x0001;

    short MAD_STATUS_REDIRECT_REQD = 0x0002;

    short MAD_STATUS_SM_UNAVAILABLE = 0x0100;

    short MAD_STATUS_PM_UNAVAILABLE = 0x0A00;

}
