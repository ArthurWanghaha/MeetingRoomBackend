package com.example.MeetingRoom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invitations")
public class Invitation {

    @Id
    private String id;
    private String bookingId;
    private String participant; // Username of the participant
    private String booker; // Booker of the meeting
    private String status; // e.g., "Pending", "Accepted", "Declined"
    private String meetingTitle;
    private String meetingRoom;
    private String date;
    private String startingTime;
    private String endingTime;

    // Constructors

    public Invitation() {
        // Default constructor
    }

    public Invitation(String bookingId, String participant, String booker, String status,
                      String meetingTitle, String meetingRoom, String date,
                      String startingTime, String endingTime) {
        this.bookingId = bookingId;
        this.participant = participant;
        this.booker = booker;
        this.status = status;
        this.meetingTitle = meetingTitle;
        this.meetingRoom = meetingRoom;
        this.date = date;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getBooker() {
        return booker;
    }

    public void setBooker(String booker) {
        this.booker = booker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(String meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }
}
