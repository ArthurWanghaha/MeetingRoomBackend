package com.example.MeetingRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, String> {
    Optional<Team> findByName(String name);
    List<Team> findByMembersContaining(String username);
}
