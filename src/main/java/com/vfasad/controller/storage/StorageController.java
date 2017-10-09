package com.vfasad.controller.storage;

import com.vfasad.dto.storage.Item;
import com.vfasad.dto.storage.StorageAction;
import com.vfasad.repo.ItemRepository;
import com.vfasad.repo.StorageActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/storage/item/add", method = RequestMethod.GET)
    public ModelAndView purchaseItemForm() {
        ModelAndView model = new ModelAndView("purchase-item-form");
        model.addObject("items", itemRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/storage/item", method = RequestMethod.POST)
    public String itemActions(
            @RequestParam Long itemId,
            @RequestParam int quantity,
            @RequestParam double price) {
        Item item = itemRepository.findOne(itemId);

        storageActionRepository.save(new StorageAction(
                quantity,
                price,
                StorageAction.Type.PURCHASE,
                item,
                null // todo: fill this from credentials
        ));

        item.setQuantity(item.getQuantity() + quantity);
        itemRepository.save(item);
        return "redirect:/storage";
    }
}
