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
 *  File Name: CircularBuffer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/02/11 22:36:11  jijunwan
 *  Archive Log:    introduced life span control on CircularBuffer to ensure we will cache properly to get latest image info for a given id
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/12 19:57:33  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A CircularBuffer with life span control. Old items or items not fit in the
 * buffer's capacity will be removed.
 */
public class CircularBuffer<K, V> {

    /**
     * Introduce lifeSpan to ensure we always have the latest imageinfo for an
     * image id. the worst case can be that on the client side we always happen
     * use the same id, then we need to figure out when the imageinfo is
     * updated. In theory the safe life span for an image should be
     * (TotalImages - 1) * SweepInterval. Consider UI client's clock speed may
     * be different from SM node's clock speed. We can adjust life span to be:
     * (TotalImages - 2) * SweepInterval. When TotalImages is less than 2, it
     * should be zero that means we do not cache images.
     */
    private final long lifeSpan; // ms

    private final ArrayBlockingQueue<BufferItem<K, V>> buffer;

    private final ConcurrentLinkedQueue<V> saveQueue;

    public CircularBuffer(int lifeSpanInSeconds) {
        this(lifeSpanInSeconds, 10);
    }

    public CircularBuffer(long lifeSpanInSeconds, int size) {
        this.lifeSpan = lifeSpanInSeconds * 1000; // to ms
        buffer = new ArrayBlockingQueue<BufferItem<K, V>>(size);
        saveQueue = new ConcurrentLinkedQueue<V>();
    }

    public void put(K key, V value) {
        BufferItem<K, V> item = new BufferItem<K, V>(key, value);
        if (get(key) != null) {
            return;
        }
        while (!buffer.offer(item)) {
            saveQueue.add(buffer.poll().getValue());
        }
    }

    public List<V> purgeSaveQueue() {
        List<V> result = new ArrayList<V>(saveQueue.size());
        while (!saveQueue.isEmpty()) {
            V value = saveQueue.poll();
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    public int getSaveQueueSize() {
        return saveQueue.size();
    }

    public V get(K key) {
        Iterator<BufferItem<K, V>> it = buffer.iterator();
        while (it.hasNext()) {
            BufferItem<K, V> item = it.next();
            if (item.getKey().equals(key)) {
                if (item.isValide()) {
                    return item.getValue();
                } else {
                    it.remove();
                }
            }
        }
        return null;
    }

    private class BufferItem<I, L> {
        private final long timeStamp;

        private final I key;

        private final L value;

        public BufferItem(I key, L value) {
            timeStamp = System.currentTimeMillis();
            this.key = key;
            this.value = value;
        }

        public I getKey() {
            return key;
        }

        public L getValue() {
            return value;
        }

        public boolean isValide() {
            return System.currentTimeMillis() - timeStamp < lifeSpan;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof BufferItem)) {
                return false;
            }
            @SuppressWarnings("unchecked")
            BufferItem<I, L> other = (BufferItem<I, L>) obj;
            return (key.equals(other.key));
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

    }

}
