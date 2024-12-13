package com.atletico.atletico_revamp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private Boolean success;
    private String status;
    private String message;
}
