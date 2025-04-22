package org.dismefront.requests.dto;

import lombok.Data;
import org.dismefront.publicatoin.PublicationPriority;

@Data
public class UpgradePriorityRequestDTO {
    private PublicationPriority priority;
}
