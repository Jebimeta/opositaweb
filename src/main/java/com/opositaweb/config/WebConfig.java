package com.opositaweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    // Define un bean que configura CORS para la aplicación.
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            // Sobrescribe el método para agregar configuraciones de CORS.
            @Override
            public void addCorsMappings(CorsRegistry registry){
                // Configura CORS para permitir todas las solicitudes desde cualquier origen, con cualquier método y encabezado.
                registry.addMapping("/**") // Permite CORS para todas las rutas.
                        .allowedOrigins("http://localhost:4200") // Permite solicitudes desde cualquier origen.
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite todos los métodos HTTP (GET, POST, etc.).
                        .allowedHeaders("*") // Permite todos los encabezados HTTP.
                        .allowCredentials(true) // No permite el envío de credenciales (cookies, cabeceras de autenticación, etc.).
                        .maxAge(3600); // Establece el tiempo máximo (en segundos) que los resultados de una solicitud preflight pueden ser almacenados en caché. // Establece el tiempo máximo en segundos que los resultados de una solicitud preflight pueden ser almacenados en caché.
            }
        };
    }
}