package com.example.MeetingRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MeetingRoomRepository extends MongoRepository<MeetingRoom, String> {
    Optional<MeetingRoom> findByName(String name);
}

