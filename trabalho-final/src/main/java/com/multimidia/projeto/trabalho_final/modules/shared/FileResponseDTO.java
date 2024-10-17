package com.multimidia.projeto.trabalho_final.modules.shared;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {

    private UUID id;
    private String fileName;
    private int size;
    private String type;
    private Integer likes;
    private List<CommentDTO> comments;
}
