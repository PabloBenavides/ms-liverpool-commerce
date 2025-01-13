package com.liverpool.api.commons.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    IN_PROCESS("EN PROCESO"),
    ON_ROUTE("EN REPARTO"),
    CANCELED("CANCELADO"),
    DELIVERED("ENTREGADO");

    private final String statusText;

    OrderStatusEnum(String statusText) {
        this.statusText = statusText;
    }

}
