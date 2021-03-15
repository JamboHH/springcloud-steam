package com.ah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class SpringCloudElasticsearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudElasticsearchApplication.class);
    }
}
