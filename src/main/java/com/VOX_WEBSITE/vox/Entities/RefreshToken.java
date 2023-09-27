package com.VOX_WEBSITE.vox.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens_table")
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //user email
    @Column(unique = true)
    private String userEmail;
    //refresh token
    private String refreshToken;


}
