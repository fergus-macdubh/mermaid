package com.vfasad.controller.storage;

import com.vfasad.repo.ItemRepository;
import com.vfasad.repo.StorageActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StorageController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StorageActionRepository storageActionRepository;

    @RequestMapping(value = "/storage", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView model = new ModelAndView("storage-dashboard");
        model.addObject("items", itemRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/storage/item/{id}/action", method = RequestMethod.GET)
    public ModelAndView itemActions(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("item-actions");
        model.addObject("actions", storageActionRepository.findByItemId(id));
        return model;
    }
}
