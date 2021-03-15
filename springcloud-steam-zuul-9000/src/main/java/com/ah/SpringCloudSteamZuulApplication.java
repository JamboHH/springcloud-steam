package com.ah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
/**
 * Created by 54110 on 2020/12/24.
 */
//exclude 启动时不需要加载数据库的连接信息
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
@EnableZuulProxy
@EnableDiscoveryClient
public class SpringCloudSteamZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSteamZuulApplication.class);
    }
}
