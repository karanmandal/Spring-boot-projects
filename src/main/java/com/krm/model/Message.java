package com.krm.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Message {

    private Date receiveAt = new Date();

    private User sender;

    private User receiver;

    private String messageText;
}
