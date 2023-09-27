package com.VOX_WEBSITE.vox.Repositories;


import com.VOX_WEBSITE.vox.Message.MessageResp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepo extends JpaRepository<MessageResp, Integer> {

    Optional<MessageResp> findMessageByUserId(long userid);
}
