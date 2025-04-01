package org.dismefront.publicatoin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.dismefront.location.LocationService;
import org.dismefront.order.dto.OrderPlacement;
import org.dismefront.photo.Photo;
import org.dismefront.publicatoin.dto.CreatePublicationRequest;
import org.dismefront.requests.PaymentRequestService;
import org.dismefront.user.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Objects;


@Service
public class PublicationService {

    @Autowired
    private AppUserDetailsService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PaymentRequestService paymentRequestService;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PublicationRepository publicationRepository;

    public OrderPlacement createPublication(CreatePublicationRequest request, String username) {
        OrderPlacement orderPlacement = null;
        Publication publication = new Publication();
        publication.setPublicationType(request.getPublicationType());
        publication.setRealEstateType(request.getRealEstateType());
        publication.setPublicationDate(new Date(System.currentTimeMillis()));
        publication.setCreatedBy(userService.getByUsername(username));
        publication.setPublicationPriority(PublicationPriority.STANDARD);

        publication.setLocation(locationService.createLocation(request.getLocation()));
        request.getPhotos().forEach(photo -> {
            publication.getPhotos().add(entityManager.getReference(Photo.class, photo.getId()));
        });

        publication.setIsApproved(false);

        Publication savedPublication = publicationRepository.save(publication);

        PublicationPriority priority = Objects.nonNull(request.getPaymentRequest()) ? request.getPaymentRequest().getRequestedPriority() : null;
        if (priority != null && priority != PublicationPriority.STANDARD) {
            request.getPaymentRequest().setPublicationId(savedPublication.getId());
            orderPlacement = paymentRequestService.createPaymentRequest(request.getPaymentRequest());
        }

        return orderPlacement;
    }

    public Publication approvePublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        publication.setIsApproved(true);
        return publicationRepository.save(publication);
    }

}
