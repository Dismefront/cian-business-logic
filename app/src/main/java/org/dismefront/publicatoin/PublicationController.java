package org.dismefront.publicatoin;

import org.dismefront.api.CreatePublicationReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @PostMapping("/create")
    public ResponseEntity createPublication(@RequestBody CreatePublicationReq req) {
        req.setPublicationDate(new Date());
        Publication savedPublication = publicationService.createPublication(req);
        return ResponseEntity.ok(savedPublication);
    }

    @GetMapping
    public ResponseEntity<Page<Publication>> listPublications(Pageable pageable) {
        Page<Publication> publications = publicationService.listPublications(pageable);
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id) {
        Publication publication = publicationService.getPublicationById(id);
        return ResponseEntity.ok(publication);
    }
}
