package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.service.VoteEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class VoteEventController {
    @Autowired
    VoteEventService voteEventService;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable Integer rsEventId,
                                      @RequestBody @Valid VoteEvent voteEvent) {
        System.out.println(voteEvent);
        voteEventService.voteRsEvent(rsEventId, voteEvent);
        return ResponseEntity.ok().build();
    }

}
