package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RsEventService rsEventService;
    @Autowired
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;
    Integer userId;
    Integer firstRsEventId;
    Integer secondRsEventId;

    @BeforeEach
    private void setUp() {
        userService.deleteAll();
        rsEventService.deleteAll();
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        userId = userService.addUser(user);
        firstRsEventId = rsEventService.addRsEvent(RsEvent.builder().eventName("热搜来了").keyword("热搜").userId(userId).build());
        secondRsEventId = rsEventService.addRsEvent(RsEvent.builder().eventName("天气好热，没有空调").keyword("难受").userId(userId).build());
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Test
    public void should_get_rs_event() throws Exception {
        mockMvc.perform(get("/rs/{id}", firstRsEventId))
                .andExpect(jsonPath("$.eventName", is("热搜来了")))
                .andExpect(jsonPath("$.keyword", is("热搜")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/{id}", secondRsEventId))
                .andExpect(jsonPath("$.eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$.keyword", is("难受")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyword", is("难受")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_rs_event() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("超级热搜来了").keyword("超级热搜").userId(userId).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyword", is("难受")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyword", is("超级热搜")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_patch_rs_event() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("买了空调").userId(userId).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{id}", secondRsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyword", is("难受")))
                .andExpect(status().isOk());

        rsEvent = RsEvent.builder().keyword("舒服").userId(userId).build();
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{id}", secondRsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyword", is("舒服")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rs/{id}", secondRsEventId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_response_with_index_when_post_a_add_request() throws Exception {
        RsEvent newRsEvent = RsEvent.builder().eventName("超级热搜来了").keyword("超级热搜").userId(userId).build();
        String jsonString = objectMapper.writeValueAsString(newRsEvent);
        MvcResult result = mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("rsEventId"))
                .andReturn();
    }

}
