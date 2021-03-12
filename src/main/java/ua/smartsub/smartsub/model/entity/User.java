package ua.smartsub.smartsub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.smartsub.smartsub.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable=false, nullable=false)
    private Long id;

    @NotBlank
    @Column( name = "USERNAME",unique=true)
    private String username;

    @NotBlank
    @Email
    @Column(name ="EMAIL", unique=true)
    private String email;

    @NotBlank
    @Column(name ="PASSWORD")
    private String password;

    @Column(name ="TELEGRAM_USER_ID")
    private String telegram_user_id;

    @Column(name = "IS_EMAIL_VERIFIED", nullable = false)
    private Boolean emailVerified;

    @ManyToMany(fetch = FetchType.LAZY)
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Subs_tokens> subs_tokens = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;


}
