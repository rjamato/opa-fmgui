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
package com.intel.stl.fecdriver.messages.adapter;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author jijunwan
 *
 */
public class SimpleDatagram<E> implements IDatagram<E> {
	protected int length;
	protected ByteBuffer buffer;
	
	public SimpleDatagram(int length) {
		this.length = length;
	}
	
	public int build(boolean force) {
		return build(force, ByteOrder.BIG_ENDIAN);
	}
	
	public int build(boolean force, ByteOrder order) {
		if (buffer==null || force) {
			buffer = ByteBuffer.allocate(length);
			buffer.order(order);
			initData();
			return buffer.capacity();
		} else
			return 0;
	}
	
	protected void initData() {
		buffer.clear();
		buffer.put(new byte[buffer.capacity()]);
	}
	
	public int wrap(byte[] data, int offset) {
		return wrap(data, offset, ByteOrder.BIG_ENDIAN);
	}
	
	public int wrap(byte[] data, int offset, ByteOrder order) {
		buffer = ByteBuffer.wrap(data, offset, length).slice();
		buffer.order(order);
		return offset+length;
	}
	
	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#hasBuffer()
	 */
	@Override
	public boolean hasBuffer() {
		return buffer!=null;
	}

	public ByteBuffer getByteBuffer() {
		return buffer;
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#getByteBuffers()
	 */
	@Override
	public ByteBuffer[] getByteBuffers() {
		if (buffer==null)
			return null;
		
		return new ByteBuffer[]{buffer};
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.internal.IDatagram#getByteOrder()
	 */
	@Override
	public ByteOrder getByteOrder() {
		return buffer.order();
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#getLength()
	 */
	@Override
	public int getLength() {
		return length;
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#dump(java.lang.String, java.io.PrintStream)
	 */
	@Override
	public void dump(String prefix, PrintStream out) {
		out.println(prefix+getClass().getSimpleName());
		if (buffer==null) {
			out.println(prefix+"null");
			return;
		}
		
		byte[] bytes = buffer.array();
		int offset = buffer.arrayOffset();
		out.print(prefix);
		for (int i=0; i<length; i++) {
			out.print(String.format("%02x", bytes[i+offset]&0xff)+" ");
			if ((i+1)%8==0)
				out.print(" ");
			if ((i+1)%16==0) {
				out.println();
				if ((i+1)<length)
					out.print(prefix);
			}
		}
		if (length==0 || length%16!=0)
			out.println();
		
	}

	/* (non-Javadoc)
	 * @see com.intel.hpc.stl.resourceadapter.data.IDatagram#toObject()
	 */
	@Override
	public E toObject() {
		return null;
	}

}
