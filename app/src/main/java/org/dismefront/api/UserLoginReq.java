package org.dismefront.api;

import lombok.Data;

@Data
public class UserLoginReq {
    private String phoneNumber;
    private String password;
}
