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
 *  File Name: PerformanceDAOImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2015/02/06 15:04:47  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/12/11 18:36:41  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/11/04 14:21:57  fernande
 *  Archive Log:    NoticeManager improvements. Added new methods in support of batch processing of notices and removed methods not used anymore because they were used for individual updates. Improved BaseDAO so that the DatabaseContext is available from within a DAO and therefore other DAOs are available within a DAO.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/18 20:50:35  fernande
 *  Archive Log:    Enabling GroupInfo saving after fixing issues in the application
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/15 21:19:32  fernande
 *  Archive Log:    Adding unit test for PerformanceApi and fixes for some issues found.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/12 20:01:12  fernande
 *  Archive Log:    We now save ImageInfo and GroupInfo to the database. As they are retrieved by the UI, they are buffered and then saved at certain thresholds.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import static com.intel.stl.common.STLMessages.STL30069_NO_IMAGEINFO;
import static com.intel.stl.common.STLMessages.STL30070_IMAGE_NUMBER_NOT_FOUND;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.performance.ImageInfoBean;
import com.intel.stl.api.performance.PerformanceDataNotFoundException;
import com.intel.stl.datamanager.ImageInfoId;
import com.intel.stl.datamanager.ImageInfoRecord;
import com.intel.stl.datamanager.SubnetRecord;
import com.intel.stl.dbengine.DatabaseContext;
import com.intel.stl.dbengine.PerformanceDAO;

public class PerformanceDAOImpl extends BaseDAO implements PerformanceDAO {

    private static Logger log = LoggerFactory.getLogger("org.hibernate.SQL");

    /**
     * Description: data access object with methods related to the Performance
     * API
     * 
     * @param entityManager
     */
    public PerformanceDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public PerformanceDAOImpl(EntityManager entityManager,
            DatabaseContext databaseCtx) {
        super(entityManager, databaseCtx);
    }

    @Override
    public void saveImageInfos(SubnetRecord subnet,
            List<ImageInfoBean> imageInfos) {
        StringBuffer keys = new StringBuffer();
        keys.append(subnet.getSubnetDescription().getName());
        char separator = '|';
        startTransaction();
        for (ImageInfoBean imageInfo : imageInfos) {
            ImageInfoRecord imageRecord =
                    new ImageInfoRecord(subnet.getId(), imageInfo);
            em.persist(imageRecord);
            keys.append(separator);
            keys.append(imageInfo.getSweepStart());
            separator = ',';
        }
        try {
            commitTransaction();
        } catch (Exception e) {
            DatabaseException dbe =
                    DatabaseUtils.createPersistDatabaseException(e,
                            ImageInfoRecord.class, keys);
            log.error(dbe.getMessage(), e);
            throw dbe;
        }
    }

    @Override
    public List<ImageInfoBean> getImageInfo(SubnetRecord subnet,
            long imageNumber) throws PerformanceDataNotFoundException {
        TypedQuery<ImageInfoBean> query =
                em.createNamedQuery("ImageInfo.findByImageNum",
                        ImageInfoBean.class);
        query.setParameter("subnetId", subnet.getId());
        query.setParameter("imageNumber", imageNumber);
        List<ImageInfoBean> imageInfos = query.getResultList();
        if (imageInfos == null || imageInfos.size() == 0) {
            PerformanceDataNotFoundException pdnf =
                    new PerformanceDataNotFoundException(
                            STL30070_IMAGE_NUMBER_NOT_FOUND, imageNumber,
                            subnet.getSubnetDescription().getName());
            throw pdnf;
        }
        return imageInfos;
    }

    @Override
    public ImageInfoBean getLastImageInfo(SubnetRecord subnet)
            throws PerformanceDataNotFoundException {
        TypedQuery<Long> query =
                em.createNamedQuery("ImageInfo.findLatest", Long.class);
        query.setParameter("subnetId", subnet.getId());
        Long latestSweepstart = query.getSingleResult();
        if (latestSweepstart == null) {
            PerformanceDataNotFoundException pdnf =
                    new PerformanceDataNotFoundException(STL30069_NO_IMAGEINFO,
                            subnet.getSubnetDescription().getName());
            throw pdnf;
        }
        ImageInfoId id = new ImageInfoId();
        id.setFabricId(subnet.getId());
        id.setSweepTimestamp(latestSweepstart);
        ImageInfoRecord latest = em.find(ImageInfoRecord.class, id);
        return latest.getImageInfo();
    }
}
