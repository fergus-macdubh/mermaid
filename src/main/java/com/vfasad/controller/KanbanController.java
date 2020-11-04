package com.vfasad.controller;

import com.google.gson.Gson;
import com.vfasad.entity.Order;
import com.vfasad.entity.OrderStatus;
import com.vfasad.service.OrderService;
import com.vfasad.service.TeamService;
import com.vfasad.validation.constraints.ElementMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Function;

import static com.vfasad.entity.User.*;
import static java.util.stream.Collectors.toMap;

@Controller
@Validated
public class KanbanController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private Gson gson;

    @Autowired
    private TeamService teamService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER, ROLE_SALES})
    public String main() {
        return "redirect:/kanban";
    }

    @RequestMapping(value = "/kanban", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER, ROLE_SALES})
    public ModelAndView kanbanBoard() {
        ModelAndView model = new ModelAndView("order/kanban-dashboard");
        List<Order> orders = orderService.getActiveOrders();
        model.addObject("orders", orders);
        model.addObject("teams", teamService.findAll());
        model.addObject("ordersJson", gson.toJson(
                orders.stream().collect(toMap(Order::getId, Function.identity()))));
        model.addObject("newOrderSumArea", orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CREATED || o.getStatus() == OrderStatus.BLOCKED)
                .mapToDouble(Order::getArea)
                .sum());
        model.addObject("newOrderSumClips", orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CREATED || o.getStatus() == OrderStatus.BLOCKED)
                .mapToDouble(Order::getClipCount)
                .sum());
        model.addObject("newOrderSumBigFurn", orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CREATED || o.getStatus() == OrderStatus.BLOCKED)
                .mapToDouble(Order::getFurnitureBigCount)
                .sum());
        model.addObject("newOrderSumSmallFurn", orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CREATED || o.getStatus() == OrderStatus.BLOCKED)
                .mapToDouble(Order::getFurnitureSmallCount)
                .sum());
        return model;
    }

    @RequestMapping(value = "/kanban", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER})
    public String moveOrder(
            @RequestParam Long orderId,
            @RequestParam(required = false)
                    long[] consumeIds,
            @RequestParam(required = false)
            @ElementMin(value = 0, message = "Quantities cannot be zero or negative.")
                    List<Double> actualQuantities,
            @RequestParam(required = false) Long teamId,
            HttpServletRequest request) {

        Order order = orderService.getOrder(orderId);
        if (order.getStatus() == OrderStatus.CREATED) {
            orderService.moveOrderInProgress(order, teamId, request.getRequestURL().toString());
        } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            orderService.moveOrderToShipping(order, consumeIds, actualQuantities, request.getRequestURL().toString());
        } else if (order.getStatus() == OrderStatus.SHIPPING) {
            orderService.closeOrder(order);
        }
        return "redirect:/kanban";
    }

    @RequestMapping(value = "/order/{id}/cancel", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public String cancelOrder(
            @PathVariable Long id) {
        orderService.cancelOrder(id);
        return "redirect:/kanban";
    }
}
