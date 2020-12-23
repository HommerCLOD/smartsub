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
public class JWT_tokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column( unique=true)
    private int id_users;
    private String access_token;
    private String refresh_token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at_time;
}
