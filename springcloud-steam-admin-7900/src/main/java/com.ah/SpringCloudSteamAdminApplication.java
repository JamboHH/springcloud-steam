package com.ah;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//springboot admin 的服务端
@EnableAdminServer
public class SpringCloudSteamAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSteamAdminApplication.class, args);
    }
}
