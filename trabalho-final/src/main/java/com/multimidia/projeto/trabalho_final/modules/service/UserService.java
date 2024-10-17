package com.multimidia.projeto.trabalho_final.modules.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multimidia.projeto.trabalho_final.modules.model.User;
import com.multimidia.projeto.trabalho_final.modules.repo.UserRepo;
import com.multimidia.projeto.trabalho_final.modules.shared.UserResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    private ModelMapper mapper = new ModelMapper();

    @org.springframework.transaction.annotation.Transactional
    public boolean authenticate(String nickname, String password) {
        User user = userRepository.findByNickname(nickname).orElse(null);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    @org.springframework.transaction.annotation.Transactional
    public void addFriend(String userNickname, String friendNickname) {
        User user = userRepository.findByNickname(userNickname).orElse(null);
        User friend = userRepository.findByNickname(friendNickname).orElse(null);

        if (user != null && friend != null) {
            user.getFriends().add(friend);
            userRepository.save(user);
        }
    }

    public void deleteByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserResponseDTO findByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElse(null);
        if (user != null) {
            return mapper.map(user, UserResponseDTO.class);
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional
    public void addFriend(UUID userId, UUID friendId) {
        User user = userRepository.findById(userId).orElse(null);
        User friend = userRepository.findById(friendId).orElse(null);

        if (user != null && friend != null) {
            user.getFriends().add(friend);
            userRepository.save(user);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return mapper.map(user, UserResponseDTO.class);
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public User login(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
