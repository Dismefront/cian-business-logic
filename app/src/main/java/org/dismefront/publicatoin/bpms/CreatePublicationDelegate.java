package org.dismefront.publicatoin.bpms;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.dismefront.location.dto.LocationDTO;
import org.dismefront.order.OrderPlacement;
import org.dismefront.publicatoin.PublicationPriority;
import org.dismefront.publicatoin.PublicationService;
import org.dismefront.publicatoin.PublicationType;
import org.dismefront.publicatoin.RealEstateType;
import org.dismefront.publicatoin.dto.CreatePublicationRequest;
import org.dismefront.requests.dto.PaymentRequestDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("createPublicationDelegate")
public class CreatePublicationDelegate implements JavaDelegate {

    private final PublicationService publicationService;

    public CreatePublicationDelegate(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("textfield_phoneNumber");

        System.out.println("Debug create publication delegate: " + execution.getVariables());

        CreatePublicationRequest request = new CreatePublicationRequest();
        request.setPublicationType(PublicationType.valueOf(execution.getVariable("select_33nnt6").toString()));
        request.setRealEstateType(RealEstateType.valueOf(execution.getVariable("select_nv4eck").toString()));

        LocationDTO location = new LocationDTO();
        location.setCityId(Long.parseLong(execution.getVariable("number_6c72j").toString()));
        location.setDistrict((String) execution.getVariable("textfield_0kxkni"));
        location.setBuildingNumber((String) execution.getVariable("textfield_zirokq"));
        location.setApartmentFloor(Long.parseLong(execution.getVariable("number_t187it").toString()));
        location.setBuildingFloorNumber(Long.parseLong(execution.getVariable("number_x97qz").toString()));
        location.setApartmentFloor(Long.parseLong(execution.getVariable("number_9dtall").toString()));
        location.setLivingArea(Double.parseDouble(execution.getVariable("textfield_dkafwr").toString()));

        request.setLocation(location);
        request.setPhotos(new ArrayList<>());

        Boolean checked_pay = (Boolean) execution.getVariable("checkbox_jnp3uh");
        if (checked_pay != null && checked_pay) {
            PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
            paymentRequest.setRequestedPriority(PublicationPriority.valueOf(execution.getVariable("select_zpmxii").toString()));
            request.setPaymentRequest(paymentRequest);
        }

        OrderPlacement orderPlacement = publicationService.createPublication(request, username);

        if (orderPlacement != null) {
            execution.setVariable("orderId", orderPlacement.getId());
            execution.setVariable("orderUUID", orderPlacement.getOrderUUID());
            execution.setVariable("orderStatus", orderPlacement.getStatus().name());
            execution.setVariable("expectedAmount", orderPlacement.getExpectedAmount());
        }
    }
}