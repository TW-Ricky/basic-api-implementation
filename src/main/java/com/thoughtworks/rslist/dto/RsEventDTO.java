package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @ManyToOne
    private UserDTO userDTO;
}