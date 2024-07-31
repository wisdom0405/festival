package com.beyond.festival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케쥴러 사용시 필요한 설정
@SpringBootApplication
public class FestivalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FestivalApplication.class, args);
	}

}
