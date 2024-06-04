package com.example.MeetingRoom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    private String booker;
    private String meetingTitle;
    private String meetingRoom;
    private String date; // Store date as string in format YYYYMMDD
    private String meetingDescription;
    private String startingTime; // Store as string in format HHmm
    private String endingTime;   // Store as string in format HHmm
    private List<byte[]> uploadedFiles;
    private List<String> participants;

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    private List<String> teams;

    private List<String> fileIds = new ArrayList<>();

    // Constructors

    public Booking() {
        // Default constructor
    }

    public Booking(String booker, String meetingTitle, String meetingRoom, String date,
                   String meetingDescription, String startingTime, String endingTime,
                   List<byte[]> uploadedFiles, List<String> participants, List<String> teams) {
        this.booker = booker;
        this.meetingTitle = meetingTitle;
        this.meetingRoom = meetingRoom;
        this.date = date;
        this.meetingDescription = meetingDescription;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.uploadedFiles = uploadedFiles;
        this.participants = participants;
        this.teams = teams;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooker() {
        return booker;
    }

    public void setBooker(String booker) {
        this.booker = booker;
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

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public void setMeetingDescription(String meetingDescription) {
        this.meetingDescription = meetingDescription;
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

    public List<byte[]> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<byte[]> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
}
