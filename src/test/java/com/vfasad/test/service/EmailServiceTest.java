package com.vfasad.test.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.User;
import com.vfasad.service.EmailSender;
import com.vfasad.service.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    private static final String MANAGER_EMAIL = "manager@gmail.com";
    private static final String MAIL_STORAGE = "test@gmail.com";

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Before
    public void setMailStorage() {
        ReflectionTestUtils.setField(emailService, "mailStorage", MAIL_STORAGE);
    }

    @Test
    public void testNotifyManagerOrderInProgress() {
        Order order = generateOrder();
        emailService.notifyManagerOrderInProgress(order, "/kanban");
        verify(emailSender, times(1)).send(eq(MANAGER_EMAIL), anyString(),eq("email/order-flow.ftl"), anyMap());
    }

    @Test
    public void testNotifyManagerOrderCompleted() {
        Order order = generateOrder();
        emailService.notifyManagerOrderCompleted(order, "/kanban");
        verify(emailSender, times(1)).send(eq(MANAGER_EMAIL), anyString(),eq("email/order-flow.ftl"), anyMap());
        verify(emailSender, times(1)).send(eq(MAIL_STORAGE), anyString(),eq("email/order-flow.ftl"), anyMap());
    }

    private Order generateOrder() {
        User user = new User();
        user.setEmail(MANAGER_EMAIL);
        Order order = new Order(user, 3.4, 2, 5, 6, "abc", 6.9, new HashSet<>(), LocalDate.now());

        return order;
    }
}
