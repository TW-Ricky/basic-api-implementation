package com.thoughtworks.rslist.api;

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
import org.springframework.test.web.servlet.MvcResult;

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
    JsonMapper jsonMapper;


    @BeforeEach
    private void setUp() {
        userService.deleteAll();
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        userService.addUser(user);
        jsonMapper = new JsonMapper();
    }

    @Test
    public void should_add_user_into_mysql_use_jpa() throws Exception {
        User user = new User("xiaoli", "male", 19, "a@b.com", "18888888888");
        String jsonString = jsonMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        List<User> userList = userService.getUserList();
        assertEquals(2, userList.size());
        assertEquals("xiaoli", userList.get(1).getUserName());
    }

    @Test
    public void should_get_user_by_id() throws Exception {
        User user = new User("xiaoli", "female", 19, "a@b.com", "18888888888");
        String jsonString = jsonMapper.writeValueAsString(user);
        String userId = mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        mockMvc.perform(get("/user/{index}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName", is("xiaoli")))
            .andExpect(jsonPath("$.gender", is("female")));
    }

    @Test
    public void should_delete_ser_by_id() throws Exception {
        User user = new User("xiaoli", "female", 19, "a@b.com", "18888888888");
        String jsonString = jsonMapper.writeValueAsString(user);
        String userId = mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        user = new User("xiaobai", "female", 19, "a@b.com", "18888888888");
        jsonString = jsonMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        mockMvc.perform(delete("/user/{index}", userId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", is("ricky")))
                .andExpect(jsonPath("$[1].userName", is("xiaobai")))
                .andExpect(status().isOk());
    }
}
