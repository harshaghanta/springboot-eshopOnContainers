package com.eshoponcontainers.webhooksclient.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.eshoponcontainers.webhooksclient.models.WebHookReceived;
import com.eshoponcontainers.webhooksclient.services.HooksRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HooksRepository hooksRepository;

    @RequestMapping({ "/", "/home" })
    public ModelAndView index() {
        List<WebHookReceived> hooksReceived = hooksRepository.getAll();
        log.info("hooksReceived: {}", hooksReceived);
        //Send hooks received to the thymeleaf template
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("webhooks", hooksReceived);

        return modelAndView;
    }
}
