package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestParam String name) {
        Team team = teamService.createTeam(name);
        return new ResponseEntity<>(team, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTeam(@PathVariable String id) {
        try {
            teamService.deleteTeam(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member/{username}")
    public ResponseEntity<List<Team>> getTeamsByMember(@PathVariable String username) {
        List<Team> teams = teamService.getTeamsByMember(username);
        return ResponseEntity.ok(teams);
    }

    @PutMapping("/addMember/{id}")
    public ResponseEntity<Team> addMemberToTeam(@PathVariable String id, @RequestParam String member) {
        try {
            Team updatedTeam = teamService.addMemberToTeam(id, member);
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/removeMember/{id}")
    public ResponseEntity<Team> removeMemberFromTeam(@PathVariable String id, @RequestParam String member) {
        try {
            Team updatedTeam = teamService.removeMemberFromTeam(id, member);
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        try {
            List<Team> teams = teamService.getAllTeams();
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable String id) {
        Optional<Team> teamData = teamService.getTeamById(id);

        if (teamData.isPresent()) {
            return new ResponseEntity<>(teamData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Team> getTeamByName(@PathVariable String name) {
        Optional<Team> teamData = teamService.getTeamByName(name);

        if (teamData.isPresent()) {
            return new ResponseEntity<>(teamData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Team>> getTeamsByUser(@PathVariable String username) {
        List<Team> teams = teamService.getTeamsByMember(username);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}
