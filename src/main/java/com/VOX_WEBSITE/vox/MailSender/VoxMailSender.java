package com.VOX_WEBSITE.vox.MailSender;



import com.VOX_WEBSITE.vox.Entities.User;
import com.VOX_WEBSITE.vox.Requests.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class VoxMailSender {

    @Autowired
    private JavaMailSender javaMailSender;



    public void sendMail(@NonNull RegisterRequest registerRequest) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();


            message.setFrom(new InternetAddress("i.pasieka@vox-alpha.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, registerRequest.getEmail());
            message.setSubject("Registration message from VOX developers team");


            String htmlContent = "<h1>Thank you " + registerRequest.getFirstName()
                    + " " + registerRequest.getSecondName() + " for using VOX ( • ω • )✧ </h1><br>" +
                    "<p>If you have any questions, bugs, problems or ideas," +
                    " write on email : contact@vox-alpha.com </p>";
            message.setContent(htmlContent, "text/html; charset=utf-8");


        javaMailSender.send(message);
    }
}
