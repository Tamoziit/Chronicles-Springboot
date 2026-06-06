package com.tamojit.chronicles.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // defining beans
public class ShopConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
