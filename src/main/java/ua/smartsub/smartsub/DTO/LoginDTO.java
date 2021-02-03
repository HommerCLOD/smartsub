package ua.smartsub.smartsub.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginDTO {
    @NotEmpty(message = "Login Username can be null but not blank")
    @ApiModelProperty(value = "Registered username", allowableValues = "NonEmpty String", allowEmptyValue = false, required = true)
    private String username;

    @NotEmpty(message = "Login Email can be null but not blank")
    @ApiModelProperty(value = "User registered email", allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Login password cannot be blank")
    @ApiModelProperty(value = "Valid user password", required = true, allowableValues = "NonEmpty String")
    private String password;

    @Valid
    @NotNull(message = "Device info cannot be null")
    @ApiModelProperty(value = "Device info", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;

}
