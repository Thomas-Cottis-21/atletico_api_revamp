package com.atletico.atletico_revamp.dto;

import java.util.Optional;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BaseResponse {
    private Optional<T> data;

    public DataResponse(boolean success, String status, String message, Optional<T> data) {
        super(success, status, message);
        this.data = data;
    }
}
