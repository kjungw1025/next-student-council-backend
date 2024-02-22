package com.dku.council.domain.oauth.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OauthResponseTypeTest {

    @Test
    void getValue() {
        OauthResponseType oauthResponseType = OauthResponseType.CODE;
        assertEquals("code", oauthResponseType.getValue());
    }
}