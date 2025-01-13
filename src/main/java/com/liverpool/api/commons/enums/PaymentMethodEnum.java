package com.liverpool.api.commons.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    CREDIT_CARD("CARD"),
    DEBIT_CARD("DEBIT_CARD"),
    PAYPAL("PAYPAL");

    private final String paymentMethodText;

    PaymentMethodEnum(String paymentMethodText) {
        this.paymentMethodText = paymentMethodText;
    }
}
