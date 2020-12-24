package ua.smartsub.smartsub.services.implentation;

import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.validator.FieldValueExists;

public interface IAuthService  {
    User registrationUser(User user);

//    JwtAuthenticationResponse loginUser(LoginRequest loginRequest);
//
//    void sendMailForRecoverPassword (String email);
//
//    void checkTokenForRecoverPassword(RecoverUserPasswordRequest recoverUserPasswordRequest);
//
//    void recoverPassword(RecoverUserPasswordRequest recoverUserPasswordRequest);

    void activateAccount(String token);
}
