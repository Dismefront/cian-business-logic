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
}
