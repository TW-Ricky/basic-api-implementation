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
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        userService.addUser(user);
        rsEventService.addRsEvent(new RsEvent("热搜来了", "热搜", user));
        rsEventService.addRsEvent(new RsEvent("天气好热，没有空调", "难受", user));
        jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_event_name() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent(null, "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_keyWord() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("测试", null, user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_user() throws Exception {
        RsEvent rsEvent = new RsEvent("测试", "测试", null);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_user_name() throws Exception {
        User user = new User(null, "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_null_gander() throws Exception {
        User user = new User("ricky", null, 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_null_email() throws Exception {
        User user = new User("ricky", "male", 19, null, "18888888888");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_null_phone() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", null);
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_user_name_over_8() throws Exception {
        User user = new User("rickyxxxx", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_age_overflow() throws Exception {
        User user = new User("ricky", "male", 15, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_error_email() throws Exception {
        User user = new User("ricky", "male", 19, "ab.com", "18888888888");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_error_phone() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "188888888881");
        RsEvent rsEvent = new RsEvent("测试", "测试", user);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
}
