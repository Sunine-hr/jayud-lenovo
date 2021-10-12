package com.jayud.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@RefreshScope
@ComponentScan(basePackages = {"com.jayud.report","org.jeecg.modules.jmreport"})
public class JayudReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(JayudReportApplication.class, args);
	}

}
