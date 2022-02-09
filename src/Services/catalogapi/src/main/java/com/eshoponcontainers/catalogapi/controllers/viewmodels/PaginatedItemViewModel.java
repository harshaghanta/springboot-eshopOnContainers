package com.eshoponcontainers.catalogapi.controllers.viewmodels;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginatedItemViewModel<T> {
    
    private Integer pageIndex;
    private Integer pageSize;
    private Integer count;
    private List<T> data;
}
