package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column( unique=true)
    private String username;
    @NotBlank
    @Column( unique=true)
    private String email;
    @NotBlank
    private String password;
    private String telegram_user_id;
    private Boolean status_active_account;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Subs_tokens> subs_tokens = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;


}
