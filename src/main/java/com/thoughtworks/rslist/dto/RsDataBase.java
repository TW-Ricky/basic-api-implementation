package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RsDataBase {
    private List<User> userList = new ArrayList<>();
    private List<RsEvent> rsList = new ArrayList<>();
}
