package com.dku.council.domain.oauth.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class CodeChallengeConverter {
    public String convertToCodeChallenge(String code, String codeChallengeMethod) {
        byte[] digest = getDigest(code.getBytes(StandardCharsets.UTF_8), codeChallengeMethod);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    private static byte[] getDigest(byte[] input, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
