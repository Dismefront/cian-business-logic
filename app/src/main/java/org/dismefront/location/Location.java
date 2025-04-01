package org.dismefront.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.location.geography.City;
import org.dismefront.publicatoin.Publication;

@Entity
@Data
@Table(name = "blps_location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "apartment_floor")
    private Long apartmentFloor;

    @Column(name = "building_floor_number")
    private Long buildingFloorNumber;

    @Column(name = "apartment_number")
    private Long apartmentNumber;

    @Column(name = "living_area")
    private Double livingArea;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonBackReference
    private Publication publication;
}
