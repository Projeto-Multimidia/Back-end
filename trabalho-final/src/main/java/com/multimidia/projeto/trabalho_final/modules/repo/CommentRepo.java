package com.multimidia.projeto.trabalho_final.modules.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multimidia.projeto.trabalho_final.modules.model.Comment;
import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
    
}
