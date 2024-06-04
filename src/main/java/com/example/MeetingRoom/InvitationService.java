package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    public Invitation sendInvitation(String bookingId, String participant, String booker,
                                     String meetingTitle, String meetingRoom, String date,
                                     String startingTime, String endingTime) {
        Invitation invitation = new Invitation(bookingId, participant, booker, "Pending",
                meetingTitle, meetingRoom, date,
                startingTime, endingTime);
        return invitationRepository.save(invitation);
    }

    public List<Invitation> getInvitationsByBookingId(String bookingId) {
        return invitationRepository.findByBookingId(bookingId);
    }

    public List<Invitation> getInvitationsByParticipant(String participant) {
        return invitationRepository.findByParticipant(participant);
    }

    public Invitation updateInvitationStatus(String id, String status) {
        Optional<Invitation> optionalInvitation = invitationRepository.findById(id);
        if (!optionalInvitation.isPresent()) {
            throw new RuntimeException("Invitation not found");
        }
        Invitation existingInvitation = optionalInvitation.get();
        existingInvitation.setStatus(status);
        return invitationRepository.save(existingInvitation);
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<Invitation> getAcceptedFutureInvitations(String participant) {
        LocalDate today = LocalDate.now();
        return invitationRepository.findByParticipantAndStatus(participant, "Accepted")
                .stream()
                .filter(invitation -> {
                    LocalDate invitationDate = LocalDate.parse(invitation.getDate(), dateFormatter);
                    return invitationDate.isAfter(today) || invitationDate.isEqual(today);
                })
                .collect(Collectors.toList());
    }

    public List<Invitation> getFutureMeetingsForUser(String username) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
        LocalDateTime now = LocalDateTime.now();

        List<Invitation> allInvitations = invitationRepository.findByParticipant(username);
        allInvitations.addAll(invitationRepository.findByBooker(username));

        return allInvitations.stream()
                .filter(invitation -> "Accepted".equals(invitation.getStatus()) || invitation.getBooker().equals(username))
                .filter(invitation -> {
                    String dateTimeString = invitation.getDate() + " " + invitation.getStartingTime();
                    LocalDateTime meetingDateTime = LocalDateTime.parse(dateTimeString, formatter);
                    return meetingDateTime.isAfter(now);
                })
                .collect(Collectors.toList());
    }
}
