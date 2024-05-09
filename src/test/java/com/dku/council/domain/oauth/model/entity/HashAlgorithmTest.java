package com.dku.council.domain.oauth.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashAlgorithmTest {

    @Test
    void getValue() {
        HashAlgorithm hashAlgorithm = HashAlgorithm.SHA1;
        assertEquals("SHA-1", hashAlgorithm.getAlgorithm());
        assertEquals("S1", hashAlgorithm.getShortenedAlgorithm());
    }
}