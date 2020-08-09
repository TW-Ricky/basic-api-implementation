package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import javafx.print.Printer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
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
    JsonMapper jsonMapper;


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
        RsEvent rsEvent = RsEvent.builder()
                .eventName("热搜来了")
                .keyword("热搜")
                .userId(userId)
                .build();
        rsEventService.addRsEvent(rsEvent);
        jsonMapper = new JsonMapper();
    }

    @Test
    public void should_add_user_into_mysql_use_jpa() throws Exception {
        User user = User.builder()
                .userName("xiaoli")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        String jsonString = jsonMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        List<User> userList = userService.getUserList();
        assertEquals(2, userList.size());
        assertEquals("xiaoli", userList.get(1).getUserName());
    }

    @Test
    public void should_get_user_by_id() throws Exception {
        User user = User.builder()
                .userName("xiaoli")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(user);
        mockMvc.perform(get("/user/{index}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName", is("xiaoli")))
            .andExpect(jsonPath("$.gender", is("female")));
    }

    @Test
    public void should_delete_user_by_id() throws Exception {
        User user = User.builder()
                .userName("xiaoli")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        Integer userId = userService.addUser(user);

        user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        userService.addUser(user);
        mockMvc.perform(delete("/user/{index}", userId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", is("ricky")))
                .andExpect(jsonPath("$[1].userName", is("xiaobai")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_add_rs_event_into_mysql() throws Exception {
        RsEvent rsEvent = RsEvent.builder()
                .eventName("超级热搜来了")
                .keyword("超级热搜")
                .userId(1)
                .build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
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
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_delete_rs_event_when_delete_user() throws Exception {
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
        rsEventService.addRsEvent(rsEvent);
        mockMvc.perform(delete("/user/{id}", userId)).andExpect(status().isOk());
        assertEquals(1, userService.getUserList().size());
        assertEquals(1, rsEventService.getRsEventList().size());
    }
    @Test
    public void should_throw_exception_when_user_id_not_match() throws Exception {
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
                .userId(userId - 1)
                .build();
        Integer rsEventId = rsEventService.addRsEvent(rsEvent);
        mockMvc.perform(patch("/rs/{id}", rsEventId)).andExpect(status().isBadRequest());
    }
}
