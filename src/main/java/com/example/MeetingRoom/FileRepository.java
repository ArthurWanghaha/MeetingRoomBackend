package com.example.MeetingRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
    List<File> findByBookingId(String bookingId);
}
