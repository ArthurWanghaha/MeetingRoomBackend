package com.example.MeetingRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface InvitationRepository extends MongoRepository<Invitation, String> {
    List<Invitation> findByBookingId(String bookingId);
    List<Invitation> findByParticipant(String participant);

    List<Invitation> findByParticipantAndStatus(String participant, String status);

    Collection<? extends Invitation> findByBooker(String username);
}
