package org.dismefront.api;

import lombok.Data;

@Data
public class UserRegisterReq {
    private String phoneNumber;
    private String password;
    private String name;
    private String surname;
}
