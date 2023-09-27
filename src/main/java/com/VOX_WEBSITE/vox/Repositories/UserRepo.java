package com.VOX_WEBSITE.vox.Repositories;


import com.VOX_WEBSITE.vox.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    //jpa database request (to find user by his email)
    Optional<User> findByEmail(String email);

}
