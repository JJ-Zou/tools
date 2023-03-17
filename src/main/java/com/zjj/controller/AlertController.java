package com.zjj.controller;

import com.zjj.controller.req.AlertMessageReq;
import com.zjj.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping("/send")
    public ResponseEntity<?> pageFileInfos(@RequestBody AlertMessageReq req) {
        alertService.alert(req.getContent());
        return ResponseEntity.ok().build();
    }
}
