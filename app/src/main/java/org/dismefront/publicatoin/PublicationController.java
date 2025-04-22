package org.dismefront.publicatoin;

import org.dismefront.publicatoin.dto.CreatePublicationRequest;
import org.dismefront.requests.dto.UpgradePriorityRequestDTO;
import org.dismefront.requests.exceptions.CannotUpgradePriorityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_STANDARD_USER')")
    public ResponseEntity createPublication(@RequestBody CreatePublicationRequest req, Principal principal) {
        return ResponseEntity.ok(publicationService.createPublication(req, principal.getName()));
    }

    @PutMapping("/upgrade-priority/{id}")
    @PreAuthorize("hasRole('ROLE_STANDARD_USER')")
    public ResponseEntity upgradePublicationPriority(@PathVariable Long id, @RequestBody UpgradePriorityRequestDTO body) {
        try {
            return ResponseEntity.ok(publicationService.upgradePublicationPriority(id, body.getPriority()));
        }
        catch (CannotUpgradePriorityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ResponseEntity<Publication> approvePublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.approvePublication(id));
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ResponseEntity<Publication> rejectPublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.rejectPublication(id));
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ROLE_STANDARD_USER')")
    public ResponseEntity<Publication> deactivatePublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.deactivatePublication(id));
    }

    @PutMapping("/activate/{id}")
    @PreAuthorize("hasRole('ROLE_STANDARD_USER')")
    public ResponseEntity<Publication> activatePublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.activatePublication(id));
    }
}
