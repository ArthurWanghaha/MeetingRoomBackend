package com.example.MeetingRoom;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // This allows all origins
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(user.getPassword())) {
            User foundUser = optionalUser.get();
            // Retrieve user details from the database
            String email = foundUser.getEmail();
            String userId = foundUser.getId(); // Assuming 'id' is the field for user ID in the User class

            // Generate token using retrieved email and ID
            String token = tokenProvider.generateToken(email, userId);

            // Return token in response body
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }




    @PostMapping("/{userId}/set-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = optionalUser.get();
            user.setProfilePicture(new Binary(file.getBytes())); // Set profile picture bytes
            userRepository.save(user);
            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }

    @GetMapping("/{userId}/user-details")
    public ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                user.getEmail(),
                user.getJobTitle(),
                user.getUsername(),
                user.getNumber(),
                user.getAddress(),
                user.getBio()
        );

        return ResponseEntity.ok(userDetailsDTO);
    }

    @GetMapping("/username/{username}/get-profile-picture")
    public ResponseEntity<byte[]> getProfilePictureByUsername(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        Binary profilePicture = user.getProfilePicture();
        if (profilePicture == null || profilePicture.length() == 0) {
            return ResponseEntity.notFound().build();
        }

        // Set the content type based on the file type (you may need to adjust this based on your requirements)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Convert the binary data to byte array
        byte[] imageData = profilePicture.getData();

        // Return the image data with appropriate headers
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }


    @GetMapping("/user-details/{username}")
    public ResponseEntity<UserDetailsDTO> getUserDetailsByUsername(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                user.getEmail(),
                user.getJobTitle(),
                user.getUsername(),
                user.getNumber(),
                user.getAddress(),
                user.getBio()
        );

        return ResponseEntity.ok(userDetailsDTO);
    }

    @PutMapping("/{userId}/update-user-details")
    public ResponseEntity<String> updateUserDetails(@PathVariable String userId, @RequestBody UserDetailsDTO userDetailsDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setEmail(userDetailsDTO.getEmail());
        user.setJobTitle(userDetailsDTO.getJobTitle());
        user.setUsername(userDetailsDTO.getUsername());
        user.setNumber(userDetailsDTO.getNumber());
        user.setAddress(userDetailsDTO.getAddress());
        user.setBio(userDetailsDTO.getBio());

        userRepository.save(user);

        return ResponseEntity.ok("User details updated successfully");
    }

    @GetMapping("/{userId}/get-profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        Binary profilePicture = user.getProfilePicture();
        if (profilePicture == null || profilePicture.length() == 0) {
            return ResponseEntity.notFound().build();
        }

        // Set the content type based on the file type (you may need to adjust this based on your requirements)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Convert the binary data to byte array
        byte[] imageData = profilePicture.getData();

        // Return the image data with appropriate headers
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}
