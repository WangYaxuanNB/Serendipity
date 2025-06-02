package com.serendipity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        logger.info("正在启动SpringBoot应用...");
        try {
            SpringApplication.run(DemoApplication.class, args);
            logger.info("SpringBoot应用启动成功！");
        } catch (Exception e) {
            logger.error("SpringBoot应用启动失败：", e);
        }
    }
}
