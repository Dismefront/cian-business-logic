package org.dismefront.photo;

import org.dismefront.photo.dto.PhotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo uploadPhoto(PhotoDTO dto) {
        Photo photo = new Photo();
        photo.setTitle(dto.getTitle());
        photo.setS3Key(UUID.randomUUID().toString());
        return photoRepository.save(photo);
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

}