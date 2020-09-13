package io.github.dayco.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Websocket 을 통해 들어온 요청이 처리 되기전 실행된다.
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            logger.debug("CONNECT");
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            logger.debug("SUBSCRIBED");
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            logger.debug("DISCONNECT");
        }
        logger.debug("auth:" + accessor.getNativeHeader("Authorization"));
        return message;
    }
}
