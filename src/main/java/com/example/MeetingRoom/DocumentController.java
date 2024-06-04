package com.example.MeetingRoom;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/api/v1/movies")
@CrossOrigin(origins = "*") // This allows all origins
public class DocumentController {
    @Autowired
    private DocumentService service;

    @PostMapping("/documents/add")
    public String addDocument(@RequestParam("title") String title,
                              @RequestParam("folderName") String folderName,
                              @RequestParam("document") MultipartFile documents,
                              Model model) throws IOException {
        String id = service.addDocuments(title, folderName, documents);
        return "redirect:/documents/" + id;
    }

    @GetMapping("/documents/folder/{folderName}")
    public ResponseEntity<List<Documents>> getDocumentsByFolder(@PathVariable String folderName) {
        List<Documents> documents = service.getDocumentsByFolder(folderName);
        return ResponseEntity.ok(documents);
    }


    @GetMapping("/documents/{id}")
    public String getDocument(@PathVariable String id, Model model) {
        Documents document = service.getPhoto(id);
        model.addAttribute("title", document.getTitle());
        model.addAttribute("documents",
                Base64.getEncoder().encodeToString(document.getDocuments().getData()));
        return "documents";
    }

    @GetMapping("/documents/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String id) {
        Documents document = service.getPhoto(id);

        try {
            // Convert byte array to a string
            String encodedData = Base64.getEncoder().encodeToString(document.getDocuments().getData());
            // Remove newline characters from the string
            String cleanEncodedData = encodedData.replaceAll("[\r\n]", "");
            // Decode the modified string
            byte[] documentData = Base64.getDecoder().decode(cleanEncodedData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Assuming it's a PDF document
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(document.getTitle() + ".pdf").build());

            // Return the document data in the ResponseEntity
            return ResponseEntity.ok().headers(headers).body(documentData);
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            e.printStackTrace(); // Log the error for debugging
            // Provide a meaningful error message to the user
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }



}
