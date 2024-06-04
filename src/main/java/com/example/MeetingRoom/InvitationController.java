package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitations")
@CrossOrigin(origins = "*")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/send")
    public ResponseEntity<Invitation> sendInvitation(@RequestParam String bookingId,
                                                     @RequestParam String participant,
                                                     @RequestParam String booker,
                                                     @RequestParam String meetingTitle,
                                                     @RequestParam String meetingRoom,
                                                     @RequestParam String date,
                                                     @RequestParam String startingTime,
                                                     @RequestParam String endingTime) {
        Invitation invitation = invitationService.sendInvitation(bookingId, participant, booker, meetingTitle, meetingRoom, date, startingTime, endingTime);
        return new ResponseEntity<>(invitation, HttpStatus.CREATED);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Invitation>> getInvitationsByBookingId(@PathVariable String bookingId) {
        List<Invitation> invitations = invitationService.getInvitationsByBookingId(bookingId);
        return new ResponseEntity<>(invitations, HttpStatus.OK);
    }

    @GetMapping("/participant/{participant}")
    public ResponseEntity<List<Invitation>> getInvitationsByParticipant(@PathVariable String participant) {
        List<Invitation> invitations = invitationService.getInvitationsByParticipant(participant);
        return new ResponseEntity<>(invitations, HttpStatus.OK);
    }

    @PutMapping("/update/{id}/{status}")
    public ResponseEntity<Invitation> updateInvitationStatus(
            @PathVariable String id,
            @PathVariable String status) {
        if (status == null || status.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Invitation updatedInvitation = invitationService.updateInvitationStatus(id, status);
        return new ResponseEntity<>(updatedInvitation, HttpStatus.OK);
    }

    @GetMapping("/participant/{participant}/accepted-future")
    public ResponseEntity<List<Invitation>> getAcceptedFutureInvitations(@PathVariable String participant) {
        List<Invitation> invitations = invitationService.getAcceptedFutureInvitations(participant);
        return new ResponseEntity<>(invitations, HttpStatus.OK);
    }

}
