package com.zjj.controller;

import com.zjj.middleware.ProxyPool;
import com.zjj.model.ProxyIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/proxy")
public class ProxyIpController {

    @Autowired
    private ProxyPool proxyPool;

    @PostMapping
    public ResponseEntity<ProxyIp> popProxyIp() {
        ProxyIp proxyIp = proxyPool.popProxy();
        if (proxyIp == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(proxyIp, HttpStatus.OK);
    }

    @DeleteMapping
    public Boolean delProxyIp(@RequestBody ProxyIp ip) {
        return proxyPool.removeProxy(ip);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProxyIp>> listAllProxyIp() {
        return new ResponseEntity<>(proxyPool.listAll(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countProxyIp() {
        return new ResponseEntity<>(proxyPool.size(), HttpStatus.OK);
    }

}
