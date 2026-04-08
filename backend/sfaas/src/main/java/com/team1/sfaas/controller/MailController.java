package com.team1.sfaas.controller;

import com.team1.sfaas.model.Hr;
import com.team1.sfaas.security.HashUtil;
import com.team1.sfaas.service.MailService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    // 이메일 전송 API
    @PostMapping("/send")
    public String sendMail(
            @RequestParam String to
    ) {
        try {
            mailService.sendMail(to);
            return "메일 전송 성공: " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "메일 전송 실패: " + e.getMessage();
        }
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyCode(@RequestBody Map<String, String> certForm) {
        
        String email = certForm.get("username");
        String code = certForm.get("password");

        boolean ok = mailService.verifyCode(email, code);
        Map<String, Object> result = new HashMap<>();
        if(ok){
            result.put("status", "success");
            result.put("message", "인증되었습니다.");
            return result;
        }
        
        result.put("status", "fail");
        result.put("message", "인증번호가 틀렸습니다.");
        return result;

    }

     
}
