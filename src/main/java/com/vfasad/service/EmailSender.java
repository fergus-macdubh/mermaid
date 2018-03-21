package com.vfasad.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class EmailSender {
    @Value("${spring.mail.properties.from}")
    private String from;

    @Value("${mail.send.enabled}")
    private boolean mailSendEnabled;

    @Autowired
    Configuration freemarkerConfiguration;

    @Autowired
    public JavaMailSender mailSender;

    public void send(String to, String subject, String template, Map<String, Object> model) {
        if (!mailSendEnabled) return;

        try {
            log.info("Trying to send email to [{}] with subject [{}]", to, subject);
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(getContent(template, model), true);

            mailSender.send(msg);
            log.info("Message is sent.");
        } catch (MessagingException | IOException | TemplateException e) {
            log.warn("Message is not sent.", e);
        }
    }

    private String getContent(String template, Map<String, Object> model) throws IOException, TemplateException {
        return FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfiguration.getTemplate(template), model);
    }
}
