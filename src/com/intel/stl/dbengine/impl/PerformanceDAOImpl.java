/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:22:29  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
