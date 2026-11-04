package com.codeper.ControlClientes.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
@AllArgsConstructor 
public class PageableResponse<T> {
    private List<T> content;
    //metadata
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
}
