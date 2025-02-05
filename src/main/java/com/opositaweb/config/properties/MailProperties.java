package com.opositaweb.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MailProperties {

    private String hostEmail;
    private String host;
    private String username;
    private String password;
}
