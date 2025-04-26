package com.example.WebLearn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");  // Prefix để subscribe
        registry.setApplicationDestinationPrefixes("/app");  // Prefix để gửi message
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                   accessor.setUser(() -> {
//                       if (accessor.getNativeHeader(HttpHeaders.AUTHORIZATION) != null && !CollectionUtils.isEmpty(accessor.getNativeHeader(HttpHeaders.AUTHORIZATION))) {
//                           String token = accessor.getNativeHeader(HttpHeaders.AUTHORIZATION).get(0).strip().substring(7).strip();
//                           return userService.verifyToken(token).getSubject();
//                       } else {
//                           return "Anonymous User";
//                       }
//                   });
//                }
//                return message;
//            }
//        });
//    }
}
