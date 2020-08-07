package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.UserDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserDTO, Integer> {
    @Override
    List<UserDTO> findAll();
}
