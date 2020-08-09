package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voteEvent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteEventDTO {
    @Id
    @GeneratedValue
    private int id;
    private Integer voteNum;
    private LocalDateTime voteTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDTO userDTO;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventDTO rsEventDTO;
}
