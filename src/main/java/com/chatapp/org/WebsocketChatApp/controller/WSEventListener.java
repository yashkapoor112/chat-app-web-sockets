package com.chatapp.org.WebsocketChatApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chatapp.org.WebsocketChatApp.model.ChatMessage;
import com.chatapp.org.WebsocketChatApp.model.MessageType;

/*
 * Handling the user connection and disconnection
 * TODO handle the connection instead of only displaying
 * @author Yash
 *
 */
@Component
public class WSEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WSEventListener.class);
	
	@Autowired
	private SimpMessageSendingOperations sendingOperations;
	
	@EventListener
	public void handleWebSocketConnectListener(final SessionConnectedEvent event)
	{
		LOGGER.info("New User Connected");
	}
	
	@EventListener
	public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event)
	{
		final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		final String username = (String) headerAccessor.getSessionAttributes().get("username");
		
		final ChatMessage chatMessage = ChatMessage.builder()
				.type(MessageType.DISCONNECT)
				.sender(username)
				.build();
		sendingOperations.convertAndSend("/topic/public", chatMessage);
	}
	
	
	
	
}
