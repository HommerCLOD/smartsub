package ua.smartsub.smartsub.DTO;

import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtAuthenticationDTO {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiryDuration;
}
