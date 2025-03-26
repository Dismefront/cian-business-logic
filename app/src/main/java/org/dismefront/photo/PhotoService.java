package org.dismefront.photo;

import org.dismefront.api.CreatePhotoReq;
import org.dismefront.publicatoin.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    public Photo uploadPhoto(CreatePhotoReq req) {
        Photo photo = new Photo();
        photo.setTitle(req.getTitle());
        photo.setS3Key(req.getS3Key());
        photo.setPublication(publicationRepository.findById(req.getPublicationId()).orElse(null));
        return photoRepository.save(photo);
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo not found"));
    }
}