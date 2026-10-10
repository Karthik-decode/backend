
package com.nutri_track.backend.dto;

public record AuthResponse(
    String message,
    String token,
    Long id,
    String name,
    String email
) {}
