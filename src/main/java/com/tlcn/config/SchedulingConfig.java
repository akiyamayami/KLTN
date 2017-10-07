package com.tlcn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tlcn.scheduler.ProposalScheduler;

@Configuration
@EnableScheduling
public class SchedulingConfig {
	@Bean
    public ProposalScheduler bean() {
        return new ProposalScheduler();
    }
}
