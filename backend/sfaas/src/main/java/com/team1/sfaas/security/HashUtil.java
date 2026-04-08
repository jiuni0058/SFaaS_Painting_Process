package com.team1.sfaas.security;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {

    private static final int SALT_LEN = 16; // 16 bytes = 128 bits

    // 솔트 생성 (SecureRandom)
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LEN];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // 바이트를 16진 문자열(hex)로 변환
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // hex -> bytes
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    // SHA-256 해시 (salt가 있을 경우 salt를 비밀번호 앞/뒤에 붙여서 해시)
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 편의 메서드: 비밀번호와 salt(byte[])를 받아 hex 해시 반환
    public static String sha256WithSaltHex(String password, byte[] salt) {
        // 권장: salt + password 순으로 합치거나 password + salt (일관성만 지키면 됨)
        byte[] pwBytes = password.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] combined = new byte[salt.length + pwBytes.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(pwBytes, 0, combined, salt.length, pwBytes.length);

        byte[] hash = sha256(combined);
        return bytesToHex(hash);
    }
}
