package com.example.ubersocketserver.controller;

import com.example.ubersocketserver.consumers.KafkaConsumerService;
import com.example.ubersocketserver.dto.RideRequestDto;
import com.example.ubersocketserver.dto.RideResponseDto;
import com.example.ubersocketserver.dto.UpdateBookingRequestDto;
import com.example.ubersocketserver.dto.UpdateBookingResponseDto;
import com.example.ubersocketserver.producer.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("/api/socket")
public class DriverRequestController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RestTemplate restTemplate;

    private final KafkaConsumerService kafkaConsumerService;

    private final KafkaProducerService kafkaProducerService;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate,KafkaConsumerService kafkaConsumerService,KafkaProducerService kafkaProducerService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = new RestTemplate();
        this.kafkaConsumerService = kafkaConsumerService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping
    public ResponseEntity<?>  getHelp(){
        kafkaProducerService.publishMessage("sample-topic","Hello Pavan");
        return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    @PostMapping("/newRide")
    @CrossOrigin(originPatterns = "*")
    public ResponseEntity<?> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto){
        sendDriverNewRideRequest(rideRequestDto);
        return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    public String sendDriverNewRideRequest(RideRequestDto rideRequestDto){
        System.out.println("Executed periodic function");
        simpMessagingTemplate.convertAndSend("/topic/rideRequest", rideRequestDto);
        return "Periodic message from server "+ System.currentTimeMillis();
    }

    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable("userId") String userId,RideResponseDto rideResponseDto){
        System.out.println(rideResponseDto.getResponse()+" "+userId);
        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId(Optional.of(Long.parseLong(userId)))
                .status("SCHEDULED")
                .build();
        ResponseEntity<UpdateBookingResponseDto>  responseDto= this.restTemplate.postForEntity("http://localhost:7479/api/v1/booking/"+rideResponseDto.getBookingId(),requestDto,UpdateBookingResponseDto.class);
        kafkaProducerService.publishMessage("sample-topic","You have been assigned a driver");
    }
}
