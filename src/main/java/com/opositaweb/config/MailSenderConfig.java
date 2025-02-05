package com.opositaweb.config;

import com.opositaweb.config.properties.OpositaWebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailSenderConfig {

    private final OpositaWebProperties properties;

    @Bean
    public JavaMailSender getJavaMailSender(){

        //Creamos una instancia de JavaMailSenderImpl con la que configuraremos el servidor de correo
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Especificamos el servidor SMTP
        mailSender.setHost("smtp.gmail.com");

        // Especificamos el puerto, es el estandar de gmail
        mailSender.setPort(587);

        // Establecemos las credenciales del correo
        mailSender.setUsername(properties.getMail().getUsername());
        mailSender.setPassword(properties.getMail().getPassword());

        // Configuramos las propiedades del servidor de correo
        Properties props = mailSender.getJavaMailProperties();

        // Definimos el tipo de protocolo
        props.put("mail.transport.protocol", "smtp");

        // Habilitamos la autenticación para enviar correos
        props.put("mail.smtp.auth", "true");

        // Habilitamos el inicio de sesión seguro activando el protocolo TLS
        props.put("mail.smtp.starttls.enable", "true");

        // Habilitamos el debug para ver los mensajes de log
        props.put("mail.debug", "true");

        // Configuracion del servidor para evitar problemas con certificados SSL
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Configuramos tiempo máximo para establecer la conexión
        props.put("mail.smtp.connectiontimeout", 5000);

        // Configuramos tiempo máximo para esperar una respuesta del servidor
        props.put("mail.smtp.timeout", 5000);

        // Configuramos tiempo máximo par enviar datos al servidor
        props.put("mail.smtp.writetimeout", 5000);

        // Configuramos la condificacion de caracteres para el mensaje
        props.put("mail.mime.charset", "UTF-8");

        // Y retornamos
        return mailSender;
    }
}
