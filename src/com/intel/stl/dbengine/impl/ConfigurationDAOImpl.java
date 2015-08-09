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
 *  File Name: ConfigurationDAOImpl.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.19  2015/02/11 22:01:46  fernande
 *  Archive Log:    Fix error introduced in previous changes where getUserSettings was returning an exception on first run, when no subnets are defined and should return the default user settings.
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2015/02/11 21:11:29  fernande
 *  Archive Log:    Adding support to remove a subnet (logical delete) from the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2015/02/06 15:04:47  fernande
 *  Archive Log:    Database modifications to use a long as the id for a SubnetDescription and to support users per subnet.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/12/11 18:36:41  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/12/10 20:32:39  rjtierne
 *  Archive Log:    Support for saving EventRules to UserSettings
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/11/11 18:04:04  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/11/04 14:21:57  fernande
 *  Archive Log:    NoticeManager improvements. Added new methods in support of batch processing of notices and removed methods not used anymore because they were used for individual updates. Improved BaseDAO so that the DatabaseContext is available from within a DAO and therefore other DAOs are available within a DAO.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/21 13:46:03  fernande
 *  Archive Log:    Fix for the Datamanager shutdown process where UserSettings was not persisted to the database randomly.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/09/29 18:59:29  fernande
 *  Archive Log:    Adding UserOptions XML and  saving it to the database. Database changes
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/05 15:39:36  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/12 20:07:38  jijunwan
 *  Archive Log:    1) renamed HexUtils to StringUtils
 *  Archive Log:    2) added a method to StringUtils to get error message for an exception
 *  Archive Log:    3) changed all code to call StringUtils to get error message
 *  Archive Log:    4) some extra ode format change
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/11 13:07:04  jypak
 *  Archive Log:    1. Added runtime, non runtime exceptions to be thrown for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    2. Updated exception generating code due to Cache Manager related changes.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/20 16:58:25  fernande
 *  Archive Log:    Added basic Entity Manager management to minimize creation of DAOs
 *  Archive Log:    Fixed bugs in database management
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/19 20:08:39  fernande
 *  Archive Log:    Added background update of database and redirected some APIs to use the database.
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/11 22:09:28  fernande
 *  Archive Log:    Changes to add more entities to database schema
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/08 13:40:36  fernande
 *  Archive Log:    Fixed issue with editing rules
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/07 21:51:59  fernande
 *  Archive Log:    Fix for errors in save in the app
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/07 19:11:34  fernande
 *  Archive Log:    Changes to save Subnets and EventRules to database
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/30 16:16:04  fernande
 *  Archive Log:    Implementing DAOs to persist EventRule and SubnetDescription
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.dbengine.impl;

import static com.intel.stl.common.STLMessages.STL30012_ENTITY_NOT_FOUND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.DatabaseException;
import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.EventRule;
import com.intel.stl.api.configuration.EventRuleAction;
import com.intel.stl.api.configuration.UserSettings;
import com.intel.stl.api.subnet.SubnetDescription;
import com.intel.stl.common.AppDataUtils;
import com.intel.stl.common.STLMessages;
import com.intel.stl.datamanager.EventActionRecord;
import com.intel.stl.datamanager.EventRuleRecord;
import com.intel.stl.datamanager.SubnetRecord;
import com.intel.stl.datamanager.UserId;
import com.intel.stl.datamanager.UserRecord;
import com.intel.stl.dbengine.ConfigurationDAO;
import com.intel.stl.dbengine.DatabaseContext;
import com.intel.stl.xml.UserOptions;
import com.intel.stl.xml.UserOptionsMarshaller;

public class ConfigurationDAOImpl extends BaseDAO implements ConfigurationDAO {

    private static Logger log = LoggerFactory
            .getLogger(ConfigurationDAOImpl.class);

    public ConfigurationDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public ConfigurationDAOImpl(EntityManager entityManager,
            DatabaseContext databaseCtx) {
        super(entityManager, databaseCtx);
    }

