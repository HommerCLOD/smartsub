package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.smartsub.smartsub.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column( unique=true)
    private String username;
    @NotBlank
    @Column( unique=true)
    private String email;
    @NotBlank
    private String password;
    private String telegram_user_id;
    @Column(name = "IS_EMAIL_VERIFIED", nullable = false)
    private Boolean emailVerified;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Subs_tokens> subs_tokens = new HashSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;


}
