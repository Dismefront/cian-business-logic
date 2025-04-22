package org.dismefront.photo;

import org.dismefront.photo.dto.PhotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping
    public ResponseEntity<Photo> uploadPhoto(@RequestBody PhotoDTO dto) {
        Photo savedPhoto = photoService.uploadPhoto(dto);
        return ResponseEntity.ok(savedPhoto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable Long id) {
        Photo photo = photoService.getPhotoById(id);
        return ResponseEntity.ok(photo);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<Photo>> getAllPhotos() {
        List<Photo> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }
}
