package com.jai.HireBridge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);
	
	@Bean(name = "mailExecutor")
	public ThreadPoolTaskExecutor mailExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("mail-");
		executor.setRejectedExecutionHandler((task , pool) -> 
		       log.error("Mail queue is full, dropping an email task"));
	    executor.setWaitForTasksToCompleteOnShutdown(true);
	    executor.setAwaitTerminationSeconds(30);
	    return executor;
	}
}
