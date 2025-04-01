package org.dismefront.moderation;

import org.dismefront.publicatoin.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

//    @Autowired
//    private PublicationRepository publicationRepository;
//
//    @PutMapping("/approve/{id}")
//    public ResponseEntity<Publication> approvePublication(@PathVariable Long id) {
//        Publication publication = publicationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Publication not found"));
//        publication.setIsApproved(true);
//        Publication updatedPublication = publicationRepository.save(publication);
//        return ResponseEntity.ok(updatedPublication);
//    }
}