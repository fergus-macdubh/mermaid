package com.vfasad.controller.storage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Dashboard {
    @RequestMapping(value = "/storage", method = RequestMethod.GET)
    String home() {
        return "hello";
    }
}
