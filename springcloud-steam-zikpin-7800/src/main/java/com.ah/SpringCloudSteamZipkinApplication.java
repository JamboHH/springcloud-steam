package com.ah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableDiscoveryClient
//标注当前工程是zipkin的服务端
@EnableZipkinServer
public class SpringCloudSteamZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSteamZipkinApplication.class, args);
    }
}
