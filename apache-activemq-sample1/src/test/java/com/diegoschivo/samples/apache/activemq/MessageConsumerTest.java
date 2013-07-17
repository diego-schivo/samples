/**
 *    Copyright 2013 Diego Schivo
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.diegoschivo.samples.apache.activemq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MessageConsumerTest
{

    private Connection connection;

    private Session session;

    private Queue queue;

    @Before
    public void before() throws Exception
    {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createTemporaryQueue();
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("Hello World!");
        producer.send(message);
    }

    @Test
    public void testReceive() throws Exception
    {
        MessageConsumer consumer = session.createConsumer(queue);
        try
        {
            Message message = consumer.receive();
            assertTrue(message instanceof TextMessage);
            assertEquals("Hello World!", ((TextMessage) message).getText());
        }
        finally
        {
            consumer.close();
        }
    }

    @After
    public void after() throws Exception
    {
        session.close();
        connection.close();
    }
}
