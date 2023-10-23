package com.space000.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class RegisterChkDto {

    @NotBlank
    @Size(min = 3)
    private String source;

    @NotBlank
    private String key;
}
