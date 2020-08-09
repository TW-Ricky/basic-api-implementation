package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rs_event")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RsEventDTO {
    @Id
    @GeneratedValue
    private int id;
    private String eventName;
    private String keyword;
    private Integer voteNum;
    @ManyToOne
    private UserDTO userDTO;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "rsEventDTO")
    private List<VoteEventDTO> voteEventDTOList;
}
