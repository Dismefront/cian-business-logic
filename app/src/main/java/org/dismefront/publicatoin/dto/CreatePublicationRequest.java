package org.dismefront.publicatoin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dismefront.location.dto.LocationDTO;
import org.dismefront.photo.dto.PhotoDTO;
import org.dismefront.publicatoin.PublicationType;
import org.dismefront.publicatoin.RealEstateType;
import org.dismefront.requests.dto.PaymentRequestDTO;

import java.util.List;

@Data
public class CreatePublicationRequest {

    @JsonProperty(required = true)
    private PublicationType publicationType;

    @JsonProperty(required = true)
    private RealEstateType realEstateType;

    @JsonProperty(required = true)
    private LocationDTO location;

    @JsonProperty(required = true)
    private List<PhotoDTO> photos;

    private PaymentRequestDTO paymentRequest;

}
