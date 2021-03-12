package ua.smartsub.smartsub.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Subs_tokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "BOT_URL")
    private String bot_url;

    @NotBlank
    @Column(name = "BOT_ACCESS_TOKEN")
    private String bot_access_token;

    //    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BYU_CURRENT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime byu_current_time;

    @Column(name = "TELEGRAM_USER_ID")
    private int telegram_user_id;

    @Column(name = "SUB_END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime sub_end_time;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> user = new HashSet<>();

}
