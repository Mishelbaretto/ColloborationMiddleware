package com.tcs.colloboration.rest.controller;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.colloboration.model.Message;
import com.tcs.colloboration.model.OutputMessage;

@RestController
public class ChatController {
	
	
	@MessageMapping("/chat")
	@SendTo("/topic/message")
	public OutputMessage sendMessage(Message message)
	{
		System.out.println("chtcntrl");
		return new OutputMessage(message,new Date());
	}
	

}
