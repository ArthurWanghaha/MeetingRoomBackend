package com.example.MeetingRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("bookingId") String bookingId, @RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                fileService.saveFile(bookingId, file);
            }
            return new ResponseEntity<>("Files uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        File file = fileService.getFileById(id);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getTitle());
        return new ResponseEntity<>(file.getContent().getData(), headers, HttpStatus.OK);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<File>> getFilesByBookingId(@PathVariable String bookingId) {
        List<File> files = fileService.getFilesByBookingId(bookingId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        try {
            fileService.deleteFile(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
