package com.codeper.ControlClientes.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@AllArgsConstructor
@NoArgsConstructor  
public class PageableRequest<T> {
    private int pageSize;
    private int pageNumber;
    private String sortField;
    private String sortOrder;
    //filtros espesificos
    private T criterials;
    private String filterValue;

}
