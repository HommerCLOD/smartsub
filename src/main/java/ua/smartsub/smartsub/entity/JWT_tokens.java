package ua.smartsub.smartsub.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime create_at_time;
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime create_at_date;
}
