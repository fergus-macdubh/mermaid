package com.vfasad.controller.reports;

import com.vfasad.entity.Order;
import com.vfasad.service.OrderService;
import com.vfasad.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

import static com.vfasad.entity.User.ROLE_ADMIN;

@Controller
@Secured(ROLE_ADMIN)
public class OrderByPaintReportController extends AbstractReportController {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;

    @GetMapping("/reports/order-by-paint")
    public ModelAndView orderByPaintReport(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd.MM.yy")
                    LocalDate start,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd.MM.yy")
                    LocalDate end,
            @RequestParam(required = false)
                    Long productId
            ) {
        ModelAndView modelAndView = new ModelAndView("report/order-by-paint-report");

        modelAndView.addObject("products", productService.findAllProducts());

        if (productId != null) {
            modelAndView.addObject("paint", productService.getProduct(productId));
            modelAndView.addObject("start", start);
            modelAndView.addObject("end", end);
            List<Order> orders = orderService.findByProduct(productId, start, end);
            modelAndView.addObject("orders", orders);
            modelAndView.addObject("sumArea", orders.stream().mapToDouble(Order::getArea).sum());
            modelAndView.addObject("sumClips", orders.stream().mapToDouble(Order::getClipCount).sum());
            modelAndView.addObject("sumFurnitureBig", orders.stream().mapToDouble(Order::getFurnitureBigCount).sum());
            modelAndView.addObject("sumFurnitureSmall", orders.stream().mapToDouble(Order::getFurnitureSmallCount).sum());
        }
        return modelAndView;
    }
}
