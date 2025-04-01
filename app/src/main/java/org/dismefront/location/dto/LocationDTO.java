package org.dismefront.location.dto;

import lombok.Data;

@Data
public class LocationDTO {
    private Long cityId;

    private String district;

    private String buildingNumber;

    private Long apartmentFloor;

    private Long buildingFloorNumber;

    private Long apartmentNumber;

    private Double livingArea;
}