    @Override
    public void saveEventRule(EventRule rule) {
        EventRuleRecord dbRule =
                em.find(EventRuleRecord.class, rule.getEventName());
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        if (dbRule == null) {
            persistEventRule(rule);
        } else {
            mergeEventRule(dbRule, rule);
        }
        try {
            tx.commit();
        } catch (Exception e) {
            DatabaseException dbe =
                    new DatabaseException(
                            STLMessages.STL30013_ERROR_SAVING_ENTITY,
                            "EventRule", rule.getEventName(),
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(dbe), e);
            log.error(rule.toString());
            throw dbe;
        }
    }

    @Override
    public void saveEventRules(List<EventRule> rules) {
        List<EventRuleRecord> currRules = getEventRuleRecords();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (EventRuleRecord rule : currRules) {
            if (!rules.contains(rule.getEventRule())) {
                em.remove(rule);
            }
        }
        for (EventRule rule : rules) {
            EventRuleRecord ruleRec = new EventRuleRecord(rule);
            int x = currRules.indexOf(ruleRec);
            if (x >= 0) {
                EventRuleRecord dbRule = currRules.get(x);
                mergeEventRule(dbRule, rule);
            } else {
                persistEventRule(rule);
            }
        }
        try {
            tx.commit();
        } catch (Exception e) {
            DatabaseException dbe =
                    new DatabaseException(
                            STLMessages.STL30013_ERROR_SAVING_ENTITY,
                            "EventRule", "@log", StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(dbe), e);
            log.error(Arrays.toString(rules.toArray()));
            throw dbe;
        }
    }

    @Override
    public List<EventRule> getEventRules() {
        List<EventRule> rules = new ArrayList<EventRule>();
        List<EventRuleRecord> ruleRecs = getEventRuleRecords();
        for (EventRuleRecord ruleRec : ruleRecs) {
            rules.add(ruleRec.getEventRule());
        }
        return rules;
    }

    private List<EventRuleRecord> getEventRuleRecords() {
        TypedQuery<EventRuleRecord> query =
                em.createNamedQuery("EventRule.All", EventRuleRecord.class);
        return query.getResultList();
    }

    @Override
    public EventRule getEventRule(String ruleName) {
        EventRuleRecord record = em.find(EventRuleRecord.class, ruleName);
        if (record == null) {
            return null;
        }
        return record.getEventRule();
    }

    private void persistEventRule(EventRule rule) throws DatabaseException {
        EventRuleRecord ruleRec = new EventRuleRecord(rule);
        Set<SubnetRecord> managedSubnets = new HashSet<SubnetRecord>();
        Set<SubnetDescription> eventSubnets = rule.getEventSubnets();
        if (eventSubnets != null) {
            for (SubnetDescription subnet : eventSubnets) {
                addSubnetToSet(managedSubnets, subnet);
            }
        }
        ruleRec.setEventSubnets(managedSubnets);
        Set<EventActionRecord> managedActions =
                new HashSet<EventActionRecord>();
        List<EventRuleAction> eventActions = rule.getEventActions();
        if (eventActions != null) {
            for (EventRuleAction action : eventActions) {
                EventActionRecord actionRec = new EventActionRecord();
                actionRec.setId(action.name());
                managedActions.add(actionRec);
            }
        }
        ruleRec.setEventActions(managedActions);
        em.persist(ruleRec);
    }

    private void mergeEventRule(EventRuleRecord ruleRec, EventRule newRule)
            throws DatabaseException {
        ruleRec.setEventRule(newRule);

        Set<SubnetRecord> newSubnets = getEventSubnets(newRule);
        Set<SubnetRecord> dbSubnets = ruleRec.getEventSubnets();

        dbSubnets.retainAll(newSubnets);
        newSubnets.removeAll(dbSubnets);
        for (SubnetRecord subnet : newSubnets) {
            addSubnetToSet(dbSubnets, subnet.getSubnetDescription());
        }
        em.merge(ruleRec);
    }

