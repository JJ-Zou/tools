//package com.zjj.config;
//
//import com.datarangers.collector.AppEventCollector;
//import com.datarangers.collector.EventCollector;
//import com.datarangers.config.DataRangersSDKConfigPropertiesInfo;
//import com.datarangers.sender.Callback;
//import com.datarangers.sender.callback.LoggingCallback;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//
//@Configuration
//@EnableAsync
//@EnableConfigurationProperties(DataRangersSDKConfigPropertiesInfo.class)
//public class DataRangersEnableAutoConfiguration {
//
//    @Autowired
//    private DataRangersSDKConfigPropertiesInfo dataRangersSDKConfigPropertiesInfo;
//
//    @Bean(name = "appEventCollector")
//    public EventCollector defaultAppCollector(Callback callback) {
//        return new AppEventCollector("app", dataRangersSDKConfigPropertiesInfo, callback);
//    }
//
//    @Bean(name = "webEventCollector")
//    public EventCollector defaultWebCollector(Callback callback) {
//        return new AppEventCollector("web", dataRangersSDKConfigPropertiesInfo, callback);
//    }
//
//    @Bean(name = "mpEventCollector")
//    public EventCollector defaultMpbCollector(Callback callback) {
//        return new AppEventCollector("mp", dataRangersSDKConfigPropertiesInfo, callback);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(Callback.class)
//    public Callback callback() {
//        return new LoggingCallback(dataRangersSDKConfigPropertiesInfo.getEventSavePath(),
//                "error-" + dataRangersSDKConfigPropertiesInfo.getEventSaveName(),
//                dataRangersSDKConfigPropertiesInfo.getEventSaveMaxFileSize());
//    }
//}
