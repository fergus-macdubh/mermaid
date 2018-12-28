package com.vfasad.controller;

import com.vfasad.entity.Client;
import com.vfasad.service.ClientService;
import com.vfasad.service.OrderService;
import com.vfasad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/clients")
    @Secured(ROLE_ADMIN)
    public ModelAndView clients() {
        ModelAndView model = new ModelAndView("client/clients");
        model.addObject("clients", clientService.getClients());
        return model;
    }

    @RequestMapping(value = "/clients/add", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public ModelAndView addClientForm() {
        ModelAndView modelAndView = new ModelAndView("client/client-form");
        modelAndView.addObject("managers", userService.getManagers());
        modelAndView.addObject("activeOrdersCount", 0);
        return modelAndView;
    }

    @PostMapping("/clients/add")
    @Secured(ROLE_ADMIN)
    public String addClient(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String contact,
            @RequestParam String email,
            @RequestParam Long managerId) {
        Client client = new Client(name, phone, contact, email, userService.getUser(managerId));
        clientService.updateClient(client);
        return "redirect:/clients";
    }

    @RequestMapping(value = "/clients/{id}/edit", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public ModelAndView editClientForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("client/client-form");
        modelAndView.addObject("targetClient", clientService.getClient(id));
        modelAndView.addObject("managers", userService.getManagers());
        modelAndView.addObject("activeOrdersCount", orderService.getClientActiveOrderCount(id));
        return modelAndView;
    }

    @RequestMapping(value = "/clients/{id}/edit", method = RequestMethod.POST)
    @Secured(ROLE_ADMIN)
    public String updateClient(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String contact,
            @RequestParam String email,
            @RequestParam Long managerId) {
        Client client = clientService.getClient(id);
        client.setName(name);
        client.setPhone(phone);
        client.setContact(contact);
        client.setEmail(email);
        client.setManager(userService.getUser(managerId));
        clientService.updateClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/clients/{id}/delete")
    @Secured(ROLE_ADMIN)
    public String deleteClient(
            @PathVariable Long id) {
        Client client = clientService.getClient(id);
        client.setDeleted(true);
        clientService.updateClient(client);
        return "redirect:/clients";
    }
}
