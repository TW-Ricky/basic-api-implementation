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
import java.util.List;

@RestController
public class VoteEventController {
    @Autowired
    VoteEventService voteEventService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable Integer rsEventId,
                                      @RequestBody @Valid VoteEvent voteEvent) {
        voteEventService.voteRsEvent(rsEventId, voteEvent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<VoteEvent>> getVoteRecord(@RequestParam Integer userId,
                                                         @RequestParam Integer rsEventId,
                                                         @RequestParam Integer pageIndex) {
        List<VoteEvent> voteEvent = voteEventService.getVoteRecord(userId, rsEventId, pageIndex);
        return ResponseEntity.ok(voteEvent);
    }
    @GetMapping("/voteRecordByTime")
    public ResponseEntity<List<VoteEvent>> getVoteRecordByTime(@RequestParam String startTime,
                                                               @RequestParam String endTime) throws JsonProcessingException {
        System.out.println(startTime);
        System.out.println(endTime);

        List<VoteEvent> voteEvent = voteEventService.getVoteRecordByTime(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime));
        return ResponseEntity.ok(voteEvent);
    }

}
