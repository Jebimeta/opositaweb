package com.opositaweb.service.about;

import com.opositaweb.domain.vo.EmailResponse;
import org.springframework.stereotype.Component;

@Component
public class EmailResponseFactory {

    public static EmailResponse createEmailResponse(String message){
        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setMessage(message);
        return emailResponse;
    }
}
