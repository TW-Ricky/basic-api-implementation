package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
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
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsListControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    public void should_get_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("热搜来了")))
                .andExpect(jsonPath("$.keyWord", is("热搜")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$.keyWord", is("难受")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_get_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_add_rs_event() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("超级热搜来了", "超级热搜", user);
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyWord", is("超级热搜")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void should_patch_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent();
        rsEvent.setEventName("买了空调");
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyWord", is("超级热搜")))
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
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyWord", is("舒服")))
                .andExpect(jsonPath("$[2].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[2].keyWord", is("超级热搜")))
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
                .andExpect(jsonPath("$[1].eventName", is("超级热搜来了")))
                .andExpect(jsonPath("$[1].keyWord", is("超级热搜")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_response_with_index_when_post_a_add_request() throws Exception {
        User user = new User("ricky", "male", 19, "a@b.com", "18888888888");
        RsEvent rsEvent = new RsEvent("超级热搜来了", "超级热搜", user);
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        MvcResult result = mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

}
