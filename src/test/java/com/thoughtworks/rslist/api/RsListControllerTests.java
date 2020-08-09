package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
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
        Integer userId = userService.addUser(user);
        rsEventService.addRsEvent(RsEvent.builder().eventName("热搜来了").keyword("热搜").userId(userId).build());
        rsEventService.addRsEvent(RsEvent.builder().eventName("天气好热，没有空调").keyword("难受").userId(userId).build());
    }

    @Test
    public void should_get_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("热搜来了")))
                .andExpect(jsonPath("$.keyword", is("热搜")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
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
        RsEvent rsEvent = RsEvent.builder().eventName("超级热搜来了").keyword("超级热搜").userId(1).build();
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

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
        RsEvent rsEvent = new RsEvent();
        rsEvent.setEventName("买了空调");
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyword", is("难受")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent();
        rsEvent.setKeyword("舒服");
        jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
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
        mockMvc.perform(delete("/rs/2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyword", is("热搜")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_response_with_index_when_post_a_add_request() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("超级热搜来了").keyword("超级热搜").userId(1).build();
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        MvcResult result = mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("index"))
                .andReturn();
    }

}
