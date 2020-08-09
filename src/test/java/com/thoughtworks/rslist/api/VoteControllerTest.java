package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.VoteEvent;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    RsEventService rsEventService;
    @Autowired
    VoteEventService voteEventService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void should_vote_rs_event_success_when_valid() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .voteNumber(10)
                .build();
        Integer userId = userService.addUser(user);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .voteNum(0)
                .userId(userId)
                .build();
        Integer rsEventId = rsEventService.addRsEvent(rsEvent);
        VoteEvent voteEvent = VoteEvent.builder()
                .voteNum(2)
                .voteTime(LocalDateTime.now())
                .userId(userId)
                .build();
        String jsonString = objectMapper.writeValueAsString(voteEvent);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        RsEvent newRsEvent = rsEventService.getRsEventById(rsEventId);
        User newUser = userService.getUserById(userId);
        List<VoteEvent> voteEventList = voteEventService.getVoteEventList();
        assertEquals(8, newUser.getVoteNumber());
        assertEquals(2, newRsEvent.getVoteNum());
        assertEquals(1, voteEventList.size());
    }
}