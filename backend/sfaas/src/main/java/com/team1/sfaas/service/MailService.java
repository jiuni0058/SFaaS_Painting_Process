package com.team1.sfaas.service;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    // email → 인증번호
    private static final ConcurrentHashMap<String, String> codeStore = new ConcurrentHashMap<>();

    public void sendMail(String to) {
        try {
            Random random = new Random();
            int number = 100000 + random.nextInt(900000); // 100000~999999
            String randomNumber = String.valueOf(number);

            // 인증번호 저장
            codeStore.put(to, randomNumber);

            // MimeMessage 생성 (HTML 지원)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String subject = "모니터링 시스템 인증 번호";


            String htmlBody = "<div style=\"font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 24px; background-color: #f8f9fa;\">" +
                "    <div style=\"background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);\">" +
                "        <!-- 헤더 -->" +
                "        <div style=\"background: linear-gradient(135deg, #3182f6 0%, #2563eb 100%); padding: 40px 32px 32px; text-align: center;\">" +
                "            <div style=\"width: 56px; height: 56px; background: rgba(255, 255, 255, 0.2); border-radius: 16px; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 16px;\">" +
                "                <div style=\"width: 32px; height: 32px; background: white; border-radius: 8px; position: relative;\">" +
                "                    <div style=\"position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); color: #3182f6; font-size: 18px; font-weight: 600;\">✓</div>" +
                "                </div>" +
                "            </div>" +
                "            <h1 style=\"color: white; font-size: 24px; font-weight: 700; margin: 0; line-height: 1.3;\">이메일 인증</h1>" +
                "            <p style=\"color: rgba(255, 255, 255, 0.8); font-size: 16px; margin: 8px 0 0; line-height: 1.5;\">안전한 인증을 위해 인증번호를 확인해주세요</p>" +
                "        </div>" +
                
                "        <!-- 컨텐츠 -->" +
                "        <div style=\"padding: 40px 32px;\">" +
                "            <div style=\"text-align: center; margin-bottom: 32px;\">" +
                "                <p style=\"color: #4b5563; font-size: 16px; margin: 0 0 24px; line-height: 1.6;\">아래 인증번호를 입력창에 정확히 입력해주세요</p>" +
                "                " +
                "                <!-- 인증번호 -->" +
                "                <div style=\"display: inline-block; background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%); border: 2px solid #e2e8f0; border-radius: 16px; padding: 24px 32px; margin: 16px 0;\">" +
                "                    <div style=\"color: #1e293b; font-size: 36px; font-weight: 800; letter-spacing: 8px; font-family: 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;\">" +
                randomNumber +
                "                    </div>" +
                "                </div>" +
                "            </div>" +
                
                "            <!-- 주의사항 -->" +
                "            <div style=\"background: #fef3c7; border: 1px solid #fde047; border-radius: 12px; padding: 16px; margin-bottom: 24px;\">" +
                "                <div style=\"display: flex; align-items: flex-start; gap: 12px;\">" +
                "                    <div style=\"color: #d97706; font-size: 16px; margin-top: 2px;\">⚠️</div>" +
                "                    <div>" +
                "                        <p style=\"color: #92400e; font-size: 14px; font-weight: 600; margin: 0 0 4px; line-height: 1.4;\">중요 안내사항</p>" +
                "                        <p style=\"color: #a16207; font-size: 13px; margin: 0; line-height: 1.5;\">• 인증번호는 <strong>10분간</strong> 유효합니다<br>• 타인과 공유하지 마세요<br>• 인증번호가 만료되면 재발송을 요청해주세요</p>" +
                "                    </div>" +
                "                </div>" +
                "            </div>" +
                
                "            <!-- 문의 -->" +
                "            <div style=\"text-align: center; padding-top: 16px; border-top: 1px solid #e5e7eb;\">" +
                "                <p style=\"color: #6b7280; font-size: 13px; margin: 0; line-height: 1.5;\">문의사항이 있으시면 고객센터로 연락주세요</p>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                
                "    <!-- 푸터 -->" +
                "    <div style=\"text-align: center; padding: 24px 16px 0;\">" +
                "        <p style=\"color: #9ca3af; font-size: 12px; margin: 0; line-height: 1.4;\">본 메일은 발신전용입니다. 답변이 필요한 경우 고객센터를 이용해주세요.</p>" +
                "    </div>" +
                "</div>";

            helper.setFrom("wlwhs043@naver.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML 모드

            System.out.println("here2");
            mailSender.send(mimeMessage); // MimeMessage 전송
            
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("이메일 전송 실패: " + e.getMessage());
        }
    }


    // 인증번호 확인
    public boolean verifyCode(String email, String code) {
        String stored = codeStore.get(email);
        System.out.println("email"+email); //현재 인증 이메일
        System.out.println("stored"+stored);  //이메일에 대한 인증번호 값
        System.out.println("code:"+code); // 실제 인증번호
        if(stored != null && stored.equals(code)) {
            codeStore.remove(email); // 인증 완료 후 제거
            return true;
        }
        return false;
    }
}
