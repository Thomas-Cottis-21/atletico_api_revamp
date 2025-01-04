package com.atletico.atletico_revamp.dto;

import java.util.Optional;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BaseResponse {
    private final T data;

    public DataResponse(boolean success, String status, String message, T data) {
        super(success, status, message);
        this.data = data;
    }
}
