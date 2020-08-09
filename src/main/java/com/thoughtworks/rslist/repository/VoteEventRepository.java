package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.dto.VoteEventDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteEventRepository extends CrudRepository<VoteEventDTO, Integer> {

    @Override
    List<VoteEventDTO> findAll();

    @Query("select v from VoteEventDTO v where v.userDTO.id = :userId and v.rsEventDTO.id = :rsEventId")
    List<VoteEventDTO> findAccordingByUserIdAndRsEventId(int userId, int rsEventId);
}
