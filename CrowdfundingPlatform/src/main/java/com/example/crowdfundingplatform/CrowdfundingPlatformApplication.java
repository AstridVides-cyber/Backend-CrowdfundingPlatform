package com.example.crowdfundingplatform;

import com.example.crowdfundingplatform.config.SslKeystoreBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrowdfundingPlatformApplication {

    public static void main(String[] args) {
        SslKeystoreBootstrap.ensureKeystoreExists();
        SpringApplication.run(CrowdfundingPlatformApplication.class, args);
    }

}
