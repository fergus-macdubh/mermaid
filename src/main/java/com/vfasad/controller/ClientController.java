package com.vfasad.controller;

import com.vfasad.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    @Secured(ROLE_ADMIN)
    public ModelAndView users() {
        ModelAndView model = new ModelAndView("client/clients");
        model.addObject("clients", clientService.getClients());
        return model;
    }
}
