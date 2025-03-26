package org.dismefront.api;

import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.payment.Payment;
import org.dismefront.photo.Photo;
import org.dismefront.user.User;

import java.util.Date;
import java.util.List;

@Data
public class CreatePublicationReq {

    private String publicationType;

    private String realEstateType;

    private String location;

    private Integer apFloor;

    private Integer numberOfFloors;

    private String apartmentNumber;

    private String apartmentType;

    private Double livingArea;

    private Date publicationDate;

    private Long createdBy;
}
