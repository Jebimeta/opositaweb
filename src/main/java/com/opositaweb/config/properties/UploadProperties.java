package com.opositaweb.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UploadProperties {

    private String directory;

    private String baseUrl;

    private String localDirectory;
}
