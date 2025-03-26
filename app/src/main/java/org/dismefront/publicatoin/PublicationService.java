package org.dismefront.publicatoin;

import org.dismefront.api.CreatePublicationReq;
import org.dismefront.payment.Payment;
import org.dismefront.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private UserRepository userRepository;

    public Publication createPublication(CreatePublicationReq request) {
        Publication publication = new Publication();

        publication.setPublicationDate(new Date());
        publication.setCreatedBy(userRepository.findById(request.getCreatedBy()).orElse(null));
        publication.setPublicationType(request.getPublicationType());
        publication.setApFloor(request.getApFloor());
        publication.setApartmentNumber(request.getApartmentNumber());
        publication.setApartmentType(request.getApartmentType());
        publication.setLivingArea(request.getLivingArea());
        publication.setLocation(request.getLocation());
        publication.setRealEstateType(request.getRealEstateType());
        return publicationRepository.save(publication);
    }

    public Page<Publication> listPublications(Pageable pageable) {
        List<Publication> publications = publicationRepository.findAll();

        publications.sort(Comparator
                .comparingDouble((Publication p) -> p.getPayments().stream().mapToDouble(Payment::getAmount).sum()).reversed()
                .thenComparing(Publication::getPublicationDate)
        );

        return publicationRepository.findAll(pageable);
    }

    public Publication getPublicationById(Long id) {
        return publicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Publication not found"));
    }
}
