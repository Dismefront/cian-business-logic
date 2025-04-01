package org.dismefront.publicatoin;

import org.dismefront.moderation.AIModerationService;
import org.dismefront.publicatoin.dto.CreatePublicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @Autowired
    AIModerationService aiModerationService;

    @PostMapping("/create")
    public ResponseEntity createPublication(@RequestBody CreatePublicationRequest req, Principal principal) {
        return ResponseEntity.ok(publicationService.createPublication(req, principal.getName()));
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Publication> approvePublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.approvePublication(id));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Publication> rejectPublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.rejectPublication(id));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Publication> deactivatePublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.deactivatePublication(id));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Publication> activatePublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.activatePublication(id));
    }
}
