package com.dku.council.domain.oauth.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeChallengeConverterTest {

    @Test
    void convertToCodeChallengeWithSHA256() {
        CodeChallengeConverter converter = new CodeChallengeConverter();
        String code = "code";
        String codeChallengeMethod = "SHA-256";
        String expected = "VpTQii5T_8rgwxA-Wtb2B2q9lg6x-KVldwQLwQKPcCs";

        String result = converter.convertToCodeChallenge(code, codeChallengeMethod);

        assertEquals(expected, result);
    }

    @Test
    void throwExceptionWhenInvalidAlgorithmProvided() {
        CodeChallengeConverter converter = new CodeChallengeConverter();
        String code = "code";
        String codeChallengeMethod = "InvalidAlgorithm";

        assertThrows(RuntimeException.class, () -> converter.convertToCodeChallenge(code, codeChallengeMethod));
    }
}
