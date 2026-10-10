
package com.nutri_track.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    String email,

    @NotBlank(message = "PIN is required")
    @Pattern(
        regexp = "^[0-9]{4}$",
        message = "PIN must contain exactly four digits"
    )
    String pin

) {}
