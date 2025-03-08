package com.opositaweb.config;


import com.opositaweb.config.properties.OpositaWebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class StaticResourceConfig implements WebMvcConfigurer {

    private final OpositaWebProperties opositaWebProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/archives/pdfs/**")
                .addResourceLocations(opositaWebProperties.getUpload().getLocalDirectory());
    }
}
