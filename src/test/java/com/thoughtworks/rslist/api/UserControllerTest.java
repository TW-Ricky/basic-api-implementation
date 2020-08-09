package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    private void setUp() {
        userService.deleteAll();
        userService.addUser(User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build());
    }

    @Test
    public void should_register_user() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_validate_name_more_than_8() throws Exception {
        User user = User.builder()
                .userName("rickyxxxxx")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_validate_age_between_18_and_100() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(15)
                .email("a@b.com")
                .gender("male")
                .phone("18888888888")
                .build();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_validate_email_suit_format() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("ab.com")
                .gender("male")
                .phone("18888888888")
                .build();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_validate_phone_suit_format() throws Exception {
        User user = User.builder()
                .userName("ricky")
                .age(19)
                .email("a@b.com")
                .gender("male")
                .phone("188888888881")
                .build();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_user_list_by_get_request() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("ricky")))
                .andExpect(jsonPath("$[0].user_age", is(19)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is("a@b.com")))
                .andExpect(jsonPath("$[0].user_phone", is("18888888888")))
                .andExpect(status().isOk());
    }

}