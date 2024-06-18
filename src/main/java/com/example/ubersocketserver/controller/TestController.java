package com.example.ubersocketserver.controller;

import com.example.ubersocketserver.dto.ChatRequest;
import com.example.ubersocketserver.dto.ChatResponse;
import com.example.ubersocketserver.dto.TestRequestDto;
import com.example.ubersocketserver.dto.TestResponseDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    //https://medium.com/swlh/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397
    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //Server will receive message from client here from app/ping
    //Then server will send it to the topic/ping
    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponseDto pingCheck(TestRequestDto requestDto){
        System.out.println("Received Message from client" + requestDto.getData());
        return TestResponseDto.builder().data("Received").build();
    }

    //Server will send message to client
    //Client is listening on topic/schdeuled & then it will show the message
    /*@SendTo("/topic/scheduled")
    @Scheduled(fixedDelay = 2000)
    public String sendPeriodicMessage(){
        System.out.println("Executed periodic function");
        simpMessagingTemplate.convertAndSend("/topic/scheduled", "Periodic Message sent " + System.currentTimeMillis());
        return "Periodic message from server "+ System.currentTimeMillis();
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public ChatResponse chatMessage(ChatRequest request){
        System.out.println("Inside the server");
        System.out.println(request.getName()+"--------------"+request.getMessage());
        return ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();
    }*/

    /*@MessageMapping("/chat/{room}")
    @SendTo("/topic/message/{room}")
    public ChatResponse chatMessage(@DestinationVariable String room, ChatRequest request){
        System.out.println("Inside the server");
        System.out.println(request.getName()+"--------------"+request.getMessage());
        return ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();
    }*/

    /*
    @MessageMapping("/privateChat/{room}/{userId}")
    @SendTo("/topic/privateMessage/{room}/{userId}")
    public void privateChatMessage(@DestinationVariable String room,@DestinationVariable String userId, ChatRequest request){
        ///System.out.println("Inside the server");
        //System.out.println(request.getName()+"--------------"+request.getMessage());
        ChatResponse chatResponse = ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();
        simpMessagingTemplate.convertAndSendToUser(userId,"/queue/privateChat/"+room, chatResponse);
    }*/
}
