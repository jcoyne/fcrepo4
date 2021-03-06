/**
 * Copyright 2013 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fcrepo.messaging.legacy;

import static javax.jcr.observation.Event.NODE_ADDED;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.observation.Event;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.fcrepo.utils.FedoraJcrTypes;
import org.junit.Before;
import org.junit.Test;

public class LegacyMethodEventFactoryTest {

    private LegacyMethodEventFactory testObj;

    @Before
    public void setUp() {
        testObj = new LegacyMethodEventFactory();
    }

    @Test
    public void testGetMessage() throws Exception {

        String testPath = "/foo/bar";
        javax.jms.Session mockJMS = mock(javax.jms.Session.class);
        TextMessage mockText = mock(TextMessage.class);
        when(mockJMS.createTextMessage(anyString())).thenReturn(mockText);
        Event mockEvent = mock(Event.class);
        when(mockEvent.getPath()).thenReturn(testPath);
        when(mockEvent.getType()).thenReturn(NODE_ADDED);
        Session mockJCR = mock(Session.class);
        Node mockSource = mock(Node.class);
        NodeType mockType = mock(NodeType.class);
        when(mockType.getName()).thenReturn(FedoraJcrTypes.FEDORA_OBJECT);
        NodeType[] mockTypes = new NodeType[] {mockType};
        when(mockSource.getMixinNodeTypes()).thenReturn(mockTypes);
        when(mockJCR.getNode(testPath)).thenReturn(mockSource);
        Message actual = testObj.getMessage(mockEvent, mockJCR, mockJMS);
        verify(mockText).setStringProperty("methodName", "ingest");
    }
}
