package com.VOX_WEBSITE.vox.Requests;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshJWTRequest {
    private String refreshToken;
}
