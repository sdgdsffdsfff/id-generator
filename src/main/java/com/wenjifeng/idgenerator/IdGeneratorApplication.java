package com.wenjifeng.idgenerator;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@MapperScan(basePackages = "com.wenjifeng.idgenerator.mapper")
@SpringBootApplication
public class IdGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdGeneratorApplication.class, args);
		log.info("IdGeneratorApplication start success");
	}

}
