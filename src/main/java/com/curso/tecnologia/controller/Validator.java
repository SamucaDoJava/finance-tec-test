package com.curso.tecnologia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/validator")
public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    @GetMapping("/server-living")
    public void isServerLiving(){
        LOGGER.debug("This server living");
    }

}
