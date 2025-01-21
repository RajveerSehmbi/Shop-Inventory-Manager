package com.invManagement.Backend_REST_API.models.User;

import com.invManagement.Backend_REST_API.models.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class User {

    @Id
    @NotEmpty(message = "ID is required.")
    private String id;

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Role is required.")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotEmpty(message = "Password is required.")
    private String password;

    public User(String id, String name, Role role, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
