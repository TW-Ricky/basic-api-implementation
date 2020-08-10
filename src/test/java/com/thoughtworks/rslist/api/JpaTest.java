package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JpaTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RsEventService rsEventService;
    @Autowired
    ObjectMapper objectMapper;
    User user;
    Integer userId;
    RsEvent rsEvent;
    Integer rsEventId;
    @BeforeEach
    private void setUp() {
        userService.deleteAll();
        rsEventService.deleteAll();
        user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        userId = userService.addUser(user);
        rsEvent = RsEvent.builder()
                .eventName("热搜来了")
                .keyword("热搜")
                .userId(userId)
                .build();
        rsEventService.addRsEvent(rsEvent);
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Test
    public void should_add_user_into_mysql_use_jpa() throws Exception {
        User newUser = User.builder()
                .userName("xiaoli")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        String jsonString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        List<User> userList = userService.getUserList();
        assertEquals(2, userList.size());
        assertEquals("ricky", userList.get(0).getUserName());
        assertEquals("xiaoli", userList.get(1).getUserName());
    }

    @Test
    public void should_get_user_by_id() throws Exception {
        mockMvc.perform(get("/user/{index}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName", is("ricky")))
            .andExpect(jsonPath("$.gender", is("male")));
    }

    @Test
    public void should_delete_user_by_id() throws Exception {
        User newUser = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer newUserId = userService.addUser(newUser);
        mockMvc.perform(delete("/user/{index}", userId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userName", is("xiaobai")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_add_rs_event_into_mysql() throws Exception {
        User newUser = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(newUser);
        RsEvent newRsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(userId)
                .build();
        String jsonString = objectMapper.writeValueAsString(newRsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[1].eventName", is("超级热搜来了")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
    @Test
    public void should_throw_user_not_exists_exception_when_add_rs_event() throws Exception {
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(5)
                .build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_delete_rs_event_when_delete_user() throws Exception {
        mockMvc.perform(delete("/user/{id}", userId)).andExpect(status().isOk());
        assertEquals(0, userService.getUserList().size());
        assertEquals(0, rsEventService.getRsEventList().size());
    }
    @Test
    public void should_throw_exception_when_patch_given_user_id_not_match() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(user);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(userId)
                .build();
        Integer rsEventId = rsEventService.addRsEvent(rsEvent);
        rsEvent.setUserId(userId - 1);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{id}", rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("userId not match")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_update_event_name_when_patch_given_user_id_match() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(user);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(userId)
                .build();
        Integer rsEventId = rsEventService.addRsEvent(rsEvent);
        rsEvent.setEventName("修改后的超级热搜来了");
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        RsEvent newRsEvent = rsEventService.getRsEventById(rsEventId);
        assertEquals("修改后的超级热搜来了", newRsEvent.getEventName());
        assertEquals("超级热搜", newRsEvent.getKeyword());
    }
    @Test
    public void should_update_keyword_when_patch_given_user_id_match() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(user);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(userId)
                .build();
        Integer rsEventId = rsEventService.addRsEvent(rsEvent);
        rsEvent.setKeyword("修改后的超级热搜");
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        RsEvent newRsEvent = rsEventService.getRsEventById(rsEventId);
        assertEquals("超级热搜来了", newRsEvent.getEventName());
        assertEquals("修改后的超级热搜", newRsEvent.getKeyword());
    }
    @Test
    public void should_update_event_name_and_keyword_when_patch_given_user_id_match() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(user);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(userId)
                .build();
        Integer rsEventId = rsEventService.addRsEvent(rsEvent);
        rsEvent.setKeyword("修改后的超级热搜");
        rsEvent.setEventName("修改后的超级热搜来了");
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventId).content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        RsEvent newRsEvent = rsEventService.getRsEventById(rsEventId);
        assertEquals("修改后的超级热搜来了", newRsEvent.getEventName());
        assertEquals("修改后的超级热搜", newRsEvent.getKeyword());
    }
}
