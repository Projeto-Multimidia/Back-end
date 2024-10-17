package com.multimidia.projeto.trabalho_final.modules.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.multimidia.projeto.trabalho_final.modules.model.Comment;
import com.multimidia.projeto.trabalho_final.modules.model.FileEntity;
import com.multimidia.projeto.trabalho_final.modules.model.User;
import com.multimidia.projeto.trabalho_final.modules.repo.CommentRepo;
import com.multimidia.projeto.trabalho_final.modules.repo.FileRepo;
import com.multimidia.projeto.trabalho_final.modules.repo.UserRepo;
import com.multimidia.projeto.trabalho_final.modules.shared.CommentDTO;
import com.multimidia.projeto.trabalho_final.modules.shared.FileResponseDTO;
import com.multimidia.projeto.trabalho_final.modules.util.FileUtils;

@Service
public class FileService {

    @Autowired
    private FileRepo fileRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    private ModelMapper mapper = new ModelMapper();

    @org.springframework.transaction.annotation.Transactional
    public void incrementLikes(UUID fileId) {
        Optional<FileEntity> fileOptional = fileRepo.findById(fileId);
        if (fileOptional.isPresent()) {
            FileEntity file = fileOptional.get();
            file.setLikes(file.getLikes() + 1);
            fileRepo.save(file);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void addComment(UUID fileId, String userNickname, String content) {
        Optional<FileEntity> fileOptional = fileRepo.findById(fileId);
        Optional<User> userOptional = userRepo.findByNickname(userNickname);
        if (fileOptional.isPresent() && userOptional.isPresent()) {
            FileEntity file = fileOptional.get();
            User user = userOptional.get();
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setFile(file);
            comment.setUser(user);
            commentRepo.save(comment);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public FileEntity findByName(String nickname, String file_name) {
        Optional<User> user = this.userRepo.findByNickname(nickname);
        if (user.isPresent()) {
            FileEntity file = user.get().getFiles().get(file_name);
            try {
                file.setData(FileUtils.decompressFile(file.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional
    public void addComment(UUID fileId, String content) {
        Optional<FileEntity> fileOptional = fileRepo.findById(fileId);
        if (fileOptional.isPresent()) {
            FileEntity file = fileOptional.get();
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setFile(file);
            commentRepo.save(comment);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<FileResponseDTO> findAll() {
        List<FileEntity> fileEntities = fileRepo.findAll();
        return mapper.map(fileEntities, new TypeToken<List<FileResponseDTO>>() {
        }.getType());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public FileEntity findById(UUID id) {
        Optional<FileEntity> fOptional = fileRepo.findById(id);
        if (fOptional.isPresent()) {
            try {
                fOptional.get().setData(FileUtils.decompressFile(fOptional.get().getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fOptional.get();
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional
    public FileResponseDTO save(String nickname, MultipartFile file) {
        Optional<User> user = this.userRepo.findByNickname(nickname);
        if (user.isPresent()) {
            try {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(file.getOriginalFilename());
                fileEntity.setType(file.getContentType());
                fileEntity.setSize(file.getSize());
                fileEntity.setUser(user.get());
                fileEntity.setLikes(0);

                // Corrected line
                fileEntity.setData(FileUtils.compressFile(file.getBytes(), fileEntity.getFileName()));

                // Comments list can be initialized in the entity itself
                // fileEntity.setComments(new ArrayList<>());

                user.get().getFiles().put(fileEntity.getFileName(), fileEntity);
                fileRepo.save(fileEntity);
                userRepo.save(user.get());

                // Prepare the response DTO
                List<CommentDTO> commentDTOs = new ArrayList<>();
                return new FileResponseDTO(
                        fileEntity.getId(),
                        fileEntity.getFileName(),
                        fileEntity.getData().length,
                        fileEntity.getType(),
                        fileEntity.getLikes(),
                        commentDTOs);
            } catch (IOException e) {
                e.printStackTrace();
                // Return an error response or throw a custom exception
            }
        }
        return null; // Or throw an exception if user is not found
    }

    public void deleteById(UUID id) {
        fileRepo.deleteById(id);
    }
}
