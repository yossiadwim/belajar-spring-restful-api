package com.example.belajar_spring_restful_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebResponse <T> {

    private T data;

    private String errors;

    private PagingResponse paging;
}
