package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.VoteEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteEventService {
    void voteRsEvent(Integer rsEventId, VoteEvent voteEvent);

    List<VoteEvent> getVoteEventList();

    List<VoteEvent> getVoteRecord(Integer userId, Integer rsEventId, Integer pageIndex);

    List<VoteEvent> getVoteRecordByTime(LocalDateTime startTime, LocalDateTime endTime);
}
