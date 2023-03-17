package com.zjj.service.impl;

import com.zjj.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.from}")
    private String from;

    @Value("${spring.mail.properties.to}")
    private String to;

    @Async("threadPoolTaskExecutor")
    @Override
    public void alert(String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject("[报警]");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
        log.debug("send message {}", simpleMailMessage);
    }
}
