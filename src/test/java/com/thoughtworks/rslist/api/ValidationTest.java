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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ValidationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RsEventService rsEventService;
    @Autowired
    private UserService userService;
    private JsonMapper jsonMapper;

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
        userService.addUser(user);
        rsEventService.addRsEvent(RsEvent.builder().eventName("热搜来了").keyword("热搜").userId(1).build());
        rsEventService.addRsEvent(RsEvent.builder().eventName("天气好热，没有空调").keyword("难受").userId(1).build());
        jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_event_name() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_keyword() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_user() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_user_name() throws Exception {
        User user = User.builder()
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_null_gander() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_null_email() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_null_phone() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_user_name_over_8() throws Exception {
        User user = User.builder()
                .userName("rickyxxxx")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_age_overflow() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(15)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_error_email() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("ab.com")
                .gender("male")
                .phone("18888888888")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_error_phone() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("188888888881")
                .build();
        RsEvent rsEvent = RsEvent.builder().eventName("测试").keyword("测试").userId(1).build();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
}
