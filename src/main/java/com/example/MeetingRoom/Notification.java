package com.example.MeetingRoom;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class Notification {

    @Id
    private String id;

    private String user;

    private String title;

    private String description;

    private String category;

    private String triggerDate;

    private boolean readStatus;

}
