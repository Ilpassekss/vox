package com.VOX_WEBSITE.vox.Services;

import com.VOX_WEBSITE.vox.Entities.RefreshToken;
import com.VOX_WEBSITE.vox.Entities.Role;
import com.VOX_WEBSITE.vox.Entities.User;
import com.VOX_WEBSITE.vox.Repositories.RefreshTokenRepo;
import com.VOX_WEBSITE.vox.Repositories.UserRepo;
import com.VOX_WEBSITE.vox.Requests.AuthenticationRequest;
import com.VOX_WEBSITE.vox.Requests.RegisterRequest;
import com.VOX_WEBSITE.vox.Controller.authentication.AuthResponse;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepo refreshTokenRepo;
    private final AuthenticationManager authenticationManager;


    public AuthResponse userRegistration(@NonNull RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .secondName(registerRequest.getSecondName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .country(registerRequest.getCountry())
                .build();

        userRepo.save(user);

        User userFromRepo = userRepo.findByEmail(registerRequest.getEmail()).get();

        var jwtAccessToken = jwtService.generateAccessToken(userFromRepo);

        var jwtRefreshToken = jwtService.generateRefreshToken(userFromRepo);

        var authRes = RefreshToken
                .builder()
                .refreshToken(jwtRefreshToken)
                .userEmail(userFromRepo.getEmail())
                .build();

        refreshTokenRepo.save(authRes);

        return AuthResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken).build();
    }


    @Transactional
    public AuthResponse userAuthentication(@NonNull AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));

        var user = userRepo.findByEmail(authenticationRequest.getEmail()).orElseThrow();


        if (refreshTokenRepo.findRefreshTokenByUserEmail(user.getEmail()).get().getRefreshToken() != null) {

            refreshTokenRepo.deleteByUserEmail(user.getEmail());

            var jwtAccessToken = jwtService.generateAccessToken(user);

            var jwtRefreshToken = jwtService.generateRefreshToken(user);

            var authRes = RefreshToken.builder().refreshToken(jwtRefreshToken).userEmail(user.getEmail()).build();

            refreshTokenRepo.save(authRes);

            return AuthResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken).build();
        } else {

            var jwtAccessToken = jwtService.generateAccessToken(user);

            var jwtRefreshToken = jwtService.generateRefreshToken(user);

            var authRes = RefreshToken.builder().refreshToken(jwtRefreshToken).userEmail(user.getEmail()).build();

            refreshTokenRepo.save(authRes);

            return AuthResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken).build();
        }
    }

    public AuthResponse refreshAccessToken(@NonNull String refreshToken) {
        if(jwtService.validateRefreshToken(refreshToken)){

           String email = jwtService.extractUsernameFromRefreshToken(refreshToken);
           String savedRefreshToken = refreshTokenRepo.findRefreshTokenByUserEmail(email).get().getRefreshToken();

            if(savedRefreshToken!=null&&savedRefreshToken.equals(refreshToken)){

                final var user = userRepo.findByEmail(email)
                        .orElseThrow(()->new AuthenticationServiceException("user is not found"));
                final String newAccessToken = jwtService.generateAccessToken(user);

                return AuthResponse.builder().accessToken(newAccessToken).refreshToken(refreshToken).build();
            }else {
                return null;
            }

        }else {
            return null;
        }
    }

    public Authentication getAuthInfo(){
        return  SecurityContextHolder.getContext().getAuthentication();
    }
}



