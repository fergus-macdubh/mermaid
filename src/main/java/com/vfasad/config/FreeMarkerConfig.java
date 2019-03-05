package com.vfasad.config;


import freemarker.template.TemplateModelException;
import no.api.freemarker.java8.Java8ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FreeMarkerConfig {
    @Value("${web.page.logo}")
    String logoFileName;
    @Value("${web.page.css}")
    String styleFileName;
    @Value("${web.page.title}")
    String title;

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void postConstruct() throws TemplateModelException {
        configuration.setObjectWrapper(
                new Java8ObjectWrapper(freemarker.template.Configuration.getVersion())); // VERSION_2_3_26
        configuration.setSharedVariable("logo", "/img/" + logoFileName);
        configuration.setSharedVariable("style", "/css/" + styleFileName);
        configuration.setSharedVariable("title", title);
    }
}
