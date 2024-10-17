package com.multimidia.projeto.trabalho_final.modules.repo;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multimidia.projeto.trabalho_final.modules.model.User;

public interface UserRepo extends JpaRepository<User, UUID>{
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
}
