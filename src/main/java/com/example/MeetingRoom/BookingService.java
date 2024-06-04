package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TeamService teamService;

    public Booking updateBooking(String id, Booking bookingDetails) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (!optionalBooking.isPresent()) {
            throw new RuntimeException("Booking not found");
        }
        Booking existingBooking = optionalBooking.get();
        existingBooking.setBooker(bookingDetails.getBooker());
        existingBooking.setMeetingTitle(bookingDetails.getMeetingTitle());
        existingBooking.setMeetingRoom(bookingDetails.getMeetingRoom());
        existingBooking.setDate(bookingDetails.getDate());
        existingBooking.setMeetingDescription(bookingDetails.getMeetingDescription());
        existingBooking.setStartingTime(bookingDetails.getStartingTime());
        existingBooking.setEndingTime(bookingDetails.getEndingTime());
        existingBooking.setUploadedFiles(bookingDetails.getUploadedFiles());
        existingBooking.setParticipants(bookingDetails.getParticipants());
        existingBooking.setTeams(bookingDetails.getTeams());
        return bookingRepository.save(existingBooking);
    }

    public List<Booking> getBookingsForUser(String username) {
        List<Booking> userBookings = bookingRepository.findByBookerOrParticipants(username);
        List<Team> userTeams = teamService.getTeamsByMember(username);

        for (Team team : userTeams) {
            List<Booking> teamBookings = bookingRepository.findByTeamsContaining(team.getName());
            for (Booking booking : teamBookings) {
                if (!userBookings.contains(booking)) {
                    userBookings.add(booking);
                }
            }
        }
        return userBookings;
    }
    public boolean isRoomAvailable(String roomName, String date, String startingTime, String endingTime) {
        List<Booking> bookings = bookingRepository.findByMeetingRoomAndDate(roomName, date);
        for (Booking booking : bookings) {
            if (!(endingTime.compareTo(booking.getStartingTime()) <= 0 || startingTime.compareTo(booking.getEndingTime()) >= 0)) {
                return false;
            }
        }
        return true;
    }
}