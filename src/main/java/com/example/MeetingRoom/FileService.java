package com.example.MeetingRoom;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public String saveFile(String bookingId, MultipartFile file) throws IOException {
        File newFile = new File();
        newFile.setTitle(file.getOriginalFilename());
        newFile.setBookingId(bookingId);
        newFile.setContent(new Binary(file.getBytes()));
        File savedFile = fileRepository.save(newFile);
        return savedFile.getId();
    }

    public File getFileById(String id) {
        Optional<File> fileOptional = fileRepository.findById(id);
        return fileOptional.orElse(null);
    }

    public List<File> getFilesByBookingId(String bookingId) {
        return fileRepository.findByBookingId(bookingId);
    }

    public void deleteFile(String id) {
        fileRepository.deleteById(id);
    }
}
