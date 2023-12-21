package com.example.UserLogin.entity;

public enum SocialProvider {
    LINKEDIN("LINKEDIN");

    private String providerType;

    public String getProviderType() {
        return providerType;
    }

    SocialProvider(final String providerType) {
        this.providerType = providerType;
    }

    }
