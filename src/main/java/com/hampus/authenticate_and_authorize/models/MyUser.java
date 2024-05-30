package com.hampus.authenticate_and_authorize.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class MyUser implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String secret;

    private String role;

    private String username;

    @Column(nullable = false)
    private String password;

    @Email(message = "please enter a valid email")
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
}
