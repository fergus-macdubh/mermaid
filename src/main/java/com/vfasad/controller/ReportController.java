package com.vfasad.controller;

import com.vfasad.entity.Order;
import com.vfasad.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
@Secured(ROLE_ADMIN)
public class ReportController {
    @Autowired
    OrderService orderService;

    @GetMapping("/reports")
    public ModelAndView reports() {
        ModelAndView modelAndView = new ModelAndView("report/reports");
        return modelAndView;
    }

    @GetMapping("/reports/month")
    public ModelAndView allMonthesReport() {
        ModelAndView modelAndView = new ModelAndView("report/all-months-report");
        Map<String, List<Order>> ordersByMonth = orderService.findAll().stream()
                .filter(o -> o.getCompleted() != null)
                .collect(Collectors.groupingBy(o -> YearMonth.from(o.getCompleted()).toString()));
        SortedMap<String, List<Order>> ordersByMonthSorted = new TreeMap<>(ordersByMonth);
        modelAndView.addObject("ordersByMonth", ordersByMonthSorted);
        return modelAndView;
    }

    @GetMapping("/reports/month/{year}-{month}")
    public ModelAndView monthReport(@PathVariable int year,
                                    @PathVariable int month) {
        ModelAndView modelAndView = new ModelAndView("report/month-report");
        List<Order> orders = orderService.findByMonth(year, month);
        modelAndView.addObject("orders", orders);
        modelAndView.addObject("month", getMonthName(month));
        modelAndView.addObject("year", year);
        return modelAndView;
    }

    private String getMonthName(int month) {
        return Month.of(month)
                .getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        new Locale("ru", "UA")
                );
    }
}
