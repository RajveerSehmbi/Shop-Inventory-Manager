package com.invManagement.Backend_REST_API.models.MyUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, String> {

    Optional<MyUser> findByUsername(String username);
}
