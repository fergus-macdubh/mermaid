package com.vfasad.service;

import com.vfasad.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {
    @Value("${mail.storage}")
    private String mailStorage;

    @Autowired
    private EmailSender emailSender;

    public void notifyManagerOrderInProgress(Order order, String url) {
        String subject = "Заказ №$" + order.getId() + " взят в работу";
        String message = subject + ". Детали заказа:";

        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        model.put("order", order);
        model.put("url", getLink(url, "kanban"));

        emailSender.send(
                order.getManager().getEmail(),
                subject,
                "email/order-flow.ftl",
                model);
    }

    public void notifyManagerOrderCompleted(Order order, String url) {
        String subject = "Заказ №$" + order.getId() + " завершен";
        String message = subject + ". Детали заказа:";

        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        model.put("order", order);
        model.put("url", getLink(url, "kanban"));

        emailSender.send(
                order.getManager().getEmail(),
                subject,
                "email/order-flow.ftl",
                model);

        // send copy to storage
        emailSender.send(
                mailStorage,
                subject,
                "email/order-flow.ftl",
                model);
    }

    private String getLink(String url, String pageName) {
        String link = "";
        try {
            URL u = new URL(url);
            String port = ((Integer) u.getPort()).toString();
            port = port.length() > 0 ? (":" + port) : "";
            link = u.getProtocol() + "://" + u.getHost() + port + "/" + pageName;
        } catch (MalformedURLException e) {
            log.warn("Link for message was not generated", e);
        }
        return link;
    }
}
