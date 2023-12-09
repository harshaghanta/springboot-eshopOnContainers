package com.eshoponcontainers.catalogapi.excep;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CustomErrorResponse {
    
    private LocalDateTime timeStamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}
