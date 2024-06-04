package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*") // This allows all origins
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    TeamService teamService;

    @Autowired
    private InvitationService invitationService;


    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/room/{roomName}/date/{date}")
    public ResponseEntity<List<Booking>> getBookingsByRoomAndDate(
            @PathVariable String roomName,
            @PathVariable String date) {
        List<Booking> bookings = bookingRepository.findByMeetingRoomAndDate(roomName, date);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable String id,
            @RequestBody Booking bookingDetails) {
        // Removed the room availability check
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (!optionalBooking.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Booking updatedBooking = bookingService.updateBooking(id, bookingDetails);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    @GetMapping("/user/{username}/all")
    public ResponseEntity<List<Booking>> getBookingsForUser(@PathVariable String username) {
        List<Booking> bookings = bookingService.getBookingsForUser(username);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable String id) {
        try {
            bookingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable String username) {
        List<Booking> bookings = bookingRepository.findByBookerOrParticipants(username);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        if (!bookingService.isRoomAvailable(booking.getMeetingRoom(), booking.getDate(), booking.getStartingTime(), booking.getEndingTime())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict
        }
        Booking savedBooking = bookingRepository.save(booking);
        // Send invitations to all participants
        for (String participant : booking.getParticipants()) {
            invitationService.sendInvitation(savedBooking.getId(), participant,
                    booking.getBooker(), booking.getMeetingTitle(),
                    booking.getMeetingRoom(), booking.getDate(),
                    booking.getStartingTime(), booking.getEndingTime());
        }
        // Send invitations to all team members
        for (String teamName : booking.getTeams()) {

            List<String> teamMembers = teamService.getMembersByTeamName(teamName);
            for (String member : teamMembers) {
                invitationService.sendInvitation(savedBooking.getId(), member,
                        booking.getBooker(), booking.getMeetingTitle(),
                        booking.getMeetingRoom(), booking.getDate(),
                        booking.getStartingTime(), booking.getEndingTime());
            }
        }
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

}