package com.VOX_WEBSITE.vox.Controller;


import com.VOX_WEBSITE.vox.MailSender.VoxMailSender;
import com.VOX_WEBSITE.vox.Requests.AuthenticationRequest;
import com.VOX_WEBSITE.vox.Requests.RefreshJWTRequest;
import com.VOX_WEBSITE.vox.Requests.RegisterRequest;
import com.VOX_WEBSITE.vox.Services.AuthenticationService;
import com.VOX_WEBSITE.vox.Controller.authentication.AuthResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//here i have to write domains which will use
@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    private final VoxMailSender mailSender;

    //user login endpoint /api/v1/auth/login
    //send user email and password
    //after login return access and refresh tokens
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthenticationRequest authRequest) {
        return ResponseEntity.ok(authenticationService.userAuthentication(authRequest));
    }

    // user registration endpoint /api/v1/auth/register here you have to send
    // user first and second name, email, password, and country
    // after registration return 2 tokens (access and refresh)
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {

        try {
            mailSender.sendMail(registerRequest);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(authenticationService.userRegistration(registerRequest));
    }

    // in this endpoint : /api/v1/auth/refresh
    // this endpoint return new access token and non expired refresh token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> getNewAccessToken(@RequestBody RefreshJWTRequest request) {
        return ResponseEntity.ok(authenticationService.refreshAccessToken(request.getRefreshToken()));
    }
}
