package com.multimidia.projeto.trabalho_final.modules.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multimidia.projeto.trabalho_final.modules.model.FileEntity;

public interface FileRepo extends JpaRepository<FileEntity, UUID>{
    
}
