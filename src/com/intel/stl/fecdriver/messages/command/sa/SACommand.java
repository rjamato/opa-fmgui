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
package com.intel.stl.fecdriver.messages.command.sa;

import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.common.Constants;
import com.intel.stl.fecdriver.messages.adapter.CommonMad;
import com.intel.stl.fecdriver.messages.adapter.IDatagram;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.adapter.sa.SAData;
import com.intel.stl.fecdriver.messages.adapter.sa.SAHeader;
import com.intel.stl.fecdriver.messages.command.FVCommand;

/**
 * @author jijunwan
 * 
 */
public abstract class SACommand<V, E extends IDatagram<?>, F> extends
        FVCommand<V, F> {
    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.message.FVCommand#prepareMad()
     */
    @Override
    public RmppMad prepareMad() {
        RmppMad mad = new RmppMad();
        mad.build(true);
        fillCommonMad(mad.getCommonMad());

        SAData saData = new SAData();
        saData.build(true);

        E rec = buildRecord();
        if (rec != null) {
            saData.setData(rec);
        }

        fillInput(saData.getSaHeader(), rec);
        mad.setData(saData);
        return mad;
    }

    protected void fillCommonMad(CommonMad comm) {
        comm.setBaseVersion(Constants.STL_BASE_VERSION);
        comm.setMgmtClass(Constants.MCLASS_SUBN_ADM);
        comm.setClassVersion(SAConstants.STL_SA_CLASS_VERSION);
        comm.setMethod(SAConstants.SUBN_ADM_GETTABLE);
    }

    abstract protected E buildRecord();

    abstract protected void fillInput(SAHeader header, E record);

}
