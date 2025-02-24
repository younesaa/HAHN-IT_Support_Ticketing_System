package com.hahn.it_support_swing_ui.auth;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class JWTUser {
    private String token;
    private String username;
    private String role;
    private Date expiresIn;

}
