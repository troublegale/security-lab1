package itmo.ivank.infobez.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull @NotBlank @Size(min = 4, max = 64) String username,
        @NotNull @NotBlank @Size(min = 8, max = 128) String password
) {
}
