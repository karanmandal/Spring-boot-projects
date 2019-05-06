package com.krm.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krm.model.User;
import com.krm.security.AuthService;
import com.krm.security.JwtTokenProvider;
import com.krm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static com.krm.contants.Constants.ACCESS_TOKEN;
import static com.krm.contants.Constants.CURRENT_USER;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SocketHandler socketHandler;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    public UserService userService;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/stream").addInterceptors(httpSessionHandshakeInterceptor());
    }

    // Authentication handshake interceptor
    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {

        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
                    HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
                    Cookie token = WebUtils.getCookie(servletRequest, ACCESS_TOKEN);

                    String jwt = token.getValue();

                    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                        String userId = tokenProvider.getUserIdFromJWT(jwt);

                        Optional<User> userDetails = authService.loadUserById(userId);

                        if (userDetails.isPresent()) {

                            User user = userDetails.get();
                            attributes.put(ACCESS_TOKEN, token.getValue());
                            attributes.put(CURRENT_USER, user);

                            return true;
                        }
                    }

                }

                return false;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            }
        };
    }
}
