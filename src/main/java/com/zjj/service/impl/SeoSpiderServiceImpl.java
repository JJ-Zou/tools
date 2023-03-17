package com.zjj.service.impl;

import com.zjj.middleware.ProxyPool;
import com.zjj.model.ProxyIp;
import com.zjj.service.SpiderService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SeoSpiderServiceImpl implements SpiderService {

    private static final String URL = "https://proxy.seofangfa.com/";

    @Autowired
    @Qualifier("restTemplateGb2312")
    private RestTemplate restTemplate;

    @Autowired
    private ProxyPool proxyPool;

    @Override
    public int getMaxPage() {
        return 1;
    }

    @Override
    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public boolean solve_single_page(int page) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "text/html,application/xhtml+xml,application/xml;");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
        String html = resp.getBody();
        if (resp.getStatusCode() != HttpStatus.OK || html == null) {
            log.error("拉取错误 {}", html);
            return false;
        }
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".table tbody tr");
        for (Element element : elements) {
            String host = element.child(0).text().trim();
            int port = Integer.parseInt(element.child(1).text().trim());
            long lastVerifyTime = getLastVerifyTimeStamp(element.child(4).text().trim(), getFormatter());
            proxyPool.setProxy(ProxyIp.builder().host(host).port(port).lastVerifyTime(lastVerifyTime).build());
        }
        log.info("拉取完seo代理第{}页", page);
        return true;
    }
}
