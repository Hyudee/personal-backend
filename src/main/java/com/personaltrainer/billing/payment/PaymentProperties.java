package com.personaltrainer.billing.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties (prefix = "app.payment")

public record PaymentProperties(
        String whatsappNumber,
        String payMessageTemplate,
        String pixPaidMessageTemplate
) {
}
