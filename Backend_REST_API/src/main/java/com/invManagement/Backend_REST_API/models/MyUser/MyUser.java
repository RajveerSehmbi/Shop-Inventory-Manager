package com.invManagement.Backend_REST_API.models.MyUser;

import com.invManagement.Backend_REST_API.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Username is required.")
    private String username;

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Role is required.")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotEmpty(message = "Password is required.")
    private String password;



    public MyUser() {}

    public MyUser(Long id, String username, String name, Role role, String password) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
