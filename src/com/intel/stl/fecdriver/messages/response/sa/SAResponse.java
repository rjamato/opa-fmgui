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
package com.intel.stl.fecdriver.messages.response.sa;

import java.util.ArrayList;
import java.util.List;

import com.intel.stl.fecdriver.messages.adapter.ComposedDatagram;
import com.intel.stl.fecdriver.messages.adapter.IDatagram;
import com.intel.stl.fecdriver.messages.adapter.sa.SAData;
import com.intel.stl.fecdriver.messages.response.FVResponse;

/**
 * @author jijunwan
 * 
 */
public abstract class SAResponse<V, E extends IDatagram<V>> extends
        FVResponse<V> {
    private static boolean DEBUG = false;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.message.FVResponse#wrap(byte[], int)
     */
    @Override
    protected IDatagram<?> wrap(byte[] bytes, int offset) {
        SAData saData = new SAData();
        int pos = saData.wrap(bytes, offset);
        int recSize = saData.getSaHeader().getAttributeOffset() * 8;
        int numRecs = (isSingleRecord() || recSize == 0) ? 1 : (bytes.length - pos) / recSize;
        IDatagram<?>[] records = new IDatagram<?>[numRecs];
        List<V> results = new ArrayList<V>();
        for (int i = 0; i < numRecs; i++) {
            E record = createRecord();
            record.wrap(bytes, pos);
            records[i] = record;
            results.add(record.toObject());
            pos += recSize;
        }
        setResults(results);
        ComposedDatagram<Void> data = new ComposedDatagram<Void>(records);
        saData.setData(data);
        if (DEBUG) {
            System.out.println("#Rec " + numRecs);
            saData.dump("", System.out);
        }
        return saData;
    }
    
    protected boolean isSingleRecord() {
        return false;
    }

    protected abstract E createRecord();

}
