package com.springSecond.space.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${server.cloudinary.api_secret}")
    private String cloudinary_api_Secret;

    @Value("${server.cloudinary.cloud_name}")
    private String cloudinary_cloud_name;

    @Value("${server.cloudinary.api_key}")
    private String cloudinary_api_key;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinary_cloud_name,
                "api_key", cloudinary_api_key,
                "api_secret", cloudinary_api_Secret));
    }
}
