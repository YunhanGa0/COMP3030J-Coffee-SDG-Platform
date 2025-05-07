package com.coffee_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 允许跨域的源，这里设置为前端应用的地址
//        config.setAllowedOrigins(List.of(
//            "http://localhost:8081", // Vue默认端口
//            "https://csi420-02-vm2.ucd.ie/" // 服务器地址
//
//        ));
//
//        // 允许携带认证信息（cookies等）
//        config.setAllowCredentials(true);
//
//        // 允许的HTTP方法
//        config.addAllowedMethod("*");
//
//        // 允许的请求头
//        config.addAllowedHeader("*");
//
//        // 暴露的响应头
//        config.addExposedHeader("*");
//
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}
