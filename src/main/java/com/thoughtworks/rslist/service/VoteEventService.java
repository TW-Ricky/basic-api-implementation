package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.VoteEvent;

import java.util.List;

public interface VoteEventService {
    void voteRsEvent(Integer rsEventId, VoteEvent voteEvent);

    List<VoteEvent> getVoteEventList();

    List<VoteEvent> getVoteRecord(Integer userId, Integer rsEventId, Integer pageIndex);
}