    private void addSubnetToSet(Set<SubnetRecord> set, SubnetDescription subnet)
            throws DatabaseException {
        try {
            set.add(em.getReference(SubnetRecord.class, subnet.getSubnetId()));
        } catch (EntityNotFoundException enf) {
            DatabaseException dbe =
                    new DatabaseException(STL30012_ENTITY_NOT_FOUND, enf,
                            "SubnetDescription", subnet.getName());
            throw dbe;
        }
    }

    private Set<SubnetRecord> getEventSubnets(EventRule rule) {
        Set<SubnetRecord> subnets = new HashSet<SubnetRecord>();
        Set<SubnetDescription> newSubnets = rule.getEventSubnets();
        for (SubnetDescription subnet : newSubnets) {
            SubnetRecord subnetRec = new SubnetRecord(subnet);
            subnets.add(subnetRec);
        }
        return subnets;
    }

    @Override
    public UserSettings getUserSettings(SubnetRecord subnet, String userName) {
        if (subnet == null) {
            subnet = new SubnetRecord();
        }
        UserId userId = new UserId();
        userId.setFabricId(subnet.getId());
        userId.setUserName(userName);
        UserRecord user = em.find(UserRecord.class, userId);
        if (user == null) {
            user = new UserRecord();
            user.setId(userId);
            user.setUserDescription("Default User");
            user.setUserOptionsXml(AppDataUtils.getDefaultUserOptions());
        }
        UserSettings settings = new UserSettings();
        settings.setUserName(user.getId().getUserName());
        settings.setUserDescription(user.getUserDescription());
        String userOptions = user.getUserOptionsXml();
        if (userOptions == null) {
            userOptions = AppDataUtils.getDefaultUserOptions();
        }
        try {
            UserOptions options = UserOptionsMarshaller.unmarshal(userOptions);
            settings.setPreferences(options.getPreferences());
            settings.setEventRules(options.getEventRules());
            settings.setPropertiesDisplayOptions(options.getPropertiesDisplay());
        } catch (JAXBException e) {
            DatabaseException dbe = createUserOptionsXmlException(userName, e);
            throw dbe;
        }
        return settings;
    }

    @Override
    public void saveUserSettings(SubnetRecord subnet, UserSettings userSettings) {
        UserId userId = new UserId();
        userId.setFabricId(subnet.getId());
        userId.setUserName(userSettings.getUserName());
        UserRecord dbUser = em.find(UserRecord.class, userId);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        if (dbUser == null) {
            UserRecord user = new UserRecord();
            user.setId(userId);
            mergeUserSettings(user, userSettings);
            em.persist(user);
        } else {
            mergeUserSettings(dbUser, userSettings);
            em.merge(dbUser);
        }
        try {
            tx.commit();
        } catch (Exception e) {
            DatabaseException dbe =
                    new DatabaseException(
                            STLMessages.STL30013_ERROR_SAVING_ENTITY,
                            "UserRecord", userSettings.getUserName(),
                            StringUtils.getErrorMessage(e));
            log.error(StringUtils.getErrorMessage(dbe), e);
            log.error(userSettings.toString());
            throw dbe;
        }
    }

    private void mergeUserSettings(UserRecord record, UserSettings settings) {
        record.setUserDescription(settings.getUserDescription());
        UserOptions options = new UserOptions();
        options.setPreferences(settings.getPreferences());
        options.setEventRules(settings.getEventRules());
        options.setPropertiesDisplay(settings.getPropertiesDisplayOptions());
        String strOptions = null;
        try {
            strOptions = UserOptionsMarshaller.marshal(options);
        } catch (JAXBException e) {
            DatabaseException dbe =
                    createUserOptionsXmlException(settings.getUserName(), e);
            throw dbe;
        }
        record.setUserOptionsXml(strOptions);
    }

    private DatabaseException createUserOptionsXmlException(String userName,
            Exception e) {
        DatabaseException dbe =
                new DatabaseException(
                        STLMessages.STL30071_INVALID_USEROPTIONS_XML, e,
                        userName, StringUtils.getErrorMessage(e));
        log.error(StringUtils.getErrorMessage(dbe), e);
        return dbe;
    }
}
