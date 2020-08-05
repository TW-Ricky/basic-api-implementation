package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsListApplicationTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    public void should_get_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("热搜来了")))
                .andExpect(jsonPath("$.keyWord", is("热搜")))
                .andExpect(jsonPath("$.user.userName", is("ricky")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$.keyWord", is("难受")))
                .andExpect(jsonPath("$.user.userName", is("ricky")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_get_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[0].user.userName", is("ricky")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(jsonPath("$[1].user.userName", is("ricky")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_add_rs_event() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("超级热搜来了", "超级热搜", user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[0].user.userName", is("ricky")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(jsonPath("$[1].user.userName", is("ricky")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyWord", is("超级热搜")))
                .andExpect(jsonPath("$[2].user.userName", is("ricky")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void should_patch_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent();
        rsEvent.setEventName("买了空调");
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[0].user.userName", is("ricky")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(jsonPath("$[1].user.userName", is("ricky")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyWord", is("超级热搜")))
                .andExpect(jsonPath("$[2].user.userName", is("ricky")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent();
        rsEvent.setKeyWord("舒服");
        jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[0].user.userName", is("ricky")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyWord", is("舒服")))
                .andExpect(jsonPath("$[1].user.userName", is("ricky")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyWord", is("超级热搜")))
                .andExpect(jsonPath("$[2].user.userName", is("ricky")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rs/2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[0].user.userName", is("ricky")))
                .andExpect(jsonPath("$[1].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[1].keyWord", is("超级热搜")))
                .andExpect(jsonPath("$[1].user.userName", is("ricky")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_event_name() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_keyWord() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setEventName("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_user() throws Exception {
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setEventName("测试");
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_rs_event_when_given_a_null_user_name() throws Exception {
        User user = new User();
        user.setAge(19);
        user.setEmail("a@b.com");
        user.setPhone("18888888888");
        user.setGender("male");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_a_user_name_over_8() throws Exception {
        User user = new User();
        user.setUserName("rickyxxxx");
        user.setAge(19);
        user.setEmail("a@b.com");
        user.setPhone("18888888888");
        user.setGender("male");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_age_overflow() throws Exception {
        User user = new User();
        user.setUserName("ricky");
        user.setAge(16);
        user.setEmail("a@b.com");
        user.setPhone("18888888888");
        user.setGender("male");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_error_email() throws Exception {
        User user = new User();
        user.setUserName("ricky");
        user.setAge(19);
        user.setEmail("ab.com");
        user.setPhone("18888888888");
        user.setGender("male");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_not_add_rs_event_when_given_error_phone() throws Exception {
        User user = new User();
        user.setUserName("ricky");
        user.setAge(19);
        user.setEmail("a@b.com");
        user.setPhone("188888888881");
        user.setGender("male");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

}
