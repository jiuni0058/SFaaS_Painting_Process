package com.team1.sfaas.controller;

import com.team1.sfaas.model.Hr;
import com.team1.sfaas.security.HashUtil;
import com.team1.sfaas.service.HrService;
import com.team1.sfaas.service.MailService;

import org.springframework.web.bind.annotation.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hr")
public class HrController {

    private final HrService hrService;

    // byte 배열로 직접 선언
    byte[] salt = new byte[] {
        (byte)0xa1, (byte)0xb2, (byte)0xc3, (byte)0xd4,
        (byte)0xe5, (byte)0xf6, (byte)0x01, (byte)0x23,
        (byte)0x45, (byte)0x67, (byte)0x89, (byte)0xab,
        (byte)0xcd, (byte)0xef, (byte)0x01, (byte)0x23
    };

    @Autowired
    public HrController(HrService hrService) {
        this.hrService = hrService;
    }

    // 단일 사용자 조회
    @GetMapping("/{id}")
    public Hr getUserById(@PathVariable String id) {
        return hrService.getUserById(id);
    }

    // 전체 사용자 조회
    @GetMapping
    public List<Hr> getAllUsers() {
        return hrService.getAllUsers();
    }

    // 사용자 등록
    @PostMapping
    public Map<String, Object> createUser(@RequestBody Hr hr) {
        Map<String, Object> result = new HashMap<>();

          // 1) 솔트 생성
        // byte[] salt = HashUtil.generateSalt();

        // 2) 해시 생성 (salt + password)
        String hashHex = HashUtil.sha256WithSaltHex(hr.getPw(), salt);

        // 3) Hr 객체에 해시/솔트 설정 (DB에는 평문 저장 금지)
        hr.setPw(hashHex);
        // 중복 체크
        if(hrService.isUserExist(hr.getId()) || hrService.isUserExistEmail(hr.getEmail())) {
            result.put("status", "fail");
            result.put("message", "이미 존재하는 계정(이메일) 또는 이름입니다.");
            return result;
        }
        
        hrService.createUser(hr);
        result.put("status", "success");
        result.put("message", "계정 등록 성공!");
        return result;
    }

    //비밀번호 초기화
    @PutMapping("/{email}")
    public Map<String, Object> updatePassword(
            @PathVariable("email") String email) {

        Map<String, Object> result = new HashMap<>();
            
        //이메일 존재 유무 체크
        if(!hrService.isUserExistEmail(email)){
            result.put("status", "fail");
            result.put("message", "메일이 존재하지 않습니다.");
            return result;
        }

        String hashHex = HashUtil.sha256WithSaltHex("1111", salt);

        boolean success = hrService.resetPasswordByEmail(email, hashHex);
        if (success) {
            result.put("status", "success");
            result.put("message", "비밀번호 초기화 성공!");
        } else {
            result.put("status", "fail");
            result.put("message", "계정을 찾을 수 없습니다.");
        }

        return result;
    }


    // 사용자 수정
    @PutMapping("/update/{id}")
    public Map<String, Object> updateUser(@RequestBody Hr hr) {
        Map<String, Object> result = new HashMap<>();
        String hashHex = HashUtil.sha256WithSaltHex(hr.getPw(), salt);
        
        hr.setPw(hashHex);
        hrService.updateUser(hr);
          if (hr.getName() != null && !hr.getName().trim().isEmpty()) {
            result.put("status", "success");
            result.put("message", hr.getName());
            result.put("id", hr.getId());
        } else {
            result.put("status", "fail");
            result.put("message", "아이디 또는 비밀번호가 틀렸습니다.");
        }

        return result; // JSON 형태로 클라이언트 전달
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        return hrService.deleteUser(id) > 0 ? "삭제 성공" : "삭제 실패";
    }

    // 로그인 처리 POST
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginForm) {
        Map<String, Object> result = new HashMap<>();
        // Map에서 값 꺼내기
        String username = loginForm.get("username");
        String password = loginForm.get("password");

        String hashHex = HashUtil.sha256WithSaltHex(password, salt);

        Hr hr = hrService.getUserByName(username, hashHex);
        if (hr == null) {
            result.put("status", "fail");
            result.put("message", "아이디 또는 비밀번호가 틀렸습니다.");
            return result;
        }

    
        if (hr.getName() != null && !hr.getName().trim().isEmpty()) {
            result.put("status", "success");
            result.put("message", hr.getName());
            result.put("id", hr.getId());
        } else {
            result.put("status", "fail");
            result.put("message", "아이디 또는 비밀번호가 틀렸습니다.");
        }

        return result; // JSON 형태로 클라이언트 전달
    }
}
