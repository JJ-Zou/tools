package com.zjj.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProxyIp {
    private String host;

    private int port;

    private long lastVerifyTime;
}
