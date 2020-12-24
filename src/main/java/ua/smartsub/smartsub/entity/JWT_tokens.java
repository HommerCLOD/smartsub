package ua.smartsub.smartsub.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class JWT_tokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private int id_users;
    @NotBlank
    private String access_token;
    @NotBlank
    private String refresh_token;
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_at_time;
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date create_at_date;
}
