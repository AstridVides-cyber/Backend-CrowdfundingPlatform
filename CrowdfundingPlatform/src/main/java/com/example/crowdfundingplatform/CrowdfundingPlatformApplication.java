package com.example.crowdfundingplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.crowdfundingplatform.config.SslKeystoreBootstrap;

@SpringBootApplication
public class CrowdfundingPlatformApplication {

    public static void main(String[] args) {
        SslKeystoreBootstrap.ensureKeystoreExists();
        SpringApplication.run(CrowdfundingPlatformApplication.class, args);
    }

}
