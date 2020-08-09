package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDTO;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    private UserDTO changeUserTOUserDTO(User user){
        return UserDTO.builder().age(user.getAge()).email(user.getEmail())
                .gender(user.getGender()).name(user.getUserName()).phone(user.getPhone())
                .voteNum(user.getVoteNumber()).build();
    }

    private User changeUserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getName());
        user.setGender(userDTO.getGender());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setVoteNumber(userDTO.getVoteNum());
        return user;
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
        List<UserDTO> userDTOList = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        userDTOList.stream().forEach(item -> userList.add(changeUserDTOToUser(item)));
        return userList;
    }


    @Override
    public User getUserById(int index) {
        return changeUserDTOToUser(userRepository.findById(index).get());
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
