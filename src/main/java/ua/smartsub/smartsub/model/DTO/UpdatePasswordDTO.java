package ua.smartsub.smartsub.model.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "Old password must not be blank")
    @ApiModelProperty(value = "Valid current user password", required = true, allowableValues = "NonEmpty String")
    private String oldPassword;

    @NotBlank(message = "New password must not be blank")
    @ApiModelProperty(value = "Valid new password string", required = true, allowableValues = "NonEmpty String")
    private String newPassword;
}
