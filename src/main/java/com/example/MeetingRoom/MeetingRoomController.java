package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meeting-rooms")
@CrossOrigin(origins = "*") // This allows all origins
public class MeetingRoomController {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/all")
    public ResponseEntity<List<MeetingRoom>> getAllMeetingRooms() {
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        return new ResponseEntity<>(meetingRooms, HttpStatus.OK);
    }

    @PostMapping("/meeting-rooms")
    public ResponseEntity<MeetingRoom> addMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        try {
            MeetingRoom savedRoom = meetingRoomRepository.save(meetingRoom);
            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<MeetingRoom>> getAvailableMeetingRooms(
            @RequestParam String startingTime, // Format: "HHmm" (e.g., "0930" for 9:30 AM)
            @RequestParam String endingTime,   // Format: "HHmm" (e.g., "1100" for 11:00 AM)
            @RequestParam String date
    ) {
        try {
            LocalTime parsedStartTime = LocalTime.parse(startingTime, DateTimeFormatter.ofPattern("HHmm"));
            LocalTime parsedEndTime = LocalTime.parse(endingTime, DateTimeFormatter.ofPattern("HHmm"));

            List<Booking> bookings = bookingRepository.findByDate(date);

            List<MeetingRoom> availableMeetingRooms = new ArrayList<>();
            for (MeetingRoom meetingRoom : meetingRoomRepository.findAll()) {
                if (isMeetingRoomAvailable(meetingRoom, bookings, parsedStartTime, parsedEndTime)) {
                    availableMeetingRooms.add(meetingRoom);
                }
            }

            return new ResponseEntity<>(availableMeetingRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle invalid time format or other errors
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MeetingRoom> getMeetingRoomByName(@PathVariable("name") String name) {
        Optional<MeetingRoom> meetingRoomData = meetingRoomRepository.findByName(name);

        if (meetingRoomData.isPresent()) {
            return new ResponseEntity<>(meetingRoomData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private boolean isMeetingRoomAvailable(
            MeetingRoom meetingRoom,
            List<Booking> bookings,
            LocalTime startTime,
            LocalTime endTime
    ) {
        // Check for overlap with existing bookings
        for (Booking booking : bookings) {
            if (booking.getMeetingRoom().equals(meetingRoom.getName())) {
                LocalTime bookingStartTime = LocalTime.parse(booking.getStartingTime(), DateTimeFormatter.ofPattern("HHmm"));
                LocalTime bookingEndTime = LocalTime.parse(booking.getEndingTime(), DateTimeFormatter.ofPattern("HHmm"));

                // Check for overlap
                if (!(endTime.isBefore(bookingStartTime) || startTime.isAfter(bookingEndTime))) {
                    return false; // Overlap found
                }
            }
        }
        return true; // No overlap
    }
}
