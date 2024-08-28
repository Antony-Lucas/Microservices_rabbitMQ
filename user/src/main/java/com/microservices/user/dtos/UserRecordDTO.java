package com.microservices.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDTO(@NotBlank String username, @NotBlank @Email String email) {

}
