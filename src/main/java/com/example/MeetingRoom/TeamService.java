package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team createTeam(String name) {
        Team team = new Team(name);
        return teamRepository.save(team);
    }

    public void deleteTeam(String id) {
        teamRepository.deleteById(id);
    }

    public Team addMemberToTeam(String teamId, String member) {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.getMembers().add(member);
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("Team not found");
        }
    }

    public List<Team> getTeamsByMember(String username) {
        return teamRepository.findAll().stream()
                .filter(team -> team.getMembers().contains(username))
                .collect(Collectors.toList());
    }

    public Team removeMemberFromTeam(String teamId, String member) {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.getMembers().remove(member);
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("Team not found");
        }
    }
    public List<Team> getTeamsByUser(String username) {
        return teamRepository.findByMembersContaining(username);
    }

    public List<String> getMembersByTeamName(String teamName) {
        Optional<Team> team = teamRepository.findByName(teamName);
        return team.map(Team::getMembers).orElse(List.of());
    }


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(String id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> getTeamByName(String name) {
        return teamRepository.findByName(name);
    }
}
