package com.vfasad.controller;

import com.vfasad.entity.Option;
import com.vfasad.service.OptionName;
import com.vfasad.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
public class OptionsController {
    @Autowired
    OptionService optionService;

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public ModelAndView getOptionsForm() {
        ModelAndView modelAndView = new ModelAndView("options/options-form");
        modelAndView.addObject("options", optionService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/options", method = RequestMethod.POST)
    @Secured(ROLE_ADMIN)
    public String updateOptions(@RequestParam String[] optionNames,
                                @RequestParam String[] values) {
        List<Option> options = IntStream.range(0, optionNames.length)
                .boxed()
                .map(i -> new Option(OptionName.valueOf(optionNames[i]), values[i]))
                .collect(Collectors.toList());
        optionService.save(options);
        return "redirect:/options";
    }
}
