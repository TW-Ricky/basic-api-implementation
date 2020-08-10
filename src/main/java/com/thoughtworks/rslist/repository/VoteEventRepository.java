package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.dto.VoteEventDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteEventRepository extends PagingAndSortingRepository<VoteEventDTO, Integer> {

    @Override
    List<VoteEventDTO> findAll();

    @Query("select v from VoteEventDTO v where v.userDTO.id = :userId and v.rsEventDTO.id = :rsEventId")
    List<VoteEventDTO> findAccordingByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);

    @Query("select v from VoteEventDTO v where v.voteTime between :startTime and :endTime")
    List<VoteEventDTO> findAccordingBetweenStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);
}
