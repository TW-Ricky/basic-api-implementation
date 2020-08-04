package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RsEvent rsEvent;

    @Test
    public void get_rs_event() throws Exception {
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
    public void get_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("天气好热，没有空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_rs_event() throws Exception {
        rsEvent.setEventName("超级热搜来了");
        rsEvent.setKeyWord("超级热搜");
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
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
    public void should_patch_rs_event() throws Exception {
        rsEvent.setEventName("买了空调");
        JsonMapper jsonMapper = new JsonMapper();
        String jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyWord", is("难受")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent();
        rsEvent.setKeyWord("舒服");
        jsonString = jsonMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/2").content(jsonString).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("热搜来了")))
                .andExpect(jsonPath("$[0].keyWord", is("热搜")))
                .andExpect(jsonPath("$[1].eventName", is("买了空调")))
                .andExpect(jsonPath("$[1].keyWord", is("舒服")))
                .andExpect(status().isOk());
    }

}
