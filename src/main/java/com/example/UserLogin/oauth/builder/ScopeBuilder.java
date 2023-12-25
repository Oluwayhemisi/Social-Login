package com.example.UserLogin.oauth.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


//  Builder for LinkedIn OAuth 2.0 scopes.

public final class ScopeBuilder {

    private final Set<String> scopes = new HashSet<>();


    public ScopeBuilder(final String... scopes) {
        withScopes(scopes);
    }

    public ScopeBuilder withScopes(final String... scopes) {
        this.scopes.addAll(Arrays.asList(scopes));
        return this;
    }

    public String build() {
        final StringBuilder scopeBuilder = new StringBuilder();
        for (String scope : scopes) {
            scopeBuilder.append(' ').append(scope);
        }
        return scopeBuilder.substring(1);
    }
}
