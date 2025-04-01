package org.dismefront.photo;

import org.dismefront.photo.dto.PhotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
