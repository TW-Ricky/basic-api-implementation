package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull
    @Size(max = 8)
    // @JsonProperty("user_name")
    private String userName;
    @NotNull
    // @JsonProperty("user_gender")
    private String gender;
    @NotNull
    @Min(18)
    @Max(100)
    // @JsonProperty("user_age")
    private int age;
    @Email
    // @JsonProperty("user_email")
    private String email;
    @NotNull
    @Pattern(regexp = "1\\w{10}")
    // @JsonProperty("user_phone")
    private String phone;
    // @JsonProperty("user_vote_number")
    private int voteNumber = 10;

}
