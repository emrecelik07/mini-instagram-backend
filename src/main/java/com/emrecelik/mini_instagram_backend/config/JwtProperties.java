package com.emrecelik.mini_instagram_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private Secret secret;
    public static class Secret {
        private String key;
        private List<String> previousKeys = List.of();
    }
}

