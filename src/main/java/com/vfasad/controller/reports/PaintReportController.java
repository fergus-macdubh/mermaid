package com.vfasad.controller.reports;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
import com.vfasad.service.OptionService;
import com.vfasad.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
@Secured(ROLE_ADMIN)
public class PaintReportController extends AbstractReportController {
    @Autowired
    OrderService orderService;
    @Autowired
    OptionService optionService;

    @GetMapping("/reports/paint/month")
    public ModelAndView allMonthesPaintReport() {
        ModelAndView modelAndView = new ModelAndView("report/all-months-paint-report");
        Map<String, List<Order>> ordersByMonth = orderService.findAll().stream()
                .filter(o -> o.getCompleted() != null)
                .collect(Collectors.groupingBy(o -> YearMonth.from(o.getCompleted()).toString()));
        SortedMap<String, List<Order>> ordersByMonthSorted = new TreeMap<>(ordersByMonth);
        modelAndView.addObject("ordersByMonth", ordersByMonthSorted);
        return modelAndView;
    }

    @GetMapping("/reports/paint/month/{year}-{month}")
    public ModelAndView monthReport(@PathVariable int year,
                                    @PathVariable int month) {
        ModelAndView modelAndView = new ModelAndView("report/month-paint-report");
        modelAndView.addObject("month", getMonthName(month));
        modelAndView.addObject("monthNum", month);
        modelAndView.addObject("year", year);

        List<Order> orders = orderService.findByMonth(year, month);
        Map<Product, PaintDto> paints = new HashMap<>();

        for (Order order : orders) {
            for (OrderConsume consume : order.getConsumes()) {
                Product product = consume.getProduct();
                PaintDto dto = paints.getOrDefault(product,
                        new PaintDto(product.getId(), product.getName(), product.getProducer()));
                dto.setOrderCount(dto.getOrderCount() + 1);
                dto.setSumQuantity(dto.getSumQuantity() + consume.getActualUsedQuantity());
                dto.setSumArea(dto.getSumArea() + order.getArea());
                dto.setSumClips(dto.getSumClips() + order.getClipCount());
                dto.setSumSmallFurniture(dto.getSumSmallFurniture() + order.getFurnitureSmallCount());
                dto.setSumBigFurniture(dto.getSumBigFurniture() + order.getFurnitureBigCount());
                paints.put(product, dto);
            }
        }

        modelAndView.addObject("paints", paints.values());

        return modelAndView;
    }
}
