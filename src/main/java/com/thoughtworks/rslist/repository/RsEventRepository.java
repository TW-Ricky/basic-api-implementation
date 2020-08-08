package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.RsEventDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventDTO, Integer> {

    @Override
    List<RsEventDTO> findAll();
}
