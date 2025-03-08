package com.opositaweb.controller;

import com.opositaweb.apifirst.api.AboutApiDelegate;
import com.opositaweb.domain.vo.EmailRequest;
import com.opositaweb.domain.vo.EmailResponse;
import com.opositaweb.service.about.AboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AboutController implements AboutApiDelegate {

    private final AboutService aboutService;

    public ResponseEntity<EmailResponse> sendEmail(EmailRequest request){
        return ResponseEntity.ok(aboutService.sendEmail(request));
    }
}
