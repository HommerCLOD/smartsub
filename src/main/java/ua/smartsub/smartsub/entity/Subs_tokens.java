package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Subs_tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private int id_users;
    @NotBlank
    private String bot_url;
    @NotBlank
    private String bot_access_token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date byu_current_time;
    private int telegram_user_id;
    private Date sub_end_time;
}
