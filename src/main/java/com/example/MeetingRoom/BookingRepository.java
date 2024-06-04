package com.example.MeetingRoom;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByDate(String date);

    List<Booking> findByBooker(String booker);
    List<Booking> findByMeetingRoomAndDate(String meetingRoom, String date);

    @Query("{ $or: [ { 'booker': ?0 }, { 'participants': ?0 } ] }")
    List<Booking> findByBookerOrParticipants(String username);

    List<Booking> findByTeamsContaining(String name);
}