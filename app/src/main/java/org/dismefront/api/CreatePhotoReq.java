package org.dismefront.api;

import lombok.Data;

@Data
public class CreatePhotoReq {
    private String title;
    private String s3Key;
    private Long publicationId;
}
