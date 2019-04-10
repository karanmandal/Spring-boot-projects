package com.krm.controller;

import com.krm.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/api")
public class KafkaController {

    private final Producer producer;

    @Autowired
    public KafkaController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping(value = "/publish")
    public ResponseEntity sendMessageToKafkaTopic(@RequestParam("topic") String topic,
                                                  @RequestParam("message") String message) {

        this.producer.sendMessage(topic, message);

        return ResponseEntity.ok("Published!");
    }

}