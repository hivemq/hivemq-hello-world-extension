package com.hivemq.extensions.helloworld;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yannick Weber
 * @since 4.6.1
 */
class HelloWorldInterceptorTest {

    private @NotNull HelloWorldInterceptor helloWorldInterceptor;
    private @NotNull PublishInboundInput publishInboundInput;
    private @NotNull PublishInboundOutput publishInboundOutput;
    private @NotNull ModifiablePublishPacket publishPacket;

    @BeforeEach
    void setUp() {
        helloWorldInterceptor = new HelloWorldInterceptor();
        publishInboundInput = mock(PublishInboundInput.class);
        publishInboundOutput = mock(PublishInboundOutput.class);
        publishPacket = mock(ModifiablePublishPacket.class);
        when(publishInboundOutput.getPublishPacket()).thenReturn(publishPacket);
    }

    @Test
    void topicHelloWorld_payloadModified() {
        when(publishPacket.getTopic()).thenReturn("hello/world");
        helloWorldInterceptor.onInboundPublish(publishInboundInput, publishInboundOutput);
        final ArgumentCaptor<ByteBuffer> captor = ArgumentCaptor.forClass(ByteBuffer.class);
        verify(publishPacket).setPayload(captor.capture());
        assertEquals("Hello World!", new String(captor.getValue().array(), StandardCharsets.UTF_8));
    }

    @Test
    void topicNotHelloWorld_payloadNotModified() {
        when(publishPacket.getTopic()).thenReturn("some/topic");
        helloWorldInterceptor.onInboundPublish(publishInboundInput, publishInboundOutput);
        verify(publishPacket, times(0)).setPayload(any());
    }
}