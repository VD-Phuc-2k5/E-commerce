package com.dontwait.server.configuration;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow credentials
        config.setAllowCredentials(true);


        config.addAllowedOriginPattern("*");

        // Allow specific origins
        config.addAllowedOrigin("http://localhost:5173");
        
        // Allow specific headers
        config.addAllowedHeader("*");
        
        // Allow specific methods
        config.addAllowedMethod("*");
        
        // Allow cookies
        config.addExposedHeader("Set-Cookie");
        config.addExposedHeader("Authorization");
        
        // Add cache control headers
        config.addExposedHeader("Cache-Control");
        config.addExposedHeader("Pragma");
        config.addExposedHeader("Expires");
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter();
    }
} 
