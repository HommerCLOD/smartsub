package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.smartsub.smartsub.audit.DateAudit;
import ua.smartsub.smartsub.model.TokenStatus;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class EmailVerificationToken  {
    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_token_seq")
    @SequenceGenerator(name = "email_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

    @Column(name = "TOKEN_STATUS")
    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;

    @Column(name = "EXPIRY_DT", nullable = false)
    private Instant expiryDate;

    public void setConfirmedStatus() {
        setTokenStatus(TokenStatus.STATUS_CONFIRMED);
    }

}
