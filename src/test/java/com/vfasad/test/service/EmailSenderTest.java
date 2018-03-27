package com.vfasad.test.service;

import com.vfasad.service.EmailSender;
import freemarker.template.Template;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

import javax.mail.internet.MimeMessage;
import freemarker.template.Configuration;

import java.io.IOException;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderTest {
    private static final String FROM = "test@gmail.com";
    private static final String TO = "to@gmail.com";
    private static final String SUBJECT = "subject";
    private static final String TEMPLATE = "template";

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private Template template;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSender emailSender;

    @Before
    public void setFields() {
        ReflectionTestUtils.setField(emailSender, "from", FROM);
        ReflectionTestUtils.setField(emailSender, "mailSendEnabled", false);
    }

    @Test
    public void testSendDisabled() {
        emailSender.send(TO, SUBJECT, TEMPLATE, new HashMap<String, Object>());
        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendEnabled() throws IOException {
        ReflectionTestUtils.setField(emailSender, "mailSendEnabled", true);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(TEMPLATE)).thenReturn(template);
        emailSender.send(TO, SUBJECT, TEMPLATE, new HashMap<>());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
