package com.VOX_WEBSITE.vox.Controller.authentication;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// jwt tokens responses (access and refresh tokens) Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private final String type = "Bearer ";

    private String accessToken;
    private String refreshToken;


}
