package com.VOX_WEBSITE.vox.Message;

import com.VOX_WEBSITE.vox.Entities.User;
import com.VOX_WEBSITE.vox.Repositories.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepo messageRepo;

    public boolean saveMessage(MessageRequest messageRequest, User user){

        if(messageRequest != null && user != null){
            var message = MessageResp.builder()
                    .user(user)
                    .message(messageRequest.getMessage())
                    .build();
            messageRepo.save(message);
            return  true;
        }else {
            return false;
        }
    }


}
