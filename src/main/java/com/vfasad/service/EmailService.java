package com.vfasad.service;

import com.vfasad.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private EmailSender emailSender;

    public void notifyManagerOrderInProgress(Order order) {
        String subject = "Заказ №$" + order.getId() + " взят в работу";
        String message = subject + ". Детали заказа:";

        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        model.put("order", order);

        emailSender.send(
                order.getManager().getEmail(),
                subject,
                "email/order-flow.ftl",
                model);
    }

    public void notifyManagerOrderCompleted(Order order) {
        String subject = "Заказ №$" + order.getId() + " звершен";
        String message = subject + ". Детали заказа:";

        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        model.put("order", order);

        emailSender.send(
                order.getManager().getEmail(),
                subject,
                "email/order-flow.ftl",
                model);
    }
}
