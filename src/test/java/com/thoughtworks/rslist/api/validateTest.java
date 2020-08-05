package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
public class validateTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_not_add_rs_event_when_given_a_null_event_name() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent();
        rsEvent.setKeyWord("测试");
        rsEvent.setUser(user);
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public static void should_not_add_rs_event_when_given_error_phone(MockMvc mockMvc) throws Exception {
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
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
}
