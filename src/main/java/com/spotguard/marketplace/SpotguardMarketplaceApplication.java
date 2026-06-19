package com.spotguard.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpotguardMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotguardMarketplaceApplication.class, args);
    }
}
