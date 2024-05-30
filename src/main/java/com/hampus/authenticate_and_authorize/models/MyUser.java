package com.hampus.authenticate_and_authorize.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class MyUser
{
    @Size(min = 2, max = 20)
    private String username;

    @Size(min = 4, max = 20)
    private String password;

    @Email(message = "please enter a valid email")
    @NotBlank
    private String email;
}
