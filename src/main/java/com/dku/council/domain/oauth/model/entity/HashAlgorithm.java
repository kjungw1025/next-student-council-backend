package com.dku.council.domain.oauth.model.entity;

import lombok.Getter;

@Getter
public enum HashAlgorithm {
    SHA1("SHA-1", "S1"),
    SHA256("SHA-256", "S256"),
    SHA512("SHA-512", "S512");

    private final String algorithm;
    private final String shortenedAlgorithm;

    HashAlgorithm(String algorithm, String shortenedAlgorithm) {
        this.algorithm = algorithm;
        this.shortenedAlgorithm = shortenedAlgorithm;
    }

}
