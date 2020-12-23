package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column( unique=true, nullable = false)
    private String username;
    @Column( unique=true, nullable = false)
    private String email;
    @Column( nullable = false)
    private String password;
    private String telegram_user_id;
    private boolean status_active_account;
}
