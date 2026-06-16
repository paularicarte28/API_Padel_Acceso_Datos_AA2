package com.padelzgz.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private String email;

    public LoginResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
