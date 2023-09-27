package com.VOX_WEBSITE.vox.Repositories;

import com.VOX_WEBSITE.vox.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {


    Optional<RefreshToken> findRefreshTokenByUserEmail(String email);
    @Modifying
    @Query("delete from RefreshToken token where token.userEmail = ?1")
    void deleteByUserEmail(String email);
}
