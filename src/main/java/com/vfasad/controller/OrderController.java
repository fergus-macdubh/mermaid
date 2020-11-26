package com.vfasad.controller;

import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
import com.vfasad.service.ClientService;
import com.vfasad.service.OptionService;
import com.vfasad.service.OrderService;
import com.vfasad.service.ProductService;
import com.vfasad.validation.constraints.ElementMin;
import com.vfasad.validation.constraints.TodayAndAfterToday;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.vfasad.entity.OrderStatus.IN_PROGRESS;
import static com.vfasad.entity.User.*;

@Controller
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_SALES})
    public ModelAndView ordersLastMonth() {
        ModelAndView model = new ModelAndView("order/order-dashboard");
        model.addObject("orders", orderService.findCurrentMonthAndOpenOrders());
        model.addObject("isFullList", false);
        model.addObject("options", optionService.getOptionsMap());
        return model;
    }

    @RequestMapping(value = "/order/archive", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_SALES})
    public ModelAndView archive() {
        ModelAndView model = new ModelAndView("order/archive");

        YearMonth oldest = YearMonth.from(orderService.findOldest().getCreated());
        YearMonth newest = YearMonth.from(orderService.findNewest().getCreated());
        YearMonth yearMonth = newest;

        List<YearMonth> months = new ArrayList<>();
        while (!yearMonth.equals(oldest)) {
            months.add(yearMonth);
            yearMonth = yearMonth.minus(1, ChronoUnit.MONTHS);
        }
        months.add(oldest);

        return model.addObject("yearMonths", months);
    }

    @RequestMapping(value = "/order/archive/{year}-{month}", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_SALES})
    public ModelAndView ordersFullList(@PathVariable int year,
                                       @PathVariable int month) {
        ModelAndView model = new ModelAndView("order/order-dashboard");
        model.addObject("orders", orderService.findByMonth(year, month));
        model.addObject("isFullList", true);
        model.addObject("options", optionService.getOptionsMap());
        return model;
    }

    @RequestMapping(value = "/order/print/inprogress", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_SALES})
    public ModelAndView printInProgress() {
        ModelAndView model = new ModelAndView("order/print/inprogress");
        model.addObject("orders", orderService.findByStatus(IN_PROGRESS));
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView addOrderForm() {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("products", productService.findAllProducts());
        model.addObject("options", optionService.getOptionsMap());
        model.addObject("clients", clientService.getClients());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String addOrder(
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Clips count cannot be negative.")
                    int clipCount,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Furniture (small) count cannot be negative.")
                    int furnitureSmallCount,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Furniture (big) count cannot be negative.")
                    int furnitureBigCount,
            @RequestParam @Min(value = 0, message = "Area cannot be negative.")
                    double area,
            @RequestParam(required = false)
                    String document,
            @RequestParam @Min(value = 0, message = "Price cannot be negative.")
                    double price,
            @RequestParam @NotEmpty
                    long[] productIds,
            @RequestParam @NotEmpty @ElementMin(value = 0, message = "Quantities cannot be zero or negative.")
                    List<Double> quantities,
            @RequestParam @TodayAndAfterToday(message = "Planned date should be more or equal to today.")
            @DateTimeFormat(pattern = "dd.MM.yyyy")LocalDate complete,
            @RequestParam(required = false) Long clientId) {
        Set<OrderConsume> consumes = new HashSet<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productService.getProduct(productIds[i]);
            consumes.add(new OrderConsume(product, quantities.get(i)));
        }

        orderService.addOrder(area, clipCount, furnitureSmallCount, furnitureBigCount, document, price,
                consumes, complete, clientService.getClient(clientId));
        return "redirect:/kanban";
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("order", orderService.getOrder(id));
        model.addObject("products", productService.findAllProducts());
        model.addObject("options", optionService.getOptionsMap());
        model.addObject("clients", clientService.getClients());
        return model;
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Clips count cannot be negative.")
                    int clipCount,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Furniture (small) count cannot be negative.")
                    int furnitureSmallCount,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Furniture (big) count cannot be negative.")
                    int furnitureBigCount,
            @RequestParam double area,
            @RequestParam String document,
            @RequestParam @Min(value = 0, message = "Price cannot be negative.")
                    double price,
            @RequestParam long[] productIds,
            @RequestParam @NotEmpty
            @ElementMin(value = 0, message = "Quantities cannot be zero or negative.")
                    List<Double> quantities,
            @RequestParam @TodayAndAfterToday(message = "Planned date should be more or equal to today.")
            @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate complete,
            @RequestParam(required = false) Long clientId) {
        Set<OrderConsume> consumes = new HashSet<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productService.getProduct(productIds[i]);
            consumes.add(new OrderConsume(product, quantities.get(i)));
        }
        orderService.updateOrder(id, area, clipCount, furnitureSmallCount, furnitureBigCount, document, price, consumes, complete, clientService.getClient(clientId));
        return "redirect:/order";
    }
}
