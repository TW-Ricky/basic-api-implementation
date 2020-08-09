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
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    User user;
    Integer userId;
    RsEvent rsEvent;
    Integer rsEventId;

    @BeforeEach
    void setUp() {
         user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .voteNumber(100)
                .build();
        userId = userService.addUser(user);
        rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .voteNum(0)
                .userId(userId)
                .build();
        rsEventId = rsEventService.addRsEvent(rsEvent);
    }

    @Test
    public void should_vote_rs_event_success_when_valid() throws Exception {
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
    @Test
    public void should_throw_exception_when_vote_rs_event_given_over_user_voteNumber() throws Exception {
        VoteEvent voteEvent = VoteEvent.builder()
                .voteNum(15)
                .voteTime(LocalDateTime.now())
                .userId(userId)
                .build();
        String jsonString = objectMapper.writeValueAsString(voteEvent);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("more votes than users own")));
    }

    @Test
    public void should_return_vote_record_by_user_id_and_rs_event_id() throws Exception {
        for (int i = 1; i <= 8; ++i) {
            VoteEvent voteEvent = VoteEvent.builder()
                    .voteNum(i)
                    .voteTime(LocalDateTime.now())
                    .userId(userId)
                    .build();
            voteEventService.voteRsEvent(rsEventId, voteEvent);
        }

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userId))
                .param("rsEventId", String.valueOf(rsEventId))
                .param("pageIndex", String.valueOf(1)))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userId)))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventId)))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userId))
                .param("rsEventId", String.valueOf(rsEventId))
                .param("pageIndex", String.valueOf(2)))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userId)))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventId)))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)));

    }


}