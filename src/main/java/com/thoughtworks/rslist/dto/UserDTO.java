package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.VoteEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userDTO")
    private List<RsEventDTO> rsEventDTOList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userDTO")
    private List<VoteEventDTO> voteEventList;
}
