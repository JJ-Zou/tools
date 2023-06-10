package com.zjj.middleware;

import com.datarangers.collector.EventCollector;
import com.datarangers.event.Header;
import com.datarangers.event.HeaderV3;
import com.zjj.constants.DataRangersConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired()
    @Qualifier("webEventCollector")
    private EventCollector webEventCollector;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        log.info("method: [{}], uri: {}, queryString: {}", method, requestURI, queryString);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        String servletPath = request.getServletPath();
        String method = request.getMethod();
        int status = response.getStatus();
        Header header = HeaderV3.Builder.getInstance()
                .setClientIp(Inet4Address.getLocalHost().getHostAddress())
                .setOsName(System.getProperty("os.name"))
                .setAppId(DataRangersConstants.APP_ID)
                .setUserUniqueId("request")
                .setBrowser(userAgent)
                .build();
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put("current_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
        eventParams.put("request_path", servletPath);
        eventParams.put("http_method", method);
        eventParams.put("http_response_status", status);
        if (ex != null) {
            eventParams.put("http_response_exception", ex.getMessage());
        }
        webEventCollector.sendEvent(header, "http_request", eventParams);
    }

}
