package org.dismefront.publicatoin;

import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.payment.Payment;
import org.dismefront.photo.Photo;
import org.dismefront.user.User;

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
    private String publicationType;

    @Column(name = "real_estate_type", nullable = false)
    private String realEstateType;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "ap_floor")
    private Integer apFloor;

    @Column(name = "number_of_floors")
    private Integer numberOfFloors;

    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "apartment_type")
    private String apartmentType;

    @Column(name = "living_area")
    private Double livingArea;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @Column(name = "publication_date", nullable = false)
    private Date publicationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
}