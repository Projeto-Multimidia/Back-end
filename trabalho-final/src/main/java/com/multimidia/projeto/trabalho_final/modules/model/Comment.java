package com.multimidia.projeto.trabalho_final.modules.model;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_tb")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "conteudo", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity file;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
