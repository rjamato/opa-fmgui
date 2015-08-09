/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: MailSender.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/04 21:29:36  jijunwan
 *  Archive Log:    added Mail Manager
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.configuration.MailProperties;

public class MailSender {
    private static Logger log = LoggerFactory.getLogger(MailSender.class);

    private static boolean DEBUG = true;

    private static int QUEUE_SIZE = 4096;

    private MailProperties properties;

    private final ExecutorService service;

    private final BlockingQueue<MessageItem> items;

    private Future<?> future;

    /**
     * Description:
     * 
     * @param properties
     */
    public MailSender(MailProperties properties) {
        super();
        service = Executors.newSingleThreadExecutor();
        // fixed size queue because we won't unbounded queue that will eat a lot
        // memory. And we will throw exception when the queue if full.
        items = new LinkedBlockingQueue<MessageItem>(QUEUE_SIZE);
        setProperties(properties);
    }

    public void setProperties(MailProperties properties) {
        // nothing to change
        if (properties == null || properties.equals(this.properties)) {
            return;
        }
        if (future != null) {
            future.cancel(true);
        }

        this.properties = properties;
        try {
            Runnable task = createTask();
            future = service.submit(task);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    protected Runnable createTask() throws MessagingException {
        final Session session = createSession();
        session.setDebug(DEBUG);
        final Transport transport = session.getTransport("smtp");
        transport.connect();

        Runnable task = new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        MessageItem item = items.take();
                        try {
                            sentMessage(session, transport, item);
                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    log.info("To close transport (" + transport + ") for "
                            + properties);
                    try {
                        transport.close();
                        log.info("Closed transport (" + transport + ") for "
                                + properties);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        return task;
    }

    protected Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", properties.isAuthEnabled());
        props.put("mail.smtp.starttls.enable", properties.isTlsEnabled());
        props.put("mail.smtp.host", properties.getSmtpServer());
        props.put("mail.smtp.port", properties.getSmtpPort());

        Session session =
                Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties
                                .getUserName(), new String(properties
                                .getPassword()));
                    }
                });
        return session;
    }

    protected void sentMessage(Session session, Transport transport,
            MessageItem item) throws AddressException, MessagingException {
        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(item.from));

        // Set To: header field of the header.
        InternetAddress[] address = { new InternetAddress(item.to) };
        message.setRecipients(Message.RecipientType.TO, address);

        // Set Subject: header field
        message.setSubject(item.subject);

        // Now set the actual message
        message.setText(item.body);

        // Send message
        transport.sendMessage(message, address);
        if (DEBUG) {
            System.out.println("Send Message " + message + " to " + address[0]);
        }
    }

    public void submitMessage(String from, String to, String subject,
            String body) {
        MessageItem item = new MessageItem(from, to, subject, body);
        // will throw exception if no space available
        items.add(item);
    }

    public void shutdown() {
        if (future != null) {
            future.cancel(true);
        }

        service.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                service.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            service.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    class MessageItem {
        String from;

        String to;

        String subject;

        String body;

        /**
         * Description:
         * 
         * @param from
         * @param to
         * @param subject
         * @param body
         */
        public MessageItem(String from, String to, String subject, String body) {
            super();
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.body = body;
        }

    }
}
