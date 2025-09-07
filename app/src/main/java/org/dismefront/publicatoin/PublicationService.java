package org.dismefront.publicatoin;

import com.rosreestr.RRConnectionFactoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.dismefront.order.OrderPlacement;
import org.dismefront.location.LocationService;
import org.dismefront.moderation.AIModerationService;
import org.dismefront.photo.Photo;
import org.dismefront.publicatoin.dto.CreatePublicationRequest;
import org.dismefront.requests.PaymentRequestService;
import org.dismefront.requests.dto.PaymentRequestDTO;
import org.dismefront.requests.exceptions.CannotUpgradePriorityException;
import org.dismefront.user.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    @Autowired
    private AIModerationService aiModerationService;

    @Autowired(required = false)
    private Socket socket;

    public String sendRRMessage(String message) throws Exception{
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(message);
        return in.readLine();
    }

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
        try {
            sendRRMessage("New publication created with ID: " + savedPublication.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        PublicationPriority priority = Objects.nonNull(request.getPaymentRequest()) ? request.getPaymentRequest().getRequestedPriority() : null;
        if (priority != null && priority != PublicationPriority.STANDARD) {
            request.getPaymentRequest().setPublicationId(savedPublication.getId());
            orderPlacement = paymentRequestService.createPaymentRequest(request.getPaymentRequest());
        }

        return orderPlacement;
    }

    public OrderPlacement upgradePublicationPriority(Long id, PublicationPriority priority) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        if (publication.getPublicationPriority().ordinal() >= priority.ordinal()) {
            throw new CannotUpgradePriorityException("Cannot upgrade to a lower or equal priority");
        }
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setPublicationId(id);
        paymentRequestDTO.setRequestedPriority(priority);

        return paymentRequestService.createPaymentRequest(paymentRequestDTO);
    }

    public Publication approvePublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        publication.setIsApproved(true);
        return publicationRepository.save(publication);
    }

    public Publication rejectPublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        publication.setIsApproved(false);
        return publicationRepository.save(publication);
    }

    public Publication deactivatePublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        publication.setIsActive(false);
        return publicationRepository.save(publication);
    }

    public Publication activatePublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found"));
        publication.setIsActive(true);
        return publicationRepository.save(publication);
    }

    @Scheduled(fixedRate = 60000)
    public void moderatePublications() {
        System.out.println("Moderating publications...");
        publicationRepository.findAll().forEach(publication -> {
            if (!publication.getIsApproved()) {
                boolean approved = aiModerationService.moderatePublication(publication);
                publication.setIsApproved(approved);
                System.out.println(publication.getId() + " " + approved);
                publicationRepository.save(publication);
            }
        });
    }

}
