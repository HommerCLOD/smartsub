package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Subs_tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column( unique=true)
    private int id_users;
    private String bot_url;
    private String bot_access_token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date byu_current_time;
    private int telegram_user_id;
    private Date sub_end_time;
}
