package com.internet_application.backend.Configuration.MailConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", false);
        mailProperties.put("mail.smtp.starttls.enable", false);
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost("mailserver");
        mailSender.setPort(1234);
        mailSender.setProtocol("smtp");
        mailSender.setUsername("");
        mailSender.setPassword("");
        return mailSender;
    }
}