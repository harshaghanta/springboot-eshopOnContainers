package com.eshoponcontainers.server.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.server.ConfigDataDto;
import com.eshoponcontainers.server.config.ConfigData;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Home")
@RequiredArgsConstructor
public class HomeController {

    private final ConfigData configData;

    @GetMapping("/Configuration")
    public ConfigDataDto getConfiguration() {
        var config = new ConfigDataDto();
        BeanUtils.copyProperties(configData, config);
        return config;
    }
}
