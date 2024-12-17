package com.opositaweb.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@ConfigurationProperties(prefix = "opositaweb")
public class OpositaWebProperties {

	private final OpositaWebSecurityProperties security;

}
