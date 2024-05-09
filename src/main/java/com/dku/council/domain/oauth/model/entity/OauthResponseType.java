package com.dku.council.domain.oauth.model.entity;

public enum OauthResponseType {
    CODE("code");

    private final String value;

    OauthResponseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
