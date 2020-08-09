package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorHandlingTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private RsEventService rsEventService;
    @Autowired
    private UserService userService;

    @BeforeEach
    private void init() {
        userService.deleteAll();
        rsEventService.deleteAll();
        User user = User.builder()
                .userName("xiaobai")
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
    public void should_throw_exception_when_get_list_invalid_request_param() throws Exception {
        mockMvc.perform(get("/rs/list?start=-1&end=9"))
                .andExpect(jsonPath("error", is("invalid request param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_exception_when_get_rs_event_invalid_index() throws Exception {
        mockMvc.perform(get("/rs/-1"))
                .andExpect(jsonPath("error", is("invalid index")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_exception_when_post_rs_event_invalid_param() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().keyword("测试").userId(1).build();
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(jsonPath("error", is("invalid param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_exception_when_post_user_invalid_user() throws Exception {
        User user = User.builder()
                .userName("xiaobai")
                .age(15)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(jsonPath("error", is("invalid user")))
                .andExpect(status().isBadRequest());
    }
}
