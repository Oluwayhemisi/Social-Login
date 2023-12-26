package com.example.UserLogin.oauth;


/**
 * Constants file
 */
public class Constants {
    public static final String SAMPLE_APP_BASE = "java-sample-application";
    public static final String SAMPLE_APP_VERSION = "version 1.0";
    enum AppName {
        OAuth,
        Marketing;
    }
    public static final String USER_AGENT_OAUTH_VALUE = String.format("%s (%s, %s)", SAMPLE_APP_BASE, SAMPLE_APP_VERSION, AppName.OAuth.name());

}
