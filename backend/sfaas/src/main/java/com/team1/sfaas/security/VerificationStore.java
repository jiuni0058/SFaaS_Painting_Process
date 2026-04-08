package com.team1.sfaas.security;

import java.util.concurrent.ConcurrentHashMap;

public class VerificationStore {
    // email → 인증번호
    private static final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public static void saveCode(String email, String code) {
        store.put(email, code);
    }

    public static boolean verifyCode(String email, String code) {
        return code.equals(store.get(email));
    }

    public static void removeCode(String email) {
        store.remove(email);
    }
}
