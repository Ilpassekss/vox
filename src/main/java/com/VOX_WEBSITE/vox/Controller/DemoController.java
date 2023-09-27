package com.VOX_WEBSITE.vox.Controller;


import com.VOX_WEBSITE.vox.Entities.User;
import com.VOX_WEBSITE.vox.Message.MessageService;
import com.VOX_WEBSITE.vox.Repositories.UserRepo;
import com.VOX_WEBSITE.vox.Message.MessageRequest;
import com.VOX_WEBSITE.vox.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class DemoController {

    private final AuthenticationService authenticationService;

    private final UserRepo userRepo ;

    private final MessageService messageService;

    // here you have to send access token and user request message.
    // if program save response in database return true , if program catch some problems return 400`s errors or false
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/sendResponse")
    public ResponseEntity<Boolean> sendResponse(@RequestBody  MessageRequest messageRequest){
        String email = authenticationService.getAuthInfo().getName();

        User user = userRepo.findByEmail(email).get();
        return ResponseEntity.ok(messageService.saveMessage(messageRequest, user));
    }


}
