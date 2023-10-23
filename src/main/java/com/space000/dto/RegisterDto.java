package com.space000.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;

@Getter
@Setter
@Slf4j
@ToString
public class RegisterDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    private String zoneCode;
    private String roadName;
    private String detailedAddress;

    @NotBlank
    @Size(min = 3, max = 8)
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{3,8}$")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^[MF]{1}$")
    private String sex;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotNull
    private Integer day;

    @Pattern(regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}$")
    private String tel;


}
