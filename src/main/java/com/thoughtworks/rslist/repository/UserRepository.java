package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.UserDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserDTO, Integer> {
    @Override
    List<UserDTO> findAll();

    @Override
    Optional<UserDTO> findById(Integer integer);
}
