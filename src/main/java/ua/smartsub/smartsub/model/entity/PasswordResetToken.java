package ua.smartsub.smartsub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "PASSWORD_RESET_TOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordResetToken {

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pwd_reset_token_seq")
    @SequenceGenerator(name = "pwd_reset_token_seq", allocationSize = 1)
    private Long id;

    @NaturalId
    @Column(name = "TOKEN_NAME", nullable = false, unique = true)
    private String token;

    @Column(name = "EXPIRY_DT", nullable = false)
    private Instant expiryDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;
}
