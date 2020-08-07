package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
