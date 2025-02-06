package com.opositaweb.config.enums;

import lombok.Getter;

@Getter
public enum OpositaWebApiEndpoints {

    // Swagger y Docs URLs
    SWAGGER_UI_HTML("/swagger-ui.html"), SWAGGER_UI("/swagger-ui/**"), API_DOCS("/v3/api-docs/**"),

    //Base URLs
    BASE_AUTH_URL("api/v1/auth"), BASE_USER_URL("api/v1/user"), BASE_PAYMENT_URL("api/v1/payment"),
    BASE_TEST_URL("api/v1/test"), BASE_PDF_URL("api/v1/pdf"), BASE_QUESTION_URL("api/v1/question"),

    // Auth Endpoints
    LOGIN_URL(BASE_AUTH_URL.getUrl() + "/login"),
    LOGOUT_URL(BASE_AUTH_URL.getUrl() + "/logout"),
    REGISTER_URL(BASE_AUTH_URL.getUrl() + "/register"),
    REFRESH_TOKEN_URL(BASE_AUTH_URL.getUrl() + "/refresh-token"),
    VERIFY_TOKEN_URL(BASE_AUTH_URL.getUrl() + "/verify/{token}"),
    AUTHENTICATED_USER_INFO_URL(BASE_AUTH_URL.getUrl() + "/authenticated-user"),

    // User Endpoints
    USERS_LIST_URL(BASE_USER_URL.getUrl() + "/list"),
    USER_BY_ID_URL(BASE_USER_URL.getUrl() + "/{id}"),
    USER_BY_EMAIL_URL(BASE_USER_URL.getUrl() + "/email/{email}"),
    USER_BY_USERNAME_URL(BASE_USER_URL.getUrl() + "/username/{username}"),
    USER_UPDATE_URL(BASE_USER_URL.getUrl() + "/update/{id}"),
    USER_DELETE_URL(BASE_USER_URL.getUrl() + "/delete/{id}"),

    // Payment Endpoints
    PAYMENT_LIST_URL(BASE_PAYMENT_URL.getUrl() + "/payments"),
    PAYMENT_BY_ID_URL(BASE_PAYMENT_URL.getUrl() + "/payments/{id}"),
    PAYMENT_SAVE_URL(BASE_PAYMENT_URL.getUrl() + "/payments/save"),
    PAYMENT_UPDATE_URL(BASE_PAYMENT_URL.getUrl() + "/payments/update/{id}"),
    PAYMENT_DELETE_URL(BASE_PAYMENT_URL.getUrl() + "/payments/delete/{id}"),

    // PaymentPlan Endpoints
    PAYMENT_PLAN_LIST_URL(BASE_PAYMENT_URL.getUrl() + "/payment-plans"),
    PAYMENT_PLAN_BY_ID_URL(BASE_PAYMENT_URL.getUrl() + "/payment-plans/{id}"),
    PAYMENT_PLAN_SAVE_URL(BASE_PAYMENT_URL.getUrl() + "/payment-plans/save"),
    PAYMENT_PLAN_UPDATE_URL(BASE_PAYMENT_URL.getUrl() + "/payment-plans/update/{id}"),
    PAYMENT_PLAN_DELETE_URL(BASE_PAYMENT_URL.getUrl() + "/payment-plans/delete/{id}"),

    // Question Endpoints
    QUESTION_LIST_URL(BASE_QUESTION_URL.getUrl() + "/questions"),
    QUESTION_BY_ID_URL(BASE_QUESTION_URL.getUrl() + "/questions/{id}"),
    QUESTION_SAVE_URL(BASE_QUESTION_URL.getUrl() + "/questions/save"),
    QUESTION_UPDATE_URL(BASE_QUESTION_URL.getUrl() + "/questions/update/{id}"),
    QUESTION_DELETE_URL(BASE_QUESTION_URL.getUrl() + "/questions/delete/{id}"),

    // Test Endpoints
    TEST_LIST_URL(BASE_TEST_URL.getUrl() + "/tests"),
    TEST_BY_ID_URL(BASE_TEST_URL.getUrl() + "/tests/{id}"),
    TEST_SAVE_URL(BASE_TEST_URL.getUrl() + "/tests/save"),
    TEST_UPDATE_URL(BASE_TEST_URL.getUrl() + "/tests/update/{id}"),
    TEST_DELETE_URL(BASE_TEST_URL.getUrl() + "/tests/delete/{id}"),

    // Pdf Endpoints
    PDF_LIST_URL(BASE_PDF_URL.getUrl() + "/pdfs"),
    PDF_BY_ID_URL(BASE_PDF_URL.getUrl() + "/pdfs/{id}"),
    PDF_SAVE_URL(BASE_PDF_URL.getUrl() + "/pdfs/save"),
    PDF_UPDATE_URL(BASE_PDF_URL.getUrl() + "/pdfs/update/{id}"),
    PDF_DELETE_URL(BASE_PDF_URL.getUrl() + "/pdfs/delete/{id}"),

    // Utils Endpoints
    PDFS_URL("/archives/pdfs/**"),
    SEND_EMAIL_URL("/api/v1/about/send-email");

    private final String url;

    OpositaWebApiEndpoints(String url) {
        this.url = url;
    }
}
