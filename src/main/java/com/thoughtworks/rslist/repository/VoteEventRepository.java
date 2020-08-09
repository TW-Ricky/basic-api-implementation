package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.VoteEventDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteEventRepository extends CrudRepository<VoteEventDTO, Integer> {

    @Override
    List<VoteEventDTO> findAll();
}
