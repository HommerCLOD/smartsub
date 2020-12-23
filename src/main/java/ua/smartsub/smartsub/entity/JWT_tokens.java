package ua.smartsub.smartsub.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class JWT_tokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column( unique=true, nullable=false)
    private int id_users;
    @Column(nullable=false)
    private String access_token;
    @Column(nullable=false)
    private String refresh_token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at_time;
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date create_at_date;
}
