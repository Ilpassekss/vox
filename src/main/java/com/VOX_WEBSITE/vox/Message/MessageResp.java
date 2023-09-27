package com.VOX_WEBSITE.vox.Message;


import com.VOX_WEBSITE.vox.Entities.User;
import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_table")
@Getter
public class MessageResp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user ;


}
