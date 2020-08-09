package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEvent {
    private Integer rsEventId;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer voteNum;
    @NotNull
    private LocalDateTime voteTime;
}
