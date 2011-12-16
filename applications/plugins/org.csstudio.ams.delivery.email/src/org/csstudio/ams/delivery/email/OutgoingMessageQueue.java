
/*
 * Copyright (c) 2011 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 *
 * $Id: DesyKrykCodeTemplates.xml,v 1.7 2010/04/20 11:43:22 bknerr Exp $
 */

package org.csstudio.ams.delivery.email;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.csstudio.ams.AmsConstants;
import org.csstudio.ams.delivery.BaseAlarmMessage.Priority;
import org.csstudio.ams.delivery.BaseAlarmMessage.State;
import org.csstudio.ams.delivery.BaseAlarmMessage.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO (mmoeller) : 
 * 
 * @author mmoeller
 * @version 1.0
 * @since 11.12.2011
 */
public class OutgoingMessageQueue implements MessageListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(OutgoingMessageQueue.class);
    
    private ConcurrentLinkedQueue<EMailAlarmMessage> content;
    
    private EMailWorkerProperties props;
    
    public OutgoingMessageQueue(EMailWorkerProperties properties) {
        content = new ConcurrentLinkedQueue<EMailAlarmMessage>();
        props = properties;
    }

    public synchronized ArrayList<EMailAlarmMessage> getCurrentContent() {
        ArrayList<EMailAlarmMessage> result = new ArrayList<EMailAlarmMessage>(content);
        content.removeAll(result);
        return result;
    }
    
    @Override
    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            LOG.info("Message received: {}", message);
            EMailAlarmMessage o = convertMessage((MapMessage) message);
            if (o != null) {
                content.add(o);
                synchronized (this) {
                    notify();
                }
            }
            acknowledge(message);
        } else {
            LOG.warn("Message is not a MapMessage object. Ignoring it...");
        }
    }
    
    private EMailAlarmMessage convertMessage(MapMessage message) {
        
        EMailAlarmMessage result = null;
        String text;
        try {
            text = message.getString(AmsConstants.MSGPROP_RECEIVERTEXT);
            final String emailadr = message.getString(AmsConstants.MSGPROP_RECEIVERADDR);
            final String userName = message.getString(AmsConstants.MSGPROP_SUBJECT_USERNAME);
            final String mySubject = props.getMailSubject();
            String myContent = props.getMailContent();
            myContent = myContent.replaceAll("%N", userName);

            // Sometimes it happens that the placeholder (e.g. $VALUE$, $HOST$, ...)
            // for the alarm message properties are still present.
            // The dollar sign of this placeholders have to be deleted because they cause an
            // IllegalArgumentException when calling method replaceAll()
            text = cleanTextString(text);
            myContent = myContent.replaceAll("%AMSG", text);
            
            result = new EMailAlarmMessage(message.getJMSTimestamp(),
                                           Priority.NORMAL,
                                           emailadr,
                                           myContent,
                                           State.NEW,
                                           Type.OUT,
                                           "NONE",
                                           false,
                                           userName,
                                           mySubject);
        } catch (JMSException jmse) {
            LOG.warn("[*** JMSException ***]: convertMessage(): {}", jmse.getMessage());
        }

        return result;
    }
    
    private String cleanTextString(final String text) {

        if (text == null) {
            return "";
        } else if (text.length() == 0) {
            return "";
        }

        return text.replace("$", "");
    }

    private void acknowledge(Message message) {
        try {
            message.acknowledge();
        } catch (JMSException jmse) {
            LOG.warn("Cannot acknowledge message: {}", message);
        }
    }
}