package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDTO;
import com.thoughtworks.rslist.exception.UserNotExistsException;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    private UserDTO changeUserTOUserDTO(User user){
        return UserDTO.builder().age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .name(user.getUserName())
                .phone(user.getPhone())
                .voteNum(user.getVoteNumber())
                .build();
    }

    private User changeUserDTOToUser(UserDTO userDTO) {
        return User.builder().voteNumber(userDTO.getVoteNum())
                .age(userDTO.getAge())
                .email(userDTO.getEmail())
                .gender(userDTO.getGender())
                .phone(userDTO.getPhone())
                .userName(userDTO.getName())
                .build();
    }
    @Override
    public boolean existsUserById(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public Integer addUser(User user) {
        UserDTO userDTO = userRepository.save(changeUserTOUserDTO(user));
        return userDTO.getId();
    }

    @Override
    public List<User> getUserList() {
        return userRepository.findAll().stream()
                .map(item -> changeUserDTOToUser(item))
                .collect(Collectors.toList());
    }


    @Override
    public User getUserById(Integer id) {
        Optional<UserDTO> userDTOOptional = userRepository.findById(id);
        if (!userDTOOptional.isPresent()) {
            throw new UserNotExistsException("user not exists");
        }
        return changeUserDTOToUser(userDTOOptional.get());
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }
}
