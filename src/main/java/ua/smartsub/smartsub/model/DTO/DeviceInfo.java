package ua.smartsub.smartsub.model.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ua.smartsub.smartsub.model.DeviceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DeviceInfo {
    @NotBlank(message = "Device id cannot be blank")
    @ApiModelProperty(value = "Device Id", required = true, dataType = "string", allowableValues = "Non empty string")
    private String deviceId;

    @NotNull(message = "Device type cannot be null")
    @ApiModelProperty(value = "Device type PHONE/PC/OTHER", required = true, dataType = "string", allowableValues =
            "DEVICE_TYPE_PHONE,DEVICE_TYPE_PC,OTHER_DEVICE")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @NotEmpty(message = "Device notification token can be null but not blank")
    @ApiModelProperty(value = "Device notification id", dataType = "string", allowableValues = "Non empty string")
    private String notificationToken;
}
