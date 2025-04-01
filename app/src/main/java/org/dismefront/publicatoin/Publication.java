package org.dismefront.publicatoin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.location.Location;
import org.dismefront.photo.Photo;
import org.dismefront.requests.PaymentRequest;
import org.dismefront.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "blps_publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publication_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PublicationType publicationType;

    @Column(name = "real_estate_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RealEstateType realEstateType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @JsonManagedReference
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publication")
    @JsonManagedReference
    private List<Photo> photos = new ArrayList<>();

    @Column(name = "publication_date", nullable = false)
    private Date publicationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User createdBy;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "publication_priority")
    @Enumerated(EnumType.STRING)
    private PublicationPriority publicationPriority;

    @Column(name = "priority_request")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publication")
    @JsonBackReference
    private List<PaymentRequest> paymentRequests = new ArrayList<>();

    public Date getExpirationDate() {
        if (publicationType == PublicationType.DAILY_RENT) {
            return new Date(publicationDate.getTime() + 365L * 24 * 60 * 60 * 1000);
        } else if (publicationType == PublicationType.MONTHLY_RENT) {
            return new Date(publicationDate.getTime() + 150L * 24 * 60 * 60 * 1000);
        }
        else if (publicationType == PublicationType.SELL) {
            return new Date(publicationDate.getTime() + 30L * 24 * 60 * 60 * 1000);
        }
        return null;
    }

    public Boolean isExpired() {
        Date expirationDate = getExpirationDate();
        if (expirationDate != null) {
            return new Date().after(expirationDate);
        }
        return false;
    }
}