package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import ua.smartsub.smartsub.audit.DateAudit;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity(name = "REFRESH_TOKEN")
public class RefreshToken  {

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String token;

    @Column(name = "REFRESH_COUNT")
    private Long refreshCount;

    @Column(name = "EXPIRY_DT", nullable = false)
    private Instant expiryDate;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_DEVICE_ID", unique = true)
    private UserDevice userDevice;

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }


}
