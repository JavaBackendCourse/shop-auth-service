package com.org.shop_auth_service.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;
}
