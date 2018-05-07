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

    public void notifyManagerOrderInProgress(Order order, URL url) throws MalformedURLException {
        String subject = "Заказ №$" + order.getId() + " взят в работу";
        String message = subject + ". Детали заказа:";

        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        model.put("order", order);
        model.put("url", getHost(url) + "/kanban");

        emailSender.send(
                order.getManager().getEmail(),
                subject,
                "email/order-flow.ftl",
                model);
    }

    public void notifyManagerOrderCompleted(Order order, URL url) throws MalformedURLException {
        String subject = "Заказ №$" + order.getId() + " завершен";
        String message = subject + ". Детали заказа:";

        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        model.put("order", order);
        model.put("url", getHost(url) + "/kanban");

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

    private URL getHost(URL url) throws MalformedURLException {
        int index = url.toString().indexOf("//");
        String host = url.toString().substring(0, url.toString().indexOf("/",index + "//".length()));
        return new URL(host);
    }
}
