package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.service.VoteEventService;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VoteEventController {
    @Autowired
    VoteEventService voteEventService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/rs/{rsEventId}/vote")
    public ResponseEntity voteRsEvent(@PathVariable Integer rsEventId,
                                      @RequestBody @Valid VoteEvent voteEvent) {
        voteEventService.voteRsEvent(rsEventId, voteEvent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<VoteEvent>> getVoteRecord(@RequestParam(required = false) Integer userId,
                                                         @RequestParam(required = false) Integer rsEventId,
                                                         @RequestParam(required = false) String startTime,
                                                         @RequestParam(required = false) String endTime,
                                                         @RequestParam Integer pageIndex) {
        List<VoteEvent> voteEvent = new ArrayList<>();
        if (userId != null && rsEventId != null) {
            voteEvent = voteEventService.getVoteRecord(userId, rsEventId, pageIndex);
        }
        if (startTime != null && endTime != null) {
            voteEvent = voteEventService.getVoteRecordByTime(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime), pageIndex);
        }
        return ResponseEntity.ok(voteEvent);
    }

}
