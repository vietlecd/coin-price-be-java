package com.javaweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ExecutorConfig {

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskScheduler() {
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
//        return Executors.newScheduledThreadPool(3);  // 3 luồng cố định
    }
}
