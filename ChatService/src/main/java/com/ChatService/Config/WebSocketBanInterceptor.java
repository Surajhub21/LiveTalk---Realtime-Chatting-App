package com.ChatService.Config;

import com.ChatService.Service.ModerationClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketBanInterceptor implements HandshakeInterceptor {

    private final ModerationClient moderationClient;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        URI uri = request.getURI();
        System.out.println("Handshake URI: " + uri);

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String username = httpRequest.getParameter("username");
            String roomId = httpRequest.getParameter("roomId");

            if (username == null || roomId == null) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return false;
            }

            List<String> bannedUsers = moderationClient.getBannedUsers(roomId);
            if(!bannedUsers.isEmpty()) {
                if (bannedUsers.contains(username)) {
                    System.out.println("Blocked banned user: " + username);
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return false; // prevent handshake
                }
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // Nothing needed
    }
}
