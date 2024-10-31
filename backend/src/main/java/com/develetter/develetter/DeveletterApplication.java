package com.develetter.develetter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing // 등록시각, 수정시각을 위한 전체 auditing 활성화를 위한 에노테이션
@EnableScheduling // 스케줄링 활성화
@EnableJpaRepositories
@SpringBootApplication
public class DeveletterApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeveletterApplication.class, args);
	}

}
