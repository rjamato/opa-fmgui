package com.intel.stl.fecdriver;

import java.util.Properties;

import com.intel.stl.common.Constants;
import com.intel.stl.fecdriver.impl.STLAdapter;

public class FEResources {
    private final static FEResourceAdapter<?> _adapter = STLAdapter.instance();

    public static FEResourceAdapter<?> getUnpooledResourceAdapter(String pHost,
            int pPort, Properties info) {
        int protocol = getProtocolVersion(pHost, pPort, info);
        if (protocol == Constants.PROTOCAL_VERSION)
            return _adapter;
        else
            throw new IllegalArgumentException("Unsupported protocol "
                    + protocol);
    }

    public static FEResourceAdapter<?> getUnpooledResourceAdapter(String pHost,
            int pPort, String user, String password) {
        int protocol = getProtocolVersion(pHost, pPort, user, password);
        if (protocol == Constants.PROTOCAL_VERSION)
            return _adapter;
        else
            throw new IllegalArgumentException("Unsupported protocol "
                    + protocol);
    }

    public static FEResourceAdapter<?> getUnpooledResourceAdapter(int protocol) {
        if (protocol == Constants.PROTOCAL_VERSION)
            return _adapter;
        else
            throw new IllegalArgumentException("Unsupported protocol "
                    + protocol);
    }

    public static FEResourceAdapter<?> getPooledResourceAdapter(
            FEResourceAdapter<?> adapter) {
        // TODO: create pooled adapter from a unpooled adapter
        return adapter;
    }

    private static int getProtocolVersion(String pHost, int pPort,
            Properties info) {
        // TODO: right now we can not get supported protocol version from FE.
        // Should do it later when FE is ready.
        return Constants.PROTOCAL_VERSION;
    }

    private static int getProtocolVersion(String pHost, int pPort, String user,
            String password) {
        // TODO: right now we can not get supported protocol version from FE.
        // Should do it later when FE is ready.
        return Constants.PROTOCAL_VERSION;
    }
}
