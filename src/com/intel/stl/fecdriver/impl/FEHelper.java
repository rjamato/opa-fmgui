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
package com.intel.stl.fecdriver.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.MadException;
import com.intel.stl.fecdriver.IConnection;
import com.intel.stl.fecdriver.IConnectionEventListener;
import com.intel.stl.fecdriver.messages.command.FVCommand;
import com.intel.stl.fecdriver.messages.response.FVResponse;

/**
 * @author jijunwan TODO: expose MadException to the higher level rather than
 *         hiding it and returning null
 */
public abstract class FEHelper {
    private static Logger log = LoggerFactory.getLogger(FEHelper.class);

    private static boolean DEBUG = false;

    protected STLConnection conn;

    protected STLStatement statement;

    public FEHelper(IConnection conn) {
        super();
        if (conn == null) {
            throw new IllegalArgumentException("STLConnection cannot be null");
        }
        this.conn = (STLConnection) conn;
        statement = this.conn.createStatement();
    }

    public void setConnectionEventListener(IConnectionEventListener listener) {
        conn.addConnectionEventListener(listener);
    }

    public void removeConnectionEventListener(IConnectionEventListener listener) {
        conn.removeConnectionEventListener(listener);
    }

    public IConnection getConnection() {
        return conn;
    }

    protected <E, F> List<F> getResponses(FVCommand<E, F> cmd) throws Exception {
        List<F> res = null;
        statement.execute(cmd);
        FVResponse<F> response = cmd.getResponse();
        res = response.get();
        Exception e = response.getError();
        if (e != null) {
            if (e instanceof MadException) {
                log.error(e.getMessage());
            } else {
                throw response.getError();
            }
        }

        if (DEBUG && res != null) {
            for (int i = 0; i < res.size(); i++) {
                System.out.println(i + " " + res.get(i));
            }
        }
        return res;
    }

    protected <E, F> F getSingleResponse(FVCommand<E, F> cmd) throws Exception {
        List<F> res = null;
        statement.execute(cmd);
        FVResponse<F> response = cmd.getResponse();
        res = response.get();
        Exception e = response.getError();
        if (e != null) {
            if (e instanceof MadException) {
                log.error(e.getMessage());
            } else {
                throw response.getError();
            }
        }

        if (DEBUG && res != null) {
            for (int i = 0; i < res.size(); i++) {
                System.out.println(i + " " + res.get(i));
            }
        }
        if (res != null && !res.isEmpty()) {
            return res.get(0);
        } else {
            return null;
        }
    }

    public void close() {
        statement.close();
    }
}
