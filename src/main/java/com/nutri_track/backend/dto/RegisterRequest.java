package com.nutri_track.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 254)
    String email,

    @NotBlank(message = "PIN is required")
    @Pattern(
        regexp = "^[0-9]{4}$",
        message = "PIN must contain exactly four digits"
    )
    String pin

) {}
