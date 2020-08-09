package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.service.VoteEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class VoteEventController {
    @Autowired
    VoteEventService voteEventService;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable Integer rsEventId,
                                      @RequestBody @Valid VoteEvent voteEvent) {
        voteEventService.voteRsEvent(rsEventId, voteEvent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<VoteEvent>> getVoteRecord(@RequestParam Integer userId,
                                                         @RequestParam Integer rsEventId) {
        List<VoteEvent> voteEvent = voteEventService.getVoteRecord(userId, rsEventId);
        return ResponseEntity.ok(voteEvent);
    }

}
