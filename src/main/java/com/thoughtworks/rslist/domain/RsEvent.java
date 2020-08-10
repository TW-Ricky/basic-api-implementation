package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEvent {
    private int id;
    @NotNull
    private String eventName;
    @NotNull
    private String keyword;
    @NotNull
    private Integer userId;
    private int voteNum = 0;

    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }
    @JsonProperty
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
