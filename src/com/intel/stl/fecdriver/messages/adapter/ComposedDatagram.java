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
import java.util.ArrayList;
import java.util.List;

/**
 * @author jijunwan
 *
 */
public class ComposedDatagram<E> implements IDatagram<E> {
	private int length = 0;
	private List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
	private ByteOrder order = null;
	private boolean hasConsistentOrder = true;
	private List<IDatagram<?>> datagrams = new ArrayList<IDatagram<?>>();
	private boolean dirty = true;
	
	public ComposedDatagram() {
	}
	
	public ComposedDatagram(IDatagram<?> ...datagrams) {
		for (IDatagram<?> datagram : datagrams) {
			this.datagrams.add(datagram);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#build(boolean)
	 */
	@Override
	public int build(boolean force) {
		int len = 0;
		for (IDatagram<?> datagram : datagrams) {
			len += datagram.build(force);
		}
		if (len>0)
			refresh();
		return getLength();
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#wrap(byte[], int)
	 */
	@Override
	public int wrap(byte[] data, int offset) {
		int pos = offset;
		for (IDatagram<?> datagram : datagrams) {
			pos = datagram.wrap(data, pos);
		}
		refresh();
		return pos;
	}

	/**
	 * NOTE: if a sub ComposedDatagram changed, it's the user's responsibility to call this method to ensure 
	 * parent ComposedDatagram has correct information
	 * TODO: add listener to automatically do this 
	 */
	public void refresh() {
		length = 0;
		buffers.clear();
		order = null;
		
		for (IDatagram<?> datagram : datagrams) {
			if (!datagram.hasBuffer())
				throw new IllegalArgumentException("Datagram has no buffer! Please initialize it with #build or #wrap method first.");
			
			length += datagram.getLength();
			
			ByteBuffer[] localBuffers = datagram.getByteBuffers();
			if (localBuffers!=null) {
				for (ByteBuffer buffer : localBuffers) {
					buffers.add(buffer);
				}
			}
			
			if ((datagram instanceof ComposedDatagram) && !((ComposedDatagram<?>)datagram).hasConsistentOrder) {
				hasConsistentOrder = false;
				continue;
			}
			
			if (order==null)
				order = datagram.getByteOrder();
			else if (hasConsistentOrder) {
				if (order!=datagram.getByteOrder()) {
					hasConsistentOrder = false;
				}
			}
		}
		
		dirty = false;
	}
	
	public List<IDatagram<?>> getDatagrams() {
		return datagrams;
	}
	
	public void addDatagram(IDatagram<?> datagram) {
		if (datagram==null)
			return;
		
		datagrams.add(datagram);
		if (!datagram.hasBuffer()) {
			dirty = true;
			return;
		}
		
		length += datagram.getLength();
		
		ByteBuffer[] localBuffers = datagram.getByteBuffers();
		if (localBuffers!=null) {
			for (ByteBuffer buffer : localBuffers) {
				buffers.add(buffer);
			}
		}
		
		if ((datagram instanceof ComposedDatagram) && !((ComposedDatagram<?>)datagram).hasConsistentOrder) {
			hasConsistentOrder = false;
			return;
		}

		if (order==null)
			order = datagram.getByteOrder();
		else if (hasConsistentOrder) {
			if (order!=datagram.getByteOrder()) {
				hasConsistentOrder = false;
			}
		}
	}
	
	public void removeDatagram(IDatagram<?> datagram) {
		if (datagram==null)
			return;
		
		if (!datagrams.remove(datagram))
			return;
		if (!datagram.hasBuffer()) {
			dirty = true;
			return;
		}
		
		length -= datagram.getLength();
		
		ByteBuffer[] localBuffers = datagram.getByteBuffers();
		if (localBuffers!=null) {
			for (ByteBuffer buffer : localBuffers) {
				buffers.remove(buffer);
			}
		}
		
		if (!hasConsistentOrder) {
			order = null;
			hasConsistentOrder = true;
			for (IDatagram<?> dg : datagrams) {
				if ((datagram instanceof ComposedDatagram) && !((ComposedDatagram<?>)datagram).hasConsistentOrder) {
					hasConsistentOrder = false;
					break;
				}
				
				if (order==null)
					order = dg.getByteOrder();
				else {
					if (order!=dg.getByteOrder()) {
						hasConsistentOrder = false;
						break;
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#getByteBuffers()
	 */
	@Override
	public ByteBuffer[] getByteBuffers() {
		if (dirty)
			refresh();
		
		return buffers.toArray(new ByteBuffer[0]);
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#getLength()
	 */
	@Override
	public int getLength() {
		if (dirty)
			refresh();
		
		return length;
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#getByteOrder()
	 */
	@Override
	public ByteOrder getByteOrder() {
		if (dirty)
			refresh();
		
		if (hasConsistentOrder)
			return order;
		else
			throw new RuntimeException("No consistent ByteOrder");
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#hasBuffer()
	 */
	@Override
	public boolean hasBuffer() {
		return !dirty;
	}

	/* (non-Javadoc)
	 * @see com.vieo.fv.resource.stl.data.IDatagram#dump(java.lang.String, java.io.PrintStream)
	 */
	@Override
	public void dump(String prefix, PrintStream out) {
		out.println(prefix+getClass().getSimpleName());
		for (IDatagram<?> datagram : datagrams) {
			datagram.dump(prefix+"  ", out);
		}
	}

	/* (non-Javadoc)
	 * @see com.intel.hpc.stl.resourceadapter.data.IDatagram#toObject()
	 */
	@Override
	public E toObject() {
		return null;
	}

}
